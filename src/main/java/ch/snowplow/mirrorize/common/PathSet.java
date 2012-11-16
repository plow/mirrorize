package ch.snowplow.mirrorize.common;

import java.util.ArrayList;
import java.util.Collection;
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

    public PathSet() {
        paths = new ArrayList<Path>(1);
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

}
