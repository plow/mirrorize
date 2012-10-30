package ch.snowplow.mirrorize;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.databuilders.PathBuilder;

public class PathTest extends TestCase {

    enum TestPath {
        PATH_ABS_1("/an/absolute/pa.th", "/an/absolute/pa.th", "pa.th", 2),
        PATH_ABS_2("/an/absolute/pa.th/", "/an/absolute/pa.th", "pa.th", 2),
        PATH_REL_1("relative/pat.h", "relative/pat.h", "pat.h", 1),
        PATH_REL_2("relative/pat.h/", "relative/pat.h", "pat.h", 1),
        PATH_ROOT_1("roo.t", "roo.t", "roo.t", 0),
        PATH_ROOT_2("roo.t/", "roo.t", "roo.t", 0);

        private Path pathObj;
        private String path, name;
        private int depth;

        TestPath(String pathOrig, String path, String name, int depth) {
            this.pathObj = new PathBuilder().withPath(pathOrig).build();
            this.path = path;
            this.name = name;
            this.depth = depth;
        }
    }

    public void testGetPath() {
        for (TestPath test : TestPath.values()) {
            assertEquals(test.path, test.pathObj.getPath());
        }
    }

    public void testGetName() {
        for (TestPath test : TestPath.values()) {
            assertEquals(test.name, test.pathObj.getName());
        }
    }

    public void testGetDepth() {
        for (TestPath test : TestPath.values()) {
            assertEquals(test.depth, test.pathObj.getDepth());
        }
    }

}
