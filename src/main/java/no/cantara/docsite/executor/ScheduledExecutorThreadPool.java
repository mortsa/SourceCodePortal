package no.cantara.docsite.executor;

import no.cantara.docsite.cache.CacheCantaraWikiKey;
import no.cantara.docsite.cache.CacheStore;
import no.cantara.docsite.domain.confluence.cantara.FetchCantaraWikiTask;
import no.ssb.config.DynamicConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorThreadPool implements ScheduledExecutorService {

    private static Logger LOG = LoggerFactory.getLogger(ScheduledExecutorThreadPool.class);

    private final java.util.concurrent.ScheduledExecutorService scheduledExecutorService;
    private final DynamicConfiguration configuration;
    private final ExecutorService executorService;
    private final CacheStore cacheStore;

    ScheduledExecutorThreadPool(DynamicConfiguration configuration, ExecutorService executorService, CacheStore cacheStore) {
        this.configuration = configuration;
        this.executorService = executorService;
        this.cacheStore = cacheStore;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void start() {
        LOG.info("Starting ScheduledExecutorService..");
        scheduledExecutorService.scheduleAtFixedRate(new ScheduledThread(configuration, executorService, cacheStore), 0, configuration.evaluateToInt("scheduled.tasks.interval"), TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            if (!scheduledExecutorService.awaitTermination(WAIT_FOR_TERMINATION, TimeUnit.MILLISECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
            LOG.info("ExecutorService shutdown success");
        } catch (InterruptedException e) {
            LOG.error("ExecutorService shutdown failed", e);
        }
    }

    @Override
    public ScheduledExecutorThreadPool getThreadPool() {
        return this;
    }

    static class ScheduledThread implements Runnable {

        private final DynamicConfiguration configuration;
        private final ExecutorService executorService;
        private final CacheStore cacheStore;

        public ScheduledThread(DynamicConfiguration configuration, ExecutorService executorService, CacheStore cacheStore) {
            this.configuration = configuration;
            this.executorService = executorService;
            this.cacheStore = cacheStore;
        }

        @Override
        public void run() {
            executorService.queue(new FetchCantaraWikiTask(configuration, executorService, cacheStore, CacheCantaraWikiKey.of("xmas-beer", "46137421")));
            executorService.queue(new FetchCantaraWikiTask(configuration, executorService, cacheStore, CacheCantaraWikiKey.of("about", "16515095")));
        }
    }
}
