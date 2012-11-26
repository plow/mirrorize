package ch.snowplow.mirrorize.gathering;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import ch.snowplow.mirrorize.common.DirHashMap;
import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.common.Tools;

/**
 * A crawler which is able to recursively traverse a file system tree. When
 * traversing, a mapping between all files/sub-directories and their hashes is
 * established. All hash algorithms supported by
 * {@link java.security.MessageDigest} are also supported by this class.
 * 
 * @author sf
 */
public class FileSysTreeCrawler {

    private static Logger log = Logger.getLogger(FileSysTreeCrawler.class);

    /**
     * Abstract class intended for computing a MD5 hash on a particular entity
     * of data (e.g. on strings or on files).
     * 
     * @author sf
     */
    abstract class MD5Hasher {

        /**
         * Returns the MD5 hash of the entity of data a concrete subclass
         * represents.
         * 
         * @return Hex-string representation of the MD5 hash
         */
        public abstract String getHash();

        /**
         * 
         * @param hash
         * @return
         */
        protected String convertMD5ToHexStr(byte[] hash) {
            final int byteMax = 0xff;
            final int twoPow8 = 0x100;
            final int hexBase = 16;
            StringBuffer result = new StringBuffer();
            // Converts the byte array containing the MD5 checksum to a hex
            for (int i = 0; i < hash.length; i++) {
                // TODO: don't do this conversion -> make a MD5 class ->
                // toString() returns hex string
                result.append(Integer.toString((hash[i] & byteMax) + twoPow8,
                        hexBase).substring(1));
            }
            return result.toString();
        }
    }

    /**
     * Inner class used to obtain a MD5 hash of a file.
     * 
     * @author sf
     */
    class FileMD5Hasher extends MD5Hasher {
        private InputStream is;

        /**
         * Creates an instance, given the path of the file to be hashed.
         * 
         * @param filePath
         *            Path of the file to be hashed.
         * @throws FileNotFoundException
         */
        public FileMD5Hasher(String filePath) throws FileNotFoundException {
            this.is = new FileInputStream(filePath);
        }

        /**
         * Returns the MD5 hash of the string, encoded as hex-string.
         */
        @Override
        public String getHash() {
            byte[] hash = null;
            try {
                hash = createHash();
            } catch (IOException e) {
                log.error("An I/O error occured while accessing a file.", e);
            }
            return convertMD5ToHexStr(hash);
        }

