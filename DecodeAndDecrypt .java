

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;
import java.nio.file.Paths;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import org.apache.commons.codec.binary.Base64;
import java.nio.file.*;



public class DecodeAndDecrypt 
{
	private String algo;
	private String path;
	private String rpath;
	private String rname;
	
	
	/*public  void sett(String algo,String path) 
	{
     		this.algo = algo; //setting algo
     		this.path = path;//setting file path
    }*/
	
	
	public void decrypt() throws Exception
	{
	        //generating same key
		byte k[] = "HignDlPs".getBytes();   
         	SecretKeySpec key = new SecretKeySpec(k,algo.split("/")[0]);  
         	//creating and initialising cipher and cipher streams
         	Cipher decrypt =  Cipher.getInstance(algo);  
         	decrypt.init(Cipher.DECRYPT_MODE, key);
         	//opening streams
         	FileInputStream fis = new FileInputStream(path);
         	try(CipherInputStream cin=new CipherInputStream(fis, decrypt))
		{  
            		try(FileOutputStream fos =new FileOutputStream(path.substring(0,path.lastIndexOf("."))))
			{
               			copy(cin,fos);
           		}
         	}
      	}
      
  	private void copy(InputStream is,OutputStream os) throws Exception
	{
	     	byte buf[] = new byte[4096];  //4K buffer set
	     	int read = 0;
		while((read = is.read(buf)) != -1)  //reading
   	     	os.write(buf,0,read);  //writing
  	}
  	
  	
	public void getkey(String addr)
	{
		File directory = new File(addr);
        File[] fList = directory.listFiles();
        for (File file : fList)
		{
			rname=file.getName();
           	rpath=file.toString();
			Path path = Paths.get(rpath);	
		}
	}
	
	
	public static void decode(String sourceFile, String targetFile) throws Exception 
	{
 
        	byte[] decodedBytes = Base64.decodeBase64(loadFileAsBytesArray(sourceFile));
        	writeByteArraysToFile(targetFile, decodedBytes);
    }
	
	
	public static byte[] loadFileAsBytesArray(String fileName) throws Exception 
	{
 
        	File file = new File(fileName);
        	int length = (int) file.length();
        	BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        	byte[] bytes = new byte[length];
        	reader.read(bytes, 0, length);
        	reader.close();
        	return bytes;
	}
	
	
	public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException 
	{
 
        	File file = new File(fileName);
        	BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        	writer.write(content);
        	writer.flush();
        	writer.close();
 	}
	

	public static void main(String[] args) throws Exception 
	{
		DecodeAndDecrypt ob=new DecodeAndDecrypt();
		ob.getkey("D:\\receiver\\Received File");
		int len=ob.rpath.length();
		if(len>0)
		{
			System.out.println("\n\n\n\nFile Found :"+ob.rname);
			//System.out.println("\n\n\n\nFile Found :"+ob.rpath);
			
			System.out.println("\n\n\n\nEnter 1 to Decode the file:");
			Scanner in=new Scanner(System.in);
			int choice=in.nextInt();
			if(choice==1)
			{
				decode(ob.rpath,ob.rpath);
				System.out.println("\nFile Decoded!");	
			}
			else
			{
				System.out.println("Wrong input!");				
				System.exit(0);
			}
			
			System.out.println("\n\n\n\nEnter 1 to Decrypt the file:");
			int choice1=in.nextInt();
			if(choice1==1)
			{
				ob.algo="DES/ECB/PKCS5Padding";
				ob.path=ob.rpath;
				ob.decrypt();	
				System.out.println("\nFile Decrtpted! \n\nYou would find the file in Received file folder!");	
			}
			else
			{
				System.out.println("Wrong input!");				
				System.exit(0);
			}
			in.close();
			System.out.println("Thank You!");
		}
		else
		{
			System.out.println("\n\n\n\nFile not Found!");
			System.exit(0);
		}
		
	}
	
}
