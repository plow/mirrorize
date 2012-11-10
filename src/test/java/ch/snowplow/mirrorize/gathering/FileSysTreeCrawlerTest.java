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

    public void testFileHashing() {
        /*
         * Files in the test directory and their MD5 sums (retrieved with the
         * bash script src/test/scripts/md5_hashes_of_dir.sh):
         * 
         * ffec21f202ceb5a96ae19bebc1df46ee test_tree/subdir/file.txt
         * b3a5ebcb4f8d273ee5d9ec33e51f7c6a test_tree/subdir/image2.jpg
         * 2b2e214b302940c07863b273b4cd8e66 test_tree/textfile.txt
         * 9e950c74df43ab18f52c72ad86935004 test_tree/image1.jpg
         */

        final String baseDir = "src/test/resources/test_tree/";
        File tree = new File(baseDir);

        FileSysTreeCrawler crawler = null;
        try {
            crawler = new FileSysTreeCrawler(tree, "MD5");
        } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
            fail("exception throwed during instantiation");
        }
        DirHashMap<String> dirHashMap = crawler.crawl();

        // expect 6 file hashes (4 files, 1 subfolder, 1 root folder)
        assertEquals(6, dirHashMap.getFileHashes().size());
        assertEquals(6, dirHashMap.getHashes().size());
        assertEquals(6, dirHashMap.getPaths().size());

        // retrieve hash by path
        assertEquals(
                "ffec21f202ceb5a96ae19bebc1df46ee",
                dirHashMap.getFileHashByPath(new PathBuilder().withPath(
                        "subdir/file.txt").build()));
        assertEquals(
                "b3a5ebcb4f8d273ee5d9ec33e51f7c6a",
                dirHashMap.getFileHashByPath(new PathBuilder().withPath(
                        "subdir/image2.jpg").build()));
        assertEquals(
                "2b2e214b302940c07863b273b4cd8e66",
                dirHashMap.getFileHashByPath(new PathBuilder().withPath(
                        "textfile.txt").build()));
        assertEquals(
                "9e950c74df43ab18f52c72ad86935004",
                dirHashMap.getFileHashByPath(new PathBuilder().withPath(
                        "image1.jpg").build()));

        // retrieve path by hash
        assertEquals(new PathBuilder().withPath("subdir/file.txt").build(),
                dirHashMap
                        .getFilePathByHash("ffec21f202ceb5a96ae19bebc1df46ee"));

        assertEquals(new PathBuilder().withPath("subdir/image2.jpg").build(),
                dirHashMap
                        .getFilePathByHash("b3a5ebcb4f8d273ee5d9ec33e51f7c6a"));

        assertEquals(new PathBuilder().withPath("textfile.txt").build(),
                dirHashMap
                        .getFilePathByHash("2b2e214b302940c07863b273b4cd8e66"));

        assertEquals(new PathBuilder().withPath("image1.jpg").build(),
                dirHashMap
                        .getFilePathByHash("9e950c74df43ab18f52c72ad86935004"));
    }

    public void testDirectoryHashing() {
        /*
         * Files in the test directory and their MD5 sums (retrieved with the
         * bash script src/test/scripts/md5_hashes_of_dir.sh):
         * 
         * ffec21f202ceb5a96ae19bebc1df46ee test_tree/subdir/file.txt
         * b3a5ebcb4f8d273ee5d9ec33e51f7c6a test_tree/subdir/image2.jpg
         * 2b2e214b302940c07863b273b4cd8e66 test_tree/textfile.txt
         * 9e950c74df43ab18f52c72ad86935004 test_tree/image1.jpg
         * 
         * directory hashes are computed employing the lexicographical order of
         * files hashes. The MD5 sums of the concatenated hashes can be
         * computed, e.g., on www.webutils.pl/MD5_Calculator:
         * 
         * md5(subdir) := md5( concat(b3a..., ffe...) =
         * 1ca03e6446ead983cf7dbfe66b62b81e
         * 
         * md5(rootdir) := md5(concat(1ca..., 2be..., 9e9...)) =
         * 97b22fd74a9e88e20ea4f43c39106f04
         */

        final String baseDir = "src/test/resources/test_tree/";
        File tree = new File(baseDir);

        FileSysTreeCrawler crawler = null;
        try {
            crawler = new FileSysTreeCrawler(tree, "MD5");
        } catch (NoSuchAlgorithmException | InvalidTreeRootException e) {
            fail("exception throwed during instantiation");
        }
        DirHashMap<String> dirHashMap = crawler.crawl();

        // expect 6 file hashes (4 files, 1 subfolder, 1 root folder)
        assertEquals(6, dirHashMap.getFileHashes().size());
        assertEquals(6, dirHashMap.getHashes().size());
        assertEquals(6, dirHashMap.getPaths().size());

        // retrieve hash by path
        assertEquals(
                "1ca03e6446ead983cf7dbfe66b62b81e",
                dirHashMap.getFileHashByPath(new PathBuilder().withPath(
                        "subdir").build()));

        assertEquals("97b22fd74a9e88e20ea4f43c39106f04",
                dirHashMap.getFileHashByPath(new PathBuilder().withPath("")
                        .build()));

        // retrieve path by hash
        assertEquals(new PathBuilder().withPath("subdir").build(),
                dirHashMap
                        .getFilePathByHash("1ca03e6446ead983cf7dbfe66b62b81e"));

        assertEquals(new PathBuilder().withPath("").build(),
                dirHashMap
                        .getFilePathByHash("97b22fd74a9e88e20ea4f43c39106f04"));
    }
}
