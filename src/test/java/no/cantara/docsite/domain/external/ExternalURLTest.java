package no.cantara.docsite.domain.external;

import no.cantara.docsite.cache.CacheRepositoryKey;
import no.cantara.docsite.domain.scm.ScmRepositoryDefinition;
import no.ssb.config.DynamicConfiguration;
import no.ssb.config.StoreBasedDynamicConfiguration;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ExternalURLTest {

    static DynamicConfiguration configuration() {
        DynamicConfiguration configuration = new StoreBasedDynamicConfiguration.Builder()
                .propertiesResource("application-defaults.properties")
                .propertiesResource("security.properties")
                .propertiesResource("application.properties")
                .build();
        return configuration;
    }

    @Test
    public void testURLs() {
        DynamicConfiguration configuration = configuration();
        List<ExternalURL<?>> list = new ArrayList<>();

        list.add(new GitHubRawRepoURL(ScmRepositoryDefinition.of(configuration, CacheRepositoryKey.of("Cantara", "repo1", "master", "group1"),
                "id", "desc", "group", "http://example.com")));

        list.add(new GitHubApiReadmeURL(ScmRepositoryDefinition.of(configuration, CacheRepositoryKey.of("Cantara", "repo1", "master", "group1"),
                "id", "desc", "group", "http://example.com")));

        list.add(new GitHubApiContentsURL(ScmRepositoryDefinition.of(configuration, CacheRepositoryKey.of("Cantara", "repo1", "master", "group1"),
                "id", "desc", "group", "http://example.com")));

        list.add(new GitHubHtmlURL("http://example.com"));

        assertEquals(list.stream().filter(externalURL -> externalURL.isGenericOf(String.class)).count(), 1);
        assertEquals(list.stream().filter(externalURL -> externalURL.isGenericOf(ScmRepositoryDefinition.class)).count(), 3);
    }
}
