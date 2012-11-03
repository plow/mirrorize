package ch.snowplow.mirrorize.analysis;

import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.Path;

public class DirDiffAnalyzer<T extends Comparable<T>> {

    private DirHashMap<T> ourMap;
    private DirHashMap<T> theirMap;

    public DirDiffAnalyzer(DirHashMap<T> ourMap, DirHashMap<T> theirMap) {
        this.ourMap = ourMap;
        this.theirMap = theirMap;
    }

    private Set<FileHash<T>> getDiffsOfHashes() {
        return getDiffSetOfHashes(ourMap, theirMap);
    }

    private Set<FileHash<T>> getDiffsOfPaths() {
        return getDiffSetOfPaths(ourMap, theirMap);
    }

    public Set<FileHash<T>> getNewFiles() {
        // get all files without a corresponding file in their map (by hash)
        Set<FileHash<T>> newFiles = new HashSet<FileHash<T>>(getDiffsOfHashes());
        // subtract files without corresponding file in their map (by path)
        newFiles.removeAll(getDiffsOfPaths());
        return newFiles;
    }

    public Set<FileHash<T>> getModifiedFiles() {
        // get all files in our map with corresponding path in their map
        Set<Path> correspPaths = new HashSet<Path>(ourMap.getPaths());
        correspPaths.retainAll(theirMap.getPaths());
        HashSet<FileHash<T>> correspFiles = new HashSet<FileHash<T>>(
                correspPaths.size());
        for (Path p : correspPaths) {
            correspFiles.add(new FileHash<T>(p, ourMap.getFileHashByPath(p)));
        }
        // subtract identical files, i.e. those also having corresponding hashes
        correspFiles.removeAll(theirMap.getFileHashes());
        return correspFiles;
    }

    public Set<FileHash<T>> getDeletedFiles() {
        // get all files in our map with corresponding path in their map
        Set<Path> nonCorrespPaths = new HashSet<Path>(theirMap.getPaths());
        nonCorrespPaths.removeAll(ourMap.getPaths());
        HashSet<FileHash<T>> nonCorrespFilesByPath = new HashSet<FileHash<T>>();
        for (Path p : nonCorrespPaths) {
            nonCorrespFilesByPath.add(new FileHash<T>(p, theirMap
                    .getFileHashByPath(p)));
        }

        Set<T> nonCorrespHashes = new HashSet<T>(theirMap.getHashes());
        nonCorrespHashes.removeAll(ourMap.getHashes());
        HashSet<FileHash<T>> nonCorrespFilesByHash = new HashSet<FileHash<T>>();
        for (T hash : nonCorrespHashes) {
            nonCorrespFilesByHash.add(new FileHash<T>(theirMap
                    .getFilePathByHash(hash), hash));
        }

        nonCorrespFilesByHash.retainAll(nonCorrespFilesByPath);
        return nonCorrespFilesByHash;
    }

    public Set<FileHash<T>> getAllFiles() {
        return ourMap.getFileHashes();
    }

    /**
     * Returns a set of all files in our directory hash map that cannot be found
     * by hash in their directory hash map.
     * 
     * @param ourMap
     *            Our directory hash map
     * @param theirMap
     *            Their directory hash map, i.e., the reference map our
     *            directory hash map was compared to.
     * @return
     */
    private Set<FileHash<T>> getDiffSetOfHashes(DirHashMap<T> ourMap,
            DirHashMap<T> theirMap) {
        Set<T> ourHashes = new HashSet<T>(ourMap.getHashes());
        ourHashes.removeAll(theirMap.getHashes());
        Set<FileHash<T>> diffSet = new HashSet<FileHash<T>>();
        for (T hash : ourHashes) {
            diffSet.add(new FileHash<T>(ourMap.getFilePathByHash(hash), hash));
        }
        return diffSet;
    }

    /**
     * Returns a set of all files in our directory hash map that cannot be found
     * by path in their directory hash map.
     * 
     * @param ourMap
     *            Our directory hash map
     * @param theirMap
     *            Their directory hash map, i.e., the reference map our
     *            directory hash map was compared to.
     * @return
     */
    private Set<FileHash<T>> getDiffSetOfPaths(DirHashMap<T> ourMap,
            DirHashMap<T> theirMap) {
        Set<Path> ourPaths = new HashSet<Path>(ourMap.getPaths());
        ourPaths.removeAll(theirMap.getPaths());
        Set<FileHash<T>> diffSet = new HashSet<FileHash<T>>();
        for (Path path : ourPaths) {
            diffSet.add(new FileHash<T>(path, ourMap.getFileHashByPath(path)));
        }
        return diffSet;
    }

}
