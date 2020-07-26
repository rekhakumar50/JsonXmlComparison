package com.test.service;

import com.test.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonToXmlConvertor implements XMLJSONConverterI {

    private static StringBuilder stringBuilder = new StringBuilder();

    /*
     * retuns the converted json to an xml string
     * */
    public String convertJSONtoXML(final String jsonFilePath, final File xmlConvertedFile) throws IOException {

        //parsing the json file into json string
        Path jsonPath = Paths.get(jsonFilePath);
        String jsonContent = Files.readString(jsonPath, StandardCharsets.US_ASCII);

        //converting the json to xml string
        if (jsonContent.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(jsonContent);
            getJsonArray(null, jsonArray);
        } else if (!jsonContent.startsWith("{")) {
            if (jsonContent.matches("-?\\d+(\\.\\d+)?")) {
                stringBuilder.append(printString("number", null, jsonContent));
            } else if (jsonContent.matches("true|false")) {
                stringBuilder.append(printString("boolean", null, jsonContent));
            } else if (jsonContent.equals("null")) {
                stringBuilder.append("<null/>");
            } else {
                stringBuilder.append(printString("string", null, jsonContent));
            }
        } else {
            JSONObject jsonObject = new JSONObject(jsonContent);
            stringBuilder.append("<object>");

            List<String> keyNodeNames = getNodeNames(jsonObject);
            keyNodeNames.forEach(keyNodeName -> {
                checkInstance(keyNodeName, jsonObject, stringBuilder);
            });
            stringBuilder.append("</object>");
        }

        /*
         * writing the xml string in to the output file 'xmlConvertedFile'
         * and returning its file location
         * */
        return Utils.writeXmlToFile(stringBuilder.toString(), xmlConvertedFile);
    }

    /*
     * get node names
     * */
    private List<String> getNodeNames(final JSONObject json) {
        List<String> nodeNames = new ArrayList<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            nodeNames.add(keys.next());
        }
        return nodeNames;
    }

    /*
     * get nodes of an object
     * */
    private void getJsonObject(final String keyNode, final JSONObject jsonObject, final StringBuilder stringBuilder) {
        stringBuilder.append(printString("object", keyNode, null));
        List<String> keyNodeNames = getNodeNames(jsonObject);

        keyNodeNames.forEach(keyNodeName -> {
            checkInstance(keyNodeName, jsonObject, stringBuilder);
        });
        stringBuilder.append("</object>");

    }

    /*
     * get the node of an array with its nodes
     * */
    private void getJsonArray(final String keyNode, final JSONArray jsonArray) {

        stringBuilder.append(printString("array", keyNode, null));

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.get(i) instanceof String) {
                stringBuilder.append(printString("string", null, jsonArray.get(i)));
            }
            if (jsonArray.get(i) instanceof Number) {
                stringBuilder.append(printString("number", null, jsonArray.get(i)));
            }
            if (jsonArray.get(i) instanceof Boolean) {
                stringBuilder.append(printString("boolean", null, jsonArray.get(i)));
            }

            if (jsonArray.get(i) instanceof JSONArray) {
                getJsonArray(null, jsonArray.getJSONArray(i));
            }

            if (jsonArray.get(i) instanceof JSONObject) {
                getJsonObject(null, jsonArray.getJSONObject(i), stringBuilder);
            }
        }
        stringBuilder.append("</array>");
    }


    /*
     * Check instances of the node
     * */
    private void checkInstance(final String keyNodeName, final JSONObject jsonObject, final StringBuilder stringBuilder) {
        if (jsonObject.get(keyNodeName) instanceof String) {
            stringBuilder.append(printString("string", keyNodeName, jsonObject.get(keyNodeName)));
        }
        if (jsonObject.get(keyNodeName) instanceof Number) {
            stringBuilder.append(printString("number", keyNodeName, jsonObject.get(keyNodeName)));
        }
        if (jsonObject.get(keyNodeName) instanceof Boolean) {
            stringBuilder.append(printString("boolean", keyNodeName, jsonObject.get(keyNodeName)));
        }
        if (jsonObject.get(keyNodeName).equals(null)) {
            stringBuilder.append(printString("null", keyNodeName, null));
        }
        if (jsonObject.get(keyNodeName) instanceof JSONArray) {
            getJsonArray(keyNodeName, jsonObject.getJSONArray(keyNodeName));
        }

        if (jsonObject.get(keyNodeName) instanceof JSONObject) {
            getJsonObject(keyNodeName, jsonObject.getJSONObject(keyNodeName), stringBuilder);
        }
    }

    /*
     * printing elements with its type name
     * */
    private String printString(String type, String name, Object value) {
        if ("null".equals(type)) {
            return "<null name=\"" + name + "\"/>";
        }
        if (name == null && value == null) {
            return "<" + type + ">";
        }
        if (value == null) {
            return "<" + type + " name=\"" + name + "\">";
        }
        if (name == null) {
            return "<" + type + ">" + value + "</" + type + ">";
        }
        return "<" + type + " name=\"" + name + "\">" + value + "</" + type + ">";
    }
}
