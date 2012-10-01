package highload;

import java.io.IOException;
import java.net.UnknownHostException;

public final class main {

	public main() 
	{
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		(new Thread(new HTTPServer())).start();
	}

}
