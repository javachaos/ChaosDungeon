package com.github.javachaos.chaosdungeons.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonParserTest {

    private static final Logger LOGGER = LogManager.getLogger(JsonParserTest.class);
    @Test
    void testJsonParser() throws Exception {
        JsonParser jp = new JsonParser("/test.json");
        LOGGER.debug(jp.printFile());
        Map<String, Object> data = jp.parse();
        ArrayList<Object> json = (ArrayList<Object>) data.get("glossary");
        LOGGER.debug(json);
//        assertEquals("[{popup=[{menuitem=[[[true]," +
//                " [false], [false], [true]," +
//                " [{onclick=[CreateNewDoc()]," +
//                " value=[New]}], [{onclick=[OpenDoc()]," +
//                " value=[Open]}], [{onclick=[CloseDoc()]," +
//                " value=[Close]}]," +
//                " [false]]]}], id=[file]," +
//                " value=[File]}]", json.toString());
    }

}
