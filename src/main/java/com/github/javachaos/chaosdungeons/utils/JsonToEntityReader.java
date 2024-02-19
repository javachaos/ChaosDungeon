package com.github.javachaos.chaosdungeons.utils;

public class JsonToEntityReader {

    private JsonParser parser;

    /**
     * Given the name of the json file, create a reader which will
     * construct an entity from the file.
     * @param jsonFilename the name of the json file
     */
    public JsonToEntityReader(String jsonFilename) {
        parser = new JsonParser(jsonFilename);

    }
}
