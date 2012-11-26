package ch.snowplow.mirrorize.common;

public class FileHashCorresp<T> extends FileHash<T> {

    private final PathSet correspPaths;

    public FileHashCorresp(Path path1, T hash, PathSet correspPaths) {
        super(path1, hash);
        this.correspPaths = correspPaths;
    }

    public PathSet getCorrespPath() {
        return correspPaths;
    }

}
