package no.cantara.docsite.domain.github.pages;

import no.cantara.docsite.cache.CacheInitializer;
import no.cantara.docsite.cache.CacheStore;
import no.cantara.docsite.domain.config.RepositoryConfigLoader;
import no.cantara.docsite.domain.config.RepositoryConfigLoaderTest;
import no.cantara.docsite.executor.ExecutorThreadPool;
import no.ssb.config.DynamicConfiguration;
import no.ssb.config.StoreBasedDynamicConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class GitHubPageServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryConfigLoaderTest.class);

    static DynamicConfiguration configuration() {
        return new StoreBasedDynamicConfiguration.Builder()
                .propertiesResource("application-defaults.properties")
                .propertiesResource("security.properties")
                .propertiesResource("application.properties")
                .build();
    }

    static CacheStore cacheStore(DynamicConfiguration configuration) {
        return CacheInitializer.initialize(configuration);
    }

    @Test //(enabled = false)
    public void testName() {
        ExecutorThreadPool executorService = new ExecutorThreadPool();
        executorService.start();
        DynamicConfiguration configuration = configuration();
        CacheStore cacheStore = cacheStore(configuration);
        RepositoryConfigLoader loader = new RepositoryConfigLoader(configuration, cacheStore);
        loader.load();

        cacheStore.getRepositoryGroups().forEach(rg -> {
            // TODO lookup CacheGroupKey for CacheKey and pass that to worker task
            // TODO use CacheGroupKey for pages
//            executorService.queue(new FetchPageTask(configuration, executorService, cacheStore, rg.getKey(), rg.getValue().readmeURL));
            LOG.trace("{} - {}", rg.getKey(), rg.getValue().readmeURL);
//            cacheStore.getPages().get(cacheStore.getCacheKeys().get(rg.getKey().asCacheKey()));
        });

        executorService.shutdown();
    }
}
