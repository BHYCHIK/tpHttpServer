package highload;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public final class HTTPServer implements Runnable 
{
	private final int defaultNumberOfWorkers = 5;
	private final int defaultListeningPort = 8081;
	private final String defaultAddress = "0.0.0.0";
	private final int defaultBacklog = 255;
	
	private int numberOfWorkers;
	private String address;
	private int listeningPort;
	private int backLog;
	
	private ServerSocket serverSocket;
	
	private HTTPWorker[] workers;
	private Thread[] workersThreads;

	@Override
	public void run()  
	{
		workers = new HTTPWorker[numberOfWorkers];
		workersThreads = new Thread[numberOfWorkers];
		
		for(int workerID = 0; workerID < numberOfWorkers; ++workerID)
		{
			workers[workerID] = new HTTPWorker(serverSocket);
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
		
		serverSocket = new ServerSocket(listeningPort, 0, InetAddress.getByName(defaultAddress));
	}	
	
}
