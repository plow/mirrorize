package ch.snowplow.mirrorize.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * A minimal {@link java.util.Set} implementation backed by an
 * {@link java.util.ArrayList} to keep the memory footprint as small as
 * possible. All optional operations of the {@link java.util.Set} specification
 * are not supported, except {@link PathSet#add(Path)}. Null elements are not
 * allowed.
 * 
 * @author sf
 * 
 */
public class PathSet implements Set<Path> {

    ArrayList<Path> paths;

    public PathSet(Path p) {
        paths = new ArrayList<Path>(1);
        paths.add(p);
    }

    @Override
    public boolean add(Path p) {
        if (addAllowed(p)) {
            return paths.add(p);
        }
        return false;
    }

    private boolean addAllowed(Path p) {
        if (p == null) {
            return false;
        }
        Iterator<Path> it = iterator();
        while (it.hasNext()) {
            if (it.next().equals(p)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return paths.size();
    }

    @Override
    public boolean isEmpty() {
        return paths.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return paths.contains(o);
    }

    @Override
    public Iterator<Path> iterator() {
        return paths.iterator();
    }

    @Override
    public Object[] toArray() {
        return paths.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return paths.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return paths.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Path> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        Collections.sort(paths);
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("[");
        strBuf.append(this.getClass().getSimpleName());
        strBuf.append(": ");
        boolean first = true;
        for (Path p : paths) {
            if (first) {
                first = false;
            } else {
                strBuf.append(" | ");
            }
            strBuf.append(p.toString());
        }
        strBuf.append("]");

        return strBuf.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((paths == null) ? 0 : paths.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PathSet other = (PathSet) obj;
        if (paths == null) {
            if (other.paths != null) {
                return false;
            }
        } else if (!paths.equals(other.paths)) {
            return false;
        }
        return true;
    }

}
