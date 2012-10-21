package ch.snowplow.mirrorize;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * The class containing the program entry point.
 * 
 * @author sf
 * 
 */
public class MirrorizeMain {

    private static Logger log = Logger.getLogger(MirrorizeMain.class);
    private static final String CRYPTO_ALGO = "MD5";

    /**
     * Program entry point.
     * 
     * @param args
     *            The arguments specified along with the program name on the
     *            command line.
     */
    public static void main(String args[]) {

        // Log all runtime exceptions and other uncaught exceptions to the
        // logger of this class
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("Uncaught exception.", e);
            }
        });

        // Immediately exit if the two roots of the trees to analyze are not
        // provided.
        if (args.length != 2) {
            System.out
                    .println("Usage: MD5Checksum /path/to/dir1 /path/to/dir2");
            log.fatal("Too " + (args.length < 2 ? "few" : "much")
                    + " arguments. Program terminates.");
            System.exit(0);
        }

        // Instantiate tree crawlers for the two directories to be compared
        File tree1Root = new File(args[0]);
        File tree2Root = new File(args[1]);
        FileSysTreeCrawler tree1Crawler, tree2Crawler;
        try {
            tree1Crawler = new FileSysTreeCrawler(tree1Root, CRYPTO_ALGO);
            tree2Crawler = new FileSysTreeCrawler(tree2Root, CRYPTO_ALGO);
        } catch (InvalidTreeRootException e) {
            log.fatal("Invalid tree root. Program terminates.", e);
            return;
        }

        // Print the results of the tree crawlers
        log.info("Tree root 1: " + args[0]);
        log.info("Tree root 2: " + args[1]);
        DirHashMap<String> hashesTree1 = tree1Crawler.crawl();
        DirHashMap<String> hashesTree2 = tree2Crawler.crawl();
        log.trace(hashesTree1.toString());
        log.trace(hashesTree2.toString());

    }

}