package ch.snowplow.mirrorize.testdata;

import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.testdata.builders.FileHashBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public enum FileHashStringTestData {

    FILEHASH_REF("this/is/a/pat.h", "abcdef"),

    // hash code / equals
    FILEHASH_SAMEHASH("this/is/another/p.ath", "abcdef"),
    FILEHASH_SAMEPATH("this/is/a/pat.h", "fedcba"),
    FILEHASH_DIFFERENT("this/is/another/pa.th", "different"),

    // comparator
    FILEHASH_LESS("this/is/a/pat.a", "abcdef"),
    FILEHASH_LESS2("this/is/a/pat.a", "zzzzzz"), // comparison only based on
                                                 // path, not on hash
    FILEHASH_MORE("this/is/a/pat.z", "abcdef"),
    FILEHASH_MORE2("this/is/a/pat.z", "aaaaaa"); // comparison only based on
                                                 // path, not on hash

    private String path;
    private String hash;

    FileHashStringTestData(String path, String hash) {
        this.path = path;
        this.hash = hash;
    }

    public FileHash<String> getFileHash() {
        return new FileHashBuilder<String>("default").withPath(path)
                .withHash(hash).build();
    }

    public Path getPath() {
        return new PathBuilder().withPath(path).build();
    }

    public String getHash() {
        return hash;
    }

}