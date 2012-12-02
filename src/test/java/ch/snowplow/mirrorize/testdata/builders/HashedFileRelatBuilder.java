package ch.snowplow.mirrorize.testdata.builders;

import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.common.HashedFileRelat;

public class HashedFileRelatBuilder<T> implements Buildable<HashedFileRelat<T>> {

    String path = "this/is/a/path.1";
    Set<String> relatedPaths = new HashSet<String>();
    T hash;

    public HashedFileRelatBuilder(T defaultHash) {
        this.hash = defaultHash;
    }

    public HashedFileRelatBuilder<T> withPath(String path) {
        this.path = path;
        return this;
    }

    public HashedFileRelatBuilder<T> withRelatedPath(String path) {
        relatedPaths.add(path);
        return this;
    }

    public HashedFileRelatBuilder<T> withRelatedPaths(String[] paths) {
        for (String path : paths) {
            relatedPaths.add(path);
        }
        return this;
    }

    public HashedFileRelatBuilder<T> withHash(T hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public HashedFileRelat<T> build() {

        if (relatedPaths.size() == 0) {
            relatedPaths.add("this/is/a/path.2");
        }
        PathSetBuilder pathSetBldr = new PathSetBuilder();
        for (String p : relatedPaths) {
            pathSetBldr.addPath(p);
        }
        return new HashedFileRelat<T>(new PathBuilder().withPath(path).build(),
                hash, pathSetBldr.build());
    }
}
