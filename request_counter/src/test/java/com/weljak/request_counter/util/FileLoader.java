package com.weljak.request_counter.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileLoader {
    private static final Logger LOG = LoggerFactory.getLogger(FileLoader.class);

    public static String loadFileContent(String fileName) {
        try {
            File file = new File(FileLoader.class.getClassLoader().getResource(fileName).getFile());
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (Exception e) {
            LOG.error("Error occurred during resolving file", e);
            return null;
        }
    }
}
