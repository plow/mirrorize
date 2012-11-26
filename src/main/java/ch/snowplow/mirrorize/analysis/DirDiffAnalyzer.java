package ch.snowplow.mirrorize.analysis;

import java.util.Arrays;
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

    public FileHashSet<T> getNewFiles() {
        // TODO: maybe it's more efficient to first removeall by path
        // get all files without a corresponding file in their map (by hash)
        FileHashSet<T> newFiles = getDiffSetOfHashes(ourMap, theirMap);
        // subtract files without corresponding file in their map (by path)
        newFiles.removeAllByPath(theirMap.getPaths());
        return newFiles;
    }

    public FileHashSet<T> getDeletedFiles() {
        // get all files in their map without corresponding path in our map (by
        // path)
        FileHashSet<T> nonCorrespFilesByPath = getDiffSetOfPaths(theirMap,
                ourMap);

        // get all files in their map without corresponding hash in our map
        Set<T> nonCorrespHashes = new HashSet<T>(theirMap.getHashes());
        nonCorrespHashes.removeAll(ourMap.getHashes());
        FileHashSet<T> nonCorrespFilesByHash = new FileHashSet<T>();
        for (T hash : nonCorrespHashes) {
            for (Path path : theirMap.getFilePathByHash(hash)) {
                nonCorrespFilesByHash.add(new FileHash<T>(path, hash));
            }
        }

        // return set with those files having neither a corresponding path nor a
        // corresponding hash in our map
        nonCorrespFilesByHash.retainAll(nonCorrespFilesByPath);
        return nonCorrespFilesByHash;
    }

    public FileHashSet<T> getModifiedFiles() {
        // get all files in our map with corresponding path in their map
        Set<Path> correspPaths = new HashSet<Path>(ourMap.getPaths());
        correspPaths.retainAll(theirMap.getPaths());

        // make file hash set out of corresponding paths
        FileHashSet<T> correspFiles = new FileHashSet<T>(correspPaths.size());
        for (Path path : correspPaths) {
            T ourHash = ourMap.getFileHashByPath(path);
            if (!ourHash.equals(theirMap.getFileHashByPath(path))) {
                // add file if files are not identical
                correspFiles.add(new FileHash<T>(path, ourHash));
            }
        }
        // subtract root element which can't be modified
        correspFiles.removeAllByPath(Arrays.asList(new Path("")));
        return correspFiles;
    }

    public FileHashSet<T> getMovedFiles() {
        // get set of corresponding hashes
        HashSet<T> identicalHashes = new HashSet<T>(ourMap.getHashes());
        identicalHashes.retainAll(theirMap.getHashes());
        // build list of correspondences to return
        // FIXME initial capacity is >=ourHashes.size() if identical files are
        // involved
        FileHashSet<T> movedFiles = new FileHashSet<T>(identicalHashes.size());
        for (T hash : identicalHashes) {
            for (Path ourPath : ourMap.getFilePathByHash(hash)) {
                movedFiles.add(new FileHashCorresp<T>(ourPath, hash, theirMap
                        .getFilePathByHash(hash)));
            }
        }

        // remove correspondences
        movedFiles.removeAll(theirMap.getFileHashSet());
        return movedFiles;
    }

    public FileHashSet<T> getAllFiles() {
        return ourMap.getFileHashSet();
    }

    public FileHashSet<T> getDiffsOfHashes() {
        return getDiffSetOfHashes(ourMap, theirMap);
    }

    public FileHashSet<T> getDiffsOfPaths() {
        return getDiffSetOfPaths(ourMap, theirMap);
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

        Set<T> diffHashes = new HashSet<T>(ourMap.getHashes());
        diffHashes.removeAll(theirMap.getHashes());
        FileHashSet<T> diffFiles = new FileHashSet<T>();
        for (T hash : diffHashes) {
            for (Path path : ourMap.getFilePathByHash(hash)) {
                diffFiles.add(new FileHash<T>(path, hash));
            }
        }
        return diffFiles;
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
