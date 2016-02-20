package io.koara;

import static io.koara.Module.BLOCKQUOTES;
import static io.koara.Module.CODE;
import static io.koara.Module.FORMATTING;
import static io.koara.Module.HEADINGS;
import static io.koara.Module.IMAGES;
import static io.koara.Module.LINKS;
import static io.koara.Module.LISTS;
import static io.koara.Module.PARAGRAPHS;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import io.koara.ast.Document;
import io.koara.renderer.XmlRenderer;

public class EndToEndTest {

	private static final String TESTSUITE_FOLDER = "../testsuite";

    
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void scenario000001() throws Exception {
        assertOutput("end2end-000001", PARAGRAPHS);
    }

    @Test
    public void scenario000002() throws Exception {
        assertOutput("end2end-000002", HEADINGS);
    }

    @Test
    public void scenario000003() throws Exception {
        assertOutput("end2end-000003", PARAGRAPHS, HEADINGS);
    }

    @Test
    public void scenario000004() throws Exception {
        assertOutput("end2end-000004", LISTS);
    }

    @Test
    public void scenario000005() throws Exception {
        assertOutput("end2end-000005", PARAGRAPHS, LISTS);
    }

    @Test
    public void scenario000006() throws Exception {
        assertOutput("end2end-000006", HEADINGS, LISTS);
    }

    @Test
    public void scenario000007() throws Exception {
        assertOutput("end2end-000007", PARAGRAPHS, HEADINGS, LISTS);
    }

    @Test
    public void scenario000008() throws Exception {
        assertOutput("end2end-000008", LINKS);
    }

    @Test
    public void scenario000009() throws Exception {
        assertOutput("end2end-000009", PARAGRAPHS, LINKS);
    }

    @Test
    public void scenario000010() throws Exception {
        assertOutput("end2end-000010", HEADINGS, LINKS);
    }

    @Test
    public void scenario000011() throws Exception {
        assertOutput("end2end-000011", PARAGRAPHS, HEADINGS, LINKS);
    }

    @Test
    public void scenario000012() throws Exception {
        assertOutput("end2end-000012", LISTS, LINKS);
    }

    @Test
    public void scenario000013() throws Exception {
        assertOutput("end2end-000013", PARAGRAPHS, LISTS, LINKS);
    }

    @Test
    public void scenario000014() throws Exception {
        assertOutput("end2end-000014", HEADINGS, LISTS, LINKS);
    }

    @Test
    public void scenario000015() throws Exception {
        assertOutput("end2end-000015", PARAGRAPHS, HEADINGS, LISTS, LINKS);
    }

    @Test
    public void scenario000016() throws Exception {
        assertOutput("end2end-000016", IMAGES);
    }

    @Test
    public void scenario000017() throws Exception {
        assertOutput("end2end-000017", PARAGRAPHS, IMAGES);
    }

    @Test
    public void scenario000018() throws Exception {
        assertOutput("end2end-000018", HEADINGS, IMAGES);
    }

    @Test
    public void scenario000019() throws Exception {
        assertOutput("end2end-000019", PARAGRAPHS, HEADINGS, IMAGES);
    }

    @Test
    public void scenario000020() throws Exception {
        assertOutput("end2end-000020", LISTS, IMAGES);
    }

    @Test
    public void scenario000021() throws Exception {
        assertOutput("end2end-000021", PARAGRAPHS, LISTS, IMAGES);
    }

    @Test
    public void scenario000022() throws Exception {
        assertOutput("end2end-000022", HEADINGS, LISTS, IMAGES);
    }

    @Test
    public void scenario000023() throws Exception {
        assertOutput("end2end-000023", PARAGRAPHS, HEADINGS, LISTS, IMAGES);
    }

    @Test
    public void scenario000024() throws Exception {
        assertOutput("end2end-000024", LINKS, IMAGES);
    }

