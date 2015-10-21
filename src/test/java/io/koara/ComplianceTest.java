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