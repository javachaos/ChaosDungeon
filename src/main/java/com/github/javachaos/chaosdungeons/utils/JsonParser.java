package com.github.javachaos.chaosdungeons.utils;

import com.github.javachaos.chaosdungeons.utils.exceptions.JsonParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class JsonParser {
    private static final Logger LOGGER = LogManager.getLogger(JsonParser.class);
    private static final int MIN_WORD_LENGTH = 1;
    private static final int MAX_WORD_LENGTH = 5;
    private static final int MAX_HASH_VALUE = 62;
    private final String filename;
    private final String[] symbols = {"", "t", ":", "-", "true", "", "n", "9", ",", "null", "", "f", "8", "+",  "",
            "false", "}", "7", "\"", "", "", "{", "6", "", "", "", "u", "5", "", "", "", "r", "4", "", "", "", "e",
            "3", "",  "", "", "b", "2", "", "", "", "]", "1", "", "", "", "\\", "0", "", "", "",  "[", "/", "", "",
            "", "E", "."};
    private CharacterStreamReader inputStream;
    private char prev;
    private Token curr;
    private int lineCount;

    public JsonParser(String jsonFilename) {
        this.filename = jsonFilename;
        reset();
    }

    private void reset() {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(filename));
        inputStream = new CharacterStreamReader(is);
    }

    public String printFile() {
        StringBuilder stringBuilder = new StringBuilder();
        while (inputStream.hasNext()) {
            stringBuilder.append(inputStream.next());
        }
        reset();
        return stringBuilder.toString();
    }

    private void whitespace() {
        while (inputStream.hasNext()) {
            Character next = inputStream.peek();
            if (next != ' ' && next != '\t' && next != '\r' && next != '\n') {
                break;
            } else {
                if (inputStream.next() == '\n') {
                    lineCount++;
                }
            }
        }
    }

    private Token getNextToken() {//test
        whitespace();
        char next;
        StringBuilder chars = new StringBuilder();
        if (inputStream.hasNext()) {
            next = inputStream.peek();
            chars.append(next);
            parseTrueFalseNull(next, chars);
            curr = Token.get(hasSymbol(chars.toString().toCharArray()));
            inputStream.next();
            prev = next;
            return curr;
        }
        return Token.NONE0;
    }

    private void parseTrueFalseNull(char next, StringBuilder chars) {
        if (prev != '\\') {
            switch (next) {
                case 't':
                    inputStream.mark(1);
                    inputStream.next();
                    if (inputStream.peek() == 'r') {
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                    } else {
                        inputStream.reset();
                    }
                    break;
                case 'f':
                    inputStream.mark(1);
                    inputStream.next();
                    if (inputStream.peek() == 'a') {
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                    } else {
                        inputStream.reset();
                    }
                    break;
                case 'n':
                    inputStream.mark(1);
                    inputStream.next();
                    if (inputStream.peek() == 'u') {
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                        chars.append(inputStream.next());
                    } else {
                        inputStream.reset();
                    }
                    break;
                default:
            }
        }
    }

    private Token peekNextToken() {
        whitespace();
        StringBuilder chars = new StringBuilder();
        if (inputStream.hasNext()) {
            char next = inputStream.peek();
            chars.append(next);
            parseTrueFalseNull(next, chars);
            return Token.get(hasSymbol(chars.toString().toCharArray()));
        }
        return Token.NONE0;
    }

    /**
     * Consume a string and return it as a String.
     *
     * @return
     */
    private String string() {
        StringBuilder str = new StringBuilder();
        char prevChar = 0;
        if (inputStream.hasNext() && prev == '"') {
            prevChar = inputStream.next();
        } else {
            LOGGER.debug("Not a string: {} line: {}", inputStream.peek(), lineCount);
        }
        int i = 0;
        while (inputStream.hasNext()) {
            str.append(prevChar);
            i++;
            if ((i == 1 && prevChar == '"') || (prevChar != '\\' && (prevChar = inputStream.next()) == '"')) {
                break;
            }
        }
        return str.toString();
    }

    /**
     * Accept the next token and advance to the next if advance is true
     *
     * @param token   the token to accept
     * @param advance the boolean flag used to indicate if we should advance after accepting token
     * @return true if the current token is token.
     */
    private boolean accept(Token token, boolean advance) {
        if (advance) {
            getNextToken();
        }
        return curr == token;
    }

    /**
     * Peek the next token, accept if it matches.
     */
    private boolean acceptPeek(Token token) {
        return peekNextToken() == token;
    }

    /**
     * Expect the next token to be token and advance if flag is true.
     *
     * @param token   the token we expect to see
     */
    private boolean expect(Token token) {
        if (accept(token, false)) {
            return true;
        }
        LOGGER.debug("Expected: {} at line: {}", token, lineCount);
        return false;
    }

    private Map<String, Object> object() {
        Map<String, Object> map = new HashMap<>();
        getNextToken();
        expect(Token.LBRACE);
        whitespace();
        if (accept(Token.RBRACE, false)) {
            getNextToken();
            return map;
        }
        do {
            whitespace();
            if (accept(Token.DQUOTE, true)) {//Start of string
                String name = string();
                whitespace();
                getNextToken();
                expect(Token.COLON);
                Object value = value();
                map.put(name, value);
                whitespace();
                if (acceptPeek(Token.RBRACE)) {
                    getNextToken();
                    expect(Token.RBRACE);
                    return map;
                }
                //TODO figure out how to detect extra trailing commas here,
                // my first idea is to use a 2 character lookahead (peek)
                // first peek 1, if its a comma, thats ok, peek one more,
                // if its a RBRACE, backtrack to 0 and report error.
            }
        } while (accept(Token.COMMA, true));
        return map;
    }

    public Map<String, Object> parse() throws Exception {
        Map<String, Object> result = object();
        inputStream.close();
        return result;
    }

    private String number(StringBuilder num) {//Done. needs testing
        switch (peekNextToken()) {
            case DASH:
                num.append("-");
                getNextToken();
                return number(num);
            case ZERO:
                num.append("0");
                curr = getNextToken();
                fraction(num);
                exponent(num);
                return num.append(digit()).toString();
            case ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE:
                num.append(digit());
                fraction(num);
                exponent(num);
                return num.toString();
            default:
                return "";
        }
    }

    private List<Object> value() {
        List<Object> list = new ArrayList<>();
        whitespace();
        Token next = peekNextToken();
        switch (next) {
            case DQUOTE:
                getNextToken();
                String s = string();
                list.add(s);
                return list;
            case DASH, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE:
                String num = number(new StringBuilder());
                list.add(num);
                return list;
            case LBRACE:
                Map<String, Object> o = object();
                list.add(o);
                return list;
            case LBRAC:
                List<Object> l = array();
                list.add(l);
                return list;
            case TRUE:
                list.add("true");
                return list;
            case FALSE:
                list.add("false");
                return list;
            case NULL:
                list.add("null");
                return list;
            default:
                return Collections.emptyList();
        }
    }

    private List<Object> array() {//Tested (not covered)
        List<Object> values = new ArrayList<>();
        getNextToken();
        expect(Token.LBRAC);
        whitespace();
        if (accept(Token.RBRAC, false)) {
            getNextToken();
            return values;
        } else {
            List<Object> o;
            do {
                o = value();
                if (!o.isEmpty()) {
                    values.add(o);
                }
                if (accept(Token.RBRAC, true)) {
                    getNextToken();
                    return values;
                }
            } while (accept(Token.COMMA, false));
            expect(Token.RBRAC);
            getNextToken();
        }
        return values;
    }

    private void exponent(StringBuilder num) {//Done. Needs testing
        if (acceptPeek(Token.E)) {
            num.append("e");
            getNextToken();
            if (acceptPeek(Token.DASH)) {
                num.append("-");
                getNextToken();
            }
            if (acceptPeek(Token.PLUS)) {
                num.append("+");
                getNextToken();
            }
        } else if (acceptPeek(Token.UE)) {
            num.append("E");
            getNextToken();
            if (acceptPeek(Token.DASH)) {
                num.append("-");
                getNextToken();
            }
            if (acceptPeek(Token.PLUS)) {
                num.append("+");
                getNextToken();
            }
        }
        num.append(digit());
    }

    private void fraction(StringBuilder num) {
        if (acceptPeek(Token.PER)) {
            num.append(".");
            getNextToken();
            num.append(digit());
        }
    }

    /**
     * Returns a string of digits from the input.
     * Advances as long as the next character is a digit [0-9].
     *
     * @return a string of digits
     */
    private String digit() {
        StringBuilder str = new StringBuilder();
        Token nextDigit = peekNextToken();
        while (isDigit(nextDigit)) {
            getNextToken();
            str.append(getDigit(nextDigit));
            nextDigit = peekNextToken();
        }
        return str.toString();
    }

    private boolean isDigit(Token d) {
        return switch (d) {
            case ZERO, ONE, TWO ,THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE -> true;
            default -> false;
        };
    }

    private char getDigit(Token d) {
        return switch (d) {
            case ZERO  -> '0';
            case ONE   -> '1';
            case TWO   -> '2';
            case THREE -> '3';
            case FOUR  -> '4';
            case FIVE  -> '5';
            case SIX   -> '6';
            case SEVEN -> '7';
            case EIGHT -> '8';
            case NINE  -> '9';
            default -> throw new JsonParseException("Not a digit: " + d + " on line: " + lineCount);
        };
    }

    private int hash(char[] str, int len) {
      final char[] associatedValues = { 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 17, 63, 63, 63, 63, 63, 63, 63, 63, 12,
                 7,  2, 61, 56, 51, 46, 41, 36, 31, 26, 21, 16, 11,  6,  1, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                60, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 55, 50, 45,
                63, 63, 63, 63, 40, 63, 63, 35, 10, 63, 63, 63, 63, 63, 63, 63,  5, 63, 63, 63, 30, 63,  0, 25, 63,
                63, 63, 63, 63, 20, 63, 15, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63};
      return len + associatedValues[str[0]];
    }

    private int hasSymbol(char[] str) {
        if (str.length <= MAX_WORD_LENGTH && str.length >= MIN_WORD_LENGTH) {
            int key = hash(str, str.length);

            if (key <= MAX_HASH_VALUE) {
                char[] s = symbols[key].toCharArray();
                if (s.length < 1) {
                    return 0;
                }
                if (str[0] == s[0] && new String(str).substring(1).equals(new String(s).substring(1))) {
                    return key;
                }
            }
        }
        return 0;
    }

    enum Token {
        NONE0, T, COLON, DASH, TRUE, NONE1, N, NINE, COMMA, NULL, NONE2, F, EIGHT, PLUS, NONE3, FALSE, RBRACE, SEVEN,
        DQUOTE, NONE4, NONE5, LBRACE, SIX, NONE6, NONE7, NONE8, U, FIVE, NONE9, NONE10, NONE11, R, FOUR, NONE12,
        NONE13, NONE14, E, THREE, NONE15, NONE16, NONE17, B, TWO, NONE18, NONE19, NONE20, RBRAC, ONE, NONE21, NONE22,
        NONE23, BACKSLASH, ZERO, NONE24, NONE25, NONE26, LBRAC, FWDSLASH, NONE27, NONE28, NONE29, UE, PER;

        public static Token get(int i) {
            return values()[i];
        }
    }

}
