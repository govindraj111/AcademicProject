

import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.net.*;

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



class SearchForFile
{
	private String fileNameToSearch;
  	private List<String> result = new ArrayList<String>();

	private String kpath="";
	private String kname="";
	private String fname;
	private String fpath;
	private String keystr;
	private long startTime;
	private long stopTime;
	public final static int DEF_PORT=9;
	public final static int MAX_SIZE=65507;

	private String algo;
	private String path;
	private String rpath;
	private String rname;
	
	
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
	


	public String getFileNameToSearch() 
	{
		return fileNameToSearch;					//return the file name to search
  	}


  	public void setFileNameToSearch(String fileNameToSearch) 
	{									//setting the file name to search
		this.fileNameToSearch = fileNameToSearch;
  	}


  	public List<String> getResult() 
	{
		return result;							//for file search
  	}

	
	public String searchDirectory(File directory, String fileNameToSearch) 	 throws Exception
	{
		setFileNameToSearch(fileNameToSearch);				//setting the file name to search		
		if (directory.isDirectory()) 
		{
	    		search(directory);					//if path is directory then search file in directory
		} 
		else 
		{
	    		System.out.println(directory.getAbsoluteFile() + " is not a directory!");
		}
		return fpath;

  	}


  	private void search(File file) throws Exception
	{
		if (file.isDirectory()) 					//if directory
		{
	  		System.out.println("\n\nSearching Main Database" +file);

            								
	    		if (file.canRead()) 					//when file has permission to read
			{
				for (File temp : file.listFiles()) 
				{
		    			if (temp.isDirectory()) 
					{
						search(temp);			//again if subdirectory then search file in sub directory
		    			} 
					else
					{					//if file 
						String tempp = temp.getName();
						
								//System.out.println("\n"+temp);
								//System.out.println("\n"+tempp);
						
								String tempaddr=temp.getAbsolutePath();
								//System.out.println("\n"+temp);
								//System.out.println("\n"+tempp);
								Boolean found;
								found=matchcontent(tempaddr,keystr);
								if(found)
								{		
									fname=tempp;				
			    						result.add(temp.getAbsoluteFile().toString());		//requested file found in sender DB. add it to "result" list
		        						//fpath=temp.getAbsoluteFile().toString();		//setting path where file found
									//System.exit(0);
									
								}
								//System.out.println(found);
					}
	    			}
		 	} 
			else 
			{
				System.out.println(file.getAbsoluteFile() + "Permission Denied");			//when file dont hav permission to access
	 		}
		}

  	}

	public Boolean matchcontent(String faddr,String keytosearch)throws Exception 
	{
		//System.out.println("\n"+faddr);
		//System.out.println("\n"+keytosearch);
		InputStream inn = new FileInputStream(new File(faddr));
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inn));
        	StringBuilder out = new StringBuilder();
        	String line;
        	while ((line = reader.readLine()) != null) 		
        	{
          		out.append(line);
        	}
		//System.out.println(out.toString());   		//Prints the string content read from input stream
        	reader.close();
		String fcontent=out.toString();				//reading the content of key and storing it in string
		//System.out.println(fcontent);
		Boolean a=fcontent.contains(keytosearch);
		if(a)
		{
			fpath=faddr;
			return true;
		}
		else
		{
			//System.out.println("not found");
			return false;
		}
	}


	public void getkey(String addr)
	{														//search for key received
		File directory = new File(addr);
        	File[] fList = directory.listFiles();
        	for (File file : fList)
		{
			kname=file.getName();
            kpath=file.toString();
		//	Path path = Paths.get(kpath);	
		}
	}

	
