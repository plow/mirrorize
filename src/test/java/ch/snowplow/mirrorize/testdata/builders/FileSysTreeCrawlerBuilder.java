package ch.snowplow.mirrorize.testdata.builders;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import ch.snowplow.mirrorize.gathering.FileSysTreeCrawler;
import ch.snowplow.mirrorize.gathering.InvalidTreeRootException;

public class FileSysTreeCrawlerBuilder implements Buildable<FileSysTreeCrawler> {

    String treeRoot, algo;

    public FileSysTreeCrawlerBuilder withTreeRoot(String treeRootPath) {
        treeRoot = treeRootPath;
        return this;
    }

    public FileSysTreeCrawlerBuilder withHashAlgo(String hashAlgo) {
        algo = hashAlgo;
        return this;
    }

    @Override
    public FileSysTreeCrawler build() {
        File root = new File(treeRoot);
        try {
            return new FileSysTreeCrawler(root, algo);
        } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
            e.printStackTrace();
            return null;
        }
    }

}
