package ch.snowplow.mirrorize.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class HashedFileSet<T extends Comparable<T>> extends HashSet<HashedFile<T>> {

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
        Iterator<HashedFile<T>> it = iterator();
        HashedFileSet<T> toRemove = new HashedFileSet<T>();
        while (it.hasNext()) {
            HashedFile<T> fh = it.next();
            if (paths.contains(fh.getPath())) {
                toRemove.add(fh);
            }
        }
        removeAll(toRemove);
    }

}
