package ch.snowplow.mirrorize.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class FileHashSet<T extends Comparable<T>> extends HashSet<FileHash<T>> {

    private static final long serialVersionUID = 1L;

    public FileHashSet() {
        super();
    }

    public FileHashSet(FileHashSet<T> fileHashSet) {
        super(fileHashSet);
    }

    public FileHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public void removeAllByPath(Collection<Path> paths) {
        // FIXME slow implementation
        Iterator<FileHash<T>> it = iterator();
        FileHashSet<T> toRemove = new FileHashSet<T>();
        while (it.hasNext()) {
            FileHash<T> fh = it.next();
            if (paths.contains(fh.getPath())) {
                toRemove.add(fh);
            }
        }
        removeAll(toRemove);
    }

}
