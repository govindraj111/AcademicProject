

import java.io.*;
import java.net.*;



public class Server_1 
{
	public final static int DEF_PORT=9;
	public final static int MAX_SIZE=65507;
	private String path;	
	
	public static void main(String args[]) 
	{
		byte[] buffer=new byte[1000000];
		byte[] bbuffer=new byte[1000000];
		byte[] buff=new byte[1000000];
		
		Server_1 obj=new Server_1();
		
		try{  
			ServerSocket ss=new ServerSocket(6666);  
			Socket s=ss.accept();//establishes connection   
			DataInputStream dis=new DataInputStream(s.getInputStream());  
			String  str=(String)dis.readUTF();  
			if(str.equals("apple"))
			{
				obj.path="F:\\Database\\apple\\File.txt.enc";
			}
			else if(str.equals("samsung"))
			{
				obj.path="F:\\Database\\samsung\\File.txt.enc";
			}
			else if(str.equals("nokia"))
			{
				obj.path="F:\\Database\\nokia\\File.txt.enc";
			}
			
			System.out.println("message= "+str +"\n"+obj.path);  
			ss.close();  
			}catch(Exception e){System.out.println("Excep"+e);}  
		
		
		try
		{
			//sending encoded file to Main DB
					
			ServerSocket ser = new ServerSocket(8020);    //use diff port number to send diff files to specified location
			Socket clientSocket = ser.accept();

			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			buffer = (byte[])ois.readObject();
			FileOutputStream fos = new FileOutputStream(obj.path);
			fos.write(buffer);
			fos.close();
			ois.close();
			ser.close();

			//sending key to Receiver
			
			ServerSocket sser = new ServerSocket(8021);    //use diff port number to send diff files to specified location
			Socket cclientSocket = sser.accept();

			ObjectInputStream oois = new ObjectInputStream(cclientSocket.getInputStream());
			bbuffer = (byte[])oois.readObject();
			FileOutputStream ffos = new FileOutputStream("D:\\receiver\\Key\\Key.txt.enc");
			ffos.write(bbuffer);
			ffos.close();				
			oois.close();
			sser.close();
			
			
		} 
			
		catch (Exception e)
		{
			e.printStackTrace();
		 }
		
	}
       
     
}
