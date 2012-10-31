package ch.snowplow.mirrorize.databuilders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import ch.snowplow.mirrorize.common.Path;

public class FileBuilder implements Buildable<File> {

    private static Logger log = Logger.getLogger(FileBuilder.class);
    private static String resourcePathPrefix = "src/test/resources/";

    Path path = new Path("path/of/a/fil.e");
    String content = "this is file content.";

    public FileBuilder withPath(Path path) {
        this.path = path;
        return this;
    }

    public FileBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public File build() {
        File f = null;
        try {
            // TODO handle absolute file paths which start with '/'
            // prevent file/folder creation outside of src/test/resources/
            File dir = new File(resourcePathPrefix + path.getDir());
            f = new File(resourcePathPrefix + path.getPath());
            log.trace(f.getAbsolutePath());
            dir.mkdirs();
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileWriter fstream = new FileWriter(resourcePathPrefix
                    + path.getPath());
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(content);
            out.close();
        } catch (IOException e) {
            log.error("I/O problem occured while creating test file.", e);
        }
        return f;
    }

}
