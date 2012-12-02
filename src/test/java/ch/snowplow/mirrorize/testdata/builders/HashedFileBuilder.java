package ch.snowplow.mirrorize.testdata.builders;

import ch.snowplow.mirrorize.common.HashedFile;

public class HashedFileBuilder<T> implements Buildable<HashedFile<T>> {

    String path = "this/is/a/path";
    T hash;

    public HashedFileBuilder(T defaultHash) {
        this.hash = defaultHash;
    }

    public HashedFileBuilder<T> withPath(String path) {
        this.path = path;
        return this;
    }

    public HashedFileBuilder<T> withHash(T hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public HashedFile<T> build() {
        return new HashedFile<T>(new PathBuilder().withPath(path).build(), hash);
    }

}
