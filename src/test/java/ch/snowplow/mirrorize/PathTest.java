package ch.snowplow.mirrorize;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.PathTestData;

public class PathTest extends TestCase {

    public void testGetPath() {
        for (PathTestData test : PathTestData.values()) {
            assertEquals(test.getPath(), test.getPathObj().getPath());
        }
    }

    public void testGetName() {
        for (PathTestData test : PathTestData.values()) {
            assertEquals(test.getName(), test.getPathObj().getName());
        }
    }

    public void testGetDir() {
        for (PathTestData test : PathTestData.values()) {
            assertEquals(test.getDir(), test.getPathObj().getDir());
        }
    }

    public void testGetDepth() {
        for (PathTestData test : PathTestData.values()) {
            assertEquals(test.getDepth(), test.getPathObj().getDepth());
        }
    }

    public void testCompareTo() {
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_REF.getPathObj()) == 0);
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_LESS1.getPathObj()) > 0);
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_LESS2.getPathObj()) > 0);
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_LESS3.getPathObj()) > 0);
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_MORE1.getPathObj()) < 0);
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_MORE2.getPathObj()) < 0);
        assertTrue(PathTestData.PATH_REF.getPathObj().compareTo(
                PathTestData.PATH_MORE3.getPathObj()) < 0);
    }

    public void testHashCode() {
        assertTrue(PathTestData.PATH_REF.getPathObj().hashCode() == PathTestData.PATH_REF
                .getPathObj().hashCode());
        assertFalse(PathTestData.PATH_REF.getPathObj().hashCode() == PathTestData.PATH_LESS1
                .getPathObj().hashCode());
        assertFalse(PathTestData.PATH_REF.getPathObj().hashCode() == PathTestData.PATH_MORE1
                .getPathObj().hashCode());
    }

    public void testEquals() {
        assertTrue(PathTestData.PATH_REF.getPathObj().equals(
                PathTestData.PATH_REF.getPathObj()));
        assertFalse(PathTestData.PATH_REF.getPathObj().equals(
                PathTestData.PATH_LESS1.getPathObj()));
        assertFalse(PathTestData.PATH_REF.getPathObj().equals(
                PathTestData.PATH_LESS2.getPathObj()));
    }
}
