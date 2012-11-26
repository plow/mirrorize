package ch.snowplow.mirrorize;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import ch.snowplow.mirrorize.analysis.DirDiffAnalyzerTest;
import ch.snowplow.mirrorize.common.DirHashMapTest;
import ch.snowplow.mirrorize.common.FileHashCorrespTest;
import ch.snowplow.mirrorize.common.FileHashSetTest;
import ch.snowplow.mirrorize.common.FileHashTest;
import ch.snowplow.mirrorize.common.PathSetTest;
import ch.snowplow.mirrorize.common.PathTest;
import ch.snowplow.mirrorize.common.TestDirHashMap;
import ch.snowplow.mirrorize.common.ToolsTest;
import ch.snowplow.mirrorize.gathering.FileSysTreeCrawlerTest;

public final class AllTestCases extends TestCase {
    /**
     * Used by junit to specify what TestCases to run.
     * 
     * @return a suite containing what TestCases to run
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite();

        // ch.snowplow.mirrorize
        suite.addTestSuite(TestFileTreeCreator.class);

        // ch.snowplow.mirrorize.analysis
        suite.addTestSuite(DirDiffAnalyzerTest.class);

        // ch.snowplow.mirrorize.common
        suite.addTestSuite(DirHashMapTest.class);
        suite.addTestSuite(FileHashCorrespTest.class);
        suite.addTestSuite(FileHashSetTest.class);
        suite.addTestSuite(FileHashTest.class);
        suite.addTestSuite(PathSetTest.class);
        suite.addTestSuite(PathTest.class);
        suite.addTestSuite(TestDirHashMap.class);
        suite.addTestSuite(ToolsTest.class);

        // ch.snowplow.mirrorize.gathering
        suite.addTestSuite(FileSysTreeCrawlerTest.class);

        return suite;
    }
}