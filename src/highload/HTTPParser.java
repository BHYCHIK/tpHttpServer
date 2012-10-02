package highload;

public final class HTTPParser 
{
	public static byte[] getReply(String request)
	{
		System.out.print(request);
		return "HTTP/1.1 200 OK\r\n"+
				"server: RemTTP\r\n"+
				"Connection: closed\r\n\r\n"+
				"<html><body><img src=\"http://yabs.yandex.ru/count/601QxfDtPqS40002ZhG1e0O5KPK2cmPfMeYwnJfG0vAaLFQUfZIAhrRqHge2fPOOP96yq4ba1fE53K6kyKz73hzl1Si4UGS0\"></body></html>\r\n\r\n";
	}
}
