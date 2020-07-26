package com.test.util;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Utils {

    /*
    * writing converted xml content to a file and returns files path
    * */
    static String writeXmlToFile(final String convertedXml, final File xmlConvertedFile) throws IOException {
        try (FileWriter fw = new FileWriter(xmlConvertedFile)) {
            fw.write(convertedXml);
            return xmlConvertedFile.getAbsolutePath();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    /*
    * compares the converted xml with the actual xml file
    * */
    static boolean compareOldAndNewXml(final String path, final String convertedXmlPath) throws IOException {
        Path actualXmlPath = Paths.get(path);
        Path expectedXmlPath = Paths.get(convertedXmlPath);

        String actualXml = Files.readString(actualXmlPath, StandardCharsets.US_ASCII);
        String expectedXml = Files.readString(expectedXmlPath, StandardCharsets.US_ASCII);

        Diff diff = DiffBuilder
                .compare(actualXml)
                .withTest(expectedXml)
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
                .checkForSimilar()
                .ignoreComments()
                .ignoreWhitespace()
                .build();

        return diff.hasDifferences();
    }

    /*
    * checks if the input file exist or not
    * */
    static void checkFileExist(final File file) {
        if(!file.exists()){
            System.out.println("File does not exist!! Give a valid input file.");
            System.exit(1);
        }
    }
}
