package ch.snowplow.mirrorize.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.HashedFileCorrespStringTestData;
import ch.snowplow.mirrorize.testdata.HashedFileStringTestData;
import ch.snowplow.mirrorize.testdata.builders.HashedFileBuilder;
import ch.snowplow.mirrorize.testdata.builders.HashedFileCorrespBuilder;

public class HashedFileCorrespTest extends TestCase {

    public void testGetPath() {
        for (HashedFileCorrespStringTestData fileHashStrTD : HashedFileCorrespStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getPath1(), fileHashStrTD.getFileHash()
                    .getPath());
        }
    }

    public void testGetPathCorresp() {
        for (HashedFileCorrespStringTestData fileHashStrTD : HashedFileCorrespStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getPath2(), fileHashStrTD.getFileHash()
                    .getCorrespPath());
        }
    }

    public void testGetHash() {
        for (HashedFileStringTestData fileHashStrTD : HashedFileStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getHash(), fileHashStrTD.getFileHash()
                    .getHash());
        }
    }

    public void testCompareTo() {
        List<HashedFileCorresp<String>> fileHashes = Arrays.asList(
                HashedFileCorrespStringTestData.FILEHASH_LESS1.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_LESS0.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_MORE0.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_MORE1.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_MORE2.getFileHash());
        Iterator<HashedFileCorresp<String>> it = fileHashes.iterator();
        HashedFileCorresp<String> smaller = it.next();
        HashedFileCorresp<String> larger = it.next();
        while (it.hasNext()) {
            smaller = larger;
            larger = it.next();
            assertTrue(smaller.compareTo(larger) < 0);
            assertTrue(larger.compareTo(smaller) > 0);
        }
    }

    public void testHashCode() {
        assertEquals(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode(), HashedFileCorrespStringTestData.FILEHASH_REF
                .getFileHash().hashCode());
        assertEquals(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode(),
                HashedFileCorrespStringTestData.FILEHASH_SAME_PATH1_HASH
                        .getFileHash().hashCode());

        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileCorrespStringTestData.FILEHASH_SAME_HASH
                .getFileHash().hashCode());
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileCorrespStringTestData.FILEHASH_SAME_PATH1
                .getFileHash().hashCode());
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileCorrespStringTestData.FILEHASH_SAME_PATH2
                .getFileHash().hashCode());
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileCorrespStringTestData.FILEHASH_SAME_PATH1_PATH2
                .getFileHash().hashCode());
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileCorrespStringTestData.FILEHASH_DIFFERENT
                .getFileHash().hashCode());

        assertEquals(
                new HashedFileBuilder<String>("").withPath("this/is/a/pat.h")
                        .withHash("abcdef").build().hashCode(),
                new HashedFileCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.h").withHash("abcdef")
                        .withPath2("test/path").build().hashCode());
        assertFalse(new HashedFileBuilder<String>("").withPath("this/is/a/pat.h")
                .withHash("abcdef").build().hashCode() == new HashedFileCorrespBuilder<String>(
                "").withPath1("this/is/a/pat.hh").withHash("abcdef")
                .withPath2("test/path").build().hashCode());
        assertFalse(new HashedFileBuilder<String>("").withPath("this/is/a/pat.h")
                .withHash("abcdef").build().hashCode() == new HashedFileCorrespBuilder<String>(
                "").withPath1("this/is/a/pat.h").withHash("abcdeff")
                .withPath2("test/path").build().hashCode());

    }

    public void testEquals() {
        assertEquals(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash());
        assertEquals(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash(),
                HashedFileCorrespStringTestData.FILEHASH_SAME_PATH1_HASH
                        .getFileHash());

        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(HashedFileCorrespStringTestData.FILEHASH_SAME_HASH
                        .getFileHash()));
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(HashedFileCorrespStringTestData.FILEHASH_SAME_PATH1
                        .getFileHash()));
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(HashedFileCorrespStringTestData.FILEHASH_SAME_PATH2
                        .getFileHash()));
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(HashedFileCorrespStringTestData.FILEHASH_SAME_PATH1_PATH2
                        .getFileHash()));
        assertFalse(HashedFileCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(HashedFileCorrespStringTestData.FILEHASH_DIFFERENT
                        .getFileHash()));

        assertEquals(
                new HashedFileBuilder<String>("").withPath("this/is/a/pat.h")
                        .withHash("abcdef").build(),
                new HashedFileCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.h").withHash("abcdef")
                        .withPath2("test/path").build());
        assertFalse(new HashedFileBuilder<String>("")
                .withPath("this/is/a/pat.h")
                .withHash("abcdef")
                .build()
                .equals(new HashedFileCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.hh").withHash("abcdef")
                        .withPath2("test/path").build()));
        assertFalse(new HashedFileBuilder<String>("")
                .withPath("this/is/a/pat.h")
                .withHash("abcdef")
                .build()
                .equals(new HashedFileCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.h").withHash("abcdeff")
                        .withPath2("test/path").build()));
    }

}
