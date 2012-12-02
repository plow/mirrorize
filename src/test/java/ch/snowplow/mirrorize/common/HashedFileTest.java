package ch.snowplow.mirrorize.common;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.HashedFileStringTestData;
import ch.snowplow.mirrorize.testdata.builders.HashedFileBuilder;

public class HashedFileTest extends TestCase {

    public void testGetPath() {
        for (HashedFileStringTestData fileHashStrTD : HashedFileStringTestData
                .values()) {
            assertEquals(fileHashStrTD.getPath(), fileHashStrTD.getFileHash()
                    .getPath());
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
        assertTrue(HashedFileStringTestData.FILEHASH_REF.getFileHash().compareTo(
                HashedFileStringTestData.FILEHASH_REF.getFileHash()) == 0);
        assertTrue(HashedFileStringTestData.FILEHASH_REF.getFileHash().compareTo(
                HashedFileStringTestData.FILEHASH_LESS.getFileHash()) > 0);
        assertTrue(HashedFileStringTestData.FILEHASH_REF.getFileHash().compareTo(
                HashedFileStringTestData.FILEHASH_LESS2.getFileHash()) > 0);
        assertTrue(HashedFileStringTestData.FILEHASH_REF.getFileHash().compareTo(
                HashedFileStringTestData.FILEHASH_MORE.getFileHash()) < 0);
        assertTrue(HashedFileStringTestData.FILEHASH_REF.getFileHash().compareTo(
                HashedFileStringTestData.FILEHASH_MORE2.getFileHash()) < 0);
    }

    public void testHashCode() {
        assertEquals(HashedFileStringTestData.FILEHASH_REF.getFileHash()
                .hashCode(), HashedFileStringTestData.FILEHASH_REF.getFileHash()
                .hashCode());
        assertEquals(
                HashedFileStringTestData.FILEHASH_REF.getFileHash().hashCode(),
                (new HashedFileBuilder<String>(""))
                        .withPath(
                                HashedFileStringTestData.FILEHASH_REF.getPath()
                                        .getPath())
                        .withHash(HashedFileStringTestData.FILEHASH_REF.getHash())
                        .build().hashCode());
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileStringTestData.FILEHASH_SAMEHASH
                .getFileHash().hashCode());
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileStringTestData.FILEHASH_SAMEPATH
                .getFileHash().hashCode());
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash()
                .hashCode() == HashedFileStringTestData.FILEHASH_DIFFERENT
                .getFileHash().hashCode());
    }

    public void testEquals() {
        assertTrue(HashedFileStringTestData.FILEHASH_REF.getFileHash().equals(
                HashedFileStringTestData.FILEHASH_REF.getFileHash()));
        assertTrue(HashedFileStringTestData.FILEHASH_REF
                .getFileHash()
                .equals((new HashedFileBuilder<String>(""))
                        .withPath(
                                HashedFileStringTestData.FILEHASH_REF.getPath()
                                        .getPath())
                        .withHash(HashedFileStringTestData.FILEHASH_REF.getHash())
                        .build()));
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash().equals(
                HashedFileStringTestData.FILEHASH_SAMEHASH.getFileHash()));
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash().equals(
                HashedFileStringTestData.FILEHASH_SAMEPATH.getFileHash()));
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash().equals(
                HashedFileStringTestData.FILEHASH_DIFFERENT.getFileHash()));

        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash().equals(
                null));
        assertFalse(HashedFileStringTestData.FILEHASH_REF.getFileHash().equals(
                new String("abc")));
        // TODO: test against other file hash types (T != String)
    }

}
