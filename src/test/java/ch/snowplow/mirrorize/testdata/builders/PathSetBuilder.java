package ch.snowplow.mirrorize.testdata.builders;

import java.util.ArrayList;
import java.util.Collection;

import ch.snowplow.mirrorize.common.PathSet;

public class PathSetBuilder implements Buildable<PathSet> {

    ArrayList<String> paths = new ArrayList<String>(1);

    public PathSetBuilder addPath(String path) {
        this.paths.add(path);
        return this;
    }

    public PathSetBuilder addPaths(Collection<String> relatPaths) {
        this.paths.addAll(relatPaths);
        return this;
    }

    @Override
    public PathSet build() {
        if (paths.size() == 0) {
            paths.add("this/is/a/path.1");
            paths.add("this/is/b/path.2");
        }
        PathSet pathSet = new PathSet(new PathBuilder().withPath(paths.get(0))
                .build());
        for (int i = 1; i < paths.size(); i++) {
            pathSet.add(new PathBuilder().withPath(paths.get(i)).build());
        }
        return pathSet;
    }

}
