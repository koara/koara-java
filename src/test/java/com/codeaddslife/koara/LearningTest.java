package com.codeaddslife.koara;

import com.codeaddslife.koara.ast.Document;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by andy on 26/06/2017.
 */
public class LearningTest {

    @Test
    public void test() throws Exception {
        Parser parser = new Parser();
        parser.parse(readFile("/Users/andy/git/koara/koara-swift/testsuite/input/code/code-050-block-c.kd"));
    }

    private String readFile(String path) throws IOException {
        BufferedReader reader = null;
        try {
            StringBuffer fileData = new StringBuffer();
            reader = new BufferedReader(new FileReader(path));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            return fileData.toString();
        } finally {
            reader.close();
        }
    }

}
