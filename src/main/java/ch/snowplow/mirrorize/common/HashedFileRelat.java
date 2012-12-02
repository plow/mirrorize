package ch.snowplow.mirrorize.common;

public class HashedFileRelat<T> extends HashedFile<T> {

    private final PathSet relatedPaths;

    public HashedFileRelat(Path path, T hash, PathSet relatedPaths) {
        super(path, hash);
        this.relatedPaths = relatedPaths;
    }

    public PathSet getRelatedPaths() {
        return relatedPaths;
    }

}
