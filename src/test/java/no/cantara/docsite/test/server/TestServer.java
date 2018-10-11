package no.cantara.docsite.test.server;

import no.cantara.docsite.Main;
import no.ssb.config.DynamicConfiguration;

import javax.cache.CacheManager;
import java.net.MalformedURLException;
import java.net.URL;

public class TestServer implements TestUriResolver {

    final DynamicConfiguration configuration;
    final Main application;
    private final int testServerServicePort;

    public TestServer(DynamicConfiguration configuration, int testServerServicePort) {
        this.configuration = configuration;
        this.testServerServicePort = testServerServicePort;
        application = new Main(configuration, configuration.evaluateToString("http.host"), testServerServicePort);
    }

    public void start() {
        application.start();
    }

    public void stop() {
        application.stop();
    }

    public String getTestServerHost() {
        return application.getHost();
    }

    public int getTestServerServicePort() {
        return testServerServicePort;
    }

    public CacheManager getCacheManager() {
        return application.getCacheManager();
    }

    @Override
    public String testURL(String uri) {
        try {
            URL url = new URL("http", application.getHost(), application.getPort(), uri);
            return url.toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public DynamicConfiguration getConfiguration() {
        return configuration;
    }

}
