package ch.snowplow.mirrorize.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.FileHashSet;
import ch.snowplow.mirrorize.common.Path;

public class DirDiffAnalyzer<T extends Comparable<T>> {

    // TODO verify that multiple calls of one method are avoided

    private DirHashMap<T> ourMap;
    private DirHashMap<T> theirMap;

    private FileHashSet<T> correspFiles = null;

    public enum SetType {
        DIFF,
        CORRESP
    }

    public DirDiffAnalyzer(DirHashMap<T> ourMap, DirHashMap<T> theirMap) {
        this.ourMap = ourMap;
        this.theirMap = theirMap;
    }

    public void analyze() {
        correspFiles = getCorrespFilesC();
    }

    public FileHashSet<T> getCorrespFilesC() {
        FileHashSet<T> correspFiles = new FileHashSet<T>(getSetOfPaths(ourMap,
                theirMap, SetType.CORRESP));
        Collection<Path> toRemove = new ArrayList<Path>();
        for (FileHash<T> fh : correspFiles) {
            Path p = fh.getPath();
            if (!ourMap.getFileHashByPath(p).equals(
                    theirMap.getFileHashByPath(p))) {
                toRemove.add(p);
            }
        }
        correspFiles.removeAllByPath(toRemove);
        return correspFiles;
    }

    public FileHashSet<T> getNewFilesC() {
        // TODO: maybe it's more efficient to first removeall by path
        // get all files without a corresponding file in their map (by hash)
        FileHashSet<T> newFiles = getSetOfHashes(ourMap, theirMap, SetType.DIFF);
        // subtract files without corresponding file in their map (by path)
        newFiles.removeAllByPath(theirMap.getPaths());
        return newFiles;
    }

    public FileHashSet<T> getDeletedFilesC() {
        // get all files in their map without corresponding path in our map (by
        // path)
        FileHashSet<T> nonCorrespFilesByPath = getSetOfPaths(theirMap, ourMap,
                SetType.DIFF);

        // get all files in their map without corresponding hash in our map
        FileHashSet<T> nonCorrespFilesByHash = getSetOfHashes(theirMap, ourMap,
                SetType.DIFF);

        // return set with those files having neither a corresponding path nor a
        // corresponding hash in our map
        nonCorrespFilesByHash.retainAll(nonCorrespFilesByPath);
        return nonCorrespFilesByHash;
    }

    public FileHashSet<T> getModifiedFilesC() {
        // get all files in our map with corresponding path in their map
        FileHashSet<T> correspFilesByPath = getSetOfPaths(ourMap, theirMap,
                SetType.CORRESP);

        // files are only modified if they are not identical
        correspFilesByPath.removeAll(getCorrespFilesC());

        // subtract root element which can't be modified
        correspFilesByPath.removeAllByPath(Arrays.asList(new Path("")));
        return correspFilesByPath;
    }

    public FileHashSet<T> getMovedFilesC() {
        // get all files in our map with corresponding hash in their map
        FileHashSet<T> correspFilesByHash = getSetOfHashes(ourMap, theirMap,
                SetType.CORRESP);

        // files are only moved if they're paths are not identical
        correspFilesByHash.removeAll(getCorrespFilesC());
        return correspFilesByHash;
    }

    public FileHashSet<T> getAllFilesC() {
        return ourMap.getFileHashSet();
    }

    public FileHashSet<T> getSetOfHashesC(SetType type) {
        return getSetOfHashes(ourMap, theirMap, type);
    }

    public FileHashSet<T> getSetOfPathsC(SetType type) {
        return getSetOfPaths(ourMap, theirMap, type);
    }

    /**
     * Returns a set of all files in our directory hash map that either cannot
     * (type=DIFF) or can (type=CORRESP) be found by hash in their directory
     * hash map.
     * 
     * @param ourMap
     *            Our directory hash map that is subject of analysis
     * @param theirMap
     *            Their directory hash map our directory hash map was compared
     *            to.
     * @param type
     *            If DIFF, the difference set of hashes is computed, i.e., the
     *            set of files in our map without corresponding hash in their
     *            map is returned. If CORRESP, the intersection set of hashes is
     *            computed, i.e., the set of files in our map with corresponding
     *            hash in their map is returned.
     * @return The set of those files in our directory hash map that fulfill the
     *         requirements implied by the type argument.
     */
    private FileHashSet<T> getSetOfHashes(DirHashMap<T> ourMap,
            DirHashMap<T> theirMap, SetType type) {
        Set<T> hashes = new HashSet<T>(ourMap.getHashes());
        switch (type) {
        case CORRESP:
            hashes.retainAll(theirMap.getHashes());
            break;
        case DIFF:
            hashes.removeAll(theirMap.getHashes());
            break;
        default:
            break;
        }
        FileHashSet<T> set = new FileHashSet<T>();
        for (T hash : hashes) {
            for (Path path : ourMap.getFilePathByHash(hash)) {
                set.add(new FileHash<T>(path, hash));
            }
        }
        return set;
    }

    /**
     * Returns a set of all files in our directory hash map that either cannot
     * (type=DIFF) or can (type=CORRESP) be found by path in their directory
     * hash map.
     * 
     * @param ourMap
     *            Our directory hash map that is subject of analysis
     * @param theirMap
     *            Their directory hash map our directory hash map was compared
     *            to.
     * @param type
     *            If DIFF, the difference set of paths is computed, i.e., the
     *            set of files in our map without corresponding path in their
     *            map is returned. If CORRESP, the intersection set of paths is
     *            computed, i.e., the set of files in our map with corresponding
     *            path in their map is returned.
     * @return The set of those files in our directory hash map that fulfill the
     *         requirements implied by the type argument.
     */
    private FileHashSet<T> getSetOfPaths(DirHashMap<T> ourMap,
            DirHashMap<T> theirMap, SetType type) {
        Set<Path> paths = new HashSet<Path>(ourMap.getPaths());
        switch (type) {
        case CORRESP:
            paths.retainAll(theirMap.getPaths());
            break;
        case DIFF:
            paths.removeAll(theirMap.getPaths());
            break;
        default:
            break;
        }
        FileHashSet<T> set = new FileHashSet<T>();
        for (Path path : paths) {
            set.add(new FileHash<T>(path, ourMap.getFileHashByPath(path)));
        }
        return set;
    }

}
