package com.github.javachaos.chaosdungeons.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.Set;

class JsonParserTest {

    private static final Logger LOGGER = LogManager.getLogger(JsonParserTest.class);
    @Test
    void testJsonParser() throws Exception {
        JsonParser jp = new JsonParser("/test.json");
        LOGGER.debug(jp.printFile());
        Set<Pair<String, Object>> data = jp.parse();
        for(Pair<String, Object> p : data) {
            LOGGER.debug(p + "\n");
        }
    }

}
