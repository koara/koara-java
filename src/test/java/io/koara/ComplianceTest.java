/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.koara;

import static io.koara.TestUtils.readFile;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.koara.ast.Document;
import io.koara.renderer.Html5Renderer;
import io.koara.renderer.XmlRenderer;

@RunWith(Parameterized.class)
public class ComplianceTest {

    private static final String TESTSUITE_FOLDER = "src/test/testsuite";

    private String module;
    private String testcase;

    public ComplianceTest(String module, String testcase) {
        this.module = module;
        this.testcase = testcase;
    }

    @Parameters(name = "{0}: {1}")
    public static Iterable<Object[]> data() {
        List<Object[]> modules = new ArrayList<Object[]>();
        for (File module : new File(TESTSUITE_FOLDER + "/input").listFiles()) {
        	if(!module.getName().startsWith("end2end")) {
        		for (File testcase : module.listFiles()) {
        			System.out.println("-" + testcase.getName().substring(0, testcase.getName().length() - 3));
        			modules.add(new Object[] { module.getName(),
                          testcase.getName().substring(0, testcase.getName().length() - 3) });
        		}
        		
        	}
        }
        return modules;
    }

    @Test
    public void testKoaraToHtml5() throws Exception {
    	String kd = readFile(TESTSUITE_FOLDER + "/input/" + module + "/" + testcase + ".kd");
        String html = readFile(TESTSUITE_FOLDER + "/output/html5/" + module + "/" + testcase + ".htm");

        Parser parser = new Parser();
        Document document = parser.parse(kd);
        Html5Renderer renderer = new Html5Renderer();
        document.accept(renderer);
        assertEquals(html, renderer.getOutput());
    }
    
    @Test
    public void testKoaraToXml() throws Exception {
    	String kd = readFile(TESTSUITE_FOLDER + "/input/" + module + "/" + testcase + ".kd");
        String xml = readFile(TESTSUITE_FOLDER + "/output/xml/" + module + "/" + testcase + ".xml");

        Parser parser = new Parser();
        Document document = parser.parse(kd);
        XmlRenderer renderer = new XmlRenderer();
        document.accept(renderer);
        assertEquals(xml, renderer.getOutput());
    }

}