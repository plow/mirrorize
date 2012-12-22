package ch.snowplow.mirrorize.common;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.builders.HashedFileBuilder;
import ch.snowplow.mirrorize.testdata.builders.HashedFileSetBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public class HashedFileSetTest extends TestCase {

    HashedFileSet<String> hfs;

    @Override
    protected void setUp() {
        hfs = new HashedFileSetBuilder<String>()
                .addHashedFile(
                        new HashedFileBuilder<String>("").withHash("ahr")
                                .withPath("a/pat.h").build())
                .addHashedFile(
                        new HashedFileBuilder<String>("").withHash("cer")
                                .withPath("g/pat.e").build())
                .addHashedFile(
                        new HashedFileBuilder<String>("").withHash("era")
                                .withPath("k/pat.q").build()).build();
    }

    public void testRemoveAllByPath() {
        assertEquals(3, hfs.size());
        List<Path> paths;
        paths = Arrays.asList(new PathBuilder().withPath("no/pat.h").build(),
                new PathBuilder().withPath("neither/a/pat.h").build());
        hfs.removeAllByPath(paths);
        assertEquals(3, hfs.size());

        paths = Arrays.asList(new PathBuilder().withPath("no/pat.h").build(),
                new PathBuilder().withPath("neither/a/pat.h").build(),
                new PathBuilder().withPath("a/pat.h").build());
        hfs.removeAllByPath(paths);
        assertEquals(2, hfs.size());
        for (HashedFile<String> fh : hfs) {
            assertFalse("a/pat.h".equals(fh.getPath().getPath()));
        }

        paths = Arrays.asList(new PathBuilder().withPath("g/pat.e").build(),
                new PathBuilder().withPath("k/pat.q").build(),
                new PathBuilder().withPath("a/pat.h").build());
        hfs.removeAllByPath(paths);
        assertEquals(0, hfs.size());
    }

    public void testRemoveAllByHash() {
        assertEquals(3, hfs.size());
        List<String> hashes;
        hashes = Arrays.asList("nohash", "neitherahash");
        hfs.removeAllByHash(hashes);
        assertEquals(3, hfs.size());

        hashes = Arrays.asList("nohash", "neitherahash", "ahr");
        hfs.removeAllByHash(hashes);
        assertEquals(2, hfs.size());
        for (HashedFile<String> fh : hfs) {
            assertFalse("ahr".equals(fh.getHash()));
        }

        hashes = Arrays.asList("nohash", "neitherahash", "ahr", "cer", "era");
        hfs.removeAllByHash(hashes);
        assertEquals(0, hfs.size());
    }

    public void testGetHashes() {
        assertEquals(3, hfs.getHashes().size());
        hfs.add(new HashedFileBuilder<String>("").withHash("yyy")
                .withPath("a/pat.h").build());
        assertEquals(4, hfs.getHashes().size());
        hfs.add(new HashedFileBuilder<String>("").withHash("yyy")
                .withPath("a/pat.h").build());
        assertEquals(4, hfs.getHashes().size());
        for (String h : new String[] { "ahr", "cer", "era", "yyy" }) {
            assertTrue(hfs.getHashes().contains(h));
        }
    }
}
