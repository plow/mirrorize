package ch.snowplow.mirrorize.testdata;

import java.io.File;

import ch.snowplow.mirrorize.databuilders.FileBuilder;
import ch.snowplow.mirrorize.databuilders.PathBuilder;

public enum FileTestData {

    // TODO move test files from test to target directory
    TEST_FILE("a/test/file.txt", "this is the text file's content.");

    private File file;

    FileTestData(String path, String content) {
        this.file = new FileBuilder()
                .withPath(new PathBuilder().withPath(path).build())
                .withContent(content).build();
    }

    public File getFile() {
        return file;
    }

}