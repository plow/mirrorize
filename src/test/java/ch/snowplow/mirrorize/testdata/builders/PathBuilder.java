package ch.snowplow.mirrorize.testdata.builders;

import ch.snowplow.mirrorize.common.Path;

public class PathBuilder implements Buildable<Path> {

    String path = "this/is/a/path";

    public PathBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public Path build() {
        return new Path(path);
    }

}
