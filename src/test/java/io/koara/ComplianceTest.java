package io.koara;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ComplianceTest {

	private static Path TESTSUITE_FOLDER = Paths.get("src/test/resources");
	
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
		for(File module : TESTSUITE_FOLDER.toFile().listFiles()) {
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
		String html = readFile(TESTSUITE_FOLDER.resolve(module).resolve(testcase + ".htm"));
		Koara koara = new Koara(new FileInputStream(TESTSUITE_FOLDER.resolve(module).resolve(testcase + ".kd").toFile()));
		ASTDocument document = koara.Document();
		Html5Renderer renderer = new Html5Renderer();
		document.jjtAccept(renderer, null);
		assertEquals(html, renderer.getOutput());
		
	}
	
	private String readFile(Path path) throws IOException {
    	byte[] encoded = Files.readAllBytes(path);
    	return Charset.forName("UTF-8").decode(ByteBuffer.wrap(encoded)).toString();
    }
	
	
	
}