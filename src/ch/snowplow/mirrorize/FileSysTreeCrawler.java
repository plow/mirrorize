package ch.snowplow.mirrorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

		// Converts the byte array containing the MD5 checksum to a hex string.
		for (int i = 0; i < b.length; i++) {
			// TODO: don't do this conversion -> make a MD5 class -> toString()
			// returns hex string
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
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

			System.out.println("------------------");
			System.out.println("Tree root 1: " + args[0]);
			File tree1Root = new File(args[0]);
			dirTraverse(tree1Root, 0);
			System.out.println("------------------");
			System.out.println("Tree root 2: " + args[1]);
			File tree2Root = new File(args[1]);
			dirTraverse(tree2Root, 0);
			System.out.println("------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void dirTraverse(File folder, int depth) {
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isDirectory()) {
				// file is a directory
				System.out.println(getSpaces(depth * 2)
						+ listOfFiles[i].getName() + "(D)");
				dirTraverse(listOfFiles[i], depth + 1);
			} else if (listOfFiles[i].isFile()) {
				// file is a file (not a directory)
				try {
					System.out.println(getSpaces(depth * 2)
							+ listOfFiles[i].getName() + "  (MD5:"
							+ getMD5Checksum(listOfFiles[i].getPath()) + ")");
				} catch (IOException e) {
					System.err.println("The following I/O exception raised: "
							+ e.getMessage());
				}
			} else
				return; // TODO fix
		}

	}

	public static String getSpaces(int n) {
		return new String(new char[n]).replace('\0', ' ');
	}

}
