package ch.snowplow.mirrorize.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.FileHashCorrespStringTestData;
import ch.snowplow.mirrorize.testdata.FileHashStringTestData;
import ch.snowplow.mirrorize.testdata.builders.FileHashBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileHashCorrespBuilder;

public class FileHashCorrespTest extends TestCase {

    public void testGetPath() {
        for (FileHashCorrespStringTestData fileHashStrTD : FileHashCorrespStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getPath1(), fileHashStrTD.getFileHash()
                    .getPath());
        }
    }

    public void testGetPathCorresp() {
        for (FileHashCorrespStringTestData fileHashStrTD : FileHashCorrespStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getPath2(), fileHashStrTD.getFileHash()
                    .getCorrespPath());
        }
    }

    public void testGetHash() {
        for (FileHashStringTestData fileHashStrTD : FileHashStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getHash(), fileHashStrTD.getFileHash()
                    .getHash());
        }
    }

    public void testCompareTo() {
        List<FileHashCorresp<String>> fileHashes = Arrays.asList(
                FileHashCorrespStringTestData.FILEHASH_LESS1.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_LESS0.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_REF.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_MORE0.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_MORE1.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_MORE2.getFileHash());
        Iterator<FileHashCorresp<String>> it = fileHashes.iterator();
        FileHashCorresp<String> smaller = it.next();
        FileHashCorresp<String> larger = it.next();
        while (it.hasNext()) {
            smaller = larger;
            larger = it.next();
            assertTrue(smaller.compareTo(larger) < 0);
            assertTrue(larger.compareTo(smaller) > 0);
        }
    }

    public void testHashCode() {
        assertEquals(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode(), FileHashCorrespStringTestData.FILEHASH_REF
                .getFileHash().hashCode());
        assertEquals(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode(),
                FileHashCorrespStringTestData.FILEHASH_SAME_PATH1_HASH
                        .getFileHash().hashCode());

        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashCorrespStringTestData.FILEHASH_SAME_HASH
                .getFileHash().hashCode());
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashCorrespStringTestData.FILEHASH_SAME_PATH1
                .getFileHash().hashCode());
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashCorrespStringTestData.FILEHASH_SAME_PATH2
                .getFileHash().hashCode());
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashCorrespStringTestData.FILEHASH_SAME_PATH1_PATH2
                .getFileHash().hashCode());
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashCorrespStringTestData.FILEHASH_DIFFERENT
                .getFileHash().hashCode());

        assertEquals(
                new FileHashBuilder<String>("").withPath("this/is/a/pat.h")
                        .withHash("abcdef").build().hashCode(),
                new FileHashCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.h").withHash("abcdef")
                        .withPath2("test/path").build().hashCode());
        assertFalse(new FileHashBuilder<String>("").withPath("this/is/a/pat.h")
                .withHash("abcdef").build().hashCode() == new FileHashCorrespBuilder<String>(
                "").withPath1("this/is/a/pat.hh").withHash("abcdef")
                .withPath2("test/path").build().hashCode());
        assertFalse(new FileHashBuilder<String>("").withPath("this/is/a/pat.h")
                .withHash("abcdef").build().hashCode() == new FileHashCorrespBuilder<String>(
                "").withPath1("this/is/a/pat.h").withHash("abcdeff")
                .withPath2("test/path").build().hashCode());

    }

    public void testEquals() {
        assertEquals(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_REF.getFileHash());
        assertEquals(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash(),
                FileHashCorrespStringTestData.FILEHASH_SAME_PATH1_HASH
                        .getFileHash());

        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(FileHashCorrespStringTestData.FILEHASH_SAME_HASH
                        .getFileHash()));
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(FileHashCorrespStringTestData.FILEHASH_SAME_PATH1
                        .getFileHash()));
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(FileHashCorrespStringTestData.FILEHASH_SAME_PATH2
                        .getFileHash()));
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(FileHashCorrespStringTestData.FILEHASH_SAME_PATH1_PATH2
                        .getFileHash()));
        assertFalse(FileHashCorrespStringTestData.FILEHASH_REF.getFileHash()
                .equals(FileHashCorrespStringTestData.FILEHASH_DIFFERENT
                        .getFileHash()));

        assertEquals(
                new FileHashBuilder<String>("").withPath("this/is/a/pat.h")
                        .withHash("abcdef").build(),
                new FileHashCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.h").withHash("abcdef")
                        .withPath2("test/path").build());
        assertFalse(new FileHashBuilder<String>("")
                .withPath("this/is/a/pat.h")
                .withHash("abcdef")
                .build()
                .equals(new FileHashCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.hh").withHash("abcdef")
                        .withPath2("test/path").build()));
        assertFalse(new FileHashBuilder<String>("")
                .withPath("this/is/a/pat.h")
                .withHash("abcdef")
                .build()
                .equals(new FileHashCorrespBuilder<String>("")
                        .withPath1("this/is/a/pat.h").withHash("abcdeff")
                        .withPath2("test/path").build()));
    }

}
