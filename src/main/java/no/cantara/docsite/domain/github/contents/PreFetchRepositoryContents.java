package no.cantara.docsite.domain.github.contents;

import no.cantara.docsite.cache.CacheStore;
import no.cantara.docsite.domain.config.RepositoryConfigLoader;
import no.cantara.docsite.executor.ExecutorThreadPool;
import no.cantara.docsite.task.FetchCommitRevisionTask;
import no.cantara.docsite.task.FetchMavenPOMTask;
import no.cantara.docsite.task.FetchPageTask;
import no.ssb.config.DynamicConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PreFetchRepositoryContents {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryConfigLoader.class);
    private final DynamicConfiguration configuration;
    private final Map<String, RepositoryConfigLoader.Group> repoConfig;
    private final ExecutorThreadPool executorService;
    private final CacheStore cacheStore;

    public PreFetchRepositoryContents(DynamicConfiguration configuration, Map<String, RepositoryConfigLoader.Group> repoConfig, ExecutorThreadPool executorService, CacheStore cacheStore) {
        this.configuration = configuration;
        this.repoConfig = repoConfig;
        this.executorService = executorService;
        this.cacheStore = cacheStore;
    }

    public void fetch() {
        repoConfig.values().forEach(g -> {
            LOG.trace("group: {}", g.groupId);
            g.getRepositories().values().forEach(r -> {
                LOG.trace("  repo: {} - {} - {} - {} - {}", r.repoName, r.repoURL, r.rawRepoURL, r.readmeURL, r.contentsURL);
                executorService.queue(new FetchMavenPOMTask(configuration, executorService, cacheStore, r));
                executorService.queue(new FetchPageTask(configuration, executorService, cacheStore, r));
                executorService.queue(new FetchCommitRevisionTask(configuration, executorService, cacheStore, g.organization, r));
            });
        });
    }

}