    @Test
    public void scenario000025() throws Exception {
        assertOutput("end2end-000025", PARAGRAPHS, LINKS, IMAGES);
    }

    @Test
    public void scenario000026() throws Exception {
        assertOutput("end2end-000026", HEADINGS, LINKS, IMAGES);
    }

    @Test
    public void scenario000027() throws Exception {
        assertOutput("end2end-000027", PARAGRAPHS, HEADINGS, LINKS, IMAGES);
    }

    @Test
    public void scenario000028() throws Exception {
        assertOutput("end2end-000028", LISTS, LINKS, IMAGES);
    }

    @Test
    public void scenario000029() throws Exception {
        assertOutput("end2end-000029", PARAGRAPHS, LISTS, LINKS, IMAGES);
    }

    @Test
    public void scenario000030() throws Exception {
        assertOutput("end2end-000030", HEADINGS, LISTS, LINKS, IMAGES);
    }

    @Test
    public void scenario000031() throws Exception {
        assertOutput("end2end-000031", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES);
    }

    @Test
    public void scenario000032() throws Exception {
        assertOutput("end2end-000032", FORMATTING);
    }

    @Test
    public void scenario000033() throws Exception {
        assertOutput("end2end-000033", PARAGRAPHS, FORMATTING);
    }

    @Test
    public void scenario000034() throws Exception {
        assertOutput("end2end-000034", HEADINGS, FORMATTING);
    }

    @Test
    public void scenario000035() throws Exception {
        assertOutput("end2end-000035", PARAGRAPHS, HEADINGS, FORMATTING);
    }

    @Test
    public void scenario000036() throws Exception {
        assertOutput("end2end-000036", LISTS, FORMATTING);
    }

    @Test
    public void scenario000037() throws Exception {
        assertOutput("end2end-000037", PARAGRAPHS, LISTS, FORMATTING);
    }

    @Test
    public void scenario000038() throws Exception {
        assertOutput("end2end-000038", HEADINGS, LISTS, FORMATTING);
    }

    @Test
    public void scenario000039() throws Exception {
        assertOutput("end2end-000039", PARAGRAPHS, HEADINGS, LISTS, FORMATTING);
    }

    @Test
    public void scenario000040() throws Exception {
        assertOutput("end2end-000040", LINKS, FORMATTING);
    }

