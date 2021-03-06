package ch.snowplow.mirrorize.common;

public class HashedFile<T> implements Comparable<HashedFile<T>> {

    private final Path path;
    private final T hash;

    public HashedFile(Path path, T hash) {
        this.path = path;
        this.hash = hash;
    }

    public Path getPath() {
        return path;
    }

    public T getHash() {
        return hash;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        HashedFile<T> other = null;
        try {
            other = (HashedFile<T>) obj;
        } catch (ClassCastException e) {
            return false;
        }
        if (hash == null) {
            if (other.hash != null) {
                return false;
            }
        } else if (!hash.equals(other.hash)) {
            return false;
        }
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(HashedFile<T> o) {
        return path.compareTo(o.getPath());
    }

    @Override
    public String toString() {
        return hash.toString() + "<=>" + path.toString();
    }
}
