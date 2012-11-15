package ch.snowplow.mirrorize.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a bidirectional mapping between the paths of files and their
 * hashes. The efficiency of retrieving a particular file path from a hash and a
 * hash from a file path is equal.
 * 
 * @author sf
 * 
 * @param <T>
 *            Data type of the hash.
 */
public class DirHashMap<T extends Comparable<T>> {

    // TODO: value needs to be List<String> because of redundant files
    private FileHashSet<T> fileHashes;
    private Map<T, Path> fileByHash;
    private Map<Path, T> fileByPath;

    /**
     * Creates a new instance with the default initial size of the underlying
     * collections.
     */
    public DirHashMap() {
        fileHashes = new FileHashSet<T>();
        fileByHash = new HashMap<T, Path>();
        fileByPath = new HashMap<Path, T>();
    }

    /**
     * Creates a new instance with a given initial size of the underlying
     * collections.
     * 
     * @param initialSize
     *            Initial size of the hash maps used to implement the
     *            bidirectional map.
     */
    public DirHashMap(int initialSize) {
        // TODO initial size is problematic because the file hashes set doesn't
        // contain the same number of element as the hash maps in case there are
        // several files with the same content, i.e., with the same hash.
        fileHashes = new FileHashSet<T>(initialSize);
        fileByHash = new HashMap<T, Path>(initialSize);
        fileByPath = new HashMap<Path, T>(initialSize);
    }

    /**
     * Adds a new hash-path pair to the data structure.
     * 
     * @param fileHash
     *            Hash of the file
     * @param filePath
     *            Path of the file
     */
    public void add(T fileHash, Path filePath) {
        fileHashes.add(new FileHash<T>(filePath, fileHash));
        fileByHash.put(fileHash, filePath);
        fileByPath.put(filePath, fileHash);
    }

    /**
     * Adds a new file hash to the data structure.
     * 
     * @param fileHash
     *            FileHash to be added
     */
    public void add(FileHash<T> fileHash) {
        fileHashes.add(fileHash);
        fileByHash.put(fileHash.getHash(), fileHash.getPath());
        fileByPath.put(fileHash.getPath(), fileHash.getHash());
    }

    /**
     * Adds all elements of the {@link ch.snowplow.mirrorize.common.DirHashMap}
     * provided as argument to this object. Just the object references are
     * copied, not the elements itself.
     * 
     * @param hashes
     *            The instance to copy all elements from.
     */
    public void addAll(DirHashMap<T> hashes) {
        fileHashes.addAll(hashes.fileHashes);
        fileByHash.putAll(hashes.fileByHash);
        fileByPath.putAll(hashes.fileByPath);
    }

    /**
     * Retrieve the file path of a specified file hash. The behavior is
     * equivalent with {@link java.util.HashMap#get(Object)}.
     * 
     * @param hash
     *            Hash of the file
     * @return Path of file with the specified hash.
     */
    public Path getFilePathByHash(T hash) {
        return fileByHash.get(hash);
    }

    /**
     * Retrieve the hash of a file, given the path of this file. The behavior is
     * equivalent with {@link java.util.HashMap#get(Object)}.
     * 
     * @param path
     *            Path of the file.
     * @return Hash of the file for which the path is given.
     */
    public T getFileHashByPath(Path path) {
        return fileByPath.get(path);
    }

    /**
     * Serializes all hashes maintained in this object by concatenating all
     * string representations of the hashes. The order of concatenation is given
     * by the natural order of the hashes, i.e., lexicographically ascending.
     * The order intentionally does not depend on the files' paths since paths
     * are sensitive to renaming.
     * 
     * @return Serialized string of all hashes maintained in this object.
     */
    public String getSerializedHashes() {
        ArrayList<T> sortedHashes = new ArrayList<T>(fileByHash.keySet());
        Collections.sort(sortedHashes);
        StringBuffer strBuf = new StringBuffer();
        for (T fileHash : sortedHashes) {
            strBuf.append(fileHash);
        }
        return strBuf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(fileByHash.size()).append(" file(s) hashed:\n");
        for (T key : fileByHash.keySet()) {
            strBuf.append(key.toString());
            strBuf.append(" -> ");
            strBuf.append(fileByHash.get(key).toString());
            strBuf.append("\n");
        }
        return strBuf.toString();
    }

    public FileHashSet<T> getFileHashes() {
        return fileHashes;
    }

    public FileHashSet<T> getFileHashes(Collection<Path> paths) {
        FileHashSet<T> fhs = new FileHashSet<T>();
        for (Path p : paths) {
            T h = fileByPath.get(p);
            if (h != null) {
                fhs.add(new FileHash<T>(p, h));
            }
        }
        return fhs;
    }

    public Set<T> getHashes() {
        return fileByHash.keySet();
    }

    public Set<Path> getPaths() {
        return fileByPath.keySet();
    }

    public FileHash<T> getRoot() {
        Path root = new Path("");
        T hash = fileByPath.get(root);
        if (hash == null) {
            return null;
        }
        return new FileHash<T>(root, hash);
    }

}
