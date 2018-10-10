package no.cantara.docsite.github;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ReadmeAsciidocWikiTest {

    @Test
    public void testAsciidocRenderer() throws IOException {
        try (InputStream is = ClassLoader.getSystemResourceAsStream("github/README.adoc")) {
            String adoc = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            ReadmeAsciidocWiki doic = new ReadmeAsciidocWiki(adoc);
            System.out.println("adoc: " + doic);
        }
    }
}
