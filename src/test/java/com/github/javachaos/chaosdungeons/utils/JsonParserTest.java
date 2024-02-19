package com.github.javachaos.chaosdungeons.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

class JsonParserTest {

    private static final Logger LOGGER = LogManager.getLogger(JsonParserTest.class);
    @Test
    void testJsonParser() throws Exception {
        JsonParser jp = new JsonParser("/test.json");
        LOGGER.debug(jp.printFile());
        Map<String, Object> data = jp.parse();
        ArrayList<Object> json = (ArrayList<Object>) data.get("menu");
        LOGGER.debug(json);
        //assertEquals("[{title=[example glossary], GlossDiv=[{GlossList=[{GlossEntry=[{GlossTerm=[Standard Generalized Markup Language], SortAs=[SGML], GlossDef=[{para=[A meta-markup language, used to create markup languages such as DocBook.], GlossSeeAlso=[[[GML], [XML]]]}], ID=[SGML], Acronym=[SGML], Abbrev=[ISO 8879:1986]}]}], title=[S]}]}]", json.toString());
    }

}
