package ch.snowplow.mirrorize.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.snowplow.mirrorize.FileSysTestCase;
import ch.snowplow.mirrorize.analysis.DirDiffAnalyzer.SetType;
import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.HashedFile;
import ch.snowplow.mirrorize.common.HashedFileSet;
import ch.snowplow.mirrorize.gathering.FileSysTreeCrawler;
import ch.snowplow.mirrorize.testdata.builders.DirHashMapBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileSysTreeCrawlerBuilder;
import ch.snowplow.mirrorize.testdata.builders.HashedFileRelatBuilder;

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

    public void testGetCorrespFiles() {
        // a/file_a1.txt
        // b
        // b/file_b1.txt
        // b_dir
        // b_dir/file_b1d.txt
        // b_dir/file_b2d.txt
        // e
        // g
        // g/file_g1.txt
        // g/file_g2.txt
        // h/h2/file_h21.txt
        // h/h4/file_h41.txt
        // h/h5/file_h51.txt
        // h/h6/file_h61.txt

        HashedFileSet<String> correspFiles = new DirDiffAnalyzer<String>(
                ourMap, theirMap).getCorrespFiles();
        assertAllContained(correspFiles, "a/file_a1.txt", "b", "b/file_b1.txt",
                "b_dir", "b_dir/file_b1d.txt", "b_dir/file_b2d.txt", "e", "g",
                "g/file_g1.txt", "g/file_g2.txt", "h/h2/file_h21.txt",
                "h/h4/file_h41.txt", "h/h5/file_h51.txt", "h/h6/file_h61.txt");
    }

    // TODO case: dirs with same hash (same files) but different paths inside

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

        HashedFileSet<String> hashDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getSetOfHashes(SetType.DIFF);
        assertAllContained(hashDiffs, "", "a", "a/file_a2.txt",
                "a/file_a5.txt", "c", "c/file_c1.txt", "c_dir",
                "c_dir/file_c1d.txt", "c_dir/file_c2d.txt", "f",
                "f/file_f.txt", "f_dir", "f_dir/file_f1d.txt",
                "f_dir/file_f2d.txt", "h", "h/h1", "h/h2", "h/h3", "h/h4",
                "h/h5", "h/h6", "h/h7");
    }

    public void testGetCorrespsOfHashes() {
        // a/file_a1.txt
        // a/file_a4_1.txt
        // b
        // b/file_b1.txt
        // b_dir
        // b_dir/file_b1d.txt
        // b_dir/file_b2d.txt
        // e
        // e/file_e1.txt
        // e_dir1
        // e_dir1/file_e1d.txt
        // e_dir1/file_e2d.txt
        // g
        // g/file_g1.txt
        // g/file_g2.txt
        // h/h1/file_h11.txt
        // h/h2/file_h21.txt
        // h/h3/file_h31.txt
        // h/h3/file_h32.txt
        // h/h4/file_h41.txt
        // h/h4/file_h42.txt
        // h/h5/file_h51.txt
        // h/h5/file_h52.txt
        // h/h6/file_h61.txt
        // h/h6/file_h62.txt
        // h/h6/file_h63.txt
        // h/h6/file_h64.txt
        // h/h7/file_h71.txt

        HashedFileSet<String> hashCorresps = new DirDiffAnalyzer<String>(
                ourMap, theirMap).getSetOfHashes(SetType.CORRESP);

        assertAllContained(hashCorresps, "a/file_a1.txt", "a/file_a4_1.txt",
                "b", "b/file_b1.txt", "b_dir", "b_dir/file_b1d.txt",
                "b_dir/file_b2d.txt", "e", "e/file_e1.txt", "e_dir1",
                "e_dir1/file_e1d.txt", "e_dir1/file_e2d.txt", "g",
                "g/file_g1.txt", "g/file_g2.txt", "h/h1/file_h11.txt",
                "h/h2/file_h21.txt", "h/h3/file_h31.txt", "h/h3/file_h32.txt",
                "h/h4/file_h41.txt", "h/h4/file_h42.txt", "h/h5/file_h51.txt",
                "h/h5/file_h52.txt", "h/h6/file_h61.txt", "h/h6/file_h62.txt",
                "h/h6/file_h63.txt", "h/h6/file_h64.txt", "h/h7/file_h71.txt");
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

        HashedFileSet<String> pathDiffs = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getSetOfPaths(SetType.DIFF);
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

        HashedFileSet<String> newFiles = new DirDiffAnalyzer<String>(ourMap,
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
        // h
        // h/h1
        // h/h2
        // h/h3
        // h/h4
        // h/h5
        // h/h6
        // h/h7
        // h/h7/file_h71.txt

        HashedFileSet<String> modFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getModifiedFiles();
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
        // h/h7/file_h71.txt -> [PathSet: h/h7/file_h72.txt]

        HashedFileSet<String> movFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getMovedFiles();
        String movedFilePaths[] = { "a/file_a4_1.txt", "e/file_e1.txt",
                "e_dir1", "e_dir1/file_e1d.txt", "e_dir1/file_e2d.txt",
                "h/h1/file_h11.txt", "h/h3/file_h31.txt", "h/h3/file_h32.txt",
                "h/h7/file_h71.txt" };
        String movedFilePathsCorresp[][] = { { "a/file_a4_2.txt" },
                { "e/file_e2.txt" }, { "e_dir2" }, { "e_dir2/file_e1d.txt" },
                { "e_dir2/file_e2d.txt" },
                { "h/h1/file_h12.txt", "h/h1/file_h13.txt" },
                { "h/h3/file_h33.txt" }, { "h/h3/file_h33.txt" },
                { "h/h7/file_h72.txt" } };
        assertAllContained(movFiles, movedFilePaths);
        for (int i = 0; i < movedFilePaths.length; i++) {
            movFiles.contains(new HashedFileRelatBuilder<String>("")
                    .withPath(movedFilePaths[i])
                    .withRelatedPaths(movedFilePathsCorresp[i]).build());
        }
    }

    public void testGetDeletedFiles() {
        // a/file_a3.txt
        // d
        // d/file_d1.txt
        // d_dir
        // d_dir/file_d1d.txt
        // d_dir/file_d2d.txt

        HashedFileSet<String> delFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getDeletedFiles();
        assertAllContained(delFiles, "a/file_a3.txt", "d", "d/file_d1.txt",
                "d_dir", "d_dir/file_d1d.txt", "d_dir/file_d2d.txt");
    }

    public void testGetAllFiles() {
        HashedFileSet<String> allFiles = new DirDiffAnalyzer<String>(ourMap,
                theirMap).getAllFiles();

        // 50 is retrieved with bash command `find . | wc -l`
        assertEquals(50, allFiles.size());
    }

    private void assertAllContained(HashedFileSet<String> fileHashes,
            String... filePaths) {
        Set<String> expectedPaths = new HashSet<String>(
                Arrays.asList(filePaths));
        assertEquals(expectedPaths.size(), fileHashes.size());
        for (HashedFile<String> fh : fileHashes) {
            String p = fh.getPath().getPath();
            assertTrue(expectedPaths.contains(p));
            expectedPaths.remove(p);
        }
        assertEquals(0, expectedPaths.size());
    }

}
