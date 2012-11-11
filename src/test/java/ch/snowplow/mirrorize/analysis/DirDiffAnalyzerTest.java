package ch.snowplow.mirrorize.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.snowplow.mirrorize.FileSysTestCase;
import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.gathering.FileSysTreeCrawler;
import ch.snowplow.mirrorize.testdata.builders.DirHashMapBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileSysTreeCrawlerBuilder;

public class DirDiffAnalyzerTest extends FileSysTestCase {

    DirHashMap<String> ourMap, theirMap;

    @Override
    public void setUp() {
        super.setUp();
        FileSysTreeCrawler crawler;
        crawler = new FileSysTreeCrawlerBuilder()
                .withTreeRoot(FileBuilder.DEFAULT_TEST_TREE_ROOT + "tree1")
                .withHashAlgo("MD5").build();
        ourMap = crawler.crawl();

        crawler = new FileSysTreeCrawlerBuilder()
                .withTreeRoot(FileBuilder.DEFAULT_TEST_TREE_ROOT + "tree2")
                .withHashAlgo("MD5").build();
        theirMap = crawler.crawl();
    }

    public void testDirDiffAnalyzer() {
        try {
            new DirDiffAnalyzer<String>(new DirHashMapBuilder<String>().add(
                    "abc", "a/path.h").build(), new DirHashMapBuilder<String>()
                    .add("abc", "a/path.h").build());
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetDiffsOfHashes() {
        // (root)
        // a
        // a/file_a2.txt
        // a/file_a5.txt
        // c
        // c/file_c1.txt
        // c_dir
        // c_dir/file_c1d.txt
        // c_dir/file_c2d.txt
        // f
        // f/file_f.txt
        // f_dir
        // f_dir/file_f1d.txt
        // f_dir/file_f2d.txt

        Set<FileHash<String>> hashDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDiffsOfHashes();
        List<String> listDiffs = Arrays.asList("", "a", "a/file_a2.txt",
                "a/file_a5.txt", "c", "c/file_c1.txt", "c_dir",
                "c_dir/file_c1d.txt", "c_dir/file_c2d.txt", "f",
                "f/file_f.txt", "f_dir", "f_dir/file_f1d.txt",
                "f_dir/file_f2d.txt");
        Set<String> setDiffs = new HashSet<String>(listDiffs);
        assertEquals(setDiffs.size(), hashDiffs.size());
        for (FileHash<String> fh : hashDiffs) {
            String p = fh.getPath().getPath();
            assertTrue(setDiffs.contains(p));
            setDiffs.remove(p);
        }
        assertEquals(0, setDiffs.size());
    }

    public void testGetDiffsOfPaths() {
        // a/file_a2.txt
        // a/file_a4_1.txt
        // c
        // c/file_c1.txt
        // c_dir
        // c_dir/file_c1d.txt
        // c_dir/file_c2d.txt
        // e/file_e1.txt
        // e_dir1
        // e_dir1/file_e1d.txt
        // e_dir1/file_e2d.txt

        Set<FileHash<String>> pathDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDiffsOfPaths();
        List<String> listDiffs = Arrays.asList("a/file_a2.txt",
                "a/file_a4_1.txt", "c", "c/file_c1.txt", "c_dir",
                "c_dir/file_c1d.txt", "c_dir/file_c2d.txt", "e/file_e1.txt",
                "e_dir1", "e_dir1/file_e1d.txt", "e_dir1/file_e2d.txt");
        Set<String> setDiffs = new HashSet<String>(listDiffs);
        assertEquals(setDiffs.size(), pathDiffs.size());
        for (FileHash<String> fh : pathDiffs) {
            String p = fh.getPath().getPath();
            assertTrue(setDiffs.contains(p));
            setDiffs.remove(p);
        }
        assertEquals(0, setDiffs.size());
    }

    public void testGetNewFiles() {
        Set<FileHash<String>> newFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getNewFiles();

        // TODO implement
    }

    public void testGetModifiedFiles() {
        Set<FileHash<String>> modFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getModifiedFiles();
        // TODO implement
    }

    public void testGetDeletedFiles() {
        Set<FileHash<String>> delFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDeletedFiles();
        // TODO implement
    }

    public void testGetAllFiles() {
        Set<FileHash<String>> allFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getAllFiles();
        // TODO implement
    }

}
