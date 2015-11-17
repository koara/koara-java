/*
 * Copyright 2015 the original author or authors.
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

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.koara.ast.Document;
import io.koara.renderer.HtmlRenderer;

@RunWith(Parameterized.class)
public class ComplianceTest {

	private static final String TESTSUITE_FOLDER = "src/test/resources";
	
	private String module;
	private String testcase;
	
    private static List<String> include = Arrays.asList();
	
	public ComplianceTest(String module, String testcase) {
  		this.module = module;
  		this.testcase = testcase;
    }
	
	@Parameters(name= "{0}: {1}")
	public static Iterable<Object[]> data() {
		List<Object[]> modules = new ArrayList<Object[]>();
		for(File module : new File(TESTSUITE_FOLDER).listFiles()) {
			if(include.size() == 0 || include.contains(module.getName())) {
				for(File testcase : module.listFiles()) {
					if(testcase.getName().endsWith(".kd")) {
						modules.add(new Object[]{module.getName(), testcase.getName().substring(0, testcase.getName().length() - 3)});
					}
				}
			}
		}
		return modules;
	}

	@Test
	public void output() throws Exception {
		String html = readFile(TESTSUITE_FOLDER + "/" + module + "/" + testcase + ".htm");
		String kd = readFile(TESTSUITE_FOLDER + "/" + module + "/" + testcase + ".kd");
		
		Parser parser = new Parser();
		Document document = parser.parse(kd); // Generate AST
		
		HtmlRenderer renderer = new HtmlRenderer();
		document.accept(renderer);
		assertEquals(html, renderer.getOutput());
	}
	
	private String readFile(String path) throws IOException {
		BufferedReader reader = null;
		try {
			StringBuffer fileData = new StringBuffer();
			reader = new BufferedReader(new FileReader(path));
			char[] buf = new char[1024];
	        int numRead=0;
	        while((numRead=reader.read(buf)) != -1){
	            String readData = String.valueOf(buf, 0, numRead);
	            fileData.append(readData);
	        }
	        return fileData.toString();
		} finally {
			reader.close();
		}
    }
	
}