package highload;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public final class HTTPServer implements Runnable 
{
	private final int defaultNumberOfWorkers = 5;
	private final int defaultListeningPort = 8080;
	private final String defaultAddress = "0.0.0.0";
	private final int defaultBacklog = 255;
	
	private int numberOfWorkers;
	private String address;
	private int listeningPort;
	private int backLog;
	
	private ServerSocket socket;

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}
	
	public HTTPServer() throws UnknownHostException, IOException
	{
		numberOfWorkers = defaultNumberOfWorkers;
		address = defaultAddress;
		listeningPort = defaultListeningPort;
		backLog = defaultBacklog;
		
		socket = new ServerSocket(listeningPort, 0, InetAddress.getByName(defaultAddress));
	}	
	
}
