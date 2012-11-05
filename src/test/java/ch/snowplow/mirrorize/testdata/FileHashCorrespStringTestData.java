package ch.snowplow.mirrorize.testdata;

import ch.snowplow.mirrorize.common.FileHashCorresp;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.testdata.builders.FileHashCorrespBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public enum FileHashCorrespStringTestData {

    FILEHASH_REF("this/is/a/pat.h", "this/is/b/pat.h", "abcdef"),

    // hash code / equals
    FILEHASH_SAME_HASH("this/is/another/p.ath", "this/is/another/p.ath",
            "abcdef"),
    FILEHASH_SAME_PATH1("this/is/a/pat.h", "this/path", "fedcba"),
    FILEHASH_SAME_PATH2("this/path", "this/is/b/pat.h", "fedcba"),
    FILEHASH_SAME_PATH1_PATH2("this/is/a/pat.h", "this/is/b/pat.h", "fedcba"),
    FILEHASH_SAME_PATH1_HASH("this/is/a/pat.h", "this/path", "abcdef"),
    FILEHASH_SAME_PATH2_HASH("this/path", "this/is/b/pat.h", "abcdef"),
    FILEHASH_DIFFERENT("this/path.1", "this/path.2", "12345"),

    // comparator
    FILEHASH_LESS2("this/is/a/pat.a", "this/is/b/pat.h", "abcdef"),
    FILEHASH_LESS1("this/is/a/pat.b", "zzzzzzzzzzz", "zzzzzzz"),
    FILEHASH_LESS0("this/is/a/pat.c", "aaaaaaaaaaa", "aaaaaaa"),
    FILEHASH_MORE0("this/is/a/pat.i", "zzzzzzzzzzz", "zzzzzzz"),
    FILEHASH_MORE1("this/is/a/pat.j", "aaaaaaaaaaa", "aaaaaaa"),
    FILEHASH_MORE2("this/is/a/pat.k", "this/is/b/pat.h", "abcdef");

    private String path1;
    private String path2;
    private String hash;

    FileHashCorrespStringTestData(String path1, String path2, String hash) {
        this.path1 = path1;
        this.path2 = path2;
        this.hash = hash;
    }

    public FileHashCorresp<String> getFileHash() {
        return new FileHashCorrespBuilder<String>("").withPath1(path1)
                .withPath2(path2).withHash(hash).build();
    }

    public Path getPath1() {
        return new PathBuilder().withPath(path1).build();
    }

    public Path getPath2() {
        return new PathBuilder().withPath(path2).build();
    }

    public String getHash() {
        return hash;
    }

}