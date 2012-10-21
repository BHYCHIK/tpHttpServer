package highload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public final class HTTPParser 
{
	private String documentRoot = "C:\\www";
	private String defaultIndex = "/index.html";
	
	private String getContentType(String filename)
	{
		String fname = getFileName(filename);
		return "Content-Type: "+URLConnection.guessContentTypeFromName(fname)+"\r\n";		
	}
	
	public String getServerTime() 
	{
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
	
	private String proccessMainPart(String[] request)
	{
		String fileName = getFileName(request[1]);
		File file = new File(documentRoot+fileName);
		if(!file.exists())
		{
			return("HTTP/1.1 404 Not found\r\n\r\n");
		}
		StringBuilder reply = new StringBuilder("HTTP/1.1 200 OK\r\n");
		reply.append("Date: ");
		reply.append(getServerTime());
		reply.append("\r\nServer: bhychikHTTP\r\n");
		reply.append(getContentType(fileName));
		reply.append("Content-Length: ");
		reply.append(file.length());
		reply.append("\r\n\r\n");
		return reply.toString();
	}
	
	private String getFileName(String string) 
	{
		if (string.endsWith("/"))
		{
			string += defaultIndex;
		}
		int paramsIndex = string.indexOf("?");
		if (paramsIndex > 0)
		{
			return string.substring(0, paramsIndex - 1);
		}
		else
		{
			return string;
		}
	}

	private String proccessLine(String line)
	{
		String[] lineWords = line.split(" ");
		if(lineWords[0].toUpperCase().equals("GET") || lineWords[0].toUpperCase().equals("POST"))
		{
			return proccessMainPart(lineWords);
		}
		else
		{
			return "";
		}
	}
	
	public byte[] getReply(String request)
	{
		System.out.print(request);
		String[] requestLines = request.split("\r\n"); 
		int linesPerRequest = requestLines.length;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < linesPerRequest; ++i)
		{
			builder.append(proccessLine(requestLines[i]));
		}
		byte[] reply = builder.toString().getBytes();		
		return reply;
	}

	public byte[] getContent(String string) 
	{
		try
		{
			String[] requestLines = string.split("\r\n");
			String firstLine = requestLines[0];
			String[] firstLineParts = firstLine.split(" ");
			String fname = firstLineParts[1]; 
			File file = new File(documentRoot + getFileName(fname));
			if(!file.exists())
				return new byte[0];
			byte[] buffer = new byte[(int) file.length()];
			FileInputStream inputStream = new FileInputStream(file);
			inputStream.read(buffer);
			inputStream.close();
			return buffer;
		}
		catch(IOException ex)
		{
			return new byte[0];
		}
		
	}
}
