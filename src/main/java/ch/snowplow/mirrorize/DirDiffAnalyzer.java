package ch.snowplow.mirrorize;

import java.util.HashSet;
import java.util.Set;

public class DirDiffAnalyzer<T extends Comparable<T>> {

    private DirHashMap<T> dirMap;
    private DirHashMap<T> refMap;

    public DirDiffAnalyzer(DirHashMap<T> dirMap, DirHashMap<T> refMap) {
        this.dirMap = dirMap;
        this.refMap = refMap;
    }

    public Set<Path> getDiffsOfHashes() {
        return getDiffSetOfHashes(dirMap, refMap);
    }

    public Set<Path> getDiffsOfPaths() {
        return getDiffSetOfPaths(dirMap, refMap);
    }

    private Set<Path> getDiffSetOfHashes(DirHashMap<T> s1, DirHashMap<T> s2) {
        Set<T> diffSetHashes = new HashSet<T>(s1.getHashes());
        Set<Path> diffSetPaths = new HashSet<Path>();
        diffSetHashes.removeAll(s2.getHashes());
        for (T hash : diffSetHashes) {
            diffSetPaths.add(s1.getFilePathByHash(hash));
        }
        return diffSetPaths;
    }

    private Set<Path> getDiffSetOfPaths(DirHashMap<T> s1, DirHashMap<T> s2) {
        Set<Path> paths = new HashSet<Path>(s1.getPaths());
        paths.removeAll(s2.getPaths());
        return paths;
    }

}
