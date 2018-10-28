package no.cantara.docsite.domain.external;

import no.cantara.docsite.domain.scm.RepositoryDefinition;

import java.util.Objects;

public class ShieldsIOGroupCommitURL extends ExternalURL<RepositoryDefinition> {

    private static final long serialVersionUID = 201847051522984036L;
    public static final String KEY = "shieldsGroupCommit";

    public ShieldsIOGroupCommitURL(RepositoryDefinition internal) {
        super(internal);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getExternalURL() {
        return String.format("https://img.shields.io/github/last-commit/%s/%s.svg", internal.cacheGroupKey.organization, internal.cacheGroupKey.repoName);
    }

    public String getExternalGroupURL() {
        Objects.requireNonNull(internal.defaultGroupRepoName);
        return String.format("https://img.shields.io/github/last-commit/%s/%s.svg", internal.cacheGroupKey.organization, internal.defaultGroupRepoName);
    }
}
