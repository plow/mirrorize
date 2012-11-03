package ch.snowplow.mirrorize.common;

import junit.framework.TestCase;

public class ToolsTest extends TestCase {

    public void testGetSpaces() {
        assertEquals("", Tools.getSpaces(-10));
        assertEquals("", Tools.getSpaces(0));
        assertEquals(" ", Tools.getSpaces(1));
        assertEquals("     ", Tools.getSpaces(5));
    }

}
