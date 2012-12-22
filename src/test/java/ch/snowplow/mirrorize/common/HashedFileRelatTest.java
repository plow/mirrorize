package ch.snowplow.mirrorize.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.HashedFileRelatStringTestData;
import ch.snowplow.mirrorize.testdata.HashedFileStringTestData;
import ch.snowplow.mirrorize.testdata.builders.HashedFileBuilder;
import ch.snowplow.mirrorize.testdata.builders.HashedFileRelatBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathSetBuilder;

public class HashedFileRelatTest extends TestCase {

    public void testGetPath() {
        for (HashedFileRelatStringTestData hashFileRelatStrTD : HashedFileRelatStringTestData
                .values()) {
            assertEquals(hashFileRelatStrTD.getPath(), hashFileRelatStrTD
                    .getHashedFileRelat().getPath());
        }
    }

    public void testGetPathCorresp() {
        for (HashedFileRelatStringTestData hashedFileRelatStrTD : HashedFileRelatStringTestData
                .values()) {
            assertEquals(
                    new PathSetBuilder()
                            .addPaths(hashedFileRelatStrTD.getRelatPaths()).build(),
                    hashedFileRelatStrTD.getHashedFileRelat().getRelatedPaths());
        }
    }

    public void testGetHash() {
        for (HashedFileStringTestData hashedFileRelatStrTD : HashedFileStringTestData
                .values()) {
            assertEquals(hashedFileRelatStrTD.getHash(), hashedFileRelatStrTD.getFileHash()
                    .getHash());
        }
    }

    public void testCompareTo() {
        List<HashedFileRelat<String>> hashedFileRelats = Arrays
                .asList(HashedFileRelatStringTestData.FILEHASH_LESS1
                        .getHashedFileRelat(),
                        HashedFileRelatStringTestData.FILEHASH_LESS0
                                .getHashedFileRelat(),
                        HashedFileRelatStringTestData.FILEHASH_REF
                                .getHashedFileRelat(),
                        HashedFileRelatStringTestData.FILEHASH_MORE0
                                .getHashedFileRelat(),
                        HashedFileRelatStringTestData.FILEHASH_MORE1
                                .getHashedFileRelat(),
                        HashedFileRelatStringTestData.FILEHASH_MORE2
                                .getHashedFileRelat());
        Iterator<HashedFileRelat<String>> it = hashedFileRelats.iterator();
        HashedFileRelat<String> smaller = it.next();
        HashedFileRelat<String> larger = it.next();
        while (it.hasNext()) {
            smaller = larger;
            larger = it.next();
            assertTrue(smaller.compareTo(larger) < 0);
            assertTrue(larger.compareTo(smaller) > 0);
        }
    }

    public void testHashCode() {
        assertEquals(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode(),
                HashedFileRelatStringTestData.FILEHASH_REF.getHashedFileRelat()
                        .hashCode());
        assertEquals(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode(),
                HashedFileRelatStringTestData.FILEHASH_SAME_PATH1_HASH
                        .getHashedFileRelat().hashCode());

        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode() == HashedFileRelatStringTestData.FILEHASH_SAME_HASH
                .getHashedFileRelat().hashCode());
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode() == HashedFileRelatStringTestData.FILEHASH_SAME_PATH1
                .getHashedFileRelat().hashCode());
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode() == HashedFileRelatStringTestData.FILEHASH_SAME_PATH2
                .getHashedFileRelat().hashCode());
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode() == HashedFileRelatStringTestData.FILEHASH_SAME_PATH1_PATH2
                .getHashedFileRelat().hashCode());
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().hashCode() == HashedFileRelatStringTestData.FILEHASH_DIFFERENT
                .getHashedFileRelat().hashCode());

        assertEquals(
                new HashedFileBuilder<String>("").withPath("this/is/a/pat.h")
                        .withHash("abcdef").build().hashCode(),
                new HashedFileRelatBuilder<String>("")
                        .withPath("this/is/a/pat.h").withHash("abcdef")
                        .withRelatedPath("test/path").build().hashCode());
        assertFalse(new HashedFileBuilder<String>("")
                .withPath("this/is/a/pat.h").withHash("abcdef").build()
                .hashCode() == new HashedFileRelatBuilder<String>("")
                .withPath("this/is/a/pat.hh").withHash("abcdef")
                .withRelatedPath("test/path").build().hashCode());
        assertFalse(new HashedFileBuilder<String>("")
                .withPath("this/is/a/pat.h").withHash("abcdef").build()
                .hashCode() == new HashedFileRelatBuilder<String>("")
                .withPath("this/is/a/pat.h").withHash("abcdeff")
                .withRelatedPath("test/path").build().hashCode());

    }

    public void testEquals() {
        assertEquals(
                HashedFileRelatStringTestData.FILEHASH_REF.getHashedFileRelat(),
                HashedFileRelatStringTestData.FILEHASH_REF.getHashedFileRelat());
        assertEquals(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat(),
                HashedFileRelatStringTestData.FILEHASH_SAME_PATH1_HASH
                        .getHashedFileRelat());

        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().equals(
                        HashedFileRelatStringTestData.FILEHASH_SAME_HASH
                                .getHashedFileRelat()));
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().equals(
                        HashedFileRelatStringTestData.FILEHASH_SAME_PATH1
                                .getHashedFileRelat()));
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().equals(
                        HashedFileRelatStringTestData.FILEHASH_SAME_PATH2
                                .getHashedFileRelat()));
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().equals(
                        HashedFileRelatStringTestData.FILEHASH_SAME_PATH1_PATH2
                                .getHashedFileRelat()));
        assertFalse(HashedFileRelatStringTestData.FILEHASH_REF
                .getHashedFileRelat().equals(
                        HashedFileRelatStringTestData.FILEHASH_DIFFERENT
                                .getHashedFileRelat()));

        assertEquals(
                new HashedFileBuilder<String>("").withPath("this/is/a/pat.h")
                        .withHash("abcdef").build(),
                new HashedFileRelatBuilder<String>("")
                        .withPath("this/is/a/pat.h").withHash("abcdef")
                        .withRelatedPath("test/path").build());
        assertFalse(new HashedFileBuilder<String>("")
                .withPath("this/is/a/pat.h")
                .withHash("abcdef")
                .build()
                .equals(new HashedFileRelatBuilder<String>("")
                        .withPath("this/is/a/pat.hh").withHash("abcdef")
                        .withRelatedPath("test/path").build()));
        assertFalse(new HashedFileBuilder<String>("")
                .withPath("this/is/a/pat.h")
                .withHash("abcdef")
                .build()
                .equals(new HashedFileRelatBuilder<String>("")
                        .withPath("this/is/a/pat.h").withHash("abcdeff")
                        .withRelatedPath("test/path").build()));
    }

}