    @Test
    public void scenario000041() throws Exception {
        assertOutput("end2end-000041", PARAGRAPHS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000042() throws Exception {
        assertOutput("end2end-000042", HEADINGS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000043() throws Exception {
        assertOutput("end2end-000043", PARAGRAPHS, HEADINGS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000044() throws Exception {
        assertOutput("end2end-000044", LISTS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000045() throws Exception {
        assertOutput("end2end-000045", PARAGRAPHS, LISTS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000046() throws Exception {
        assertOutput("end2end-000046", HEADINGS, LISTS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000047() throws Exception {
        assertOutput("end2end-000047", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING);
    }

    @Test
    public void scenario000048() throws Exception {
        assertOutput("end2end-000048", IMAGES, FORMATTING);
    }

    @Test
    public void scenario000049() throws Exception {
        assertOutput("end2end-000049", PARAGRAPHS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000050() throws Exception {
        assertOutput("end2end-000050", HEADINGS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000051() throws Exception {
        assertOutput("end2end-000051", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000052() throws Exception {
        assertOutput("end2end-000052", LISTS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000053() throws Exception {
        assertOutput("end2end-000053", PARAGRAPHS, LISTS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000054() throws Exception {
        assertOutput("end2end-000054", HEADINGS, LISTS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000055() throws Exception {
        assertOutput("end2end-000055", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000056() throws Exception {
        assertOutput("end2end-000056", LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000057() throws Exception {
        assertOutput("end2end-000057", PARAGRAPHS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000058() throws Exception {
        assertOutput("end2end-000058", HEADINGS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000059() throws Exception {
        assertOutput("end2end-000059", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000060() throws Exception {
        assertOutput("end2end-000060", LISTS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000061() throws Exception {
        assertOutput("end2end-000061", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000062() throws Exception {
        assertOutput("end2end-000062", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000063() throws Exception {
        assertOutput("end2end-000063", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING);
    }

    @Test
    public void scenario000064() throws Exception {
        assertOutput("end2end-000064", BLOCKQUOTES);
    }

    @Test
    public void scenario000065() throws Exception {
        assertOutput("end2end-000065", PARAGRAPHS, BLOCKQUOTES);
    }

    @Test
    public void scenario000066() throws Exception {
        assertOutput("end2end-000066", HEADINGS, BLOCKQUOTES);
    }

    @Test
    public void scenario000067() throws Exception {
        assertOutput("end2end-000067", PARAGRAPHS, HEADINGS, BLOCKQUOTES);
    }

    @Test
    public void scenario000068() throws Exception {
        assertOutput("end2end-000068", LISTS, BLOCKQUOTES);
    }

    @Test
    public void scenario000069() throws Exception {
        assertOutput("end2end-000069", PARAGRAPHS, LISTS, BLOCKQUOTES);
    }

    @Test
    public void scenario000070() throws Exception {
        assertOutput("end2end-000070", HEADINGS, LISTS, BLOCKQUOTES);
    }

    @Test
    public void scenario000071() throws Exception {
        assertOutput("end2end-000071", PARAGRAPHS, HEADINGS, LISTS, BLOCKQUOTES);
    }

    @Test
    public void scenario000072() throws Exception {
        assertOutput("end2end-000072", LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000073() throws Exception {
        assertOutput("end2end-000073", PARAGRAPHS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000074() throws Exception {
        assertOutput("end2end-000074", HEADINGS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000075() throws Exception {
        assertOutput("end2end-000075", PARAGRAPHS, HEADINGS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000076() throws Exception {
        assertOutput("end2end-000076", LISTS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000077() throws Exception {
        assertOutput("end2end-000077", PARAGRAPHS, LISTS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000078() throws Exception {
        assertOutput("end2end-000078", HEADINGS, LISTS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000079() throws Exception {
        assertOutput("end2end-000079", PARAGRAPHS, HEADINGS, LISTS, LINKS, BLOCKQUOTES);
    }

    @Test
    public void scenario000080() throws Exception {
        assertOutput("end2end-000080", IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000081() throws Exception {
        assertOutput("end2end-000081", PARAGRAPHS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000082() throws Exception {
        assertOutput("end2end-000082", HEADINGS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000083() throws Exception {
        assertOutput("end2end-000083", PARAGRAPHS, HEADINGS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000084() throws Exception {
        assertOutput("end2end-000084", LISTS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000085() throws Exception {
        assertOutput("end2end-000085", PARAGRAPHS, LISTS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000086() throws Exception {
        assertOutput("end2end-000086", HEADINGS, LISTS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000087() throws Exception {
        assertOutput("end2end-000087", PARAGRAPHS, HEADINGS, LISTS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000088() throws Exception {
        assertOutput("end2end-000088", LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000089() throws Exception {
        assertOutput("end2end-000089", PARAGRAPHS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000090() throws Exception {
        assertOutput("end2end-000090", HEADINGS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000091() throws Exception {
        assertOutput("end2end-000091", PARAGRAPHS, HEADINGS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000092() throws Exception {
        assertOutput("end2end-000092", LISTS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000093() throws Exception {
        assertOutput("end2end-000093", PARAGRAPHS, LISTS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000094() throws Exception {
        assertOutput("end2end-000094", HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000095() throws Exception {
        assertOutput("end2end-000095", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES);
    }

    @Test
    public void scenario000096() throws Exception {
        assertOutput("end2end-000096", FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000097() throws Exception {
        assertOutput("end2end-000097", PARAGRAPHS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000098() throws Exception {
        assertOutput("end2end-000098", HEADINGS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000099() throws Exception {
        assertOutput("end2end-000099", PARAGRAPHS, HEADINGS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000100() throws Exception {
        assertOutput("end2end-000100", LISTS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000101() throws Exception {
        assertOutput("end2end-000101", PARAGRAPHS, LISTS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000102() throws Exception {
        assertOutput("end2end-000102", HEADINGS, LISTS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000103() throws Exception {
        assertOutput("end2end-000103", PARAGRAPHS, HEADINGS, LISTS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000104() throws Exception {
        assertOutput("end2end-000104", LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000105() throws Exception {
        assertOutput("end2end-000105", PARAGRAPHS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000106() throws Exception {
        assertOutput("end2end-000106", HEADINGS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000107() throws Exception {
        assertOutput("end2end-000107", PARAGRAPHS, HEADINGS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000108() throws Exception {
        assertOutput("end2end-000108", LISTS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000109() throws Exception {
        assertOutput("end2end-000109", PARAGRAPHS, LISTS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000110() throws Exception {
        assertOutput("end2end-000110", HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000111() throws Exception {
        assertOutput("end2end-000111", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000112() throws Exception {
        assertOutput("end2end-000112", IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000113() throws Exception {
        assertOutput("end2end-000113", PARAGRAPHS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000114() throws Exception {
        assertOutput("end2end-000114", HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000115() throws Exception {
        assertOutput("end2end-000115", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000116() throws Exception {
        assertOutput("end2end-000116", LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000117() throws Exception {
        assertOutput("end2end-000117", PARAGRAPHS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000118() throws Exception {
        assertOutput("end2end-000118", HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000119() throws Exception {
        assertOutput("end2end-000119", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000120() throws Exception {
        assertOutput("end2end-000120", LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000121() throws Exception {
        assertOutput("end2end-000121", PARAGRAPHS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000122() throws Exception {
        assertOutput("end2end-000122", HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000123() throws Exception {
        assertOutput("end2end-000123", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000124() throws Exception {
        assertOutput("end2end-000124", LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000125() throws Exception {
        assertOutput("end2end-000125", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000126() throws Exception {
        assertOutput("end2end-000126", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000127() throws Exception {
        assertOutput("end2end-000127", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES);
    }

    @Test
    public void scenario000128() throws Exception {
        assertOutput("end2end-000128", CODE);
    }

    @Test
    public void scenario000129() throws Exception {
        assertOutput("end2end-000129", PARAGRAPHS, CODE);
    }

    @Test
    public void scenario000130() throws Exception {
        assertOutput("end2end-000130", HEADINGS, CODE);
    }

    @Test
    public void scenario000131() throws Exception {
        assertOutput("end2end-000131", PARAGRAPHS, HEADINGS, CODE);
    }

    @Test
    public void scenario000132() throws Exception {
        assertOutput("end2end-000132", LISTS, CODE);
    }

    @Test
    public void scenario000133() throws Exception {
        assertOutput("end2end-000133", PARAGRAPHS, LISTS, CODE);
    }

    @Test
    public void scenario000134() throws Exception {
        assertOutput("end2end-000134", HEADINGS, LISTS, CODE);
    }

    @Test
    public void scenario000135() throws Exception {
        assertOutput("end2end-000135", PARAGRAPHS, HEADINGS, LISTS, CODE);
    }

    @Test
    public void scenario000136() throws Exception {
        assertOutput("end2end-000136", LINKS, CODE);
    }

    @Test
    public void scenario000137() throws Exception {
        assertOutput("end2end-000137", PARAGRAPHS, LINKS, CODE);
    }

    @Test
    public void scenario000138() throws Exception {
        assertOutput("end2end-000138", HEADINGS, LINKS, CODE);
    }

    @Test
    public void scenario000139() throws Exception {
        assertOutput("end2end-000139", PARAGRAPHS, HEADINGS, LINKS, CODE);
    }

    @Test
    public void scenario000140() throws Exception {
        assertOutput("end2end-000140", LISTS, LINKS, CODE);
    }

    @Test
    public void scenario000141() throws Exception {
        assertOutput("end2end-000141", PARAGRAPHS, LISTS, LINKS, CODE);
    }

    @Test
    public void scenario000142() throws Exception {
        assertOutput("end2end-000142", HEADINGS, LISTS, LINKS, CODE);
    }

    @Test
    public void scenario000143() throws Exception {
        assertOutput("end2end-000143", PARAGRAPHS, HEADINGS, LISTS, LINKS, CODE);
    }

    @Test
    public void scenario000144() throws Exception {
        assertOutput("end2end-000144", IMAGES, CODE);
    }

    @Test
    public void scenario000145() throws Exception {
        assertOutput("end2end-000145", PARAGRAPHS, IMAGES, CODE);
    }

    @Test
    public void scenario000146() throws Exception {
        assertOutput("end2end-000146", HEADINGS, IMAGES, CODE);
    }

    @Test
    public void scenario000147() throws Exception {
        assertOutput("end2end-000147", PARAGRAPHS, HEADINGS, IMAGES, CODE);
    }

    @Test
    public void scenario000148() throws Exception {
        assertOutput("end2end-000148", LISTS, IMAGES, CODE);
    }

    @Test
    public void scenario000149() throws Exception {
        assertOutput("end2end-000149", PARAGRAPHS, LISTS, IMAGES, CODE);
    }

    @Test
    public void scenario000150() throws Exception {
        assertOutput("end2end-000150", HEADINGS, LISTS, IMAGES, CODE);
    }

    @Test
    public void scenario000151() throws Exception {
        assertOutput("end2end-000151", PARAGRAPHS, HEADINGS, LISTS, IMAGES, CODE);
    }

    @Test
    public void scenario000152() throws Exception {
        assertOutput("end2end-000152", LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000153() throws Exception {
        assertOutput("end2end-000153", PARAGRAPHS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000154() throws Exception {
        assertOutput("end2end-000154", HEADINGS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000155() throws Exception {
        assertOutput("end2end-000155", PARAGRAPHS, HEADINGS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000156() throws Exception {
        assertOutput("end2end-000156", LISTS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000157() throws Exception {
        assertOutput("end2end-000157", PARAGRAPHS, LISTS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000158() throws Exception {
        assertOutput("end2end-000158", HEADINGS, LISTS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000159() throws Exception {
        assertOutput("end2end-000159", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, CODE);
    }

    @Test
    public void scenario000160() throws Exception {
        assertOutput("end2end-000160", FORMATTING, CODE);
    }

    @Test
    public void scenario000161() throws Exception {
        assertOutput("end2end-000161", PARAGRAPHS, FORMATTING, CODE);
    }

    @Test
    public void scenario000162() throws Exception {
        assertOutput("end2end-000162", HEADINGS, FORMATTING, CODE);
    }

    @Test
    public void scenario000163() throws Exception {
        assertOutput("end2end-000163", PARAGRAPHS, HEADINGS, FORMATTING, CODE);
    }

    @Test
    public void scenario000164() throws Exception {
        assertOutput("end2end-000164", LISTS, FORMATTING, CODE);
    }

    @Test
    public void scenario000165() throws Exception {
        assertOutput("end2end-000165", PARAGRAPHS, LISTS, FORMATTING, CODE);
    }

    @Test
    public void scenario000166() throws Exception {
        assertOutput("end2end-000166", HEADINGS, LISTS, FORMATTING, CODE);
    }

    @Test
    public void scenario000167() throws Exception {
        assertOutput("end2end-000167", PARAGRAPHS, HEADINGS, LISTS, FORMATTING, CODE);
    }

    @Test
    public void scenario000168() throws Exception {
        assertOutput("end2end-000168", LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000169() throws Exception {
        assertOutput("end2end-000169", PARAGRAPHS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000170() throws Exception {
        assertOutput("end2end-000170", HEADINGS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000171() throws Exception {
        assertOutput("end2end-000171", PARAGRAPHS, HEADINGS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000172() throws Exception {
        assertOutput("end2end-000172", LISTS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000173() throws Exception {
        assertOutput("end2end-000173", PARAGRAPHS, LISTS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000174() throws Exception {
        assertOutput("end2end-000174", HEADINGS, LISTS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000175() throws Exception {
        assertOutput("end2end-000175", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING, CODE);
    }

    @Test
    public void scenario000176() throws Exception {
        assertOutput("end2end-000176", IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000177() throws Exception {
        assertOutput("end2end-000177", PARAGRAPHS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000178() throws Exception {
        assertOutput("end2end-000178", HEADINGS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000179() throws Exception {
        assertOutput("end2end-000179", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000180() throws Exception {
        assertOutput("end2end-000180", LISTS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000181() throws Exception {
        assertOutput("end2end-000181", PARAGRAPHS, LISTS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000182() throws Exception {
        assertOutput("end2end-000182", HEADINGS, LISTS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000183() throws Exception {
        assertOutput("end2end-000183", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000184() throws Exception {
        assertOutput("end2end-000184", LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000185() throws Exception {
        assertOutput("end2end-000185", PARAGRAPHS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000186() throws Exception {
        assertOutput("end2end-000186", HEADINGS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000187() throws Exception {
        assertOutput("end2end-000187", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000188() throws Exception {
        assertOutput("end2end-000188", LISTS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000189() throws Exception {
        assertOutput("end2end-000189", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000190() throws Exception {
        assertOutput("end2end-000190", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000191() throws Exception {
        assertOutput("end2end-000191", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, CODE);
    }

    @Test
    public void scenario000192() throws Exception {
        assertOutput("end2end-000192", BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000193() throws Exception {
        assertOutput("end2end-000193", PARAGRAPHS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000194() throws Exception {
        assertOutput("end2end-000194", HEADINGS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000195() throws Exception {
        assertOutput("end2end-000195", PARAGRAPHS, HEADINGS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000196() throws Exception {
        assertOutput("end2end-000196", LISTS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000197() throws Exception {
        assertOutput("end2end-000197", PARAGRAPHS, LISTS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000198() throws Exception {
        assertOutput("end2end-000198", HEADINGS, LISTS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000199() throws Exception {
        assertOutput("end2end-000199", PARAGRAPHS, HEADINGS, LISTS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000200() throws Exception {
        assertOutput("end2end-000200", LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000201() throws Exception {
        assertOutput("end2end-000201", PARAGRAPHS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000202() throws Exception {
        assertOutput("end2end-000202", HEADINGS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000203() throws Exception {
        assertOutput("end2end-000203", PARAGRAPHS, HEADINGS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000204() throws Exception {
        assertOutput("end2end-000204", LISTS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000205() throws Exception {
        assertOutput("end2end-000205", PARAGRAPHS, LISTS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000206() throws Exception {
        assertOutput("end2end-000206", HEADINGS, LISTS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000207() throws Exception {
        assertOutput("end2end-000207", PARAGRAPHS, HEADINGS, LISTS, LINKS, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000208() throws Exception {
        assertOutput("end2end-000208", IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000209() throws Exception {
        assertOutput("end2end-000209", PARAGRAPHS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000210() throws Exception {
        assertOutput("end2end-000210", HEADINGS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000211() throws Exception {
        assertOutput("end2end-000211", PARAGRAPHS, HEADINGS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000212() throws Exception {
        assertOutput("end2end-000212", LISTS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000213() throws Exception {
        assertOutput("end2end-000213", PARAGRAPHS, LISTS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000214() throws Exception {
        assertOutput("end2end-000214", HEADINGS, LISTS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000215() throws Exception {
        assertOutput("end2end-000215", PARAGRAPHS, HEADINGS, LISTS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000216() throws Exception {
        assertOutput("end2end-000216", LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000217() throws Exception {
        assertOutput("end2end-000217", PARAGRAPHS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000218() throws Exception {
        assertOutput("end2end-000218", HEADINGS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000219() throws Exception {
        assertOutput("end2end-000219", PARAGRAPHS, HEADINGS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000220() throws Exception {
        assertOutput("end2end-000220", LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000221() throws Exception {
        assertOutput("end2end-000221", PARAGRAPHS, LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000222() throws Exception {
        assertOutput("end2end-000222", HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000223() throws Exception {
        assertOutput("end2end-000223", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000224() throws Exception {
        assertOutput("end2end-000224", FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000225() throws Exception {
        assertOutput("end2end-000225", PARAGRAPHS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000226() throws Exception {
        assertOutput("end2end-000226", HEADINGS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000227() throws Exception {
        assertOutput("end2end-000227", PARAGRAPHS, HEADINGS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000228() throws Exception {
        assertOutput("end2end-000228", LISTS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000229() throws Exception {
        assertOutput("end2end-000229", PARAGRAPHS, LISTS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000230() throws Exception {
        assertOutput("end2end-000230", HEADINGS, LISTS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000231() throws Exception {
        assertOutput("end2end-000231", PARAGRAPHS, HEADINGS, LISTS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000232() throws Exception {
        assertOutput("end2end-000232", LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000233() throws Exception {
        assertOutput("end2end-000233", PARAGRAPHS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000234() throws Exception {
        assertOutput("end2end-000234", HEADINGS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000235() throws Exception {
        assertOutput("end2end-000235", PARAGRAPHS, HEADINGS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000236() throws Exception {
        assertOutput("end2end-000236", LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000237() throws Exception {
        assertOutput("end2end-000237", PARAGRAPHS, LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000238() throws Exception {
        assertOutput("end2end-000238", HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000239() throws Exception {
        assertOutput("end2end-000239", PARAGRAPHS, HEADINGS, LISTS, LINKS, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000240() throws Exception {
        assertOutput("end2end-000240", IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000241() throws Exception {
        assertOutput("end2end-000241", PARAGRAPHS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000242() throws Exception {
        assertOutput("end2end-000242", HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000243() throws Exception {
        assertOutput("end2end-000243", PARAGRAPHS, HEADINGS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000244() throws Exception {
        assertOutput("end2end-000244", LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000245() throws Exception {
        assertOutput("end2end-000245", PARAGRAPHS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000246() throws Exception {
        assertOutput("end2end-000246", HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000247() throws Exception {
        assertOutput("end2end-000247", PARAGRAPHS, HEADINGS, LISTS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000248() throws Exception {
        assertOutput("end2end-000248", LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000249() throws Exception {
        assertOutput("end2end-000249", PARAGRAPHS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000250() throws Exception {
        assertOutput("end2end-000250", HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000251() throws Exception {
        assertOutput("end2end-000251", PARAGRAPHS, HEADINGS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000252() throws Exception {
        assertOutput("end2end-000252", LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000253() throws Exception {
        assertOutput("end2end-000253", PARAGRAPHS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000254() throws Exception {
        assertOutput("end2end-000254", HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    @Test
    public void scenario000255() throws Exception {
        assertOutput("end2end-000255", PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
    }

    private void assertOutput(String file, Module... modules) throws Exception {
        File kd = new File(TESTSUITE_FOLDER + "/input/end2end.kd");
        String xml = readFile(TESTSUITE_FOLDER + "/output/xml/end2end/" + file + ".xml");

        parser.setModules(modules);
        Document document = parser.parseFile(kd);
        XmlRenderer renderer = new XmlRenderer();
        document.accept(renderer);
        
        assertEquals(xml, renderer.getOutput());
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