/*	public static void sendToDir(String desloc, byte[] content) throws IOException 
	{
       	File fileee = new File(desloc);
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(fileee));			//sending key to main DB method
        writer.write(content);
        writer.flush();
        writer.close();
 	}

*/
	static void copy(String src) throws IOException 
	{
		try{       	

			InetAddress server=InetAddress.getByName("127.0.0.1");
			Socket soc = new Socket(server, 8023);	
	
			FileInputStream fis = new FileInputStream(src);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			
			fis.close();
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream()) ;
			oos.writeObject(buffer); 

			oos.close();
			soc.close();
		}
		catch(Exception e)
		{
			System.out.println("Error : "+e);
		}
  }


	public static void main(String[] args) throws Exception 
	{
		SearchForFile ob=new SearchForFile();
		ob.getkey("F:\\Received Key");					//find d key received and store in string
		//System.out.println(ob.kpath);
		System.out.println("\n\n\n\nKey Found :"+ob.kname);

		InputStream inn = new FileInputStream(new File(ob.kpath));
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inn));
        	StringBuilder out = new StringBuilder();
        	String line;
        	while ((line = reader.readLine()) != null) 		
		{
          		out.append(line);
        	}
		//System.out.println(out.toString());   		//Prints the string content read from input stream
        	reader.close();
		ob.keystr=out.toString();				//reading the content of key and storing it in string
		//System.out.println(ob.keystr);
		//System.out.println(ob.keystr.substring(0,14));
		//System.out.println(ob.keystr.substring(14,38));
		String pathstr=ob.keystr.substring(14,38);
		ob.keystr=ob.keystr.substring(0,14);
		//System.out.println(pathstr);
		//System.out.println(ob.keystr);
		
		

		
		
		String pathaddr="F:\\Received Key\\Newkey.txt.enc";
		writeByteArraysToFile( pathaddr, pathstr.getBytes());
		
		
		decode( pathaddr, pathaddr);
		
		ob.algo="DES/ECB/PKCS5Padding";
		ob.path=pathaddr;
		ob.decrypt();	
		
		String content = new String(Files.readAllBytes(Paths.get("F:\\Received Key\\Newkey.txt")));

	/*	String contents = new String(Files.readAllBytes(Paths.get("F:\\Received Key\\Newkey.txt")));
	*/	System.out.println("Contents (Java 7) : " + content);
		String ppp=""; 
		if(content.equals("apple.com"))
		{
			ppp="F:\\Database\\apple";
		}
		else if(content.equals("samsung.com"))
		{
			ppp="F:\\Database\\samsung";
		}
		else if(content.equals("nokia.com"))
		{
			ppp="F:\\Database\\nokia";
		}

		


	String addr="F:\\Received Key\\key.txt.enc";
	writeByteArraysToFile( addr, ob.keystr.getBytes());				//re writing "key" wch contains key to search in file in main DB

		
		
		
		System.out.println("\nEnter 1 to search for file :");
		Scanner in = new Scanner(System.in);
		int choice=in.nextInt();					//sending key to main DB
		if(choice==1)
		{								//F:\\Database
			try
			{
				ob.startTime = System.nanoTime();
				String path = ob.searchDirectory(new File(ppp),"");	//search the file in DB
				ob.stopTime = System.nanoTime();
				System.out.println("\n\n\nTime taken to search " +(double)(ob.stopTime - ob.startTime)/1000000000+" seconds");
			}				
			catch(Exception e)
			{
				System.out.println(e);
			}
			int count = ob.getResult().size();
			if(count == 0)
			{
	    			System.out.println("\nNo file found!\nKey not matching!");	//when file not in sender DB
				System.exit(0);
			}
			else
			{
				//System.out.println("\n\nFile : " +fsend +" Found at: " + path);			//print found
	    			System.out.println("\nFound " + count + " File!\nMatching Key :-)");			
	    			for (String matched : ob.getResult())
				{
					System.out.println("\n\nLocation : " + matched);
	    			}
				//System.out.println(ob.fpath);
			}
	
			
			System.out.println("\nEnter 1 to send the file to receiver :");
			int choice1=in.nextInt();
			in.close();
			if(choice1==1)
			{	
				//String recloc="D:\\receiver\\Received File\\";		//set the path where the key has to be sent in main DB
				//String fdesloc=recloc+ob.fname;
				copy(ob.fpath);
				
				System.out.println("\n\nFile sent Successfully!");
				System.out.println("\n\nThank You!");
			}
			else
			{
				System.out.println("Wrong input!");				
				System.exit(0);
			}

		}
		else
		{
			System.out.println("Wrong input!");				
			System.exit(0);
		}
	}
}	
	
	
	
	
		