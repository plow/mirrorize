package ch.snowplow.mirrorize.analysis;

import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.FileHashCorresp;
import ch.snowplow.mirrorize.common.FileHashSet;
import ch.snowplow.mirrorize.common.Path;

public class DirDiffAnalyzer<T extends Comparable<T>> {

    private DirHashMap<T> ourMap;
    private DirHashMap<T> theirMap;

    public DirDiffAnalyzer(DirHashMap<T> ourMap, DirHashMap<T> theirMap) {
        this.ourMap = ourMap;
        this.theirMap = theirMap;
    }

    public FileHashSet<T> getDiffsOfHashes() {
        return getDiffSetOfHashes(ourMap, theirMap);
    }

    public FileHashSet<T> getDiffsOfPaths() {
        return getDiffSetOfPaths(ourMap, theirMap);
    }

    public FileHashSet<T> getNewFiles() {
        // get all files without a corresponding file in their map (by hash)
        FileHashSet<T> newFiles = new FileHashSet<T>(getDiffsOfHashes());
        // subtract files without corresponding file in their map (by path)
        newFiles.removeAllByPath(theirMap.getPaths());
        return newFiles;
    }

    public FileHashSet<T> getModifiedFiles() {
        // get all files in our map with corresponding path in their map
        Set<Path> correspPaths = new HashSet<Path>(ourMap.getPaths());
        correspPaths.retainAll(theirMap.getPaths());
        FileHashSet<T> correspFiles = new FileHashSet<T>(correspPaths.size());
        for (Path p : correspPaths) {
            correspFiles.add(new FileHash<T>(p, ourMap.getFileHashByPath(p)));
        }
        // subtract identical files, i.e. those also having corresponding hashes
        correspFiles.removeAll(theirMap.getFileHashes());
        return correspFiles;
    }

    public FileHashSet<T> getMovedFiles() {
        // get set of corresponding hashes
        HashSet<T> ourHashes = new HashSet<T>(ourMap.getHashes());
        ourHashes.retainAll(theirMap.getHashes());
        // build list of correspondences to return
        FileHashSet<T> correspFiles = new FileHashSet<T>(ourHashes.size());
        for (T hash : ourHashes) {
            correspFiles.add(new FileHashCorresp<T>(ourMap
                    .getFilePathByHash(hash), theirMap.getFilePathByHash(hash),
                    hash));
        }
        correspFiles.removeAll(theirMap.getFileHashes());
        return correspFiles;
    }

    public FileHashSet<T> getDeletedFiles() {
        // get all files in our map with corresponding path in their map
        Set<Path> nonCorrespPaths = new HashSet<Path>(theirMap.getPaths());
        nonCorrespPaths.removeAll(ourMap.getPaths());
        FileHashSet<T> nonCorrespFilesByPath = new FileHashSet<T>();
        for (Path p : nonCorrespPaths) {
            nonCorrespFilesByPath.add(new FileHash<T>(p, theirMap
                    .getFileHashByPath(p)));
        }

        Set<T> nonCorrespHashes = new HashSet<T>(theirMap.getHashes());
        nonCorrespHashes.removeAll(ourMap.getHashes());
        FileHashSet<T> nonCorrespFilesByHash = new FileHashSet<T>();
        for (T hash : nonCorrespHashes) {
            nonCorrespFilesByHash.add(new FileHash<T>(theirMap
                    .getFilePathByHash(hash), hash));
        }

        nonCorrespFilesByHash.retainAll(nonCorrespFilesByPath);
        return nonCorrespFilesByHash;
    }

    public FileHashSet<T> getAllFiles() {
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
    private FileHashSet<T> getDiffSetOfHashes(DirHashMap<T> ourMap,
            DirHashMap<T> theirMap) {
        Set<T> ourHashes = new HashSet<T>(ourMap.getHashes());
        ourHashes.removeAll(theirMap.getHashes());
        FileHashSet<T> diffSet = new FileHashSet<T>();
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
    private FileHashSet<T> getDiffSetOfPaths(DirHashMap<T> ourMap,
            DirHashMap<T> theirMap) {
        Set<Path> ourPaths = new HashSet<Path>(ourMap.getPaths());
        ourPaths.removeAll(theirMap.getPaths());
        FileHashSet<T> diffSet = new FileHashSet<T>();
        for (Path path : ourPaths) {
            diffSet.add(new FileHash<T>(path, ourMap.getFileHashByPath(path)));
        }
        return diffSet;
    }

}
