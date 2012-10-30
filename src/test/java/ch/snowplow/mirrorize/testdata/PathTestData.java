package ch.snowplow.mirrorize.testdata;

import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.databuilders.PathBuilder;

public enum PathTestData {
    // test path, name, depth
    PATH_ABS_1("/an/absolute/pa.th", "/an/absolute/pa.th", "pa.th", 2),
    PATH_ABS_2("/an/absolute/pa.th/", "/an/absolute/pa.th", "pa.th", 2),
    PATH_REL_1("relative/pat.h", "relative/pat.h", "pat.h", 1),
    PATH_REL_2("relative/pat.h/", "relative/pat.h", "pat.h", 1),
    PATH_ROOT_1("roo.t", "roo.t", "roo.t", 0),
    PATH_ROOT_2("roo.t/", "roo.t", "roo.t", 0),

    // test comparable
    PATH_REF("this/is/a/path", "this/is/a/path", "path", 3),
    PATH_LESS1("this/is/a/pat", "this/is/a/pat", "pat", 3),
    PATH_LESS2("this/is/a/pata", "this/is/a/pata", "pata", 3),
    PATH_LESS3("this/is/a", "this/is/a", "a", 2),
    PATH_MORE1("this/is/a/pathh", "this/is/a/pathh", "pathh", 3),
    PATH_MORE2("this/is/a/patz", "this/is/a/patz", "patz", 3),
    PATH_MORE3("this/is/a/path/a", "this/is/a/path/a", "a", 4);

    private Path pathObj;
    private String path, name;
    private int depth;

    PathTestData(String pathOrig, String path, String name, int depth) {
        this.pathObj = new PathBuilder().withPath(pathOrig).build();
        this.path = path;
        this.name = name;
        this.depth = depth;
    }

    public Path getPathObj() {
        return pathObj;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getDepth() {
        return depth;
    }

}