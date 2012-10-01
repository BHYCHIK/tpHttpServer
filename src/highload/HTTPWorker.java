package highload;

import java.net.ServerSocket;

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
		
	}
}
