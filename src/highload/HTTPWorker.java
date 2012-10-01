package highload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public final class HTTPWorker implements Runnable 
{
	
	private ServerSocket serverSocket;
	
	public HTTPWorker(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
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
		int emptyStr;
		StringBuilder requestBuilder;
		String nextLine;
		
		while(true)
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
				requestBuilder.append(nextLine+"\n");
			}
			HTTPParser.getReply(requestBuilder.toString());
			bufferedReader.close();
			inputStream.close();
			socket.close();
		}
	}
}
