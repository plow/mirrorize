package ch.snowplow.mirrorize.testdata.builders;

import java.util.HashMap;

import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.common.PathSet;

public class DirHashMapBuilder<T extends Comparable<T>> implements
        Buildable<DirHashMap<T>> {

    HashMap<T, PathSet> files = new HashMap<T, PathSet>();

    public DirHashMapBuilder<T> add(T hash, String path) {
        if (files.get(hash) == null) {
            files.put(hash, new PathSetBuilder().addPath(path).build());
        } else {
            files.get(hash).add(new PathBuilder().withPath(path).build());
        }
        return this;
    }

    @Override
    public DirHashMap<T> build() {
        DirHashMap<T> dirHashMap = new DirHashMap<T>();
        for (T hash : files.keySet()) {
            for (Path p : files.get(hash)) {
                dirHashMap.add(hash, p);
            }
        }
        return dirHashMap;
    }

}
