package com.texttalk.core;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Test
public class TextProcessorTest {

    private final static Logger logger = LoggerFactory.getLogger(TextProcessorTest.class);

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testTextSplit() {
        List<String> splittedText = TextProcessor.splitText("Aaaa      bbbbb   cccccccc asashdgadxxxxxxxxxssssssssssswwwwwwwwwww jashd llaksjd klaksjd klla 111 222 333",
                20
        );

        logger.debug("List size: " + splittedText.size());

        for(String text: splittedText) {
            logger.debug("'" + text + "'");
        }
    }

    @Test
    public void testTextSplit2() {
        List<String> splittedText = TextProcessor.splitText("Leidinys rengiamas Nacionalinės bibliografijos duomenų " +
                "banko (NBDB) bei LNB elektroninio katalogo pagrindu. Medžiaga grupuojama temomis, o jų viduje - " +
                "abėcėlės tvarka išdėstyti knygų (atskirti žvaigždutėmis), serialinių leidinių ir jų sudedamųjų dalių " +
                "bibliografiniai įrašai, pabaigoje pateikiamos nuorodos. Turinyje prie temos pavadinimo nurodomi " +
                "bibliografinių įrašų numeriai. Rodyklės pabaigoje pridėtas sąrašas leidinių, kurių straipsniai " +
                "užregistruoti, asmenvardžių rodyklė ir skelbiami leidinių bibliotekininkystės klausimais interneto adresai.",
                50
        );

        logger.debug("List size: " + splittedText.size());

        for(String text: splittedText) {
            logger.debug("'" + text + "'");
        }
    }
}