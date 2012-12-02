package ch.snowplow.mirrorize.common;

public class HashedFileCorresp<T> extends HashedFile<T> {

    private final PathSet correspPaths;

    public HashedFileCorresp(Path path1, T hash, PathSet correspPaths) {
        super(path1, hash);
        this.correspPaths = correspPaths;
    }

    public PathSet getCorrespPath() {
        return correspPaths;
    }

}
