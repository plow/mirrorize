package ch.snowplow.mirrorize.testdata.builders;

import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.FileHashSet;

public class FileHashSetBuilder<T extends Comparable<T>> implements
        Buildable<FileHashSet<T>> {

    Set<FileHash<T>> fileHashes = new HashSet<FileHash<T>>();

    public FileHashSetBuilder<T> addFileHash(FileHash<T> fh) {
        this.fileHashes.add(fh);
        return this;
    }

    @Override
    public FileHashSet<T> build() {
        FileHashSet<T> fhs = new FileHashSet<T>(fileHashes.size());
        for (FileHash<T> fh : fileHashes) {
            fhs.add(fh);
        }
        return fhs;
    }

}
