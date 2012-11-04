package ch.snowplow.mirrorize.common;

public class FileHashCorresp<T> extends FileHash<T> {

    private final Path correspPath;

    public FileHashCorresp(Path path1, Path path2, T hash) {
        super(path1, hash);
        this.correspPath = path2;
    }

    public Path getCorrespPath() {
        return correspPath;
    }

}
