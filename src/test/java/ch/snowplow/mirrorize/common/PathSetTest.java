package ch.snowplow.mirrorize.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public class PathSetTest extends TestCase {

    PathSet ps;

    @Override
    public void setUp() {
        ps = new PathSet();
    }

    public void testNewAndAddAndSizeAndEmpty() {
        assertNotNull(ps);
        assertEquals(0, ps.size());
        assertTrue(ps.isEmpty());
        assertTrue(ps.add(new PathBuilder().withPath("a/pat.h").build()));
        assertEquals(1, ps.size());
        assertFalse(ps.isEmpty());
        assertTrue(ps.add(new PathBuilder().withPath("b/pat.h").build()));
        assertEquals(2, ps.size());
        assertFalse(ps.isEmpty());
        assertFalse(ps.add(new PathBuilder().withPath("a/pat.h").build()));
        assertEquals(2, ps.size());
        assertFalse(ps.isEmpty());
    }

    public void testContains() {
        assertTrue(ps.add(new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(ps.add(new PathBuilder().withPath("b/pat.h").build()));
        assertTrue(ps.contains(new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(ps.contains(new PathBuilder().withPath("b/pat.h").build()));
        assertFalse(ps.contains(new PathBuilder().withPath("c/pat.h").build()));
        assertFalse(ps.contains("a string object"));
    }

    public void testIterator() {
        assertTrue(ps.add(new PathBuilder().withPath("a/pat.h").build()));
        assertIteratorLenght(ps.iterator(), 1);
        assertTrue(ps.add(new PathBuilder().withPath("b/pat.h").build()));
        assertIteratorLenght(ps.iterator(), 2);
        assertTrue(ps.add(new PathBuilder().withPath("c/pat.h").build()));
        assertIteratorLenght(ps.iterator(), 3);
    }

    private void assertIteratorLenght(Iterator<Path> it, int len) {
        assertNotNull(it);
        int n;
        for (n = 0; it.hasNext(); n++) {
            assertNotNull(it.next());
        }
        assertEquals(len, n);
    }

    public void testToArray() {
        assertTrue(ps.add(new PathBuilder().withPath("a/pat.h").build()));
        assertTrue(ps.add(new PathBuilder().withPath("b/pat.h").build()));
        Object[] array = ps.toArray();
        assertEquals(2, array.length);
        assertEquals(new PathBuilder().withPath("a/pat.h").build(), array[0]);
        assertEquals(new PathBuilder().withPath("b/pat.h").build(), array[1]);
    }

    public void testToArrayT() {
        ps.add(new PathBuilder().withPath("a/pat.h").build());
        ps.add(new PathBuilder().withPath("b/pat.h").build());
        Path[] paths = new Path[2];
        Path[] pathsRet = ps.toArray(paths);
        assertTrue(pathsRet == paths);
        ps.toArray(paths);
        assertArrayContains(paths, new PathBuilder().withPath("a/pat.h")
                .build(), new PathBuilder().withPath("b/pat.h").build());

        paths = new Path[1];
        pathsRet = ps.toArray(paths);
        assertTrue(pathsRet != paths);
        assertArrayContains(pathsRet, new PathBuilder().withPath("a/pat.h")
                .build(), new PathBuilder().withPath("b/pat.h").build());

        paths = new Path[10];
        pathsRet = ps.toArray(paths);
        Path[] subArr = new Path[2];
        assertTrue(pathsRet == paths);
        subArr[0] = pathsRet[0];
        subArr[1] = pathsRet[1];
        assertArrayContains(subArr, new PathBuilder().withPath("a/pat.h")
                .build(), new PathBuilder().withPath("b/pat.h").build());
        assertNull(pathsRet[2]); // element after the last one must be null
    }

    private void assertArrayContains(Path[] array, Path... paths) {
        assertNotNull(array);
        assertNotNull(paths);
        assertEquals(array.length, paths.length);

        Set<Path> arraySet = new HashSet<Path>();
        for (int i = 0; i < array.length; arraySet.add(array[i++]))
            ;
        Set<Path> pathSet = new HashSet<Path>();
        for (int i = 0; i < paths.length; pathSet.add(paths[i++]))
            ;
        assertEquals(arraySet.size(), array.length);
        assertEquals(pathSet.size(), array.length);

        Set<Path> arraySetCopy = new HashSet<Path>(arraySet);
        Set<Path> pathSetCopy = new HashSet<Path>(pathSet);
        arraySetCopy.removeAll(pathSet);
        pathSetCopy.removeAll(arraySet);
        assertEquals(0, arraySetCopy.size());
        assertEquals(0, pathSetCopy.size());
    }

    public void testRemove() {
        boolean catched = false;
        ps.add(new PathBuilder().withPath("a/pat.h").build());
        try {
            ps.remove(new PathBuilder().withPath("a/pat.h").build());
        } catch (UnsupportedOperationException e) {
            catched = true;
        }
        if (!catched) {
            fail("UnsupportedOperationException expected");
        }
    }

    public void testContainsAll() {
        ps.add(new PathBuilder().withPath("a/pat.h").build());
        ps.add(new PathBuilder().withPath("b/pat.h").build());
        assertTrue(ps.containsAll(Arrays.asList(new PathBuilder().withPath(
                "a/pat.h").build())));
        assertTrue(ps.containsAll(Arrays.asList(
                new PathBuilder().withPath("a/pat.h").build(),
                new PathBuilder().withPath("b/pat.h").build())));
        assertFalse(ps.containsAll(Arrays.asList(
                new PathBuilder().withPath("a/pat.h").build(),
                new PathBuilder().withPath("b/pat.h").build(),
                new PathBuilder().withPath("c/pat.h").build())));
    }

    public void testAddAll() {
        assertThrown(new Executor() {
            @Override
            public void execute() {
                ps.addAll(getPathCollection());
            }
        });
    }

    public void testRetainAll() {
        assertThrown(new Executor() {
            @Override
            public void execute() {
                ps.retainAll(getPathCollection());
            }
        });
    }

    public void testRemoveAll() {
        assertThrown(new Executor() {
            @Override
            public void execute() {
                ps.removeAll(getPathCollection());
            }
        });
    }

    private Collection<Path> getPathCollection() {
        Collection<Path> pathList = new ArrayList<Path>();
        pathList.add(new PathBuilder().withPath("a/pat.h").build());
        pathList.add(new PathBuilder().withPath("b/pat.h").build());
        return pathList;
    }

    public void testClear() {
        assertThrown(new Executor() {
            @Override
            public void execute() {
                ps.clear();
            }
        });
    }

    private void assertThrown(Executor executor) {
        boolean catched = false;
        try {
            executor.execute();
        } catch (UnsupportedOperationException e) {
            catched = true;
        }
        if (!catched) {
            fail("UnsupportedOperationException expected");
        }
    }

    interface Executor {
        void execute();
    }

}
