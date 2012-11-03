package ch.snowplow.mirrorize.common;

public final class Tools {

    /**
     * Returns a string consisting of a specified number of spaces.
     * 
     * @param n
     *            The number of spaces
     * @return The empty string if n<=0. Otherwise, a string of n concatenated
     *         spaces.
     */
    public static String getSpaces(int n) {
        if (n < 1) {
            return "";
        } else {
            return new String(new char[n]).replace('\0', ' ');
        }
    }

}
