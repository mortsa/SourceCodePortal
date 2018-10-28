package no.cantara.docsite.domain.scm;

import no.cantara.docsite.cache.CacheKey;
import no.cantara.docsite.cache.CacheService;
import no.cantara.docsite.cache.CacheStore;

import javax.cache.Cache;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RepositoryContentsService implements CacheService<CacheKey, RepositoryContents> {

    private final CacheStore cacheStore;

    public RepositoryContentsService(CacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @Override
    public RepositoryContents get(CacheKey key) {
        return cacheStore.getPages().get(key);
    }

    @Override
    public Iterator<Cache.Entry<CacheKey, RepositoryContents>> getAll() {
        return cacheStore.getPages().iterator();
    }

    @Override
    public Set<CacheKey> keySet() {
        return StreamSupport.stream(cacheStore.getPages().spliterator(), false).map(m -> m.getKey()).collect(Collectors.toSet());
    }

    @Override
    public Map<CacheKey, RepositoryContents> entrySet() {
        return StreamSupport.stream(cacheStore.getPages().spliterator(), false).collect(Collectors.toMap(Cache.Entry::getKey, Cache.Entry::getValue));
    }
}