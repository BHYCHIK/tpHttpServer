package highload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.jar.JarEntry;

public final class HTTPWorker implements Runnable 
{
	private final int defaultTimeout = 15000;
	
	private ServerSocket serverSocket;
	private HTTPParser parser;
	private int timeout;
	
	public HTTPWorker(ServerSocket serverSocket, HTTPParser parser)
	{
		this.serverSocket = serverSocket;
		this.parser = parser;
		this.timeout = defaultTimeout;
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
		int CRLFPos;
		
		while(true)
		{
			socket = serverSocket.accept();
			socket.setSoTimeout(timeout);
			inputStream = socket.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			requestBuilder = new StringBuilder("");
			outputStream = null;
			char[] buf = new char[1500];
			int readed;
			try
			{
				CRLFPos = 0;
				while(requestBuilder.indexOf("\r\n\r\n", CRLFPos) == -1)
				{
					if(bufferedReader.ready())
					{
						readed = bufferedReader.read(buf);
						requestBuilder.append(new String(buf, 0, readed));
						if((CRLFPos = requestBuilder.length() - 4) < 0)
						{
							CRLFPos = 0;
						}
					}
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
			catch(SocketTimeoutException exception)
			{
				System.out.print("TIMEOUT");
			}
			finally
			{
				if(outputStream != null)
				{
					outputStream.close();
				}
				bufferedReader.close();
				inputStream.close();
				socket.close();
			}
		}
	}
}
