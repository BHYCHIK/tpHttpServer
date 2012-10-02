package highload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class HTTPWorker implements Runnable 
{
	
	private ServerSocket serverSocket;
	private HTTPParser parser;
	
	public HTTPWorker(ServerSocket serverSocket, HTTPParser parser)
	{
		this.serverSocket = serverSocket;
		this.parser = parser;
	}
	
	@Override
	public void run() 
	{
		try
		{
			Handle();
		} 
		catch (IOException e) 
		{
			
		}
	}
	
	private void Handle() throws IOException
	{
		Socket socket;
		InputStream inputStream;
		BufferedReader bufferedReader;
		StringBuilder requestBuilder;
		String nextLine;
		byte[] reply;
		OutputStream outputStream;
		
		while(!Thread.interrupted())
		{
			socket = serverSocket.accept();
			inputStream = socket.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			requestBuilder = new StringBuilder("");
			while(true)
			{
				nextLine = bufferedReader.readLine();
				if(nextLine == null || nextLine.equals(""))
				{
					break;
				}
				requestBuilder.append(nextLine+"\r\n");
			}
			reply = HTTPParser.getReply(requestBuilder.toString());
			outputStream = socket.getOutputStream();
			outputStream.write(reply);
			outputStream.close();
			bufferedReader.close();
			inputStream.close();
			socket.close();
		}
	}
}
