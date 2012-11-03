package ch.snowplow.mirrorize;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.FileTestData;

public class TestFileTreeCreator extends TestCase {

    public void testCreateTestFileTrees() {
        for (FileTestData testFile : FileTestData.values()) {
            testFile.build("target/test_trees_runtime/");
        }
    }

}
