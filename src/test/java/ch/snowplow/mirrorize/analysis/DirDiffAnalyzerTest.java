package ch.snowplow.mirrorize.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.FileSysTestCase;
import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.FileHashSet;
import ch.snowplow.mirrorize.gathering.FileSysTreeCrawler;
import ch.snowplow.mirrorize.testdata.builders.DirHashMapBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileHashCorrespBuilder;
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

        FileHashSet<String> hashDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDiffsOfHashes();
        assertAllContained(hashDiffs, "", "a", "a/file_a2.txt",
                "a/file_a5.txt", "c", "c/file_c1.txt", "c_dir",
                "c_dir/file_c1d.txt", "c_dir/file_c2d.txt", "f",
                "f/file_f.txt", "f_dir", "f_dir/file_f1d.txt",
                "f_dir/file_f2d.txt");

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

        FileHashSet<String> pathDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDiffsOfPaths();
        assertAllContained(pathDiffs, "a/file_a2.txt", "a/file_a4_1.txt", "c",
                "c/file_c1.txt", "c_dir", "c_dir/file_c1d.txt",
                "c_dir/file_c2d.txt", "e/file_e1.txt", "e_dir1",
                "e_dir1/file_e1d.txt", "e_dir1/file_e2d.txt");
    }

    public void testGetNewFiles() {
        // a/file_a2.txt
        // c
        // c/file_c1.txt
        // c_dir
        // c_dir/file_c1d.txt
        // c_dir/file_c2d.txt

        FileHashSet<String> newFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getNewFiles();
        assertAllContained(newFiles, "a/file_a2.txt", "c", "c/file_c1.txt",
                "c_dir", "c_dir/file_c1d.txt", "c_dir/file_c2d.txt");
    }

    public void testGetModifiedFiles() {
        // a
        // a/file_a5.txt
        // f
        // f/file_f.txt
        // f_dir
        // f_dir/file_f1d.txt
        // f_dir/file_f2d.txt

        FileHashSet<String> modFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getModifiedFiles();
        assertAllContained(modFiles, "a", "a/file_a5.txt", "f", "f/file_f.txt",
                "f_dir", "f_dir/file_f1d.txt", "f_dir/file_f2d.txt");
    }

    public void testGetMovedFiles() {
        // a/file_a4_1.txt -> a/file_a4_2.txt
        // e/file_e1.txt -> e/file_e2.txt
        // e_dir1 -> e_dir2
        // e_dir1/file_e1d.txt -> e_dir2/file_e1d.txt
        // e_dir1/file_e2d.txt -> e_dir2/file_e2d.txt

        FileHashSet<String> movFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getMovedFiles();
        String movedFilePaths[] = { "a/file_a4_1.txt", "e/file_e1.txt",
                "e_dir1", "e_dir1/file_e1d.txt", "e_dir1/file_e2d.txt" };
        String movedFilePathsCorresp[] = { "a/file_a4_2.txt", "e/file_e2.txt",
                "e_dir2", "e_dir2/file_e1d.txt", "e_dir2/file_e2d.txt" };
        assertAllContained(movFiles, movedFilePaths);
        for (int i = 0; i < movedFilePaths.length; i++) {
            movFiles.contains(new FileHashCorrespBuilder<String>("")
                    .withPath1(movedFilePaths[i])
                    .withPath2(movedFilePathsCorresp[i]).build());
        }
    }

    public void testGetDeletedFiles() {
        // a/file_a3.txt
        // d
        // d/file_d1.txt
        // d_dir
        // d_dir/file_d1d.txt
        // d_dir/file_d2d.txt

        FileHashSet<String> delFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDeletedFiles();
        assertAllContained(delFiles, "a/file_a3.txt", "d", "d/file_d1.txt",
                "d_dir", "d_dir/file_d1d.txt", "d_dir/file_d2d.txt");
    }

    public void testGetAllFiles() {
        FileHashSet<String> allFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getAllFiles();

        // 29 is retrieved with bash command `find . | wc -l`
        assertEquals(29, allFiles.size());
    }

    private void assertAllContained(FileHashSet<String> fileHashes,
            String... filePaths) {
        Set<String> expectedPaths = new HashSet<String>(
                Arrays.asList(filePaths));
        assertEquals(expectedPaths.size(), fileHashes.size());
        for (FileHash<String> fh : fileHashes) {
            String p = fh.getPath().getPath();
            assertTrue(expectedPaths.contains(p));
            expectedPaths.remove(p);
        }
        assertEquals(0, expectedPaths.size());
    }

}
