package ch.snowplow.mirrorize.databuilders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.gathering.InvalidTreeRootException;

public class FileBuilder implements Buildable<File> {

    private static Logger log = Logger.getLogger(FileBuilder.class);
    public static final String TEST_TREE_ROOT = "target/test_trees/";

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

            // avoid absolute paths outside the current directory
            String pathDir = this.path.getDir() + Path.SEPARATOR;
            String currDir = new File(".").getCanonicalPath() + Path.SEPARATOR;
            boolean relativePath = true;
            if (pathDir.startsWith(Character.toString(Path.SEPARATOR))) {
                relativePath = false;
                if (!pathDir.startsWith(currDir + TEST_TREE_ROOT))
                    throw new InvalidTreeRootException();
            }

            // create directories if necessary
            Path absPath = new Path(relativePath ? currDir + TEST_TREE_ROOT
                    + path.getPath() : path.getPath());
            File dir = new File(absPath.getDir());
            f = new File(absPath.getPath());
            log.trace("File to create: " + f.getCanonicalPath());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // create or re-create file and write content
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileWriter fstream = new FileWriter(absPath.getPath());
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(content);
            out.close();
        } catch (IOException e) {
            log.fatal("I/O problem occured while creating test file.", e);
        } catch (InvalidTreeRootException e) {
            log.fatal(
                    "Absolute paths outside the current directory are not allowed.",
                    e);
        }
        return f;
    }

}
