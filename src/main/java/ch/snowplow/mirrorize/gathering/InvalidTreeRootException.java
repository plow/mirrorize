package ch.snowplow.mirrorize.gathering;

/**
 * An exception thrown if a directory path specified as one of the two root
 * folders to be compared is not valid, i.e., does not exist on the file system
 * or is not a directory.
 * 
 * @author sf
 * 
 */
public class InvalidTreeRootException extends Exception {

    /**
     * Serialization version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a specified description.
     */
    public InvalidTreeRootException() {
        super(
                "The specified tree root does not exist on file system or is not a directory.");
    }

}
