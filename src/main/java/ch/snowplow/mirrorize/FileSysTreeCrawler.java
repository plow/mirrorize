package ch.snowplow.mirrorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileSysTreeCrawler {

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] createChecksum(String filename) throws IOException {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = null;
		try {
			complete = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO: use log4j
			System.err
					.println("The requested cryptographic algorithm is not available on this system.");
			e.printStackTrace();
		}
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				if (complete != null)
					complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	/**
	 * Obtains the MD5 checksum of a file and converts the the result to a hex
	 * string.
	 * 
	 * @param filename
	 *            Path to the file for which the chesum is to be computed
	 * @return Hex string of the MD5 checksum
	 * @throws IOException
	 *             In case something's went wrong while reading the file
	 */
	public static String getMD5Checksum(String filename) throws IOException {
		// obtain MD5 checksum of a file and store it in a byte array
		byte[] b = createChecksum(filename);
		String result = "";

		// Converts the byte array containing the MD5 checksum to a hex
		for (int i = 0; i < b.length; i++) {
			// TODO: don't do this conversion -> make a MD5 class -> toString()
			// returns hex string
			// TODO: use string buffer
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
		// return new String(b);
	}

	public static String getMD5OfString(String s) {
		byte[] bytesOfMessage;
		MessageDigest md;
		String result = "";
		try {
			bytesOfMessage = s.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(bytesOfMessage);

			// Converts the byte array containing the MD5 checksum to a hex
			for (int i = 0; i < b.length; i++) {
				// TODO: don't do this conversion -> make a MD5 class ->
				// toString()
				// returns hex string
				// TODO: use string buffer
				result += Integer.toString((b[i] & 0xff) + 0x100, 16)
						.substring(1);
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static void main(String args[]) {

		// Immediately exit if the two roots of the trees to analyze are not
		// provided.
		if (args.length != 2) {
			System.out
					.println("Usage: MD5Checksum /path/to/dir1 /path/to/dir2");
			System.exit(0);
		}

		// "Hello world!" -> 86fb269d190d2c85f6e0468ceca42a20

		try {

			/*
			 * - list files in both trees (depth first tree traversal) - hash
			 * all files in both trees - after hashing the last file of a dir:
			 * make directory hash of all file hashes and directory hashes of
			 * the directory
			 * 
			 * TODO: handle symbolic links properly in order to prevent
			 * inappropriate tree expansion TODO: automatic directory tree
			 * generator for testing (params: mean and std. deviation for files
			 * per folder, for folders per folder, and for filesizes, depth)
			 */

			DirHashMap<String> hashesTree1 = new DirHashMap<String>();
			DirHashMap<String> hashesTree2 = new DirHashMap<String>();

			System.out.println("------------------");
			System.out.println("Tree root 1: " + args[0]);
			File tree1Root = new File(args[0]);
			dirTraverse(tree1Root, 0, hashesTree1);
			System.out.println("------------------");
			System.out.println("Tree root 2: " + args[1]);
			File tree2Root = new File(args[1]);
			dirTraverse(tree2Root, 0, hashesTree2);
			System.out.println("------------------");
			System.out.println(hashesTree1.toString());
			System.out.println("------------------");
			System.out.println(hashesTree2.toString());
			System.out.println("------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void dirTraverse(File folder, int depth,
			DirHashMap<String> hashes) {

		File[] listOfFiles = folder.listFiles();
		DirHashMap<String> folderHashes = new DirHashMap<String>(); // TODO:
																	// provide
																	// size

		for (int i = 0; i < listOfFiles.length; i++) {

			File file = listOfFiles[i];

			if (file.isDirectory()) {
				// file is a directory
				System.out.println(getSpaces(depth * 2) + file.getName()
						+ "(D)");
				dirTraverse(file, depth + 1, hashes);
			}

			else if (file.isFile()) {
				// file is a file (not a directory)
				try {
					String fileHash = getMD5Checksum(file.getPath());
					folderHashes.add(fileHash, file.getPath());
					System.out.println(getSpaces(depth * 2) + file.getName()
							+ "  (MD5:" + fileHash + ")");
				} catch (IOException e) {
					System.err.println("The following I/O exception raised: "
							+ e.getMessage());
				}
			} else
				return; // TODO fix
		}

		String folderHash = getMD5OfString(folderHashes.getFolderHashes());
		folderHashes.add(folderHash, folder.getPath());
		hashes.addAll(folderHashes);
		System.out.println(getSpaces(depth * 2) + folder.getName()
				+ "  (folder MD5:" + folderHash + ")");

	}

	public static String getSpaces(int n) {
		return new String(new char[n]).replace('\0', ' ');
	}

}
