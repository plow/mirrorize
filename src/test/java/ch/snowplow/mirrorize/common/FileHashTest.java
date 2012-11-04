package ch.snowplow.mirrorize.common;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.FileHashStringTestData;
import ch.snowplow.mirrorize.testdata.builders.FileHashBuilder;

public class FileHashTest extends TestCase {

    public void testGetPath() {
        for (FileHashStringTestData fileHashStrTD : FileHashStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getPath(), fileHashStrTD.getFileHash()
                    .getPath());
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
        assertTrue(FileHashStringTestData.FILEHASH_REF.getFileHash().compareTo(
                FileHashStringTestData.FILEHASH_REF.getFileHash()) == 0);
        assertTrue(FileHashStringTestData.FILEHASH_REF.getFileHash().compareTo(
                FileHashStringTestData.FILEHASH_LESS.getFileHash()) > 0);
        assertTrue(FileHashStringTestData.FILEHASH_REF.getFileHash().compareTo(
                FileHashStringTestData.FILEHASH_LESS2.getFileHash()) > 0);
        assertTrue(FileHashStringTestData.FILEHASH_REF.getFileHash().compareTo(
                FileHashStringTestData.FILEHASH_MORE.getFileHash()) < 0);
        assertTrue(FileHashStringTestData.FILEHASH_REF.getFileHash().compareTo(
                FileHashStringTestData.FILEHASH_MORE2.getFileHash()) < 0);
    }

    public void testHashCode() {
        assertEquals(FileHashStringTestData.FILEHASH_REF.getFileHash()
                .hashCode(), FileHashStringTestData.FILEHASH_REF.getFileHash()
                .hashCode());
        assertEquals(
                FileHashStringTestData.FILEHASH_REF.getFileHash().hashCode(),
                (new FileHashBuilder<String>(""))
                        .withPath(
                                FileHashStringTestData.FILEHASH_REF.getPath()
                                        .getPath())
                        .withHash(FileHashStringTestData.FILEHASH_REF.getHash())
                        .build().hashCode());
        assertFalse(FileHashStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashStringTestData.FILEHASH_SAMEHASH
                .getFileHash().hashCode());
        assertFalse(FileHashStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashStringTestData.FILEHASH_SAMEPATH
                .getFileHash().hashCode());
        assertFalse(FileHashStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == FileHashStringTestData.FILEHASH_DIFFERENT
                .getFileHash().hashCode());
    }

    public void testEquals() {
        assertTrue(FileHashStringTestData.FILEHASH_REF.getFileHash().equals(
                FileHashStringTestData.FILEHASH_REF.getFileHash()));
        assertTrue(FileHashStringTestData.FILEHASH_REF
                .getFileHash()
                .equals((new FileHashBuilder<String>(""))
                        .withPath(
                                FileHashStringTestData.FILEHASH_REF.getPath()
                                        .getPath())
                        .withHash(FileHashStringTestData.FILEHASH_REF.getHash())
                        .build()));
        assertFalse(FileHashStringTestData.FILEHASH_REF.getFileHash().equals(
                FileHashStringTestData.FILEHASH_SAMEHASH.getFileHash()));
        assertFalse(FileHashStringTestData.FILEHASH_REF.getFileHash().equals(
                FileHashStringTestData.FILEHASH_SAMEPATH.getFileHash()));
        assertFalse(FileHashStringTestData.FILEHASH_REF.getFileHash().equals(
                FileHashStringTestData.FILEHASH_DIFFERENT.getFileHash()));
    }

}
