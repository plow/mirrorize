package ch.snowplow.mirrorize.common;

// TODO javadoc: leightweight representation of a file/directory path with respect to a small memory footprint.
public class Path implements Comparable<Path> {

    // TODO dynamically read the separator from the JVM at runtime
    public static final char SEPARATOR = '/';
    private final String path;

    public Path(String path) {
        // TODO matches(String regex) to verify an appropriate path format
        if (path.length() > 0
                && path.lastIndexOf(SEPARATOR) == path.length() - 1) {
            this.path = path.substring(0, path.length() - 1);
        } else {
            this.path = path;
        }
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        int i = path.lastIndexOf(SEPARATOR);
        return (i < 0 ? path : path.substring(i + 1));
    }

    public int getDepth() {
        return getDepth(path.indexOf(SEPARATOR) == 0 ? path.substring(1) : path);
    }

    private int getDepth(String p) {
        int i = p.indexOf(SEPARATOR);
        return (i < 0 ? 0 : getDepth(p.substring(i + 1)) + 1);
    }

    @Override
    public int compareTo(Path o) {
        return path.compareTo(o.getPath());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Path other = (Path) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return path;
    }

}
