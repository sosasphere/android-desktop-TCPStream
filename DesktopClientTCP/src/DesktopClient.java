import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DesktopClient {
private Socket clientSocket;
	
	public void startConnection(String ip, int port) throws IOException {
		try {
			
			clientSocket = new Socket(ip, port);
	        InputStream is = new FileInputStream(new File("videoFile.mp4"));
	        byte[] bytes = new byte[1024];
	        OutputStream stream = clientSocket.getOutputStream();
	        int count = is.read(bytes, 0, 1024);
	        while(count != -1) {
	        	stream.write(bytes, 0, 1024);
	        	count = is.read(bytes, 0, 1024);
	        	stream.flush();
	        }
	        System.out.print("here");
	        is.close();
	        stream.close();
	        clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) throws IOException {
		DesktopClient cli = new DesktopClient();
		//ip of Android
		cli.startConnection("192.168.1.216", 8888);
	}
}
