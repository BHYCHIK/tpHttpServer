package highload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public final class HTTPParser 
{
	private String documentRoot = "C:\\highload\\www\\";
	private String defaultIndex = "/index.html";
	
	private String getContentType(String filename)
	{
		int pointIndex = filename.indexOf(".");
		int paramsIndex = filename.indexOf("?");
		String extension;
		if(pointIndex + 1 <= paramsIndex - 1)
		{
			extension = filename.substring(pointIndex + 1, paramsIndex - 1);
		}
		else
		{
			extension = filename.substring(pointIndex + 1);
		}
		if(extension.toLowerCase().equals("html") || extension.toLowerCase().equals("htm"))
		{
			return "Content-Type: text/html; charset=windows1251\r\n";
		}
		if(extension.toLowerCase().equals("js"))
		{
			return "Content-Type: text/javascript; charset=windows1251\r\n";
		}
		if(extension.toLowerCase().equals("gif"))
		{
			return "Content-Type: image/gif\r\n";
		}
		if(extension.toLowerCase().equals("png"))
		{
			return "Content-Type: image/png\r\n";
		}
		if(extension.toLowerCase().equals("bmp"))
		{
			return "Content-Type: image/x-bmp\r\n";
		}
		if(extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("jpeg"))
		{
			return "Content-Type: image/jpeg\r\n";
		}
		return "";
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
		reply.append((new Date()).toGMTString());
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
