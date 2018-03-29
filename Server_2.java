


import java.io.*;
import java.net.*;

public class Server_2 
{
	
		public final static int DEF_PORT=9;
		public final static int MAX_SIZE=65507;
			
		public static void main(String args[]) 
		{
			byte[] buffer=new byte[1000000];
			byte[] buff=new byte[1000000];
			
			try
			{
				//sending encoded file to Manin DB
						
				ServerSocket ser = new ServerSocket(8022);    //use diff port number to send diff files to specified loication
				Socket clientSocket = ser.accept();

				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				buffer = (byte[])ois.readObject();
				FileOutputStream fos = new FileOutputStream("F:\\Received Key\\Key.txt.enc");
				fos.write(buffer);
				fos.close();
				ois.close();
				ser.close();
				
				
				ServerSocket sser = new ServerSocket(8023);    //use diff port number to send diff files to specified location
				Socket cclientSocket = sser.accept();

				ObjectInputStream oois = new ObjectInputStream(cclientSocket.getInputStream());
				buff = (byte[])oois.readObject();
				FileOutputStream ffos = new FileOutputStream("D:\\receiver\\Received File\\File.txt.enc");
				ffos.write(buff);
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
