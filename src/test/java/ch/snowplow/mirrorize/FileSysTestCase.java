package ch.snowplow.mirrorize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import ch.snowplow.mirrorize.gathering.InvalidTreeRootException;
import ch.snowplow.mirrorize.testdata.FileTestData;
import ch.snowplow.mirrorize.testdata.builders.FileBuilder;

/**
 * The base class for all test cases which need the test file trees on the file
 * system.
 * 
 * @author sf
 * 
 */
public class FileSysTestCase extends TestCase {

    private static Logger log = Logger.getLogger(FileSysTestCase.class);

    public FileSysTestCase() {
        super();
    }

    public FileSysTestCase(String name) {
        super(name);
    }

    @Override
    public void setUp() {
        for (FileTestData testFile : FileTestData.values()) {
            testFile.build();
        }
    }

    @Override
    public void tearDown() {

        File testDirRoot = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT);
        if (!testDirRoot.isDirectory() || !testDirRoot.exists()) {
            try {
                throw new InvalidTreeRootException();
            } catch (InvalidTreeRootException e) {
                log.fatal(
                        "The root of the test file trees does not exist or is not a directory.",
                        e);
            }
        }
        log.trace("Delete root of test file trees: "
                + testDirRoot.getAbsolutePath());
        try {
            delete(testDirRoot);
        } catch (IOException e) {
            log.fatal("I/O error occured while deleting a test file.", e);
        }

    }

    /**
     * Recursively deletes files
     * 
     * @param f
     *            File to delete (can represent a file or a directory)
     * @throws IOException
     */
    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f);
        }
    }
}
