package ch.snowplow.mirrorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class FileSysTreeCrawler {

    private static Logger log = Logger.getLogger(MirrorizeMain.class);

    abstract class MD5Hasher {
        public abstract String getHash();

        protected String convertMD5ToHexStr(byte[] hash) {
            String result = "";
            // Converts the byte array containing the MD5 checksum to a hex
            for (int i = 0; i < hash.length; i++) {
                // TODO: don't do this conversion -> make a MD5 class ->
                // toString() returns hex string
                // TODO: use string buffer
                result += Integer.toString((hash[i] & 0xff) + 0x100, 16)
                        .substring(1);
            }
            return result;
        }
    }

    class FileMD5Hasher extends MD5Hasher {
        InputStream is;

        public FileMD5Hasher(String filePath) throws FileNotFoundException {
            this.is = new FileInputStream(filePath);
        }

        @Override
        public String getHash() {
            byte[] hash = null;
            try {
                hash = createChecksum(is);
            } catch (IOException e) {
                log.error("An I/O error occured while accessing a file.", e);
            }
            return convertMD5ToHexStr(hash);
        }

        public byte[] createChecksum(InputStream is) throws IOException {

            byte[] buffer = new byte[1024];
            int numRead;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    if (digest != null)
                        digest.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            is.close();
            return digest.digest();
        }

    }

    class StringMD5Hasher extends MD5Hasher {

        private String s;

        public StringMD5Hasher(String s) {
            this.s = s;
        }

        @Override
        public String getHash() {
            byte[] hash = null;
            try {
                hash = createChecksum(s);
            } catch (UnsupportedEncodingException e) {
                log.error(
                        "Unsupported encoding detected for the string to be hashed.",
                        e);
            }
            return convertMD5ToHexStr(hash);
        }

        public byte[] createChecksum(String s)
                throws UnsupportedEncodingException {
            byte[] bytesOfStr;
            bytesOfStr = s.getBytes("UTF-8");
            return digest.digest(bytesOfStr);
        }

    }

    private MessageDigest digest;
    private final File treeRoot;
    private DirHashMap<String> hashesTree;

    public FileSysTreeCrawler(File treeRoot, String hashAlgo) {
        this.treeRoot = treeRoot;
        this.hashesTree = new DirHashMap<String>();
        digest = null;
        try {
            digest = MessageDigest.getInstance(hashAlgo);
        } catch (NoSuchAlgorithmException e) {
            log.error("The requested cryptographic algorithm ('" + hashAlgo
                    + "') is not available on this system.", e);
        }
    }

    public DirHashMap<String> crawl() {
        dirTraverse(treeRoot, 0, hashesTree);
        return hashesTree;
    }

    private void dirTraverse(File folder, int depth, DirHashMap<String> hashes) {

        File[] listOfFiles = folder.listFiles();
        DirHashMap<String> folderHashes = new DirHashMap<String>(); // TODO:
                                                                    // provide
                                                                    // size

        for (int i = 0; i < listOfFiles.length; i++) {

            File file = listOfFiles[i];

            if (file.isDirectory()) {
                // file is a directory
                log.info(getSpaces(depth * 2) + file.getName() + "(D)");
                dirTraverse(file, depth + 1, hashes);
            }

            else if (file.isFile()) {
                // file is a file (not a directory)
                try {
                    String fileHash = (new FileMD5Hasher(file.getPath()))
                            .getHash();
                    folderHashes.add(fileHash, file.getPath());
                    log.info(getSpaces(depth * 2) + file.getName() + "  (MD5:"
                            + fileHash + ")");
                } catch (IOException e) {
                    log.error(
                            "An I/O exception has raised while hashing the file "
                                    + file.getPath() + ".", e);

                }
            } else {
                log.warn("There lives something in your file system that is neither file, nor folder.");
                return;
            }
        }

        String folderHash = (new StringMD5Hasher(folderHashes.getFolderHashes()))
                .getHash();
        folderHashes.add(folderHash, folder.getPath());
        hashes.addAll(folderHashes);
        log.info((getSpaces(depth * 2) + folder.getName() + "  (folder MD5:"
                + folderHash + ")"));

    }

    private static String getSpaces(int n) {
        return new String(new char[n]).replace('\0', ' ');
    }

}
