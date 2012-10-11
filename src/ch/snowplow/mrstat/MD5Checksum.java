package ch.snowplow.mrstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Checksum {

	
	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
   }

   // see this How-to for a faster way to convert
   // a byte array to a HEX string
   public static String getMD5Checksum(String filename) throws Exception {
       byte[] b = createChecksum(filename);
       String result = "";

       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring(1);
       }
       return result;
   }
   
   public static String dirTraverse(File folder, int depth) {
	   File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				return depth + " File " + listOfFiles[i].getName();
			} 
			else if (listOfFiles[i].isDirectory()) {
				return depth + " Directory " + listOfFiles[i].getName();
			}
		}
		return "error"; // TODO fix
   }

   public static void main(String args[]) {
	   
	   if(args.length != 2) {
		   System.out.println("Usage: MD5Checksum /path/to/dir1 /path/to/dir2");
		   System.exit(0);
	   }
	   
       try {
    	   
    	   /*
    	    * - list files in both trees (depth first tree traversal)
    	    * - hash all files in both trees
    	    * - after hashing the last file of a dir: make directory hash of all file hashes and directory hashes of the directory
    	    * 
    	    * TODO: handle symbolic links properly in order to prevent inappropriate tree expansion
    	    * TODO: automatic directory tree generator for testing (params: mean and std. deviation for files per folder, for folders per folder, and for filesizes, depth)
    	    * 
    	    */
    	   
    	   
    	   System.out.println("------------------");
    	   System.out.println("Tree root 1: " + args[0]);
    	   System.out.println("Tree root 2: " + args[1]);
    	   
			File tree1Root = new File(args[0]);
			File tree2Root = new File(args[1]);
			
			System.out.println(dirTraverse(tree1Root, 0));
			System.out.println(dirTraverse(tree2Root, 0));
			
			System.out.println("------------------");	
    	   
    	   
    	   
    	   for(int i=0; i<10000; i++) {
    		   if(i%1000==0)
    			   System.out.println("MD5: " + getMD5Checksum("resources/tree1/file1.txt"));
    		   else
    			   getMD5Checksum("resources/tree1/file1.txt");
    			   
    	   }
           // output :
           //  0bb2827c5eacf570b6064e24e0e6653b
           // ref :
           //  http://www.apache.org/dist/
           //          tomcat/tomcat-5/v5.5.17/bin
           //              /apache-tomcat-5.5.17.exe.MD5
           //  0bb2827c5eacf570b6064e24e0e6653b *apache-tomcat-5.5.17.exe
       }
       catch (Exception e) {
    	   e.printStackTrace();
       }
   }
   
}

