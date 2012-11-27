package ch.snowplow.mirrorize.testdata.builders;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ch.snowplow.mirrorize.common.FileHashCorresp;

public class FileHashCorrespBuilder<T> implements Buildable<FileHashCorresp<T>> {

    String path = "this/is/a/path.1";
    Set<String> correspPaths = new HashSet<String>();
    T hash;

    public FileHashCorrespBuilder(T defaultHash) {
        this.hash = defaultHash;
    }

    public FileHashCorrespBuilder<T> withPath1(String path) {
        this.path = path;
        return this;
    }

    public FileHashCorrespBuilder<T> withPath2(String path) {
        this.correspPaths.add(path);
        return this;
    }

    public FileHashCorrespBuilder<T> withHash(T hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public FileHashCorresp<T> build() {

        if (correspPaths.size() == 0) {
            correspPaths.add("this/is/a/path.2");
        }
        PathSetBuilder pathSetB = new PathSetBuilder();
        Iterator<String> it = correspPaths.iterator();
        while (it.hasNext()) {
            pathSetB.addPath(it.next());
        }
        return new FileHashCorresp<T>(new PathBuilder().withPath(path).build(),
                hash, pathSetB.build());
    }
}
