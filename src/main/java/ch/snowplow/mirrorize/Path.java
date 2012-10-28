package ch.snowplow.mirrorize;

public class Path implements Comparable<Path> {

    // TODO dynamically read the separator from the JVM at runtime
    public static final char SEPARATOR = '/';
    private final String path;

    public Path(String path) {
        // TODO matches(String regex) to verify an appropriate path format
        if (path.lastIndexOf(SEPARATOR) == path.length() - 1) {
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
    public String toString() {
        return path;
    }

}
