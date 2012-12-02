package ch.snowplow.mirrorize.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HashedFileSet<T extends Comparable<T>> extends
        HashSet<HashedFile<T>> {

    private static final long serialVersionUID = 1L;

    public HashedFileSet() {
        super();
    }

    public HashedFileSet(HashedFileSet<T> fileHashSet) {
        super(fileHashSet);
    }

    public HashedFileSet(int initialCapacity) {
        super(initialCapacity);
    }

    public void removeAllByPath(Collection<Path> paths) {
        // FIXME slow implementation
        HashedFileSet<T> toRemove = new HashedFileSet<T>();
        for (HashedFile<T> hf : this) {
            if (paths.contains(hf.getPath())) {
                toRemove.add(hf);
            }
        }
        removeAll(toRemove);
    }

    public void removeAllByHash(Collection<T> hashes) {
        // FIXME slow implementation
        HashedFileSet<T> toRemove = new HashedFileSet<T>();
        for (HashedFile<T> hf : this) {
            if (hashes.contains(hf.getHash())) {
                toRemove.add(hf);
            }
        }
        removeAll(toRemove);
    }

    public Collection<T> getHashes() {
        Set<T> hashes = new HashSet<T>();
        for (HashedFile<T> hf : this) {
            hashes.add(hf.getHash());
        }
        return hashes;
    }

}
