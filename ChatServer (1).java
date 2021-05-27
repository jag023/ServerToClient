package chatapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	static ArrayList<String> userNames = new ArrayList<>();
	static ArrayList<PrintWriter> printWriters = new ArrayList<>();
	
	public static void main(String[]arg) {
		System.out.println("waiting for clients-_-......");
		@SuppressWarnings("resource")
		ServerSocket ss;
		try {
			ss = new ServerSocket(9800);
			while(true) {
				Socket soc = ss.accept();
				System.out.println("connection established");
				ConversationHandler handler = new ConversationHandler(soc);
				handler.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}

class ConversationHandler extends Thread{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	public ConversationHandler(Socket socket) {
		this.socket=socket;
		
	}
	
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			int count =0;
			while(true) {
				if(count>0) {
					out.println("Name Already Exist");
				}
				else {
					out.println("Name Required");
				}
				name = in.readLine();
				if(name==null) {
					return;
				}
				if(!ChatServer.userNames.contains(name)) {
					ChatServer.userNames.add(name);
					break;
				}
				count++;
			}
			out.println("Name Accepted" + name);
			ChatServer.printWriters.add(out);
			
			while(true) {
				String message = in.readLine();
				if(message == null) {
					return;
				}
				for(PrintWriter writer : ChatServer.printWriters) {
					writer.println(name + ": "+message);
				}
				
			}
			
		}catch(Exception e) {
			System.out.println(e);
		}
	} // end of run
}