package ch.snowplow.mirrorize.testdata.builders;

import ch.snowplow.mirrorize.common.FileHashCorresp;

public class FileHashCorrespBuilder<T> implements Buildable<FileHashCorresp<T>> {

    String path1 = "this/is/a/path.1";
    String path2 = "this/is/a/path.2";
    T hash;

    public FileHashCorrespBuilder(T defaultHash) {
        this.hash = defaultHash;
    }

    public FileHashCorrespBuilder<T> withPath1(String path1) {
        this.path1 = path1;
        return this;
    }

    public FileHashCorrespBuilder<T> withPath2(String path2) {
        this.path2 = path2;
        return this;
    }

    public FileHashCorrespBuilder<T> withHash(T hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public FileHashCorresp<T> build() {
        return new FileHashCorresp<T>(
                new PathBuilder().withPath(path1).build(), new PathBuilder()
                        .withPath(path2).build(), hash);
    }
}
