package com.test;

import com.test.service.JsonToXmlConvertor;
import com.test.util.Utils;

import java.io.File;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        String jsonFilePath = null;
        String xmlFilePath = null;

        /*
        * get the args value
        * from the command-line argument
        * */
        if (args.length == 0 || args.length > 2) {
            System.out.println("Invalid Arguments!!");
            System.exit(1);
        } else if (args.length == 1) {
            jsonFilePath = args[0];
            Utils.checkFileExist(new File(jsonFilePath));
        } else {
            jsonFilePath = args[0];
            xmlFilePath = args[1];
            Utils.checkFileExist(new File(jsonFilePath));
            Utils.checkFileExist(new File(xmlFilePath));
        }

        JsonToXmlConvertor jsonToXmlConvertor = new JsonToXmlConvertor();

        //creating output xml file
        File xmlConvertedFile = new File("convertedXml.xml");

        //converting json to xml
        String convertedXmlPath = jsonToXmlConvertor.convertJSONtoXML(jsonFilePath, xmlConvertedFile);


        if (xmlFilePath != null) {
            /*
             * compares the converted xml file with the actual xml file
             * */
            if (Utils.compareOldAndNewXml(xmlFilePath, convertedXmlPath)) {
                System.out.println("Fail : JSON and XML content does not match!!");
            } else {
                System.out.println("Pass : JSON and XML content matched!!");
            }
        } else {
            /*
            * executes then the task is to convert json to xml
            * returns the path of the converted xml file
            * */
            System.out.println("Converted Json to Xml file. Path Location: " + convertedXmlPath);
        }

    }
}
