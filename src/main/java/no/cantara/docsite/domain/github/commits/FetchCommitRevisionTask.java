package no.cantara.docsite.domain.github.commits;

import no.cantara.docsite.cache.CacheGroupKey;
import no.cantara.docsite.cache.CacheKey;
import no.cantara.docsite.cache.CacheShaKey;
import no.cantara.docsite.cache.CacheStore;
import no.cantara.docsite.commands.GetGitHubCommand;
import no.cantara.docsite.executor.ExecutorService;
import no.cantara.docsite.executor.WorkerTask;
import no.cantara.docsite.util.JsonbFactory;
import no.ssb.config.DynamicConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Set;

public class FetchCommitRevisionTask extends WorkerTask {

    private static final Logger LOG = LoggerFactory.getLogger(FetchCommitRevisionsTask.class);
    private final CacheStore cacheStore;
    private final CacheKey cacheKey;
    private final String sha;

    public FetchCommitRevisionTask(DynamicConfiguration configuration, ExecutorService executor, CacheStore cacheStore, CacheKey cacheKey, String sha) {
        super(configuration, executor);
        this.cacheStore = cacheStore;
        this.cacheKey = cacheKey;
        this.sha = sha;
    }

    @Override
    public void execute() {
        String url = String.format("https://api.github.com/repos/%s/%s/commits/%s", cacheKey.organization, cacheKey.repoName, sha);
        GetGitHubCommand<String> cmd = new GetGitHubCommand<>("githubPage", getConfiguration(), Optional.of(this), url, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = cmd.execute();
        if (GetGitHubCommand.anyOf(response, 200)) {
            CommitRevision commitRevision = JsonbFactory.instance().fromJson(response.body(), CommitRevision.class);
            Set<CacheGroupKey> keys = cacheStore.getCacheGroupKeys(CacheKey.of(cacheKey.organization, cacheKey.repoName, cacheKey.branch));
            for(CacheGroupKey key : keys) {
                CacheShaKey cacheShaKey = CacheShaKey.of(cacheKey, key.groupId, commitRevision.sha);
                cacheStore.getCommits().put(cacheShaKey, commitRevision);
            }
        } else {
            LOG.warn("Resource not found: {} ({})", response.uri(), response.statusCode());
        }
    }

}
