package ch.snowplow.mirrorize.common;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.builders.DirHashMapBuilder;
import ch.snowplow.mirrorize.testdata.builders.FileHashBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public class DirHashMapTest extends TestCase {

    public void testAdd() {
        DirHashMap<String> map = new DirHashMapBuilder<String>().build();
        assertEquals(0, map.getHashes().size());
        assertEquals(0, map.getPaths().size());
        assertEquals(0, map.getFileHashes().size());
        assertFalse(map.getHashes().contains("abc"));
        assertFalse(map.getPaths().contains("a/pat.h"));
        assertFalse(map.getFileHashes().contains(
                new FileHashBuilder<String>("").withHash("abc")
                        .withPath("a/pat.h").build()));
        map.add("abc", new PathBuilder().withPath("a/pat.h").build());
        assertEquals(1, map.getHashes().size());
        assertEquals(1, map.getPaths().size());
        assertEquals(1, map.getFileHashes().size());
        assertTrue(map.getHashes().contains("abc"));
        assertTrue(map.getPaths().contains(
                new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(map.getFileHashes().contains(
                new FileHashBuilder<String>("").withHash("abc")
                        .withPath("a/pat.h").build()));
    }

    public void testAddFileHash() {
        DirHashMap<String> map = new DirHashMapBuilder<String>().build();
        assertEquals(0, map.getHashes().size());
        assertEquals(0, map.getPaths().size());
        assertEquals(0, map.getFileHashes().size());
        assertFalse(map.getHashes().contains("abc"));
        assertFalse(map.getPaths().contains("a/pat.h"));
        assertFalse(map.getFileHashes().contains(
                new FileHashBuilder<String>("").withHash("abc")
                        .withPath("a/pat.h").build()));
        map.add(new FileHashBuilder<String>("").withHash("abc")
                .withPath("a/pat.h").build());
        assertEquals(1, map.getHashes().size());
        assertEquals(1, map.getPaths().size());
        assertEquals(1, map.getFileHashes().size());
        assertTrue(map.getHashes().contains("abc"));
        assertTrue(map.getPaths().contains(
                new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(map.getFileHashes().contains(
                new FileHashBuilder<String>("").withHash("abc")
                        .withPath("a/pat.h").build()));
    }

    public void testAddAll() {
        DirHashMap<String> map = new DirHashMapBuilder<String>().build();
        assertEquals(0, map.getHashes().size());
        assertEquals(0, map.getPaths().size());
        assertFalse(map.getHashes().contains("abc"));
        assertFalse(map.getPaths().contains(
                new PathBuilder().withPath("a/pat.h").build()));
        assertFalse(map.getHashes().contains("xyz"));
        assertFalse(map.getPaths().contains(
                new PathBuilder().withPath("b/pat.h").build()));

        DirHashMap<String> mapToAdd = new DirHashMapBuilder<String>()
                .add("abc", "a/pat.h").add("xyz", "b/pat.h").build();
        map.addAll(mapToAdd);

        assertEquals(2, map.getHashes().size());
        assertEquals(2, map.getPaths().size());
        assertTrue(map.getHashes().contains("abc"));
        assertTrue(map.getPaths().contains(
                new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(map.getHashes().contains("xyz"));
        assertTrue(map.getPaths().contains(
                new PathBuilder().withPath("b/pat.h").build()));
    }

    public void testGetFilePathByHash() {
        DirHashMap<String> map = new DirHashMapBuilder<String>()
                .add("abc", "a/pat.h").add("xyz", "b/pat.h").build();
        assertNull(map.getFilePathByHash("abcd"));
        assertEquals(new PathBuilder().withPath("a/pat.h").build(),
                map.getFilePathByHash("abc"));
        assertEquals(new PathBuilder().withPath("b/pat.h").build(),
                map.getFilePathByHash("xyz"));
    }

    public void testGetFileHashByPath() {
        DirHashMap<String> map = new DirHashMapBuilder<String>()
                .add("abc", "a/pat.h").add("xyz", "b/pat.h").build();
        assertNull(map.getFileHashByPath(new PathBuilder().withPath("no/pat.h")
                .build()));
        assertEquals("abc", map.getFileHashByPath(new PathBuilder().withPath(
                "a/pat.h").build()));
        assertEquals("xyz", map.getFileHashByPath(new PathBuilder().withPath(
                "b/pat.h").build()));
    }

    public void testGetSerializedHashes() {
        DirHashMap<String> map1 = new DirHashMapBuilder<String>()
                .add("a", "a/pat.h").add("b", "b/pat.h").add("c", "c/pat.h")
                .build();
        assertEquals("abc", map1.getSerializedHashes());

        DirHashMap<String> map2 = new DirHashMapBuilder<String>()
                .add("a", "c/pat.b").add("b", "c/pat.a").add("c", "c")
                .add("d", "b/pat.b").add("e", "b/pat.a").add("f", "b")
                .add("g", "a/pat.b").add("h", "a/pat.a").add("i", "a").build();
        assertEquals("ihgfedcba", map2.getSerializedHashes());
    }

    public void testToString() {
        // TODO implement
    }

    public void testGetFileHashes() {
        DirHashMap<String> map = new DirHashMapBuilder<String>()
                .add("a", "a/pat.h").add("b", "b/pat.h").add("c", "c/pat.h")
                .build();
        HashSet<FileHash<String>> hashes = map.getFileHashes();
        assertEquals(3, hashes.size());
        assertTrue(hashes.contains(new FileHashBuilder<String>("")
                .withHash("a").withPath("a/pat.h").build()));
        assertTrue(hashes.contains(new FileHashBuilder<String>("")
                .withHash("b").withPath("b/pat.h").build()));
        assertTrue(hashes.contains(new FileHashBuilder<String>("")
                .withHash("c").withPath("c/pat.h").build()));
        assertFalse(hashes.contains(new FileHashBuilder<String>("")
                .withHash("ab").withPath("a/pat.h").build()));
        assertFalse(hashes.contains(new FileHashBuilder<String>("")
                .withHash("b").withPath("bb/pat.h").build()));
        assertFalse(hashes.contains(new FileHashBuilder<String>("")
                .withHash("cc").withPath("cc/pat.h").build()));
    }

    public void testGetHashes() {
        DirHashMap<String> map = new DirHashMapBuilder<String>()
                .add("a", "a/pat.h").add("b", "b/pat.h").add("c", "c/pat.h")
                .build();
        Set<String> hashes = map.getHashes();
        assertEquals(3, hashes.size());
        assertTrue(hashes.contains("a"));
        assertTrue(hashes.contains("b"));
        assertTrue(hashes.contains("c"));
        assertFalse(hashes.contains("d"));
    }

    public void testGetPaths() {
        DirHashMap<String> map = new DirHashMapBuilder<String>()
                .add("a", "a/pat.h").add("b", "b/pat.h").add("c", "c/pat.h")
                .build();
        Set<Path> paths = map.getPaths();
        assertEquals(3, paths.size());
        assertTrue(paths
                .contains(new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(paths
                .contains(new PathBuilder().withPath("b/pat.h").build()));
        assertTrue(paths
                .contains(new PathBuilder().withPath("c/pat.h").build()));
        assertFalse(paths.contains(new PathBuilder().withPath("d/pat.h")
                .build()));
    }
}
