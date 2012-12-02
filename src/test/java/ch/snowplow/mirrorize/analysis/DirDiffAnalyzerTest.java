package ch.snowplow.mirrorize.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.FileSysTestCase;
import ch.snowplow.mirrorize.analysis.DirDiffAnalyzer.SetType;
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
        // h
        // h/h1
        // h/h2
        // h/h3
        // h/h4
        // h/h5
        // h/h6
        // h/h7

        FileHashSet<String> hashDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getSetOfHashesC(SetType.DIFF);
        assertAllContained(hashDiffs, "", "a", "a/file_a2.txt",
                "a/file_a5.txt", "c", "c/file_c1.txt", "c_dir",
                "c_dir/file_c1d.txt", "c_dir/file_c2d.txt", "f",
                "f/file_f.txt", "f_dir", "f_dir/file_f1d.txt",
                "f_dir/file_f2d.txt", "h", "h/h1", "h/h2", "h/h3", "h/h4",
                "h/h5", "h/h6", "h/h7");
    }

    // TODO also test getSetOfHashesC(SetType.CORRESP) here

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
        // h/h1/file_h11.txt
        // h/h3/file_h31.txt
        // h/h3/file_h32.txt
        // h/h4/file_h42.txt
        // h/h5/file_h52.txt
        // h/h6/file_h62.txt
        // h/h6/file_h63.txt
        // h/h6/file_h64.txt

        FileHashSet<String> pathDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getSetOfPathsC(SetType.DIFF);
        assertAllContained(pathDiffs, "a/file_a2.txt", "a/file_a4_1.txt", "c",
                "c/file_c1.txt", "c_dir", "c_dir/file_c1d.txt",
                "c_dir/file_c2d.txt", "e/file_e1.txt", "e_dir1",
                "e_dir1/file_e1d.txt", "e_dir1/file_e2d.txt",
                "h/h1/file_h11.txt", "h/h3/file_h31.txt", "h/h3/file_h32.txt",
                "h/h4/file_h42.txt", "h/h5/file_h52.txt", "h/h6/file_h62.txt",
                "h/h6/file_h63.txt", "h/h6/file_h64.txt");
    }

    // TODO also test getSetOfPathsC(SetType.CORRESP) here

    public void testGetNewFiles() {
        // a/file_a2.txt
        // c
        // c/file_c1.txt
        // c_dir
        // c_dir/file_c1d.txt
        // c_dir/file_c2d.txt

        FileHashSet<String> newFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getNewFilesC();
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
        // h
        // h/h1
        // h/h2
        // h/h3
        // h/h4
        // h/h5
        // h/h6
        // h/h7
        // h/h7/file_h71.txt

        FileHashSet<String> modFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getModifiedFilesC();
        assertAllContained(modFiles, "a", "a/file_a5.txt", "f", "f/file_f.txt",
                "f_dir", "f_dir/file_f1d.txt", "f_dir/file_f2d.txt", "h",
                "h/h1", "h/h2", "h/h3", "h/h4", "h/h5", "h/h6", "h/h7",
                "h/h7/file_h71.txt");
    }

    public void testGetMovedFiles() {
        // a/file_a4_1.txt -> [PathSet: a/file_a4_2.txt]
        // e/file_e1.txt -> [PathSet: e/file_e2.txt]
        // e_dir1 -> [PathSet: e_dir2]
        // e_dir1/file_e1d.txt -> [PathSet: e_dir2/file_e1d.txt]
        // e_dir1/file_e2d.txt -> [PathSet: e_dir2/file_e2d.txt]

        // h/h1/file_h11.txt -> [PathSet: h/h1/file_h12.txt | h/h1/file_h13.txt]
        // h/h3/file_h31.txt -> [PathSet: h/h3/file_h33.txt]
        // h/h3/file_h32.txt -> [PathSet: h/h3/file_h33.txt]
        // h/h4/file_h42.txt -> [PathSet: h/h4/file_h41.txt]
        // h/h5/file_h52.txt -> [PathSet: h/h5/file_h55.txt | h/h5/file_h51.txt
        // | h/h5/file_h54.txt | h/h5/file_h53.txt]
        // h/h6/file_h62.txt -> [PathSet: h/h6/file_h65.txt | h/h6/file_h66.txt
        // | h/h6/file_h61.txt]
        // h/h6/file_h63.txt -> [PathSet: h/h6/file_h65.txt | h/h6/file_h66.txt
        // | h/h6/file_h61.txt]
        // h/h6/file_h64.txt -> [PathSet: h/h6/file_h65.txt | h/h6/file_h66.txt
        // | h/h6/file_h61.txt]
        // h/h7/file_h71.txt -> [PathSet: h/h7/file_h72.txt]

        FileHashSet<String> movFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getMovedFilesC();
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
                theirMap).getDeletedFilesC();
        assertAllContained(delFiles, "a/file_a3.txt", "d", "d/file_d1.txt",
                "d_dir", "d_dir/file_d1d.txt", "d_dir/file_d2d.txt");
    }

    public void testGetAllFiles() {
        FileHashSet<String> allFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getAllFilesC();

        // 50 is retrieved with bash command `find . | wc -l`
        assertEquals(50, allFiles.size());
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
