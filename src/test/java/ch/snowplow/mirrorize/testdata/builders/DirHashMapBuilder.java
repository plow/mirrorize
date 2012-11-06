package ch.snowplow.mirrorize.testdata.builders;

import java.util.HashMap;

import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.Path;

public class DirHashMapBuilder<T extends Comparable<T>> implements
        Buildable<DirHashMap<T>> {

    HashMap<T, Path> files = new HashMap<T, Path>();

    public DirHashMapBuilder<T> add(T hash, String path) {
        files.put(hash, new PathBuilder().withPath(path).build());
        return this;
    }

    @Override
    public DirHashMap<T> build() {
        DirHashMap<T> dirHashMap = new DirHashMap<T>();
        for (T hash : files.keySet()) {
            dirHashMap.add(hash, files.get(hash));
        }
        return dirHashMap;
    }

}
