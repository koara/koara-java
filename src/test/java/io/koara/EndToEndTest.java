package io.koara;

import static io.koara.TestUtils.readFile;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import io.koara.ast.Document;
import io.koara.renderer.HtmlRenderer;

import static io.koara.Module.*;

public class EndToEndTest {

	private static final String TESTSUITE_FOLDER = "src/test/resources";
	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	@Test
	public void scenario000001() throws Exception {
	  assertOutput("scenario000001", PARAGRAPHS);
	}

	@Test
	public void scenario000002() throws Exception {
	  assertOutput("scenario000002", HEADINGS);
	}

	@Test
	public void scenario000003() throws Exception {
	  assertOutput("scenario000003", PARAGRAPHS, HEADINGS);
	}

	@Test
	public void scenario000004() throws Exception {
	  assertOutput("scenario000004", LISTS);
	}

//	@Test
//	public void scenario000005() throws Exception {
//	  assertOutput("scenario000005", new String[]{"paragraphs","lists"});
//	}
//
//	@Test
//	public void scenario000006() throws Exception {
//	  assertOutput("scenario000006", new String[]{"headers","lists"});
//	}
//
//	@Test
//	public void scenario000007() throws Exception {
//	  assertOutput("scenario000007", new String[]{"paragraphs","headers","lists"});
//	}
//
//	@Test
//	public void scenario000008() throws Exception {
//	  assertOutput("scenario000008", new String[]{"links"});
//	}
//
//	@Test
//	public void scenario000009() throws Exception {
//	  assertOutput("scenario000009", new String[]{"paragraphs","links"});
//	}
//
//	@Test
//	public void scenario000010() throws Exception {
//	  assertOutput("scenario000010", new String[]{"headers","links"});
//	}
//
//	@Test
//	public void scenario000011() throws Exception {
//	  assertOutput("scenario000011", new String[]{"paragraphs","headers","links"});
//	}
//
//	@Test
//	public void scenario000012() throws Exception {
//	  assertOutput("scenario000012", new String[]{"lists","links"});
//	}
//
//	@Test
//	public void scenario000013() throws Exception {
//	  assertOutput("scenario000013", new String[]{"paragraphs","lists","links"});
//	}
//
//	@Test
//	public void scenario000014() throws Exception {
//	  assertOutput("scenario000014", new String[]{"headers","lists","links"});
//	}
//
//	@Test
//	public void scenario000015() throws Exception {
//	  assertOutput("scenario000015", new String[]{"paragraphs","headers","lists","links"});
//	}
//
//	@Test
//	public void scenario000016() throws Exception {
//	  assertOutput("scenario000016", new String[]{"images"});
//	}
//
//	@Test
//	public void scenario000017() throws Exception {
//	  assertOutput("scenario000017", new String[]{"paragraphs","images"});
//	}
//
//	@Test
//	public void scenario000018() throws Exception {
//	  assertOutput("scenario000018", new String[]{"headers","images"});
//	}
//
//	@Test
//	public void scenario000019() throws Exception {
//	  assertOutput("scenario000019", new String[]{"paragraphs","headers","images"});
//	}
//
//	@Test
//	public void scenario000020() throws Exception {
//	  assertOutput("scenario000020", new String[]{"lists","images"});
//	}
//
//	@Test
//	public void scenario000021() throws Exception {
//	  assertOutput("scenario000021", new String[]{"paragraphs","lists","images"});
//	}
//
//	@Test
//	public void scenario000022() throws Exception {
//	  assertOutput("scenario000022", new String[]{"headers","lists","images"});
//	}
//
//	@Test
//	public void scenario000023() throws Exception {
//	  assertOutput("scenario000023", new String[]{"paragraphs","headers","lists","images"});
//	}
//
//	@Test
//	public void scenario000024() throws Exception {
//	  assertOutput("scenario000024", new String[]{"links","images"});
//	}
//
//	@Test
//	public void scenario000025() throws Exception {
//	  assertOutput("scenario000025", new String[]{"paragraphs","links","images"});
//	}
//
//	@Test
//	public void scenario000026() throws Exception {
//	  assertOutput("scenario000026", new String[]{"headers","links","images"});
//	}
//
//	@Test
//	public void scenario000027() throws Exception {
//	  assertOutput("scenario000027", new String[]{"paragraphs","headers","links","images"});
//	}
//
//	@Test
//	public void scenario000028() throws Exception {
//	  assertOutput("scenario000028", new String[]{"lists","links","images"});
//	}
//
//	@Test
//	public void scenario000029() throws Exception {
//	  assertOutput("scenario000029", new String[]{"paragraphs","lists","links","images"});
//	}
//
//	@Test
//	public void scenario000030() throws Exception {
//	  assertOutput("scenario000030", new String[]{"headers","lists","links","images"});
//	}
//
//	@Test
//	public void scenario000031() throws Exception {
//	  assertOutput("scenario000031", new String[]{"paragraphs","headers","lists","links","images"});
//	}
//
//	@Test
//	public void scenario000032() throws Exception {
//	  assertOutput("scenario000032", new String[]{"formatting"});
//	}
//
//	@Test
//	public void scenario000033() throws Exception {
//	  assertOutput("scenario000033", new String[]{"paragraphs","formatting"});
//	}
//
//	@Test
//	public void scenario000034() throws Exception {
//	  assertOutput("scenario000034", new String[]{"headers","formatting"});
//	}
//
//	@Test
//	public void scenario000035() throws Exception {
//	  assertOutput("scenario000035", new String[]{"paragraphs","headers","formatting"});
//	}
//
//	@Test
//	public void scenario000036() throws Exception {
//	  assertOutput("scenario000036", new String[]{"lists","formatting"});
//	}
//
//	@Test
//	public void scenario000037() throws Exception {
//	  assertOutput("scenario000037", new String[]{"paragraphs","lists","formatting"});
//	}
//
//	@Test
//	public void scenario000038() throws Exception {
//	  assertOutput("scenario000038", new String[]{"headers","lists","formatting"});
//	}
//
//	@Test
//	public void scenario000039() throws Exception {
//	  assertOutput("scenario000039", new String[]{"paragraphs","headers","lists","formatting"});
//	}
//
//	@Test
//	public void scenario000040() throws Exception {
//	  assertOutput("scenario000040", new String[]{"links","formatting"});
//	}
//
//	@Test
//	public void scenario000041() throws Exception {
//	  assertOutput("scenario000041", new String[]{"paragraphs","links","formatting"});
//	}
//
//	@Test
//	public void scenario000042() throws Exception {
//	  assertOutput("scenario000042", new String[]{"headers","links","formatting"});
//	}
//
//	@Test
//	public void scenario000043() throws Exception {
//	  assertOutput("scenario000043", new String[]{"paragraphs","headers","links","formatting"});
//	}
//
//	@Test
//	public void scenario000044() throws Exception {
//	  assertOutput("scenario000044", new String[]{"lists","links","formatting"});
//	}
//
//	@Test
//	public void scenario000045() throws Exception {
//	  assertOutput("scenario000045", new String[]{"paragraphs","lists","links","formatting"});
//	}
//
//	@Test
//	public void scenario000046() throws Exception {
//	  assertOutput("scenario000046", new String[]{"headers","lists","links","formatting"});
//	}
//
//	@Test
//	public void scenario000047() throws Exception {
//	  assertOutput("scenario000047", new String[]{"paragraphs","headers","lists","links","formatting"});
//	}
//
//	@Test
//	public void scenario000048() throws Exception {
//	  assertOutput("scenario000048", new String[]{"images","formatting"});
//	}
//
//	@Test
//	public void scenario000049() throws Exception {
//	  assertOutput("scenario000049", new String[]{"paragraphs","images","formatting"});
//	}
//
//	@Test
//	public void scenario000050() throws Exception {
//	  assertOutput("scenario000050", new String[]{"headers","images","formatting"});
//	}
//
//	@Test
//	public void scenario000051() throws Exception {
//	  assertOutput("scenario000051", new String[]{"paragraphs","headers","images","formatting"});
//	}
//
//	@Test
//	public void scenario000052() throws Exception {
//	  assertOutput("scenario000052", new String[]{"lists","images","formatting"});
//	}
//
//	@Test
//	public void scenario000053() throws Exception {
//	  assertOutput("scenario000053", new String[]{"paragraphs","lists","images","formatting"});
//	}
//
//	@Test
//	public void scenario000054() throws Exception {
//	  assertOutput("scenario000054", new String[]{"headers","lists","images","formatting"});
//	}
//
//	@Test
//	public void scenario000055() throws Exception {
//	  assertOutput("scenario000055", new String[]{"paragraphs","headers","lists","images","formatting"});
//	}
//
//	@Test
//	public void scenario000056() throws Exception {
//	  assertOutput("scenario000056", new String[]{"links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000057() throws Exception {
//	  assertOutput("scenario000057", new String[]{"paragraphs","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000058() throws Exception {
//	  assertOutput("scenario000058", new String[]{"headers","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000059() throws Exception {
//	  assertOutput("scenario000059", new String[]{"paragraphs","headers","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000060() throws Exception {
//	  assertOutput("scenario000060", new String[]{"lists","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000061() throws Exception {
//	  assertOutput("scenario000061", new String[]{"paragraphs","lists","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000062() throws Exception {
//	  assertOutput("scenario000062", new String[]{"headers","lists","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000063() throws Exception {
//	  assertOutput("scenario000063", new String[]{"paragraphs","headers","lists","links","images","formatting"});
//	}
//
//	@Test
//	public void scenario000064() throws Exception {
//	  assertOutput("scenario000064", new String[]{"blockquotes"});
//	}
//
//	@Test
//	public void scenario000065() throws Exception {
//	  assertOutput("scenario000065", new String[]{"paragraphs","blockquotes"});
//	}
//
//	@Test
//	public void scenario000066() throws Exception {
//	  assertOutput("scenario000066", new String[]{"headers","blockquotes"});
//	}
//
//	@Test
//	public void scenario000067() throws Exception {
//	  assertOutput("scenario000067", new String[]{"paragraphs","headers","blockquotes"});
//	}
//
//	@Test
//	public void scenario000068() throws Exception {
//	  assertOutput("scenario000068", new String[]{"lists","blockquotes"});
//	}
//
//	@Test
//	public void scenario000069() throws Exception {
//	  assertOutput("scenario000069", new String[]{"paragraphs","lists","blockquotes"});
//	}
//
//	@Test
//	public void scenario000070() throws Exception {
//	  assertOutput("scenario000070", new String[]{"headers","lists","blockquotes"});
//	}
//
//	@Test
//	public void scenario000071() throws Exception {
//	  assertOutput("scenario000071", new String[]{"paragraphs","headers","lists","blockquotes"});
//	}
//
//	@Test
//	public void scenario000072() throws Exception {
//	  assertOutput("scenario000072", new String[]{"links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000073() throws Exception {
//	  assertOutput("scenario000073", new String[]{"paragraphs","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000074() throws Exception {
//	  assertOutput("scenario000074", new String[]{"headers","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000075() throws Exception {
//	  assertOutput("scenario000075", new String[]{"paragraphs","headers","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000076() throws Exception {
//	  assertOutput("scenario000076", new String[]{"lists","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000077() throws Exception {
//	  assertOutput("scenario000077", new String[]{"paragraphs","lists","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000078() throws Exception {
//	  assertOutput("scenario000078", new String[]{"headers","lists","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000079() throws Exception {
//	  assertOutput("scenario000079", new String[]{"paragraphs","headers","lists","links","blockquotes"});
//	}
//
//	@Test
//	public void scenario000080() throws Exception {
//	  assertOutput("scenario000080", new String[]{"images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000081() throws Exception {
//	  assertOutput("scenario000081", new String[]{"paragraphs","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000082() throws Exception {
//	  assertOutput("scenario000082", new String[]{"headers","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000083() throws Exception {
//	  assertOutput("scenario000083", new String[]{"paragraphs","headers","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000084() throws Exception {
//	  assertOutput("scenario000084", new String[]{"lists","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000085() throws Exception {
//	  assertOutput("scenario000085", new String[]{"paragraphs","lists","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000086() throws Exception {
//	  assertOutput("scenario000086", new String[]{"headers","lists","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000087() throws Exception {
//	  assertOutput("scenario000087", new String[]{"paragraphs","headers","lists","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000088() throws Exception {
//	  assertOutput("scenario000088", new String[]{"links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000089() throws Exception {
//	  assertOutput("scenario000089", new String[]{"paragraphs","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000090() throws Exception {
//	  assertOutput("scenario000090", new String[]{"headers","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000091() throws Exception {
//	  assertOutput("scenario000091", new String[]{"paragraphs","headers","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000092() throws Exception {
//	  assertOutput("scenario000092", new String[]{"lists","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000093() throws Exception {
//	  assertOutput("scenario000093", new String[]{"paragraphs","lists","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000094() throws Exception {
//	  assertOutput("scenario000094", new String[]{"headers","lists","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000095() throws Exception {
//	  assertOutput("scenario000095", new String[]{"paragraphs","headers","lists","links","images","blockquotes"});
//	}
//
//	@Test
//	public void scenario000096() throws Exception {
//	  assertOutput("scenario000096", new String[]{"formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000097() throws Exception {
//	  assertOutput("scenario000097", new String[]{"paragraphs","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000098() throws Exception {
//	  assertOutput("scenario000098", new String[]{"headers","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000099() throws Exception {
//	  assertOutput("scenario000099", new String[]{"paragraphs","headers","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000100() throws Exception {
//	  assertOutput("scenario000100", new String[]{"lists","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000101() throws Exception {
//	  assertOutput("scenario000101", new String[]{"paragraphs","lists","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000102() throws Exception {
//	  assertOutput("scenario000102", new String[]{"headers","lists","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000103() throws Exception {
//	  assertOutput("scenario000103", new String[]{"paragraphs","headers","lists","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000104() throws Exception {
//	  assertOutput("scenario000104", new String[]{"links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000105() throws Exception {
//	  assertOutput("scenario000105", new String[]{"paragraphs","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000106() throws Exception {
//	  assertOutput("scenario000106", new String[]{"headers","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000107() throws Exception {
//	  assertOutput("scenario000107", new String[]{"paragraphs","headers","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000108() throws Exception {
//	  assertOutput("scenario000108", new String[]{"lists","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000109() throws Exception {
//	  assertOutput("scenario000109", new String[]{"paragraphs","lists","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000110() throws Exception {
//	  assertOutput("scenario000110", new String[]{"headers","lists","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000111() throws Exception {
//	  assertOutput("scenario000111", new String[]{"paragraphs","headers","lists","links","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000112() throws Exception {
//	  assertOutput("scenario000112", new String[]{"images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000113() throws Exception {
//	  assertOutput("scenario000113", new String[]{"paragraphs","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000114() throws Exception {
//	  assertOutput("scenario000114", new String[]{"headers","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000115() throws Exception {
//	  assertOutput("scenario000115", new String[]{"paragraphs","headers","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000116() throws Exception {
//	  assertOutput("scenario000116", new String[]{"lists","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000117() throws Exception {
//	  assertOutput("scenario000117", new String[]{"paragraphs","lists","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000118() throws Exception {
//	  assertOutput("scenario000118", new String[]{"headers","lists","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000119() throws Exception {
//	  assertOutput("scenario000119", new String[]{"paragraphs","headers","lists","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000120() throws Exception {
//	  assertOutput("scenario000120", new String[]{"links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000121() throws Exception {
//	  assertOutput("scenario000121", new String[]{"paragraphs","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000122() throws Exception {
//	  assertOutput("scenario000122", new String[]{"headers","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000123() throws Exception {
//	  assertOutput("scenario000123", new String[]{"paragraphs","headers","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000124() throws Exception {
//	  assertOutput("scenario000124", new String[]{"lists","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000125() throws Exception {
//	  assertOutput("scenario000125", new String[]{"paragraphs","lists","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000126() throws Exception {
//	  assertOutput("scenario000126", new String[]{"headers","lists","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000127() throws Exception {
//	  assertOutput("scenario000127", new String[]{"paragraphs","headers","lists","links","images","formatting","blockquotes"});
//	}
//
//	@Test
//	public void scenario000128() throws Exception {
//	  assertOutput("scenario000128", new String[]{"code"});
//	}
//
//	@Test
//	public void scenario000129() throws Exception {
//	  assertOutput("scenario000129", new String[]{"paragraphs","code"});
//	}
//
//	@Test
//	public void scenario000130() throws Exception {
//	  assertOutput("scenario000130", new String[]{"headers","code"});
//	}
//
//	@Test
//	public void scenario000131() throws Exception {
//	  assertOutput("scenario000131", new String[]{"paragraphs","headers","code"});
//	}
//
//	@Test
//	public void scenario000132() throws Exception {
//	  assertOutput("scenario000132", new String[]{"lists","code"});
//	}
//
//	@Test
//	public void scenario000133() throws Exception {
//	  assertOutput("scenario000133", new String[]{"paragraphs","lists","code"});
//	}
//
//	@Test
//	public void scenario000134() throws Exception {
//	  assertOutput("scenario000134", new String[]{"headers","lists","code"});
//	}
//
//	@Test
//	public void scenario000135() throws Exception {
//	  assertOutput("scenario000135", new String[]{"paragraphs","headers","lists","code"});
//	}
//
//	@Test
//	public void scenario000136() throws Exception {
//	  assertOutput("scenario000136", new String[]{"links","code"});
//	}
//
//	@Test
//	public void scenario000137() throws Exception {
//	  assertOutput("scenario000137", new String[]{"paragraphs","links","code"});
//	}
//
//	@Test
//	public void scenario000138() throws Exception {
//	  assertOutput("scenario000138", new String[]{"headers","links","code"});
//	}
//
//	@Test
//	public void scenario000139() throws Exception {
//	  assertOutput("scenario000139", new String[]{"paragraphs","headers","links","code"});
//	}
//
//	@Test
//	public void scenario000140() throws Exception {
//	  assertOutput("scenario000140", new String[]{"lists","links","code"});
//	}
//
//	@Test
//	public void scenario000141() throws Exception {
//	  assertOutput("scenario000141", new String[]{"paragraphs","lists","links","code"});
//	}
//
//	@Test
//	public void scenario000142() throws Exception {
//	  assertOutput("scenario000142", new String[]{"headers","lists","links","code"});
//	}
//
//	@Test
//	public void scenario000143() throws Exception {
//	  assertOutput("scenario000143", new String[]{"paragraphs","headers","lists","links","code"});
//	}
//
//	@Test
//	public void scenario000144() throws Exception {
//	  assertOutput("scenario000144", new String[]{"images","code"});
//	}
//
//	@Test
//	public void scenario000145() throws Exception {
//	  assertOutput("scenario000145", new String[]{"paragraphs","images","code"});
//	}
//
//	@Test
//	public void scenario000146() throws Exception {
//	  assertOutput("scenario000146", new String[]{"headers","images","code"});
//	}
//
//	@Test
//	public void scenario000147() throws Exception {
//	  assertOutput("scenario000147", new String[]{"paragraphs","headers","images","code"});
//	}
//
//	@Test
//	public void scenario000148() throws Exception {
//	  assertOutput("scenario000148", new String[]{"lists","images","code"});
//	}
//
//	@Test
//	public void scenario000149() throws Exception {
//	  assertOutput("scenario000149", new String[]{"paragraphs","lists","images","code"});
//	}
//
//	@Test
//	public void scenario000150() throws Exception {
//	  assertOutput("scenario000150", new String[]{"headers","lists","images","code"});
//	}
//
//	@Test
//	public void scenario000151() throws Exception {
//	  assertOutput("scenario000151", new String[]{"paragraphs","headers","lists","images","code"});
//	}
//
//	@Test
//	public void scenario000152() throws Exception {
//	  assertOutput("scenario000152", new String[]{"links","images","code"});
//	}
//
//	@Test
//	public void scenario000153() throws Exception {
//	  assertOutput("scenario000153", new String[]{"paragraphs","links","images","code"});
//	}
//
//	@Test
//	public void scenario000154() throws Exception {
//	  assertOutput("scenario000154", new String[]{"headers","links","images","code"});
//	}
//
//	@Test
//	public void scenario000155() throws Exception {
//	  assertOutput("scenario000155", new String[]{"paragraphs","headers","links","images","code"});
//	}
//
//	@Test
//	public void scenario000156() throws Exception {
//	  assertOutput("scenario000156", new String[]{"lists","links","images","code"});
//	}
//
//	@Test
//	public void scenario000157() throws Exception {
//	  assertOutput("scenario000157", new String[]{"paragraphs","lists","links","images","code"});
//	}
//
//	@Test
//	public void scenario000158() throws Exception {
//	  assertOutput("scenario000158", new String[]{"headers","lists","links","images","code"});
//	}
//
//	@Test
//	public void scenario000159() throws Exception {
//	  assertOutput("scenario000159", new String[]{"paragraphs","headers","lists","links","images","code"});
//	}
//
//	@Test
//	public void scenario000160() throws Exception {
//	  assertOutput("scenario000160", new String[]{"formatting","code"});
//	}
//
//	@Test
//	public void scenario000161() throws Exception {
//	  assertOutput("scenario000161", new String[]{"paragraphs","formatting","code"});
//	}
//
//	@Test
//	public void scenario000162() throws Exception {
//	  assertOutput("scenario000162", new String[]{"headers","formatting","code"});
//	}
//
//	@Test
//	public void scenario000163() throws Exception {
//	  assertOutput("scenario000163", new String[]{"paragraphs","headers","formatting","code"});
//	}
//
//	@Test
//	public void scenario000164() throws Exception {
//	  assertOutput("scenario000164", new String[]{"lists","formatting","code"});
//	}
//
//	@Test
//	public void scenario000165() throws Exception {
//	  assertOutput("scenario000165", new String[]{"paragraphs","lists","formatting","code"});
//	}
//
//	@Test
//	public void scenario000166() throws Exception {
//	  assertOutput("scenario000166", new String[]{"headers","lists","formatting","code"});
//	}
//
//	@Test
//	public void scenario000167() throws Exception {
//	  assertOutput("scenario000167", new String[]{"paragraphs","headers","lists","formatting","code"});
//	}
//
//	@Test
//	public void scenario000168() throws Exception {
//	  assertOutput("scenario000168", new String[]{"links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000169() throws Exception {
//	  assertOutput("scenario000169", new String[]{"paragraphs","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000170() throws Exception {
//	  assertOutput("scenario000170", new String[]{"headers","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000171() throws Exception {
//	  assertOutput("scenario000171", new String[]{"paragraphs","headers","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000172() throws Exception {
//	  assertOutput("scenario000172", new String[]{"lists","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000173() throws Exception {
//	  assertOutput("scenario000173", new String[]{"paragraphs","lists","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000174() throws Exception {
//	  assertOutput("scenario000174", new String[]{"headers","lists","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000175() throws Exception {
//	  assertOutput("scenario000175", new String[]{"paragraphs","headers","lists","links","formatting","code"});
//	}
//
//	@Test
//	public void scenario000176() throws Exception {
//	  assertOutput("scenario000176", new String[]{"images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000177() throws Exception {
//	  assertOutput("scenario000177", new String[]{"paragraphs","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000178() throws Exception {
//	  assertOutput("scenario000178", new String[]{"headers","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000179() throws Exception {
//	  assertOutput("scenario000179", new String[]{"paragraphs","headers","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000180() throws Exception {
//	  assertOutput("scenario000180", new String[]{"lists","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000181() throws Exception {
//	  assertOutput("scenario000181", new String[]{"paragraphs","lists","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000182() throws Exception {
//	  assertOutput("scenario000182", new String[]{"headers","lists","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000183() throws Exception {
//	  assertOutput("scenario000183", new String[]{"paragraphs","headers","lists","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000184() throws Exception {
//	  assertOutput("scenario000184", new String[]{"links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000185() throws Exception {
//	  assertOutput("scenario000185", new String[]{"paragraphs","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000186() throws Exception {
//	  assertOutput("scenario000186", new String[]{"headers","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000187() throws Exception {
//	  assertOutput("scenario000187", new String[]{"paragraphs","headers","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000188() throws Exception {
//	  assertOutput("scenario000188", new String[]{"lists","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000189() throws Exception {
//	  assertOutput("scenario000189", new String[]{"paragraphs","lists","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000190() throws Exception {
//	  assertOutput("scenario000190", new String[]{"headers","lists","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000191() throws Exception {
//	  assertOutput("scenario000191", new String[]{"paragraphs","headers","lists","links","images","formatting","code"});
//	}
//
//	@Test
//	public void scenario000192() throws Exception {
//	  assertOutput("scenario000192", new String[]{"blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000193() throws Exception {
//	  assertOutput("scenario000193", new String[]{"paragraphs","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000194() throws Exception {
//	  assertOutput("scenario000194", new String[]{"headers","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000195() throws Exception {
//	  assertOutput("scenario000195", new String[]{"paragraphs","headers","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000196() throws Exception {
//	  assertOutput("scenario000196", new String[]{"lists","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000197() throws Exception {
//	  assertOutput("scenario000197", new String[]{"paragraphs","lists","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000198() throws Exception {
//	  assertOutput("scenario000198", new String[]{"headers","lists","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000199() throws Exception {
//	  assertOutput("scenario000199", new String[]{"paragraphs","headers","lists","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000200() throws Exception {
//	  assertOutput("scenario000200", new String[]{"links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000201() throws Exception {
//	  assertOutput("scenario000201", new String[]{"paragraphs","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000202() throws Exception {
//	  assertOutput("scenario000202", new String[]{"headers","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000203() throws Exception {
//	  assertOutput("scenario000203", new String[]{"paragraphs","headers","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000204() throws Exception {
//	  assertOutput("scenario000204", new String[]{"lists","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000205() throws Exception {
//	  assertOutput("scenario000205", new String[]{"paragraphs","lists","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000206() throws Exception {
//	  assertOutput("scenario000206", new String[]{"headers","lists","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000207() throws Exception {
//	  assertOutput("scenario000207", new String[]{"paragraphs","headers","lists","links","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000208() throws Exception {
//	  assertOutput("scenario000208", new String[]{"images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000209() throws Exception {
//	  assertOutput("scenario000209", new String[]{"paragraphs","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000210() throws Exception {
//	  assertOutput("scenario000210", new String[]{"headers","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000211() throws Exception {
//	  assertOutput("scenario000211", new String[]{"paragraphs","headers","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000212() throws Exception {
//	  assertOutput("scenario000212", new String[]{"lists","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000213() throws Exception {
//	  assertOutput("scenario000213", new String[]{"paragraphs","lists","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000214() throws Exception {
//	  assertOutput("scenario000214", new String[]{"headers","lists","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000215() throws Exception {
//	  assertOutput("scenario000215", new String[]{"paragraphs","headers","lists","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000216() throws Exception {
//	  assertOutput("scenario000216", new String[]{"links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000217() throws Exception {
//	  assertOutput("scenario000217", new String[]{"paragraphs","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000218() throws Exception {
//	  assertOutput("scenario000218", new String[]{"headers","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000219() throws Exception {
//	  assertOutput("scenario000219", new String[]{"paragraphs","headers","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000220() throws Exception {
//	  assertOutput("scenario000220", new String[]{"lists","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000221() throws Exception {
//	  assertOutput("scenario000221", new String[]{"paragraphs","lists","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000222() throws Exception {
//	  assertOutput("scenario000222", new String[]{"headers","lists","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000223() throws Exception {
//	  assertOutput("scenario000223", new String[]{"paragraphs","headers","lists","links","images","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000224() throws Exception {
//	  assertOutput("scenario000224", new String[]{"formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000225() throws Exception {
//	  assertOutput("scenario000225", new String[]{"paragraphs","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000226() throws Exception {
//	  assertOutput("scenario000226", new String[]{"headers","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000227() throws Exception {
//	  assertOutput("scenario000227", new String[]{"paragraphs","headers","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000228() throws Exception {
//	  assertOutput("scenario000228", new String[]{"lists","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000229() throws Exception {
//	  assertOutput("scenario000229", new String[]{"paragraphs","lists","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000230() throws Exception {
//	  assertOutput("scenario000230", new String[]{"headers","lists","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000231() throws Exception {
//	  assertOutput("scenario000231", new String[]{"paragraphs","headers","lists","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000232() throws Exception {
//	  assertOutput("scenario000232", new String[]{"links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000233() throws Exception {
//	  assertOutput("scenario000233", new String[]{"paragraphs","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000234() throws Exception {
//	  assertOutput("scenario000234", new String[]{"headers","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000235() throws Exception {
//	  assertOutput("scenario000235", new String[]{"paragraphs","headers","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000236() throws Exception {
//	  assertOutput("scenario000236", new String[]{"lists","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000237() throws Exception {
//	  assertOutput("scenario000237", new String[]{"paragraphs","lists","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000238() throws Exception {
//	  assertOutput("scenario000238", new String[]{"headers","lists","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000239() throws Exception {
//	  assertOutput("scenario000239", new String[]{"paragraphs","headers","lists","links","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000240() throws Exception {
//	  assertOutput("scenario000240", new String[]{"images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000241() throws Exception {
//	  assertOutput("scenario000241", new String[]{"paragraphs","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000242() throws Exception {
//	  assertOutput("scenario000242", new String[]{"headers","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000243() throws Exception {
//	  assertOutput("scenario000243", new String[]{"paragraphs","headers","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000244() throws Exception {
//	  assertOutput("scenario000244", new String[]{"lists","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000245() throws Exception {
//	  assertOutput("scenario000245", new String[]{"paragraphs","lists","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000246() throws Exception {
//	  assertOutput("scenario000246", new String[]{"headers","lists","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000247() throws Exception {
//	  assertOutput("scenario000247", new String[]{"paragraphs","headers","lists","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000248() throws Exception {
//	  assertOutput("scenario000248", new String[]{"links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000249() throws Exception {
//	  assertOutput("scenario000249", new String[]{"paragraphs","links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000250() throws Exception {
//	  assertOutput("scenario000250", new String[]{"headers","links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000251() throws Exception {
//	  assertOutput("scenario000251", new String[]{"paragraphs","headers","links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000252() throws Exception {
//	  assertOutput("scenario000252", new String[]{"lists","links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000253() throws Exception {
//	  assertOutput("scenario000253", new String[]{"paragraphs","lists","links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000254() throws Exception {
//	  assertOutput("scenario000254", new String[]{"headers","lists","links","images","formatting","blockquotes","code"});
//	}
//
//	@Test
//	public void scenario000255() throws Exception {
//	  assertOutput("scenario000255", new String[]{"paragraphs","headers","lists","links","images","formatting","blockquotes","code"});
//	}
//	
	private void assertOutput(String file, Module... includes) throws Exception {
		File kd = new File(TESTSUITE_FOLDER + "/e2e/e2e.kd");
		String html = readFile(TESTSUITE_FOLDER + "/e2e/" + file + ".htm");
		
		parser.setIncludes(includes);
		Document document = parser.parse(kd); // Generate AST
		HtmlRenderer renderer = new HtmlRenderer();
		document.accept(renderer);
		assertEquals(html, renderer.getOutput());
	}
	
}
