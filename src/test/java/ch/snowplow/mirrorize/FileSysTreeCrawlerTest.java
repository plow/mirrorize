package ch.snowplow.mirrorize;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import ch.snowplow.mirrorize.gathering.FileSysTreeCrawler;
import ch.snowplow.mirrorize.gathering.InvalidTreeRootException;
import ch.snowplow.mirrorize.testdata.builders.FileBuilder;

/**
 * Unit test for simple App.
 */
public class FileSysTreeCrawlerTest extends FileSysTestCase {

    /**
     * Test that only existing directory paths can be used as root folders.
     */
    public void testRootDirectories() {
        // Root directories
        File nonexistingDir = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT
                + "treeInvalid");
        File existingFile = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT
                + "tree1/a/file_a1.txt");
        File existingDir = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT
                + "tree1");
        assertFalse(nonexistingDir.exists());
        assertTrue(existingFile != null && !existingFile.isDirectory());
        assertTrue(existingDir != null && existingDir.isDirectory());
        File existingAbsDir = new File(existingDir.getAbsolutePath());
        assertTrue(existingAbsDir != null && existingAbsDir.isDirectory());

        final String HASH_ALGO = "MD5";

        // verify that non-existing directory yields an exception
        try {
            new FileSysTreeCrawler(nonexistingDir, HASH_ALGO);
            fail();
        } catch (InvalidTreeRootException | NoSuchAlgorithmException e) {
        }

        // verify that a file instead of a directory yields an exception
        try {
            new FileSysTreeCrawler(existingFile, HASH_ALGO);
            fail();
        } catch (InvalidTreeRootException | NoSuchAlgorithmException e) {
        }

        // verify that an existing root directory does not yield an exception
        try {
            new FileSysTreeCrawler(existingDir, HASH_ALGO);
        } catch (InvalidTreeRootException | NoSuchAlgorithmException e) {
            fail();
        }

        // verify that an existing root directory specified as absolute path
        // does not yield an exception
        try {
            new FileSysTreeCrawler(existingAbsDir, HASH_ALGO);
        } catch (InvalidTreeRootException | NoSuchAlgorithmException e) {
            fail();
        }
    }

    /**
     * Test that all of the hash algorithms MD5, SHA-1, and SHA-256 can be used.
     * These three algorithms must be supported by every implementation of the
     * Java platform. Verify that unsupported algorithms yield an exception.
     */
    public void testHashAlgos() {

        final String[] hashAlgos = { "MD5", "SHA-1", "SHA-256", "INVALID" };
        final boolean[] validAlgos = { true, true, true, false };
        final File rootDir = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT
                + "tree1");

        for (int i = 0; i < hashAlgos.length; i++) {
            try {
                new FileSysTreeCrawler(rootDir, hashAlgos[i]);
                if (!validAlgos[i]) {
                    fail();
                }
            } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
                if (validAlgos[i]) {
                    fail();
                }
            }
        }
    }

    // TODO make fixtures of files
    // TODO create trees just by means of these fixtures in setup()

}
