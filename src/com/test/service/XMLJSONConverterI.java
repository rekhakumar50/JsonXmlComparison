package com.test.service;

import java.io.File;
import java.io.IOException;

public interface XMLJSONConverterI {

    String convertJSONtoXML(final String jsonContent, File xmlConvertedFile) throws IOException;

}
