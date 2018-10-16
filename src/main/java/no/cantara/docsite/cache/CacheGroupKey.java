package no.cantara.docsite.cache;

import no.cantara.docsite.util.JsonUtil;

import java.util.Objects;

public class CacheGroupKey extends CacheKey {

    private static final long serialVersionUID = 9068679113704432070L;

    public final String groupId;

    public CacheGroupKey(String organization, String repoName, String branch, String groupId) {
        super(organization, repoName, branch);
        this.groupId = groupId;
    }

    public boolean compareTo(String groupId) {
        return groupId.equals(this.groupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheGroupKey)) return false;
        if (!super.equals(o)) return false;
        CacheGroupKey that = (CacheGroupKey) o;
        return Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groupId);
    }

    @Override
    public String toString() {
        return JsonUtil.asString(this);
    }

    public static CacheGroupKey of(CacheKey cacheKey, String groupId) {
        return new CacheGroupKey(cacheKey.organization, cacheKey.repoName, cacheKey.branch, groupId);
    }

    public static CacheGroupKey of(String organization, String repoName, String branch, String groupId) {
        return new CacheGroupKey(organization, repoName, branch, groupId);
    }
}