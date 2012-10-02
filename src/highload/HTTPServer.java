package highload;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.eclipse.jetty.http.HttpParser;

public final class HTTPServer implements Runnable 
{
	private final int defaultNumberOfWorkers = 5;
	private final int defaultListeningPort = 8081;
	private final String defaultAddress = "0.0.0.0";
	private final int defaultBacklog = 255;
	private final int defaultTimeoutToAccept = 0;
	
	private int numberOfWorkers;
	private String address;
	private int listeningPort;
	private int backLog;
	private int timeoutToAccept;
	
	private ServerSocket serverSocket;
	
	private HTTPWorker[] workers;
	private Thread[] workersThreads;
	private HTTPParser parser;

	@Override
	public void run()  
	{
		workers = new HTTPWorker[numberOfWorkers];
		workersThreads = new Thread[numberOfWorkers];
		
		for(int workerID = 0; workerID < numberOfWorkers; ++workerID)
		{
			workers[workerID] = new HTTPWorker(serverSocket, parser);
			workersThreads[workerID] = new Thread(workers[workerID]);
			workersThreads[workerID].run();
		}
		
		while(true);
		
	}

	public HTTPServer() throws UnknownHostException, IOException
	{
		numberOfWorkers = defaultNumberOfWorkers;
		address = defaultAddress;
		listeningPort = defaultListeningPort;
		backLog = defaultBacklog;
		timeoutToAccept = defaultTimeoutToAccept;
		
		serverSocket = new ServerSocket(listeningPort, 0, InetAddress.getByName(defaultAddress));
		serverSocket.setSoTimeout(timeoutToAccept);
		
		parser = new HTTPParser();
	}	
	
}
