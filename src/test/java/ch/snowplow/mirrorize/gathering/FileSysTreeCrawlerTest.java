package ch.snowplow.mirrorize.gathering;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Set;

import ch.snowplow.mirrorize.FileSysTestCase;
import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.gathering.FileSysTreeCrawler.StringMD5Hasher;
import ch.snowplow.mirrorize.testdata.FileTestData;
import ch.snowplow.mirrorize.testdata.builders.FileBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public class FileSysTreeCrawlerTest extends FileSysTestCase {

    MessageDigest digest;

    public FileSysTreeCrawlerTest() {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            fail("invalid algorithm");
        }
    }

    public void testCrawlPaths() {
        for (String treeName : Arrays.asList("tree1", "tree2")) {
            File tree = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT + treeName);

            FileSysTreeCrawler crawler = null;
            try {
                crawler = new FileSysTreeCrawler(tree, "MD5");
            } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
                fail("exception throwed during instantiation");
            }

            DirHashMap<String> dirHashMap = crawler.crawl();

            Set<Path> pathSet = dirHashMap.getPaths();
            for (FileTestData f : FileTestData.values()) {
                if (!f.getPath().startsWith(treeName)) {
                    pathSet.remove(new PathBuilder().withPath(f.getPath())
                            .build());
                }
            }
            assertEquals(pathSet.size(), dirHashMap.getPaths().size());
            for (Path p : pathSet) {
                assertTrue(dirHashMap.getPaths().contains(p));
                assertNotNull(dirHashMap.getFileHashByPath(p));
            }
            assertNull(dirHashMap.getFileHashByPath(new PathBuilder().withPath(
                    "invalid/path").build()));
        }
    }

    public void testCrawlHashes() {
        for (String treeName : Arrays.asList("tree1", "tree2")) {
            File tree = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT + treeName);

            FileSysTreeCrawler crawler = null;
            try {
                crawler = new FileSysTreeCrawler(tree, "MD5");
            } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
                fail("exception throwed during instantiation");
            }

            DirHashMap<String> dirHashMap = crawler.crawl();

            Set<String> hashSet = dirHashMap.getHashes();
            for (FileTestData f : FileTestData.values()) {
                if (!f.getPath().startsWith(treeName)) {
                    StringMD5Hasher hasher = crawler.new StringMD5Hasher(
                            f.getContent());
                    hashSet.remove(hasher.getHash());
                }
            }
            assertEquals(hashSet.size(), dirHashMap.getHashes().size());
            for (String h : hashSet) {
                assertTrue(dirHashMap.getHashes().contains(h));
                assertNotNull(dirHashMap.getFilePathByHash(h));
            }
            assertNull(dirHashMap.getFilePathByHash("invalid_hash"));
        }
    }

    public void testHashAlgosValid() {
        File tree = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT + "tree1");
        for (String algo : Arrays.asList("MD5", "SHA-1", "SHA-256")) {
            try {
                new FileSysTreeCrawler(tree, algo);
            } catch (NoSuchAlgorithmException e) {
                fail("valid algorithm");
            } catch (InvalidTreeRootException e) {
                fail("invalid tree root");
            }
        }
    }

    public void testHashAlgosInvalid() {
        File tree = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT + "tree1");
        try {
            new FileSysTreeCrawler(tree, "INVALID");
        } catch (NoSuchAlgorithmException e) {
            return;
        } catch (InvalidTreeRootException e) {
            fail("invalid tree root");
        }
        fail("invalid algorithm");
    }

    public void testTreeRootInvalid() {
        File tree = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT
                + "tree_invalid");
        try {
            new FileSysTreeCrawler(tree, "MD5");
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidTreeRootException e) {
            return;
        }
        fail("invalid tree root");
    }

    public void testMD5Hashing() {
        File tree = new File(FileBuilder.DEFAULT_TEST_TREE_ROOT + "tree1");

        FileSysTreeCrawler crawler = null;
        try {
            crawler = new FileSysTreeCrawler(tree, "MD5");
        } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
            fail("exception throwed during instantiation");
        }

        assertEquals("327b6f07435811239bc47e1544353273",
                (crawler.new StringMD5Hasher("foo bar")).getHash());
        assertEquals("486eb65274adb86441072afa1e2289f3",
                (crawler.new StringMD5Hasher("this is a test string"))
                        .getHash());
    }

    // TODO test the directory hash computation

}
