package io.koara;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import io.koara.ast.Document;

public class EndToEndTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void scenario000001() throws Exception {
        assertOutput("end2end-000001", "paragraphs");
    }

    @Test
    public void scenario000002() throws Exception {
        assertOutput("end2end-000002", "headings");
    }

    @Test
    public void scenario000003() throws Exception {
        assertOutput("end2end-000003", "paragraphs", "headings");
    }

    @Test
    public void scenario000004() throws Exception {
        assertOutput("end2end-000004", "lists");
    }

    @Test
    public void scenario000005() throws Exception {
        assertOutput("end2end-000005", "paragraphs", "lists");
    }

    @Test
    public void scenario000006() throws Exception {
        assertOutput("end2end-000006", "headings", "lists");
    }

    @Test
    public void scenario000007() throws Exception {
        assertOutput("end2end-000007", "paragraphs", "headings", "lists");
    }

    @Test
    public void scenario000008() throws Exception {
        assertOutput("end2end-000008", "links");
    }

    @Test
    public void scenario000009() throws Exception {
        assertOutput("end2end-000009", "paragraphs", "links");
    }

    @Test
    public void scenario000010() throws Exception {
        assertOutput("end2end-000010", "headings", "links");
    }

    @Test
    public void scenario000011() throws Exception {
        assertOutput("end2end-000011", "paragraphs", "headings", "links");
    }

    @Test
    public void scenario000012() throws Exception {
        assertOutput("end2end-000012", "lists", "links");
    }

    @Test
    public void scenario000013() throws Exception {
        assertOutput("end2end-000013", "paragraphs", "lists", "links");
    }

    @Test
    public void scenario000014() throws Exception {
        assertOutput("end2end-000014", "headings", "lists", "links");
    }

    @Test
    public void scenario000015() throws Exception {
        assertOutput("end2end-000015", "paragraphs", "headings", "lists", "links");
    }

    @Test
    public void scenario000016() throws Exception {
        assertOutput("end2end-000016", "images");
    }

    @Test
    public void scenario000017() throws Exception {
        assertOutput("end2end-000017", "paragraphs", "images");
    }

    @Test
    public void scenario000018() throws Exception {
        assertOutput("end2end-000018", "headings", "images");
    }

    @Test
    public void scenario000019() throws Exception {
        assertOutput("end2end-000019", "paragraphs", "headings", "images");
    }

    @Test
    public void scenario000020() throws Exception {
        assertOutput("end2end-000020", "lists", "images");
    }

    @Test
    public void scenario000021() throws Exception {
        assertOutput("end2end-000021", "paragraphs", "lists", "images");
    }

    @Test
    public void scenario000022() throws Exception {
        assertOutput("end2end-000022", "headings", "lists", "images");
    }

    @Test
    public void scenario000023() throws Exception {
        assertOutput("end2end-000023", "paragraphs", "headings", "lists", "images");
    }

    @Test
    public void scenario000024() throws Exception {
        assertOutput("end2end-000024", "links", "images");
    }

    @Test
    public void scenario000025() throws Exception {
        assertOutput("end2end-000025", "paragraphs", "links", "images");
    }

    @Test
    public void scenario000026() throws Exception {
        assertOutput("end2end-000026", "headings", "links", "images");
    }

    @Test
    public void scenario000027() throws Exception {
        assertOutput("end2end-000027", "paragraphs", "headings", "links", "images");
    }

    @Test
    public void scenario000028() throws Exception {
        assertOutput("end2end-000028", "lists", "links", "images");
    }

    @Test
    public void scenario000029() throws Exception {
        assertOutput("end2end-000029", "paragraphs", "lists", "links", "images");
    }

    @Test
    public void scenario000030() throws Exception {
        assertOutput("end2end-000030", "headings", "lists", "links", "images");
    }

    @Test
    public void scenario000031() throws Exception {
        assertOutput("end2end-000031", "paragraphs", "headings", "lists", "links", "images");
    }

    @Test
    public void scenario000032() throws Exception {
        assertOutput("end2end-000032", "formatting");
    }

    @Test
    public void scenario000033() throws Exception {
        assertOutput("end2end-000033", "paragraphs", "formatting");
    }

    @Test
    public void scenario000034() throws Exception {
        assertOutput("end2end-000034", "headings", "formatting");
    }

    @Test
    public void scenario000035() throws Exception {
        assertOutput("end2end-000035", "paragraphs", "headings", "formatting");
    }

    @Test
    public void scenario000036() throws Exception {
        assertOutput("end2end-000036", "lists", "formatting");
    }

    @Test
    public void scenario000037() throws Exception {
        assertOutput("end2end-000037", "paragraphs", "lists", "formatting");
    }

    @Test
    public void scenario000038() throws Exception {
        assertOutput("end2end-000038", "headings", "lists", "formatting");
    }

    @Test
    public void scenario000039() throws Exception {
        assertOutput("end2end-000039", "paragraphs", "headings", "lists", "formatting");
    }

    @Test
    public void scenario000040() throws Exception {
        assertOutput("end2end-000040", "links", "formatting");
    }

    @Test
    public void scenario000041() throws Exception {
        assertOutput("end2end-000041", "paragraphs", "links", "formatting");
    }

    @Test
    public void scenario000042() throws Exception {
        assertOutput("end2end-000042", "headings", "links", "formatting");
    }

    @Test
    public void scenario000043() throws Exception {
        assertOutput("end2end-000043", "paragraphs", "headings", "links", "formatting");
    }

    @Test
    public void scenario000044() throws Exception {
        assertOutput("end2end-000044", "lists", "links", "formatting");
    }

    @Test
    public void scenario000045() throws Exception {
        assertOutput("end2end-000045", "paragraphs", "lists", "links", "formatting");
    }

    @Test
    public void scenario000046() throws Exception {
        assertOutput("end2end-000046", "headings", "lists", "links", "formatting");
    }

    @Test
    public void scenario000047() throws Exception {
        assertOutput("end2end-000047", "paragraphs", "headings", "lists", "links", "formatting");
    }

    @Test
    public void scenario000048() throws Exception {
        assertOutput("end2end-000048", "images", "formatting");
    }

    @Test
    public void scenario000049() throws Exception {
        assertOutput("end2end-000049", "paragraphs", "images", "formatting");
    }

    @Test
    public void scenario000050() throws Exception {
        assertOutput("end2end-000050", "headings", "images", "formatting");
    }

    @Test
    public void scenario000051() throws Exception {
        assertOutput("end2end-000051", "paragraphs", "headings", "images", "formatting");
    }

    @Test
    public void scenario000052() throws Exception {
        assertOutput("end2end-000052", "lists", "images", "formatting");
    }

    @Test
    public void scenario000053() throws Exception {
        assertOutput("end2end-000053", "paragraphs", "lists", "images", "formatting");
    }

    @Test
    public void scenario000054() throws Exception {
        assertOutput("end2end-000054", "headings", "lists", "images", "formatting");
    }

    @Test
    public void scenario000055() throws Exception {
        assertOutput("end2end-000055", "paragraphs", "headings", "lists", "images", "formatting");
    }

    @Test
    public void scenario000056() throws Exception {
        assertOutput("end2end-000056", "links", "images", "formatting");
    }

    @Test
    public void scenario000057() throws Exception {
        assertOutput("end2end-000057", "paragraphs", "links", "images", "formatting");
    }

    @Test
    public void scenario000058() throws Exception {
        assertOutput("end2end-000058", "headings", "links", "images", "formatting");
    }

    @Test
    public void scenario000059() throws Exception {
        assertOutput("end2end-000059", "paragraphs", "headings", "links", "images", "formatting");
    }

    @Test
    public void scenario000060() throws Exception {
        assertOutput("end2end-000060", "lists", "links", "images", "formatting");
    }

    @Test
    public void scenario000061() throws Exception {
        assertOutput("end2end-000061", "paragraphs", "lists", "links", "images", "formatting");
    }

    @Test
    public void scenario000062() throws Exception {
        assertOutput("end2end-000062", "headings", "lists", "links", "images", "formatting");
    }

    @Test
    public void scenario000063() throws Exception {
        assertOutput("end2end-000063", "paragraphs", "headings", "lists", "links", "images", "formatting");
    }

    @Test
    public void scenario000064() throws Exception {
        assertOutput("end2end-000064", "blockquotes");
    }

    @Test
    public void scenario000065() throws Exception {
        assertOutput("end2end-000065", "paragraphs", "blockquotes");
    }

    @Test
    public void scenario000066() throws Exception {
        assertOutput("end2end-000066", "headings", "blockquotes");
    }

    @Test
    public void scenario000067() throws Exception {
        assertOutput("end2end-000067", "paragraphs", "headings", "blockquotes");
    }

    @Test
    public void scenario000068() throws Exception {
        assertOutput("end2end-000068", "lists", "blockquotes");
    }

    @Test
    public void scenario000069() throws Exception {
        assertOutput("end2end-000069", "paragraphs", "lists", "blockquotes");
    }

    @Test
    public void scenario000070() throws Exception {
        assertOutput("end2end-000070", "headings", "lists", "blockquotes");
    }

    @Test
    public void scenario000071() throws Exception {
        assertOutput("end2end-000071", "paragraphs", "headings", "lists", "blockquotes");
    }

    @Test
    public void scenario000072() throws Exception {
        assertOutput("end2end-000072", "links", "blockquotes");
    }

    @Test
    public void scenario000073() throws Exception {
        assertOutput("end2end-000073", "paragraphs", "links", "blockquotes");
    }

    @Test
    public void scenario000074() throws Exception {
        assertOutput("end2end-000074", "headings", "links", "blockquotes");
    }

    @Test
    public void scenario000075() throws Exception {
        assertOutput("end2end-000075", "paragraphs", "headings", "links", "blockquotes");
    }

    @Test
    public void scenario000076() throws Exception {
        assertOutput("end2end-000076", "lists", "links", "blockquotes");
    }

    @Test
    public void scenario000077() throws Exception {
        assertOutput("end2end-000077", "paragraphs", "lists", "links", "blockquotes");
    }

    @Test
    public void scenario000078() throws Exception {
        assertOutput("end2end-000078", "headings", "lists", "links", "blockquotes");
    }

    @Test
    public void scenario000079() throws Exception {
        assertOutput("end2end-000079", "paragraphs", "headings", "lists", "links", "blockquotes");
    }

    @Test
    public void scenario000080() throws Exception {
        assertOutput("end2end-000080", "images", "blockquotes");
    }

    @Test
    public void scenario000081() throws Exception {
        assertOutput("end2end-000081", "paragraphs", "images", "blockquotes");
    }

    @Test
    public void scenario000082() throws Exception {
        assertOutput("end2end-000082", "headings", "images", "blockquotes");
    }

    @Test
    public void scenario000083() throws Exception {
        assertOutput("end2end-000083", "paragraphs", "headings", "images", "blockquotes");
    }

    @Test
    public void scenario000084() throws Exception {
        assertOutput("end2end-000084", "lists", "images", "blockquotes");
    }

    @Test
    public void scenario000085() throws Exception {
        assertOutput("end2end-000085", "paragraphs", "lists", "images", "blockquotes");
    }

    @Test
    public void scenario000086() throws Exception {
        assertOutput("end2end-000086", "headings", "lists", "images", "blockquotes");
    }

    @Test
    public void scenario000087() throws Exception {
        assertOutput("end2end-000087", "paragraphs", "headings", "lists", "images", "blockquotes");
    }

    @Test
    public void scenario000088() throws Exception {
        assertOutput("end2end-000088", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000089() throws Exception {
        assertOutput("end2end-000089", "paragraphs", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000090() throws Exception {
        assertOutput("end2end-000090", "headings", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000091() throws Exception {
        assertOutput("end2end-000091", "paragraphs", "headings", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000092() throws Exception {
        assertOutput("end2end-000092", "lists", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000093() throws Exception {
        assertOutput("end2end-000093", "paragraphs", "lists", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000094() throws Exception {
        assertOutput("end2end-000094", "headings", "lists", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000095() throws Exception {
        assertOutput("end2end-000095", "paragraphs", "headings", "lists", "links", "images", "blockquotes");
    }

    @Test
    public void scenario000096() throws Exception {
        assertOutput("end2end-000096", "formatting", "blockquotes");
    }

    @Test
    public void scenario000097() throws Exception {
        assertOutput("end2end-000097", "paragraphs", "formatting", "blockquotes");
    }

    @Test
    public void scenario000098() throws Exception {
        assertOutput("end2end-000098", "headings", "formatting", "blockquotes");
    }

    @Test
    public void scenario000099() throws Exception {
        assertOutput("end2end-000099", "paragraphs", "headings", "formatting", "blockquotes");
    }

    @Test
    public void scenario000100() throws Exception {
        assertOutput("end2end-000100", "lists", "formatting", "blockquotes");
    }

    @Test
    public void scenario000101() throws Exception {
        assertOutput("end2end-000101", "paragraphs", "lists", "formatting", "blockquotes");
    }

    @Test
    public void scenario000102() throws Exception {
        assertOutput("end2end-000102", "headings", "lists", "formatting", "blockquotes");
    }

    @Test
    public void scenario000103() throws Exception {
        assertOutput("end2end-000103", "paragraphs", "headings", "lists", "formatting", "blockquotes");
    }

    @Test
    public void scenario000104() throws Exception {
        assertOutput("end2end-000104", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000105() throws Exception {
        assertOutput("end2end-000105", "paragraphs", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000106() throws Exception {
        assertOutput("end2end-000106", "headings", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000107() throws Exception {
        assertOutput("end2end-000107", "paragraphs", "headings", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000108() throws Exception {
        assertOutput("end2end-000108", "lists", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000109() throws Exception {
        assertOutput("end2end-000109", "paragraphs", "lists", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000110() throws Exception {
        assertOutput("end2end-000110", "headings", "lists", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000111() throws Exception {
        assertOutput("end2end-000111", "paragraphs", "headings", "lists", "links", "formatting", "blockquotes");
    }

    @Test
    public void scenario000112() throws Exception {
        assertOutput("end2end-000112", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000113() throws Exception {
        assertOutput("end2end-000113", "paragraphs", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000114() throws Exception {
        assertOutput("end2end-000114", "headings", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000115() throws Exception {
        assertOutput("end2end-000115", "paragraphs", "headings", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000116() throws Exception {
        assertOutput("end2end-000116", "lists", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000117() throws Exception {
        assertOutput("end2end-000117", "paragraphs", "lists", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000118() throws Exception {
        assertOutput("end2end-000118", "headings", "lists", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000119() throws Exception {
        assertOutput("end2end-000119", "paragraphs", "headings", "lists", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000120() throws Exception {
        assertOutput("end2end-000120", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000121() throws Exception {
        assertOutput("end2end-000121", "paragraphs", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000122() throws Exception {
        assertOutput("end2end-000122", "headings", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000123() throws Exception {
        assertOutput("end2end-000123", "paragraphs", "headings", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000124() throws Exception {
        assertOutput("end2end-000124", "lists", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000125() throws Exception {
        assertOutput("end2end-000125", "paragraphs", "lists", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000126() throws Exception {
        assertOutput("end2end-000126", "headings", "lists", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000127() throws Exception {
        assertOutput("end2end-000127", "paragraphs", "headings", "lists", "links", "images", "formatting", "blockquotes");
    }

    @Test
    public void scenario000128() throws Exception {
        assertOutput("end2end-000128", "code");
    }

    @Test
    public void scenario000129() throws Exception {
        assertOutput("end2end-000129", "paragraphs", "code");
    }

    @Test
    public void scenario000130() throws Exception {
        assertOutput("end2end-000130", "headings", "code");
    }

    @Test
    public void scenario000131() throws Exception {
        assertOutput("end2end-000131", "paragraphs", "headings", "code");
    }

    @Test
    public void scenario000132() throws Exception {
        assertOutput("end2end-000132", "lists", "code");
    }

    @Test
    public void scenario000133() throws Exception {
        assertOutput("end2end-000133", "paragraphs", "lists", "code");
    }

    @Test
    public void scenario000134() throws Exception {
        assertOutput("end2end-000134", "headings", "lists", "code");
    }

    @Test
    public void scenario000135() throws Exception {
        assertOutput("end2end-000135", "paragraphs", "headings", "lists", "code");
    }

    @Test
    public void scenario000136() throws Exception {
        assertOutput("end2end-000136", "links", "code");
    }

    @Test
    public void scenario000137() throws Exception {
        assertOutput("end2end-000137", "paragraphs", "links", "code");
    }

    @Test
    public void scenario000138() throws Exception {
        assertOutput("end2end-000138", "headings", "links", "code");
    }

    @Test
    public void scenario000139() throws Exception {
        assertOutput("end2end-000139", "paragraphs", "headings", "links", "code");
    }

    @Test
    public void scenario000140() throws Exception {
        assertOutput("end2end-000140", "lists", "links", "code");
    }

    @Test
    public void scenario000141() throws Exception {
        assertOutput("end2end-000141", "paragraphs", "lists", "links", "code");
    }

    @Test
    public void scenario000142() throws Exception {
        assertOutput("end2end-000142", "headings", "lists", "links", "code");
    }

    @Test
    public void scenario000143() throws Exception {
        assertOutput("end2end-000143", "paragraphs", "headings", "lists", "links", "code");
    }

    @Test
    public void scenario000144() throws Exception {
        assertOutput("end2end-000144", "images", "code");
    }

    @Test
    public void scenario000145() throws Exception {
        assertOutput("end2end-000145", "paragraphs", "images", "code");
    }

    @Test
    public void scenario000146() throws Exception {
        assertOutput("end2end-000146", "headings", "images", "code");
    }

    @Test
    public void scenario000147() throws Exception {
        assertOutput("end2end-000147", "paragraphs", "headings", "images", "code");
    }

    @Test
    public void scenario000148() throws Exception {
        assertOutput("end2end-000148", "lists", "images", "code");
    }

    @Test
    public void scenario000149() throws Exception {
        assertOutput("end2end-000149", "paragraphs", "lists", "images", "code");
    }

    @Test
    public void scenario000150() throws Exception {
        assertOutput("end2end-000150", "headings", "lists", "images", "code");
    }

    @Test
    public void scenario000151() throws Exception {
        assertOutput("end2end-000151", "paragraphs", "headings", "lists", "images", "code");
    }

    @Test
    public void scenario000152() throws Exception {
        assertOutput("end2end-000152", "links", "images", "code");
    }

    @Test
    public void scenario000153() throws Exception {
        assertOutput("end2end-000153", "paragraphs", "links", "images", "code");
    }

    @Test
    public void scenario000154() throws Exception {
        assertOutput("end2end-000154", "headings", "links", "images", "code");
    }

    @Test
    public void scenario000155() throws Exception {
        assertOutput("end2end-000155", "paragraphs", "headings", "links", "images", "code");
    }

    @Test
    public void scenario000156() throws Exception {
        assertOutput("end2end-000156", "lists", "links", "images", "code");
    }

    @Test
    public void scenario000157() throws Exception {
        assertOutput("end2end-000157", "paragraphs", "lists", "links", "images", "code");
    }

    @Test
    public void scenario000158() throws Exception {
        assertOutput("end2end-000158", "headings", "lists", "links", "images", "code");
    }

    @Test
    public void scenario000159() throws Exception {
        assertOutput("end2end-000159", "paragraphs", "headings", "lists", "links", "images", "code");
    }

    @Test
    public void scenario000160() throws Exception {
        assertOutput("end2end-000160", "formatting", "code");
    }

    @Test
    public void scenario000161() throws Exception {
        assertOutput("end2end-000161", "paragraphs", "formatting", "code");
    }

    @Test
    public void scenario000162() throws Exception {
        assertOutput("end2end-000162", "headings", "formatting", "code");
    }

    @Test
    public void scenario000163() throws Exception {
        assertOutput("end2end-000163", "paragraphs", "headings", "formatting", "code");
    }

    @Test
    public void scenario000164() throws Exception {
        assertOutput("end2end-000164", "lists", "formatting", "code");
    }

    @Test
    public void scenario000165() throws Exception {
        assertOutput("end2end-000165", "paragraphs", "lists", "formatting", "code");
    }

    @Test
    public void scenario000166() throws Exception {
        assertOutput("end2end-000166", "headings", "lists", "formatting", "code");
    }

    @Test
    public void scenario000167() throws Exception {
        assertOutput("end2end-000167", "paragraphs", "headings", "lists", "formatting", "code");
    }

    @Test
    public void scenario000168() throws Exception {
        assertOutput("end2end-000168", "links", "formatting", "code");
    }

    @Test
    public void scenario000169() throws Exception {
        assertOutput("end2end-000169", "paragraphs", "links", "formatting", "code");
    }

    @Test
    public void scenario000170() throws Exception {
        assertOutput("end2end-000170", "headings", "links", "formatting", "code");
    }

    @Test
    public void scenario000171() throws Exception {
        assertOutput("end2end-000171", "paragraphs", "headings", "links", "formatting", "code");
    }

    @Test
    public void scenario000172() throws Exception {
        assertOutput("end2end-000172", "lists", "links", "formatting", "code");
    }

    @Test
    public void scenario000173() throws Exception {
        assertOutput("end2end-000173", "paragraphs", "lists", "links", "formatting", "code");
    }

    @Test
    public void scenario000174() throws Exception {
        assertOutput("end2end-000174", "headings", "lists", "links", "formatting", "code");
    }

    @Test
    public void scenario000175() throws Exception {
        assertOutput("end2end-000175", "paragraphs", "headings", "lists", "links", "formatting", "code");
    }

    @Test
    public void scenario000176() throws Exception {
        assertOutput("end2end-000176", "images", "formatting", "code");
    }

    @Test
    public void scenario000177() throws Exception {
        assertOutput("end2end-000177", "paragraphs", "images", "formatting", "code");
    }

    @Test
    public void scenario000178() throws Exception {
        assertOutput("end2end-000178", "headings", "images", "formatting", "code");
    }

    @Test
    public void scenario000179() throws Exception {
        assertOutput("end2end-000179", "paragraphs", "headings", "images", "formatting", "code");
    }

    @Test
    public void scenario000180() throws Exception {
        assertOutput("end2end-000180", "lists", "images", "formatting", "code");
    }

    @Test
    public void scenario000181() throws Exception {
        assertOutput("end2end-000181", "paragraphs", "lists", "images", "formatting", "code");
    }

    @Test
    public void scenario000182() throws Exception {
        assertOutput("end2end-000182", "headings", "lists", "images", "formatting", "code");
    }

    @Test
    public void scenario000183() throws Exception {
        assertOutput("end2end-000183", "paragraphs", "headings", "lists", "images", "formatting", "code");
    }

    @Test
    public void scenario000184() throws Exception {
        assertOutput("end2end-000184", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000185() throws Exception {
        assertOutput("end2end-000185", "paragraphs", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000186() throws Exception {
        assertOutput("end2end-000186", "headings", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000187() throws Exception {
        assertOutput("end2end-000187", "paragraphs", "headings", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000188() throws Exception {
        assertOutput("end2end-000188", "lists", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000189() throws Exception {
        assertOutput("end2end-000189", "paragraphs", "lists", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000190() throws Exception {
        assertOutput("end2end-000190", "headings", "lists", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000191() throws Exception {
        assertOutput("end2end-000191", "paragraphs", "headings", "lists", "links", "images", "formatting", "code");
    }

    @Test
    public void scenario000192() throws Exception {
        assertOutput("end2end-000192", "blockquotes", "code");
    }

    @Test
    public void scenario000193() throws Exception {
        assertOutput("end2end-000193", "paragraphs", "blockquotes", "code");
    }

    @Test
    public void scenario000194() throws Exception {
        assertOutput("end2end-000194", "headings", "blockquotes", "code");
    }

    @Test
    public void scenario000195() throws Exception {
        assertOutput("end2end-000195", "paragraphs", "headings", "blockquotes", "code");
    }

    @Test
    public void scenario000196() throws Exception {
        assertOutput("end2end-000196", "lists", "blockquotes", "code");
    }

    @Test
    public void scenario000197() throws Exception {
        assertOutput("end2end-000197", "paragraphs", "lists", "blockquotes", "code");
    }

    @Test
    public void scenario000198() throws Exception {
        assertOutput("end2end-000198", "headings", "lists", "blockquotes", "code");
    }

    @Test
    public void scenario000199() throws Exception {
        assertOutput("end2end-000199", "paragraphs", "headings", "lists", "blockquotes", "code");
    }

    @Test
    public void scenario000200() throws Exception {
        assertOutput("end2end-000200", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000201() throws Exception {
        assertOutput("end2end-000201", "paragraphs", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000202() throws Exception {
        assertOutput("end2end-000202", "headings", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000203() throws Exception {
        assertOutput("end2end-000203", "paragraphs", "headings", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000204() throws Exception {
        assertOutput("end2end-000204", "lists", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000205() throws Exception {
        assertOutput("end2end-000205", "paragraphs", "lists", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000206() throws Exception {
        assertOutput("end2end-000206", "headings", "lists", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000207() throws Exception {
        assertOutput("end2end-000207", "paragraphs", "headings", "lists", "links", "blockquotes", "code");
    }

    @Test
    public void scenario000208() throws Exception {
        assertOutput("end2end-000208", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000209() throws Exception {
        assertOutput("end2end-000209", "paragraphs", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000210() throws Exception {
        assertOutput("end2end-000210", "headings", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000211() throws Exception {
        assertOutput("end2end-000211", "paragraphs", "headings", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000212() throws Exception {
        assertOutput("end2end-000212", "lists", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000213() throws Exception {
        assertOutput("end2end-000213", "paragraphs", "lists", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000214() throws Exception {
        assertOutput("end2end-000214", "headings", "lists", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000215() throws Exception {
        assertOutput("end2end-000215", "paragraphs", "headings", "lists", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000216() throws Exception {
        assertOutput("end2end-000216", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000217() throws Exception {
        assertOutput("end2end-000217", "paragraphs", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000218() throws Exception {
        assertOutput("end2end-000218", "headings", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000219() throws Exception {
        assertOutput("end2end-000219", "paragraphs", "headings", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000220() throws Exception {
        assertOutput("end2end-000220", "lists", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000221() throws Exception {
        assertOutput("end2end-000221", "paragraphs", "lists", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000222() throws Exception {
        assertOutput("end2end-000222", "headings", "lists", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000223() throws Exception {
        assertOutput("end2end-000223", "paragraphs", "headings", "lists", "links", "images", "blockquotes", "code");
    }

    @Test
    public void scenario000224() throws Exception {
        assertOutput("end2end-000224", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000225() throws Exception {
        assertOutput("end2end-000225", "paragraphs", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000226() throws Exception {
        assertOutput("end2end-000226", "headings", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000227() throws Exception {
        assertOutput("end2end-000227", "paragraphs", "headings", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000228() throws Exception {
        assertOutput("end2end-000228", "lists", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000229() throws Exception {
        assertOutput("end2end-000229", "paragraphs", "lists", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000230() throws Exception {
        assertOutput("end2end-000230", "headings", "lists", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000231() throws Exception {
        assertOutput("end2end-000231", "paragraphs", "headings", "lists", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000232() throws Exception {
        assertOutput("end2end-000232", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000233() throws Exception {
        assertOutput("end2end-000233", "paragraphs", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000234() throws Exception {
        assertOutput("end2end-000234", "headings", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000235() throws Exception {
        assertOutput("end2end-000235", "paragraphs", "headings", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000236() throws Exception {
        assertOutput("end2end-000236", "lists", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000237() throws Exception {
        assertOutput("end2end-000237", "paragraphs", "lists", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000238() throws Exception {
        assertOutput("end2end-000238", "headings", "lists", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000239() throws Exception {
        assertOutput("end2end-000239", "paragraphs", "headings", "lists", "links", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000240() throws Exception {
        assertOutput("end2end-000240", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000241() throws Exception {
        assertOutput("end2end-000241", "paragraphs", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000242() throws Exception {
        assertOutput("end2end-000242", "headings", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000243() throws Exception {
        assertOutput("end2end-000243", "paragraphs", "headings", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000244() throws Exception {
        assertOutput("end2end-000244", "lists", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000245() throws Exception {
        assertOutput("end2end-000245", "paragraphs", "lists", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000246() throws Exception {
        assertOutput("end2end-000246", "headings", "lists", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000247() throws Exception {
        assertOutput("end2end-000247", "paragraphs", "headings", "lists", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000248() throws Exception {
        assertOutput("end2end-000248", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000249() throws Exception {
        assertOutput("end2end-000249", "paragraphs", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000250() throws Exception {
        assertOutput("end2end-000250", "headings", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000251() throws Exception {
        assertOutput("end2end-000251", "paragraphs", "headings", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000252() throws Exception {
        assertOutput("end2end-000252", "lists", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000253() throws Exception {
        assertOutput("end2end-000253", "paragraphs", "lists", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000254() throws Exception {
        assertOutput("end2end-000254", "headings", "lists", "links", "images", "formatting", "blockquotes", "code");
    }

    @Test
    public void scenario000255() throws Exception {
        assertOutput("end2end-000255", "paragraphs", "headings", "lists", "links", "images", "formatting", "blockquotes", "code");
    }

    private void assertOutput(String file, String... modules) throws Exception {
        File input = new File("testsuite/input/end2end.kd");
        String html = readFile("testsuite/output/html5/end2end/" + file + ".htm");

        parser.setModules(modules);
        Document document = parser.parseFile(input);
        Html5Renderer renderer = new Html5Renderer();
        document.accept(renderer);
        assertEquals(html, renderer.getOutput());
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
