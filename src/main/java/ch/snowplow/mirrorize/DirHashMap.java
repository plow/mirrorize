package ch.snowplow.mirrorize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DirHashMap<T extends Comparable<T>> {

    // TODO: value needs to be List<String> because of redundant files
    private HashMap<T, String> fileByHash;
    private HashMap<String, T> fileByPath;

    public DirHashMap() {
        fileByHash = new HashMap<T, String>();
        fileByPath = new HashMap<String, T>();
    }

    public void add(T fileHash, String filePath) {
        fileByHash.put(fileHash, filePath);
        fileByPath.put(filePath, fileHash);
    }

    public String getFilePathByHash(T hash) {
        return fileByHash.get(hash);
    }

    public T getFileHashByPath(String path) {
        return fileByPath.get(path);
    }

    public void addAll(DirHashMap<T> hashes) {
        fileByHash.putAll(hashes.fileByHash);
        fileByPath.putAll(hashes.fileByPath);
    }

    public String getFolderHashes() {
        ArrayList<T> fileHashes = new ArrayList<T>(fileByHash.keySet());
        Collections.sort(fileHashes);
        StringBuffer strBuf = new StringBuffer();
        for (T fileHash : fileHashes) {
            strBuf.append(fileHash.toString());
        }
        return strBuf.toString();
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        for (T key : fileByHash.keySet()) {
            strBuf.append(key.toString());
            strBuf.append(" -> ");
            strBuf.append(fileByHash.get(key).toString());
            strBuf.append("\n");
        }
        return strBuf.toString();
    }
}
