package ch.snowplow.mirrorize.testdata;

import java.util.Arrays;
import java.util.Collection;

import ch.snowplow.mirrorize.common.HashedFileRelat;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.testdata.builders.HashedFileRelatBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public enum HashedFileRelatStringTestData {

    FILEHASH_REF("this/is/a/pat.h", "this/is/b/pat.h", "abcdef"),
    // TODO do tests with FileHashRelat objects with multiple related paths
    FILEHASH_REF_R("this/is/a/pat.h", new String[] { "this/is/c/pat.h",
            "this/is/b/pat.h" }, "abcdef"),

    // hash code / equals
    FILEHASH_SAME_HASH(
            "this/is/another/p.ath",
            "this/is/another/p.ath",
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

    private String path;
    private String[] relatPaths;
    private String hash;

    HashedFileRelatStringTestData(String path, String relatPath, String hash) {
        this.path = path;
        this.relatPaths = new String[] { relatPath };
        this.hash = hash;
    }

    HashedFileRelatStringTestData(String path, String[] relatPaths, String hash) {
        this.path = path;
        this.relatPaths = relatPaths;
        this.hash = hash;
    }

    public HashedFileRelat<String> getHashedFileRelat() {
        return new HashedFileRelatBuilder<String>("").withPath(path)
                .withRelatedPaths(relatPaths).withHash(hash).build();
    }

    public Path getPath() {
        return new PathBuilder().withPath(path).build();
    }

    public Collection<String> getRelatPaths() {
        return Arrays.asList(relatPaths);
    }

    public String getHash() {
        return hash;
    }

}