        /**
         * Computes the MD5 hash of the file by reading from the respective file
         * input stream. The file contents are read in pieces of 1KB.
         * 
         * @return MD5 hash of the file
         * @throws IOException
         */
        public byte[] createHash() throws IOException {

            final int len = 1024;
            byte[] buffer = new byte[len];
            int numRead;
            do {
                numRead = is.read(buffer);
                if (numRead > 0 && digest != null) {
                    digest.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            is.close();
            return digest.digest();
        }

    }

    /**
     * Inner class used to obtain a MD5 hash of a string.
     * 
     * @author sf
     * 
     */
    class StringMD5Hasher extends MD5Hasher {

        private String s;

        /**
         * Creates an instance, given a particular string.
         * 
         * @param s
         *            String which can be hashed in this object.
         */
        public StringMD5Hasher(String s) {
            this.s = s;
        }

        /**
         * Returns the MD5 hash of the file, encoded as hex-string.
         */
        @Override
        public String getHash() {
            byte[] hash = null;
            try {
                hash = createHash();
            } catch (UnsupportedEncodingException e) {
                log.error(
                        "Unsupported encoding detected for the string to be hashed.",
                        e);
            }
            return convertMD5ToHexStr(hash);
        }

        /**
         * Computes the MD5 hash of the string by converting it into a sequence
         * of bytes. The hash is computed over this byte array. The string is
         * assumed to be UTF-8 encoded during the conversion into the byte
         * array.
         * 
         * @return MD5 hash of the file
         * @throws UnsupportedEncodingException
         */
        public byte[] createHash() throws UnsupportedEncodingException {
            byte[] bytesOfStr;
            bytesOfStr = s.getBytes("UTF-8");
            return digest.digest(bytesOfStr);
        }

    }

    private MessageDigest digest;
    private final File treeRoot;
    private final int treeRootPrefixLen;
    private DirHashMap<String> hashStore;

    /**
     * Creates a tree crawler, given a root directory to be crawled and a hash
     * algorithm.
     * 
     * @param treeRoot
     *            Root directory to be crawled. All elements under the root
     *            directory are to be hashed.
     * @param hashAlgo
     *            Name of the hash algorithm used for hashing. The supported
     *            algorithm names are those supported by the
     *            {@link java.security.MessageDigest} class.
     * @throws InvalidTreeRootException
     * @throws NoSuchAlgorithmException
     */
    public FileSysTreeCrawler(File treeRoot, String hashAlgo)
            throws InvalidTreeRootException, NoSuchAlgorithmException {
        // TODO there's only MD5 supported by now, so forget hashAlgo
        this.treeRoot = treeRoot;
        this.treeRootPrefixLen = treeRoot.getPath().length() + 1;
        this.hashStore = new DirHashMap<String>();
        digest = MessageDigest.getInstance(hashAlgo);
        if (treeRoot == null | !treeRoot.isDirectory()) {
            throw new InvalidTreeRootException();
        }
    }

    /**
     * Crawls recursively through the directory given while constructing this
     * object, i.e., a mapping between all files/sub-directories and their
     * hashes is established.
     * 
     * @return The hash store containing the mapping between all
     *         files/sub-directories and their hashes.
     */
    public DirHashMap<String> crawl() {
        dirTraverse(treeRoot, 0, hashStore);
        return hashStore;
    }

    /**
     * This method is used to recursively traverse a file system tree using DFS
     * strategy. On a given depth, files are hashed and folders initiate a new
     * recursion step. Once all files in a directory on depth n are hashed, the
     * hashes are serialized into a string which is hashed again to obtain a
     * hash representing the whole directory. While backtracking, this directory
     * hash is treated like a normal file hash on depth n-1.
     * 
     * @param folder
     *            The directory for which the containing files and
     *            sub-directories are to be hashed.
     * @param depth
     *            The current recursion depth, starting with depth=0 on the root
     *            folder.
     * @param hashStore
     *            Reference to the hash store which saves all hashes for all
     *            files/directories under the root element.
     * @return Directory hash of the current directory.
     */
    private FileHash<String> dirTraverse(File folder, int depth,
            DirHashMap<String> hashStore) {

        File[] listOfFiles = folder.listFiles();
        DirHashMap<String> dirHashes = new DirHashMap<String>();
        DirHashMap<String> dirHashesCurrDepth = new DirHashMap<String>(
                listOfFiles.length);

        for (int i = 0; i < listOfFiles.length; i++) {
            // iterate over all elements in the given folder

            File file = listOfFiles[i];

            if (file.isDirectory()) {
                // file is a directory
                log.info(Tools.getSpaces(depth * 2) + file.getName() + "(dir)");
                dirHashesCurrDepth.add(dirTraverse(file, depth + 1, dirHashes));
            }

            else if (file.isFile()) {
                // file is a file (not a directory)
                try {
                    String fileHash = new FileMD5Hasher(file.getPath())
                            .getHash();
                    dirHashes.add(fileHash,
                            new Path(file.getPath()
                                    .substring(treeRootPrefixLen)));
                    dirHashesCurrDepth.add(fileHash, new Path(file.getPath()
                            .substring(treeRootPrefixLen)));
                    log.info(Tools.getSpaces(depth * 2) + file.getName()
                            + "  (MD5:" + fileHash + ")");
                } catch (IOException e) {
                    log.error(
                            "An I/O exception has raised while hashing the file "
                                    + file.getPath() + ".", e);

                }
            } else {
                log.warn("There lives something in your file system that is neither file, nor folder.");
            }
        }

        // after all files are hashed: obtain a folder hash
        // TODO make an MD5Hasher subclass for creating directory hashes
        String folderHash = (new StringMD5Hasher(
                dirHashesCurrDepth.getSerializedHashes())).getHash();

        FileHash<String> folderFileHash = new FileHash<String>(
                new Path(depth == 0 ? "" : folder.getPath().substring(
                        treeRootPrefixLen)), folderHash);
        dirHashes.add(folderFileHash);

        // add all hashes of the directory to the hash store
        hashStore.addAll(dirHashes);
        log.info((Tools.getSpaces(depth * 2) + folder.getName()
                + "  (folder MD5:" + folderHash + ")"));

        return folderFileHash;
    }

}
