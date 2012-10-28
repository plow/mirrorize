package ch.snowplow.mirrorize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private HashMap<T, Path> fileByHash;
    private HashMap<Path, T> fileByPath;

    /**
     * Creates a new instance with the default initial size of the hash maps
     * used to implement the bidirectional map.
     */
    public DirHashMap() {
        fileByHash = new HashMap<T, Path>();
        fileByPath = new HashMap<Path, T>();
    }

    /**
     * Creates a new instance with a given initial size of the hash maps used to
     * implement the bidirectional map.
     * 
     * @param initialSize
     *            Initial size of the hash maps used to implement the
     *            bidirectional map.
     */
    public DirHashMap(int initialSize) {
        fileByHash = new HashMap<T, Path>(initialSize);
        fileByPath = new HashMap<Path, T>(initialSize);
    }

    /**
     * Adds a new hash/filepath pair to the data structure.
     * 
     * @param fileHash
     *            Hash of the file
     * @param filePath
     *            Path of the file
     */
    public void add(T fileHash, Path filePath) {
        fileByHash.put(fileHash, filePath);
        fileByPath.put(filePath, fileHash);
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
    public T getFileHashByPath(String path) {
        return fileByPath.get(path);
    }

    /**
     * Adds all elements of the {@link ch.snowplow.mirrorize.DirHashMap}
     * provided as argument to this object. Just the object references are
     * copied, not the elements itself.
     * 
     * @param hashes
     *            The instance to copy all elements from.
     */
    public void addAll(DirHashMap<T> hashes) {
        fileByHash.putAll(hashes.fileByHash);
        fileByPath.putAll(hashes.fileByPath);
    }

    /**
     * Serializes all hashes maintained in this object by concatenating all
     * string representations of the hashes. The order of concatenation is given
     * since the list of hashes is sorted into ascending order, according to the
     * natural ordering of its elements.
     * 
     * @return Serialized string of all hashes maintained in this object.
     */
    public String getSerializedHashes() {
        ArrayList<T> fileHashes = new ArrayList<T>(fileByHash.keySet());
        Collections.sort(fileHashes);
        StringBuffer strBuf = new StringBuffer();
        for (T fileHash : fileHashes) {
            strBuf.append(fileHash.toString());
        }
        return strBuf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(fileByHash.size() + " file(s) hashed:\n");
        for (T key : fileByHash.keySet()) {
            strBuf.append(key.toString());
            strBuf.append(" -> ");
            strBuf.append(fileByHash.get(key).toString());
            strBuf.append("\n");
        }
        return strBuf.toString();
    }

    public Set<T> getHashes() {
        return fileByHash.keySet();
    }

}
