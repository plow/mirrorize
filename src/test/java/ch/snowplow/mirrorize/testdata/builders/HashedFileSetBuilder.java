package ch.snowplow.mirrorize.testdata.builders;

import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.common.HashedFile;
import ch.snowplow.mirrorize.common.HashedFileSet;

public class HashedFileSetBuilder<T extends Comparable<T>> implements
        Buildable<HashedFileSet<T>> {

    Set<HashedFile<T>> fileHashes = new HashSet<HashedFile<T>>();

    public HashedFileSetBuilder<T> addFileHash(HashedFile<T> fh) {
        this.fileHashes.add(fh);
        return this;
    }

    @Override
    public HashedFileSet<T> build() {
        HashedFileSet<T> fhs = new HashedFileSet<T>(fileHashes.size());
        for (HashedFile<T> fh : fileHashes) {
            fhs.add(fh);
        }
        return fhs;
    }

}
