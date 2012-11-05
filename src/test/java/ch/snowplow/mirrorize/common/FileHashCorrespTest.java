package ch.snowplow.mirrorize.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.FileHashCorrespStringTestData;
import ch.snowplow.mirrorize.testdata.FileHashStringTestData;

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
        // TODO implement
    }

    public void testEquals() {
        // TODO implement
    }

}
