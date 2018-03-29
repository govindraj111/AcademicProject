

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import java.io.BufferedReader;
import org.apache.commons.codec.binary.Base64;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.*;
import java.io.*;
import java.net.*;

class SendKeyToDB
{
	private String kpath="";
	private String kname="";
	
	public final static int DEF_PORT=9;
	public final static int MAX_SIZE=65507;


	public void getkey(String addr)
	{														//search for key received
		File directory = new File(addr);
        File[] fList = directory.listFiles();
        for (File file : fList)
        {
        	kname=file.getName();
           	kpath=file.toString();
           //	System.out.println(kname);
           	//System.out.println(kpath);
          // 	Path Path = Paths.get(kpath);	
        }
	}
	
	
	public static void sendToDir(String fileName, byte[] content)
	{
		try
		{       	
			InetAddress server=InetAddress.getByName("127.0.0.1");
			Socket soc = new Socket(server, 8022);	
	
			FileInputStream fis = new FileInputStream("D:\\receiver\\Key\\Key.txt.enc");
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
        /*	File file = new File(fileName);
        	BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));			//sending key to main DB method
        	writer.write(content);
        	writer.flush();
        	writer.close();
        */
 	}

	
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{  
		SendKeyToDB ob=new SendKeyToDB();
		ob.getkey("D:\\receiver\\Key");					//find d key received and display
		//System.out.println(ob.kpath);
		System.out.println("\n\n\n\nKey Found :"+ob.kname);

	
		
			System.out.println("\nEnter 1 to send key to DB and Fetch file :");
			Scanner in = new Scanner(System.in);
			int choice=in.nextInt();					//sending key to main DB
			if(choice==1)
			{
				InputStream inn = new FileInputStream(new File(ob.kpath));
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inn));
        		StringBuilder out = new StringBuilder();
        		String line;						//reading the content of file and storing it in string
        		while ((line = reader.readLine()) != null) 		
        		{
            			out.append(line);
        		}
			
        		//System.out.println(out.toString());   		//Prints the string content read from input stream
        		reader.close();
        		in.close();
        		String temp=out.toString();				//reading the content of key and storing it in string
        		//System.out.println(temp);
	
        		byte[] keybytes = temp.getBytes();
        		String DBpath="D:\\receiver\\Key\\Key.txt.enc";		//set the path where the key has to be sent in main DB
        		sendToDir(DBpath, keybytes);
        		System.out.println("\nKey sent Successfully!");
			}
			else
			{
				System.out.println("Wrong input!");				
				System.exit(0);
			}		
    
	}	
}