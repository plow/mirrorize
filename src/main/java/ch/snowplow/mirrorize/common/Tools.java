package ch.snowplow.mirrorize.common;

public final class Tools {

    /**
     * Returns a string consisting of a specified number of spaces.
     * 
     * @param n
     *            The number of spaces
     * @return A string of spaces
     */
    public static String getSpaces(int n) {
        return new String(new char[n]).replace('\0', ' ');
    }

}
