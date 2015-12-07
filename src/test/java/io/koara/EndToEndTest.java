package io.koara;

import static io.koara.TestUtils.readFile;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.PrintWriter;

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

	@Test
	public void scenario000005() throws Exception {
	  assertOutput("scenario000005", PARAGRAPHS, LISTS);
	}

	@Test
	public void scenario000006() throws Exception {
	  assertOutput("scenario000006", HEADINGS, LISTS);
	}

	@Test
	public void scenario000007() throws Exception {
	  assertOutput("scenario000007", PARAGRAPHS, HEADINGS, LISTS);
	}

	@Test
	public void scenario000008() throws Exception {
	  assertOutput("scenario000008", LINKS);
	}

	@Test
	public void scenario000009() throws Exception {
	  assertOutput("scenario000009", PARAGRAPHS, LINKS);
	}

	@Test
	public void scenario000010() throws Exception {
	  assertOutput("scenario000010", HEADINGS, LINKS);
	}

	@Test
	public void scenario000011() throws Exception {
	  assertOutput("scenario000011", PARAGRAPHS, HEADINGS, LINKS);
	}

	@Test
	public void scenario000012() throws Exception {
	  assertOutput("scenario000012", LISTS, LINKS);
	}

	@Test
	public void scenario000013() throws Exception {
	  assertOutput("scenario000013", PARAGRAPHS, LISTS, LINKS);
	}

	@Test
	public void scenario000014() throws Exception {
	  assertOutput("scenario000014", HEADINGS, LISTS, LINKS);
	}

	@Test
	public void scenario000015() throws Exception {
	  assertOutput("scenario000015", PARAGRAPHS, HEADINGS, LISTS, LINKS);
	}

	@Test
	public void scenario000016() throws Exception {
	  assertOutput("scenario000016", IMAGES);
	}

	@Test
	public void scenario000017() throws Exception {
	  assertOutput("scenario000017", PARAGRAPHS, IMAGES);
	}

	@Test
	public void scenario000018() throws Exception {
	  assertOutput("scenario000018", HEADINGS, IMAGES);
	}

	@Test
	public void scenario000019() throws Exception {
	  assertOutput("scenario000019", PARAGRAPHS, HEADINGS, IMAGES);
	}

	@Test
	public void scenario000020() throws Exception {
	  assertOutput("scenario000020", LISTS, IMAGES);
	}

	@Test
	public void scenario000021() throws Exception {
	  assertOutput("scenario000021", PARAGRAPHS, LISTS, IMAGES);
	}

	@Test
	public void scenario000022() throws Exception {
	  assertOutput("scenario000022", HEADINGS, LISTS, IMAGES);
	}

	@Test
	public void scenario000023() throws Exception {
	  assertOutput("scenario000023", PARAGRAPHS, HEADINGS, LISTS, IMAGES);
	}

	@Test
	public void scenario000024() throws Exception {
	  assertOutput("scenario000024", LINKS, IMAGES);
	}

	@Test
	public void scenario000025() throws Exception {
	  assertOutput("scenario000025", PARAGRAPHS, LINKS, IMAGES);
	}

	@Test
	public void scenario000026() throws Exception {
	  assertOutput("scenario000026", HEADINGS, LINKS, IMAGES);
	}

	@Test
	public void scenario000027() throws Exception {
	  assertOutput("scenario000027", PARAGRAPHS, HEADINGS, LINKS, IMAGES);
	}

	@Test
	public void scenario000028() throws Exception {
	  assertOutput("scenario000028", LISTS, LINKS, IMAGES);
	}

	@Test
	public void scenario000029() throws Exception {
	  assertOutput("scenario000029", PARAGRAPHS, LISTS, LINKS, IMAGES);
	}

	@Test
	public void scenario000030() throws Exception {
	  assertOutput("scenario000030", HEADINGS, LISTS, LINKS, IMAGES);
	}

	@Test
	public void scenario000031() throws Exception {
	  assertOutput("scenario000031", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES);
	}

	@Test
	public void scenario000032() throws Exception {
	  assertOutput("scenario000032", FORMATTING);
	}

	@Test
	public void scenario000033() throws Exception {
	  assertOutput("scenario000033", PARAGRAPHS, FORMATTING);
	}

	@Test
	public void scenario000034() throws Exception {
	  assertOutput("scenario000034", HEADINGS, FORMATTING);
	}

	@Test
	public void scenario000035() throws Exception {
	  assertOutput("scenario000035", PARAGRAPHS, HEADINGS, FORMATTING);
	}

	@Test
	public void scenario000036() throws Exception {
	  assertOutput("scenario000036", LISTS, FORMATTING);
	}

	@Test
	public void scenario000037() throws Exception {
	  assertOutput("scenario000037", PARAGRAPHS, LISTS, FORMATTING);
	}

	@Test
	public void scenario000038() throws Exception {
	  assertOutput("scenario000038", HEADINGS, LISTS, FORMATTING);
	}

	@Test
	public void scenario000039() throws Exception {
	  assertOutput("scenario000039", PARAGRAPHS, HEADINGS, LISTS, FORMATTING);
	}

	@Test
	public void scenario000040() throws Exception {
	  assertOutput("scenario000040", LINKS, FORMATTING);
	}

	@Test
	public void scenario000041() throws Exception {
	  assertOutput("scenario000041", PARAGRAPHS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000042() throws Exception {
	  assertOutput("scenario000042", HEADINGS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000043() throws Exception {
	  assertOutput("scenario000043", PARAGRAPHS, HEADINGS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000044() throws Exception {
	  assertOutput("scenario000044", LISTS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000045() throws Exception {
	  assertOutput("scenario000045", PARAGRAPHS, LISTS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000046() throws Exception {
	  assertOutput("scenario000046", HEADINGS, LISTS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000047() throws Exception {
	  assertOutput("scenario000047", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING);
	}

	@Test
	public void scenario000048() throws Exception {
	  assertOutput("scenario000048", IMAGES, FORMATTING);
	}

	@Test
	public void scenario000049() throws Exception {
	  assertOutput("scenario000049", PARAGRAPHS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000050() throws Exception {
	  assertOutput("scenario000050", HEADINGS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000051() throws Exception {
	  assertOutput("scenario000051", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000052() throws Exception {
	  assertOutput("scenario000052", LISTS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000053() throws Exception {
	  assertOutput("scenario000053", PARAGRAPHS, LISTS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000054() throws Exception {
	  assertOutput("scenario000054", HEADINGS, LISTS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000055() throws Exception {
	  assertOutput("scenario000055", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000056() throws Exception {
	  assertOutput("scenario000056", LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000057() throws Exception {
	  assertOutput("scenario000057", PARAGRAPHS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000058() throws Exception {
	  assertOutput("scenario000058", HEADINGS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000059() throws Exception {
	  assertOutput("scenario000059", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000060() throws Exception {
	  assertOutput("scenario000060", LISTS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000061() throws Exception {
	  assertOutput("scenario000061", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000062() throws Exception {
	  assertOutput("scenario000062", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000063() throws Exception {
	  assertOutput("scenario000063", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING);
	}

	@Test
	public void scenario000064() throws Exception {
	  assertOutput("scenario000064", BLOCKQUOTES);
	}

	@Test
	public void scenario000065() throws Exception {
	  assertOutput("scenario000065", PARAGRAPHS, BLOCKQUOTES);
	}

	@Test
	public void scenario000066() throws Exception {
	  assertOutput("scenario000066", HEADINGS, BLOCKQUOTES);
	}

	@Test
	public void scenario000067() throws Exception {
	  assertOutput("scenario000067", PARAGRAPHS, HEADINGS, BLOCKQUOTES);
	}

	@Test
	public void scenario000068() throws Exception {
	  assertOutput("scenario000068", LISTS, BLOCKQUOTES);
	}

	@Test
	public void scenario000069() throws Exception {
	  assertOutput("scenario000069", PARAGRAPHS, LISTS, BLOCKQUOTES);
	}

	@Test
	public void scenario000070() throws Exception {
	  assertOutput("scenario000070", HEADINGS, LISTS, BLOCKQUOTES);
	}

	@Test
	public void scenario000071() throws Exception {
	  assertOutput("scenario000071", PARAGRAPHS, HEADINGS, LISTS, BLOCKQUOTES);
	}

	@Test
	public void scenario000072() throws Exception {
	  assertOutput("scenario000072", LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000073() throws Exception {
	  assertOutput("scenario000073", PARAGRAPHS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000074() throws Exception {
	  assertOutput("scenario000074", HEADINGS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000075() throws Exception {
	  assertOutput("scenario000075", PARAGRAPHS, HEADINGS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000076() throws Exception {
	  assertOutput("scenario000076", LISTS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000077() throws Exception {
	  assertOutput("scenario000077", PARAGRAPHS, LISTS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000078() throws Exception {
	  assertOutput("scenario000078", HEADINGS, LISTS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000079() throws Exception {
	  assertOutput("scenario000079", PARAGRAPHS, HEADINGS, LISTS, LINKS, BLOCKQUOTES);
	}

	@Test
	public void scenario000080() throws Exception {
	  assertOutput("scenario000080", IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000081() throws Exception {
	  assertOutput("scenario000081", PARAGRAPHS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000082() throws Exception {
	  assertOutput("scenario000082", HEADINGS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000083() throws Exception {
	  assertOutput("scenario000083", PARAGRAPHS, HEADINGS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000084() throws Exception {
	  assertOutput("scenario000084", LISTS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000085() throws Exception {
	  assertOutput("scenario000085", PARAGRAPHS, LISTS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000086() throws Exception {
	  assertOutput("scenario000086", HEADINGS, LISTS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000087() throws Exception {
	  assertOutput("scenario000087", PARAGRAPHS, HEADINGS, LISTS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000088() throws Exception {
	  assertOutput("scenario000088", LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000089() throws Exception {
	  assertOutput("scenario000089", PARAGRAPHS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000090() throws Exception {
	  assertOutput("scenario000090", HEADINGS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000091() throws Exception {
	  assertOutput("scenario000091", PARAGRAPHS, HEADINGS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000092() throws Exception {
	  assertOutput("scenario000092", LISTS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000093() throws Exception {
	  assertOutput("scenario000093", PARAGRAPHS, LISTS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000094() throws Exception {
	  assertOutput("scenario000094", HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000095() throws Exception {
	  assertOutput("scenario000095", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES);
	}

	@Test
	public void scenario000096() throws Exception {
	  assertOutput("scenario000096", FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000097() throws Exception {
	  assertOutput("scenario000097", PARAGRAPHS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000098() throws Exception {
	  assertOutput("scenario000098", HEADINGS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000099() throws Exception {
	  assertOutput("scenario000099", PARAGRAPHS, HEADINGS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000100() throws Exception {
	  assertOutput("scenario000100", LISTS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000101() throws Exception {
	  assertOutput("scenario000101", PARAGRAPHS, LISTS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000102() throws Exception {
	  assertOutput("scenario000102", HEADINGS, LISTS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000103() throws Exception {
	  assertOutput("scenario000103", PARAGRAPHS, HEADINGS, LISTS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000104() throws Exception {
	  assertOutput("scenario000104", LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000105() throws Exception {
	  assertOutput("scenario000105", PARAGRAPHS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000106() throws Exception {
	  assertOutput("scenario000106", HEADINGS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000107() throws Exception {
	  assertOutput("scenario000107", PARAGRAPHS, HEADINGS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000108() throws Exception {
	  assertOutput("scenario000108", LISTS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000109() throws Exception {
	  assertOutput("scenario000109", PARAGRAPHS, LISTS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000110() throws Exception {
	  assertOutput("scenario000110", HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000111() throws Exception {
	  assertOutput("scenario000111", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000112() throws Exception {
	  assertOutput("scenario000112", IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000113() throws Exception {
	  assertOutput("scenario000113", PARAGRAPHS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000114() throws Exception {
	  assertOutput("scenario000114", HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000115() throws Exception {
	  assertOutput("scenario000115", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000116() throws Exception {
	  assertOutput("scenario000116", LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000117() throws Exception {
	  assertOutput("scenario000117", PARAGRAPHS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000118() throws Exception {
	  assertOutput("scenario000118", HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000119() throws Exception {
	  assertOutput("scenario000119", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000120() throws Exception {
	  assertOutput("scenario000120", LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000121() throws Exception {
	  assertOutput("scenario000121", PARAGRAPHS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000122() throws Exception {
	  assertOutput("scenario000122", HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000123() throws Exception {
	  assertOutput("scenario000123", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000124() throws Exception {
	  assertOutput("scenario000124", LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000125() throws Exception {
	  assertOutput("scenario000125", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000126() throws Exception {
	  assertOutput("scenario000126", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000127() throws Exception {
	  assertOutput("scenario000127", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
	}

	@Test
	public void scenario000128() throws Exception {
	  assertOutput("scenario000128", CODE);
	}

	@Test
	public void scenario000129() throws Exception {
	  assertOutput("scenario000129", PARAGRAPHS, CODE);
	}

	@Test
	public void scenario000130() throws Exception {
	  assertOutput("scenario000130", HEADINGS, CODE);
	}

	@Test
	public void scenario000131() throws Exception {
	  assertOutput("scenario000131", PARAGRAPHS, HEADINGS, CODE);
	}

	@Test
	public void scenario000132() throws Exception {
	  assertOutput("scenario000132", LISTS, CODE);
	}

	@Test
	public void scenario000133() throws Exception {
	  assertOutput("scenario000133", PARAGRAPHS, LISTS, CODE);
	}

	@Test
	public void scenario000134() throws Exception {
	  assertOutput("scenario000134", HEADINGS, LISTS, CODE);
	}

	@Test
	public void scenario000135() throws Exception {
	  assertOutput("scenario000135", PARAGRAPHS, HEADINGS, LISTS, CODE);
	}

	@Test
	public void scenario000136() throws Exception {
	  assertOutput("scenario000136", LINKS, CODE);
	}

	@Test
	public void scenario000137() throws Exception {
	  assertOutput("scenario000137", PARAGRAPHS, LINKS, CODE);
	}

	@Test
	public void scenario000138() throws Exception {
	  assertOutput("scenario000138", HEADINGS, LINKS, CODE);
	}

	@Test
	public void scenario000139() throws Exception {
	  assertOutput("scenario000139", PARAGRAPHS, HEADINGS, LINKS, CODE);
	}

	@Test
	public void scenario000140() throws Exception {
	  assertOutput("scenario000140", LISTS, LINKS, CODE);
	}

	@Test
	public void scenario000141() throws Exception {
	  assertOutput("scenario000141", PARAGRAPHS, LISTS, LINKS, CODE);
	}

	@Test
	public void scenario000142() throws Exception {
	  assertOutput("scenario000142", HEADINGS, LISTS, LINKS, CODE);
	}

	@Test
	public void scenario000143() throws Exception {
	  assertOutput("scenario000143", PARAGRAPHS, HEADINGS, LISTS, LINKS, CODE);
	}

	@Test
	public void scenario000144() throws Exception {
	  assertOutput("scenario000144", IMAGES, CODE);
	}

	@Test
	public void scenario000145() throws Exception {
	  assertOutput("scenario000145", PARAGRAPHS, IMAGES, CODE);
	}

	@Test
	public void scenario000146() throws Exception {
	  assertOutput("scenario000146", HEADINGS, IMAGES, CODE);
	}

	@Test
	public void scenario000147() throws Exception {
	  assertOutput("scenario000147", PARAGRAPHS, HEADINGS, IMAGES, CODE);
	}

	@Test
	public void scenario000148() throws Exception {
	  assertOutput("scenario000148", LISTS, IMAGES, CODE);
	}

	@Test
	public void scenario000149() throws Exception {
	  assertOutput("scenario000149", PARAGRAPHS, LISTS, IMAGES, CODE);
	}

	@Test
	public void scenario000150() throws Exception {
	  assertOutput("scenario000150", HEADINGS, LISTS, IMAGES, CODE);
	}

	@Test
	public void scenario000151() throws Exception {
	  assertOutput("scenario000151", PARAGRAPHS, HEADINGS, LISTS, IMAGES, CODE);
	}

	@Test
	public void scenario000152() throws Exception {
	  assertOutput("scenario000152", LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000153() throws Exception {
	  assertOutput("scenario000153", PARAGRAPHS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000154() throws Exception {
	  assertOutput("scenario000154", HEADINGS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000155() throws Exception {
	  assertOutput("scenario000155", PARAGRAPHS, HEADINGS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000156() throws Exception {
	  assertOutput("scenario000156", LISTS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000157() throws Exception {
	  assertOutput("scenario000157", PARAGRAPHS, LISTS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000158() throws Exception {
	  assertOutput("scenario000158", HEADINGS, LISTS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000159() throws Exception {
	  assertOutput("scenario000159", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, CODE);
	}

	@Test
	public void scenario000160() throws Exception {
	  assertOutput("scenario000160", FORMATTING, CODE);
	}

	@Test
	public void scenario000161() throws Exception {
	  assertOutput("scenario000161", PARAGRAPHS, FORMATTING, CODE);
	}

	@Test
	public void scenario000162() throws Exception {
	  assertOutput("scenario000162", HEADINGS, FORMATTING, CODE);
	}

	@Test
	public void scenario000163() throws Exception {
	  assertOutput("scenario000163", PARAGRAPHS, HEADINGS, FORMATTING, CODE);
	}

	@Test
	public void scenario000164() throws Exception {
	  assertOutput("scenario000164", LISTS, FORMATTING, CODE);
	}

	@Test
	public void scenario000165() throws Exception {
	  assertOutput("scenario000165", PARAGRAPHS, LISTS, FORMATTING, CODE);
	}

	@Test
	public void scenario000166() throws Exception {
	  assertOutput("scenario000166", HEADINGS, LISTS, FORMATTING, CODE);
	}

	@Test
	public void scenario000167() throws Exception {
	  assertOutput("scenario000167", PARAGRAPHS, HEADINGS, LISTS, FORMATTING, CODE);
	}

	@Test
	public void scenario000168() throws Exception {
	  assertOutput("scenario000168", LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000169() throws Exception {
	  assertOutput("scenario000169", PARAGRAPHS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000170() throws Exception {
	  assertOutput("scenario000170", HEADINGS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000171() throws Exception {
	  assertOutput("scenario000171", PARAGRAPHS, HEADINGS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000172() throws Exception {
	  assertOutput("scenario000172", LISTS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000173() throws Exception {
	  assertOutput("scenario000173", PARAGRAPHS, LISTS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000174() throws Exception {
	  assertOutput("scenario000174", HEADINGS, LISTS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000175() throws Exception {
	  assertOutput("scenario000175", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING, CODE);
	}

	@Test
	public void scenario000176() throws Exception {
	  assertOutput("scenario000176", IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000177() throws Exception {
	  assertOutput("scenario000177", PARAGRAPHS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000178() throws Exception {
	  assertOutput("scenario000178", HEADINGS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000179() throws Exception {
	  assertOutput("scenario000179", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000180() throws Exception {
	  assertOutput("scenario000180", LISTS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000181() throws Exception {
	  assertOutput("scenario000181", PARAGRAPHS, LISTS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000182() throws Exception {
	  assertOutput("scenario000182", HEADINGS, LISTS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000183() throws Exception {
	  assertOutput("scenario000183", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000184() throws Exception {
	  assertOutput("scenario000184", LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000185() throws Exception {
	  assertOutput("scenario000185", PARAGRAPHS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000186() throws Exception {
	  assertOutput("scenario000186", HEADINGS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000187() throws Exception {
	  assertOutput("scenario000187", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000188() throws Exception {
	  assertOutput("scenario000188", LISTS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000189() throws Exception {
	  assertOutput("scenario000189", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000190() throws Exception {
	  assertOutput("scenario000190", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000191() throws Exception {
	  assertOutput("scenario000191", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, CODE);
	}

	@Test
	public void scenario000192() throws Exception {
	  assertOutput("scenario000192", BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000193() throws Exception {
	  assertOutput("scenario000193", PARAGRAPHS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000194() throws Exception {
	  assertOutput("scenario000194", HEADINGS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000195() throws Exception {
	  assertOutput("scenario000195", PARAGRAPHS, HEADINGS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000196() throws Exception {
	  assertOutput("scenario000196", LISTS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000197() throws Exception {
	  assertOutput("scenario000197", PARAGRAPHS, LISTS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000198() throws Exception {
	  assertOutput("scenario000198", HEADINGS, LISTS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000199() throws Exception {
	  assertOutput("scenario000199", PARAGRAPHS, HEADINGS, LISTS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000200() throws Exception {
	  assertOutput("scenario000200", LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000201() throws Exception {
	  assertOutput("scenario000201", PARAGRAPHS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000202() throws Exception {
	  assertOutput("scenario000202", HEADINGS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000203() throws Exception {
	  assertOutput("scenario000203", PARAGRAPHS, HEADINGS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000204() throws Exception {
	  assertOutput("scenario000204", LISTS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000205() throws Exception {
	  assertOutput("scenario000205", PARAGRAPHS, LISTS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000206() throws Exception {
	  assertOutput("scenario000206", HEADINGS, LISTS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000207() throws Exception {
	  assertOutput("scenario000207", PARAGRAPHS, HEADINGS, LISTS, LINKS, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000208() throws Exception {
	  assertOutput("scenario000208", IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000209() throws Exception {
	  assertOutput("scenario000209", PARAGRAPHS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000210() throws Exception {
	  assertOutput("scenario000210", HEADINGS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000211() throws Exception {
	  assertOutput("scenario000211", PARAGRAPHS, HEADINGS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000212() throws Exception {
	  assertOutput("scenario000212", LISTS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000213() throws Exception {
	  assertOutput("scenario000213", PARAGRAPHS, LISTS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000214() throws Exception {
	  assertOutput("scenario000214", HEADINGS, LISTS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000215() throws Exception {
	  assertOutput("scenario000215", PARAGRAPHS, HEADINGS, LISTS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000216() throws Exception {
	  assertOutput("scenario000216", LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000217() throws Exception {
	  assertOutput("scenario000217", PARAGRAPHS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000218() throws Exception {
	  assertOutput("scenario000218", HEADINGS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000219() throws Exception {
	  assertOutput("scenario000219", PARAGRAPHS, HEADINGS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000220() throws Exception {
	  assertOutput("scenario000220", LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000221() throws Exception {
	  assertOutput("scenario000221", PARAGRAPHS, LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000222() throws Exception {
	  assertOutput("scenario000222", HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000223() throws Exception {
	  assertOutput("scenario000223", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000224() throws Exception {
	  assertOutput("scenario000224", FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000225() throws Exception {
	  assertOutput("scenario000225", PARAGRAPHS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000226() throws Exception {
	  assertOutput("scenario000226", HEADINGS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000227() throws Exception {
	  assertOutput("scenario000227", PARAGRAPHS, HEADINGS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000228() throws Exception {
	  assertOutput("scenario000228", LISTS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000229() throws Exception {
	  assertOutput("scenario000229", PARAGRAPHS, LISTS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000230() throws Exception {
	  assertOutput("scenario000230", HEADINGS, LISTS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000231() throws Exception {
	  assertOutput("scenario000231", PARAGRAPHS, HEADINGS, LISTS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000232() throws Exception {
	  assertOutput("scenario000232", LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000233() throws Exception {
	  assertOutput("scenario000233", PARAGRAPHS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000234() throws Exception {
	  assertOutput("scenario000234", HEADINGS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000235() throws Exception {
	  assertOutput("scenario000235", PARAGRAPHS, HEADINGS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000236() throws Exception {
	  assertOutput("scenario000236", LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000237() throws Exception {
	  assertOutput("scenario000237", PARAGRAPHS, LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000238() throws Exception {
	  assertOutput("scenario000238", HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000239() throws Exception {
	  assertOutput("scenario000239", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000240() throws Exception {
	  assertOutput("scenario000240", IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000241() throws Exception {
	  assertOutput("scenario000241", PARAGRAPHS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000242() throws Exception {
	  assertOutput("scenario000242", HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000243() throws Exception {
	  assertOutput("scenario000243", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000244() throws Exception {
	  assertOutput("scenario000244", LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000245() throws Exception {
	  assertOutput("scenario000245", PARAGRAPHS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000246() throws Exception {
	  assertOutput("scenario000246", HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000247() throws Exception {
	  assertOutput("scenario000247", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000248() throws Exception {
	  assertOutput("scenario000248", LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000249() throws Exception {
	  assertOutput("scenario000249", PARAGRAPHS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000250() throws Exception {
	  assertOutput("scenario000250", HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000251() throws Exception {
	  assertOutput("scenario000251", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000252() throws Exception {
	  assertOutput("scenario000252", LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000253() throws Exception {
	  assertOutput("scenario000253", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000254() throws Exception {
	  assertOutput("scenario000254", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}

	@Test
	public void scenario000255() throws Exception {
	  assertOutput("scenario000255", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
	}


	
	private void assertOutput(String file, Module... includes) throws Exception {
		File kd = new File(TESTSUITE_FOLDER + "/e2e/e2e.kd");
		String html = readFile(TESTSUITE_FOLDER + "/e2e/" + file + ".htm");
		
		parser.setIncludes(includes);
		Document document = parser.parse(kd); // Generate AST
		HtmlRenderer renderer = new HtmlRenderer();
		document.accept(renderer);
		
//		PrintWriter out = new PrintWriter("/Users/andy/Desktop/" + file + ".htm");
//		out.write(renderer.getOutput());
//		out.close();
		
		assertEquals(html, renderer.getOutput());
	}
	
}
