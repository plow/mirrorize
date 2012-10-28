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

    public Set<Path> getMissingFiles() {
        return getDiffSet(refMap, dirMap);
    }

    public Set<Path> getNewFiles() {
        return getDiffSet(dirMap, refMap);
    }

    private Set<Path> getDiffSet(DirHashMap<T> s1, DirHashMap<T> s2) {
        Set<T> diffSetHashes = new HashSet<T>(s1.getHashes());
        Set<Path> diffSetPaths = new HashSet<Path>();
        diffSetHashes.removeAll(s2.getHashes());
        for (T hash : diffSetHashes) {
            diffSetPaths.add(s1.getFilePathByHash(hash));
        }
        return diffSetPaths;
    }

}
