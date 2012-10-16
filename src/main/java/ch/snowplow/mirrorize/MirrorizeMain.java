package ch.snowplow.mirrorize;

import java.io.File;

public class MirrorizeMain {

	private static final String CRYPTO_ALGO = "MD5";

	public static void main(String args[]) {

		// Immediately exit if the two roots of the trees to analyze are not
		// provided.
		if (args.length != 2) {
			System.out
					.println("Usage: MD5Checksum /path/to/dir1 /path/to/dir2");
			System.exit(0);
		}

		// Instantiate tree crawlers for the two directories to be compared
		File tree1Root = new File(args[0]);
		FileSysTreeCrawler tree1Crawler = new FileSysTreeCrawler(tree1Root,
				CRYPTO_ALGO);

		File tree2Root = new File(args[1]);
		FileSysTreeCrawler tree2Crawler = new FileSysTreeCrawler(tree2Root,
				CRYPTO_ALGO);

		// Print the results of the tree crawlers
		System.out.println("------------------");
		System.out.println("Tree root 1: " + args[0]);
		DirHashMap<String> hashesTree1 = tree1Crawler.crawl();
		System.out.println("------------------");
		System.out.println("Tree root 2: " + args[1]);
		DirHashMap<String> hashesTree2 = tree2Crawler.crawl();
		System.out.println("------------------");
		System.out.println(hashesTree1.toString());
		System.out.println("------------------");
		System.out.println(hashesTree2.toString());
		System.out.println("------------------");

	}

}
