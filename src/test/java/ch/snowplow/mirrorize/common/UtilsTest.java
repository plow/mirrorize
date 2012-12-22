package ch.snowplow.mirrorize.common;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

    public void testGetSpaces() {
        assertEquals("", Utils.getSpaces(-10));
        assertEquals("", Utils.getSpaces(0));
        assertEquals(" ", Utils.getSpaces(1));
        assertEquals("     ", Utils.getSpaces(5));
    }

}
