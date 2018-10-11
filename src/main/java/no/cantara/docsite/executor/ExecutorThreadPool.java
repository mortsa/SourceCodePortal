package no.cantara.docsite.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorThreadPool {

    static final int MAX_RETRIES = 3;
    static int BLOCKING_QUEUE_SIZE = 250;
    static long WAIT_FOR_THREAD_POOL = 50;
    static long WAIT_FOR_TERMINATION = 100;
    static long SLEEP_INTERVAL = 100;
    private static Logger LOG = LoggerFactory.getLogger(ExecutorThreadPool.class);
    private final BlockingQueue internalEventsQueue;
    private ThreadPoolExecutor executorThreadPool;
    private boolean isRunning;

    public ExecutorThreadPool() {
        this.internalEventsQueue = new ArrayBlockingQueue(BLOCKING_QUEUE_SIZE);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        if (isRunning) {
            return;
        }

        executorThreadPool = new ThreadPoolExecutor(
                8, // core size
                50, // max size
                10 * 60, // idle timeout
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE)); // queue with a size;

        try {
            executorThreadPool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    LOG.trace("Rejected: {}", r.getClass().getName());
                }
            });
            executorThreadPool.execute(() -> {
                isRunning = true;
                while (!executorThreadPool.isTerminated()) {
                    try {
                        WorkerTask workerTask = (WorkerTask) internalEventsQueue.take();
                        int retryCount = workerTask.incrementCount();
                        if (retryCount < MAX_RETRIES) {
                            if (retryCount > 0) LOG.warn("RetryCount: {} for {}", retryCount, workerTask.getClass().getName());
                            executorThreadPool.execute(new Worker(workerTask));
                        }

                    } catch (InterruptedException e) {
                        LOG.trace("Exiting thread: {}", Thread.currentThread());
                    } catch (Exception e) {
                        LOG.error("Error or interrupted:", e);
                    }
                }
                LOG.trace("Exiting thread: {}", Thread.currentThread());
            });
            TimeUnit.MILLISECONDS.sleep(WAIT_FOR_THREAD_POOL);

        } catch (InterruptedException e) {
            LOG.error("Error or interrupted:", e);
            isRunning = false;
        }
    }

    public ExecutorService getThreadPool() {
        return executorThreadPool;
    }

    public void shutdown() {
        executorThreadPool.shutdown();
        try {
            if (!executorThreadPool.awaitTermination(WAIT_FOR_TERMINATION, TimeUnit.MILLISECONDS)) {
                executorThreadPool.shutdownNow();
                isRunning = false;
            }
            LOG.info("shutdown success");
        } catch (InterruptedException e) {
            LOG.error("shutdown failed", e);
        }
    }

    public void waitForWorkerCompletion() throws InterruptedException {
        LOG.trace("thradCOunt: {}", countActiveThreads());
        while (countActiveThreads() > 1) {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_INTERVAL);
            } catch (Exception e) {
                throw new InterruptedException(e.getMessage());
            }
        }
    }

    public void queue(WorkerTask workerTask) {
        internalEventsQueue.add(workerTask);
    }

    public int queued() {
        return internalEventsQueue.size();
    }

    public int countActiveThreads() {
        return executorThreadPool.getActiveCount();
    }

}