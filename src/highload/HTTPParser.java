package highload;

public final class HTTPParser 
{
	public static String getReply(String request)
	{
		System.out.print(request);
		return "HTTP/1.1 301 Moved Permanently\r\n"+
				"Location: http://google.com\r\n"+
				"Connection: closed\r\n\r\n";
	}
}
