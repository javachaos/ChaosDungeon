package com.github.javachaos.chaosdungeons.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FileUtils {
    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

    public static ByteBuffer loadFontFile(String filePath) {
        try (InputStream inputStream = FileUtils.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Font file not found: " + filePath);
            }

            byte[] fontData = inputStream.readAllBytes();

            ByteBuffer buffer = BufferUtils.createByteBuffer(fontData.length);
            buffer.put(fontData);
            buffer.flip();

            return buffer;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
