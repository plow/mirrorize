package ch.snowplow.mirrorize.common;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.builders.DirHashMapBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public class DirHashMapTest extends TestCase {

    public void testAdd() {
        DirHashMap<String> map = new DirHashMapBuilder<String>().build();
        assertEquals(0, map.getHashes().size());
        assertEquals(0, map.getPaths().size());
        assertFalse(map.getHashes().contains("abc"));
        assertFalse(map.getPaths().contains("a/pat.h"));
        map.add("abc", new PathBuilder().withPath("a/pat.h").build());
        assertEquals(1, map.getHashes().size());
        assertEquals(1, map.getPaths().size());
        assertTrue(map.getHashes().contains("abc"));
        assertTrue(map.getPaths().contains(
                new PathBuilder().withPath("a/pat.h").build()));
    }

    public void testAddAll() {
        // TODO implement
    }

    public void testGetFilePathByHash() {
        // TODO implement
    }

    public void testGetFileHashByPath() {
        // TODO implement
    }

    public void testGetSerializedHashes() {
        // TODO implement
    }

    public void testToString() {
        // TODO implement
    }

    public void testGetFileHashes() {
        // TODO implement
    }

    public void testGetHashes() {
        // TODO implement
    }

    public void testGetPaths() {
        // TODO implement
    }
}
