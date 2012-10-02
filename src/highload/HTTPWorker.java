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
	private final int maxIters = 100;
	
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
		byte[] reply;
		OutputStream outputStream;
		int iters;
		
		while(true)
		{
			socket = serverSocket.accept();
			inputStream = socket.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			requestBuilder = new StringBuilder("");
			char[] buf = new char[1500];
			try
			{
				iters = 0;
				while(requestBuilder.indexOf("\r\n\r\n") == -1)
				{
					
					bufferedReader.read(buf);
					requestBuilder.append(new String(buf));
					if(iters == maxIters)
					{
						throw (new IOException());
					}
					++iters;
				}
				String request = requestBuilder.toString();
				reply = parser.getReply(request);
				outputStream = socket.getOutputStream();
				outputStream.write(reply);
				byte[] content = parser.getContent(request);
				if(content.length > 0)
				{
					outputStream.write(content);
				}
				outputStream.close();		
			}
			finally
			{
				bufferedReader.close();
				inputStream.close();
				socket.close();
			}
		}
	}
}
