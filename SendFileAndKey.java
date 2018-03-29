

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.net.*;


class SendFileAndKey
{
	private String algo;
	private String path;
	
	public final static int DEF_PORT=9;
	public final static int MAX_SIZE=65507;

	private String fileNameToSearch;
  	private List<String> result = new ArrayList<String>();
	private String fpath;							//for file search
	
	private static final boolean IS_CHUNKED = true;
	
	public void sett(String algo,String path) 
    	{
     		this.algo = algo; //setting algo				//setting algorithm and path for encryption
     		this.path = path;//setting file path
    	}
	
	
	public void encrypt() throws Exception
	{
    			     //generating key
         	byte k[] = "HignDlPs".getBytes();   
         	SecretKeySpec key = new SecretKeySpec(k,algo.split("/")[0]);  
         	//creating and initialising cipher and cipher streams
         	Cipher encrypt =  Cipher.getInstance(algo);  			//encrypting using DES
         	encrypt.init(Cipher.ENCRYPT_MODE, key);
         	//opening streams
         	FileOutputStream fos =new FileOutputStream(path+".enc");
         	try(FileInputStream fis =new FileInputStream(path))
         	{
            		try(CipherOutputStream cout=new CipherOutputStream(fos, encrypt))
            		{
                		copy(fis,cout);
            		}
         	}
        }
	
	
	private void copy(InputStream is,OutputStream os) throws Exception
	{
     		byte buf[] = new byte[4096];  //4K buffer set
     		int read = 0;
     		while((read = is.read(buf)) != -1)  //reading			//writing encrypted text to file
        	os.write(buf,0,read);  //writing
  	}

	
	private static String encode(String sourceFile, String targetFile, boolean isChunked, String pt) throws Exception 
	{
 		String tmpstr="";
		//String a="";

        	byte[] base64EncodedData = Base64.encodeBase64(loadFileAsBytesArray(sourceFile), isChunked);		//encoding encrypted text
        	tmpstr = writeByteArraysToFile(targetFile, base64EncodedData);
        	//a= writeByteArraysToFile(pt, base64EncodedData);
        	
        	// // // // // // //
        	try{       	

    			InetAddress server=InetAddress.getByName("127.0.0.1");
    			Socket soc = new Socket(server, 8020);	
    	
    			FileInputStream fis = new FileInputStream(targetFile);
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
		
        	
        	// // // // // //
        	
    		return tmpstr; 
	}

	
	public static byte[] loadFileAsBytesArray(String fileName) throws Exception 
	{ 
        	File file = new File(fileName);
        	int length = (int) file.length();
        	BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));			//reading the content of file and converting it into bytes
        	byte[] bytes = new byte[length];
        	reader.read(bytes, 0, length);
        	reader.close();
        	return bytes;
    	}
	
	
	public static String writeByteArraysToFile(String fileName, byte[] content) throws IOException 
	{
		
		
       	File file = new File(fileName);
       	BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));		
       	writer.write(content);										//writing encoded text to file
       	writer.flush();
       	writer.close(); 
        String encdstr=new String(content); 
		return encdstr; 
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

	
	public String searchDirectory(File directory, String fileNameToSearch) 	
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

  	private void search(File file) 
	{
		if (file.isDirectory()) 					//if directory
		{
	  		System.out.println("\n\nSearching Sender Database");

            								
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
						if ((tempp.substring(tempp.lastIndexOf('.') + 1, tempp.length()).toLowerCase()).equals("txt"))		//checking for .txt file
						{
							if (getFileNameToSearch().equals(temp.getName()))		//checking filename matching or not
							{
			    					result.add(temp.getAbsoluteFile().toString());		//requested file found in sender DB. add it to "result" list
		        					fpath=temp.getAbsoluteFile().toString();		//setting path where file found
							}
						}

					}
	    			}
		 	} 
			else 
			{
				System.out.println(file.getAbsoluteFile() + "Permission Denied");			//when file dont hav permission to access
	 		}
		}

  	}
	
	
	public static void SendKeyToRecv(String pth) throws IOException 
	{
		try{       	

			InetAddress sserver=InetAddress.getByName("127.0.0.1");
			Socket ssoc = new Socket(sserver, 8021);	
	
			FileInputStream ffis = new FileInputStream(pth);
			byte[] buffer = new byte[ffis.available()];
			ffis.read(buffer);
			
			ffis.close();
			ObjectOutputStream ooos = new ObjectOutputStream(ssoc.getOutputStream()) ;
			ooos.writeObject(buffer); 
			ooos.close();
			ssoc.close();
		}
		catch(Exception e)
		{
			System.out.println("Error : "+e);
		}
		
		/*
        	File file = new File(fileName);
        	BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));			//sending key to receiver method
        	writer.write(content);
        	writer.flush();
        	writer.close(); 
        */
 	}

	public static void writekeyfile(String Filname, byte[] ccontent) throws Exception
	{
		File file = new File(Filname);
    	BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));			//sending key to receiver method
    	writer.write(ccontent);
    	writer.flush();
    	writer.close();
	}
	
	public static void main(String[] args) throws Exception
    { 				  
		String apple="P/VDjKCd5sAjgUBtXI60BQ==";
		String samsung="VGvG5iSJdhYn1N6gmmeTGA==";
		String nokia="Ea03QzUW/94jgUBtXI60BQ==";
		
		//System.out.println("\napple :"+apple.length());
		//System.out.println("\nsamsung :"+samsung.length());
		//System.out.println("\nnokia:"+nokia.length());
		
		
		String aa="abcdefghijk"+apple;
		//System.out.println(aa);
		//System.out.println(aa.length());
		//System.out.println(apple.equals(aa.substring(11,35)));
		
		
		System.out.println("\nEnter the File name to send :");				//read file name to send to DB
		Scanner in = new Scanner(System.in);
		String fsend = in.nextLine();
		
		
		System.out.println("\n1.Apple 2.Samsung 3.Nokia \n\n" +fsend +" belong to ?");				//read apple samsung or nokia
		int fbelong = in.nextInt();
		String add="";
		String fb="";
		
		if(fbelong==1)
		{
			add=apple;
			fb="apple";
		}
		else if(fbelong==2)
		{
			add=samsung;
			fb="samsung";
		}
		else if(fbelong==3)
		{
			add=nokia;
			fb="nokia";
		}
		else
		{
			System.out.println("\nERROR Invalid Input!");
			System.exit(0);
		}
		
		
		try{      
			Socket s=new Socket("localhost",6666);  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			dout.writeUTF(fb);  
			dout.flush();  
			dout.close();  
			//s.close();  
			}catch(Exception e){System.out.println(e);}  

		
		
		
		
		
		
		SendFileAndKey fSearchDB = new SendFileAndKey();
		String path = fSearchDB.searchDirectory(new File("C:\\sender\\Sender Database"),fsend);	//search the file in DB
		int count = fSearchDB.getResult().size();
		if(count == 0)
		{
	    		System.out.println("\nRequested File : " +fsend +" not found!");	//when file not in sender DB
			System.exit(0);
		}
		else
		{
			System.out.println("\nFile : " +fsend +" Found at: " + path);			//print found
	    		//System.out.println("\nFound " + count + " result!\n");			
	    		//for (String matched : fSearchDB.getResult())
			//{
			//	System.out.println("Found : " + matched);
	    		//}
		}
		

		System.out.println("\n\nEnter 1 to Encrypt the file :");			//encrypt the file
		int choice=in.nextInt();
		
		if(choice==1)
		{
			fSearchDB.sett("DES/ECB/PKCS5Padding",path);
			fSearchDB.encrypt();
			System.out.println("File Encrypted!");
		}
		else
		{
			System.out.println("Wrong input!");				
			System.exit(0);
		}
			
		String eyfile=fsend+".enc";
		
		String eyfilepath=path+".enc";						//get the path of encrypted file
		
		System.out.println("\n\nEnter 1 to Encode the file and send to Database:");			//encode the file
		int choice1=in.nextInt();
		
		String EncodedTxt="";
		if(choice1==1)
		{
			String DB="F:\\Database\\";					//set the path of Main DB to send the filr 
			String DBpath=DB+eyfile;
			
			EncodedTxt=encode(eyfilepath,eyfilepath, IS_CHUNKED, DBpath);
			System.out.println("File Encoded! and sent to Database \n");
		}
		else
		{
			System.out.println("Wrong input!");				
			System.exit(0);
		}
		
		System.out.println("\nEnter 1 to generate key and send to Receiver :");			//generating key
		int choice2=in.nextInt();
		
		if(choice2==1)
		{
			String key = EncodedTxt.substring(0,14);
			key=key+add;	 
			String recvpath="C:\\sender\\Sender Database\\Key.txt.enc";			//key path in DB
			byte[] keybytes = key.getBytes();
			writekeyfile(recvpath, keybytes);									//writing key to a file
			SendKeyToRecv(recvpath);
			System.out.println("\nKey generated! and sent to Receiver\n");
		}
		else
		{
			System.out.println("Wrong input!");				
			System.exit(0);
		}
		in.close();
		System.out.println("\nThankyou\n");
						
	}
}
