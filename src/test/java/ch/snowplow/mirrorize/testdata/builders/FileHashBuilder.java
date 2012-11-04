package ch.snowplow.mirrorize.testdata.builders;

import ch.snowplow.mirrorize.common.FileHash;

public class FileHashBuilder<T> implements Buildable<FileHash<T>> {

    String path = "this/is/a/path";
    T hash;

    public FileHashBuilder(T defaultHash) {
        this.hash = defaultHash;
    }

    public FileHashBuilder<T> withPath(String path) {
        this.path = path;
        return this;
    }

    public FileHashBuilder<T> withHash(T hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public FileHash<T> build() {
        return new FileHash<T>(new PathBuilder().withPath(path).build(), hash);
    }

}
