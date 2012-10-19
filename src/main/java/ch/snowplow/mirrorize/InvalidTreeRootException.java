package ch.snowplow.mirrorize;

public class InvalidTreeRootException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidTreeRootException() {
        super(
                "The specified tree root does not exist on file system or is not a directory.");
    }

}
