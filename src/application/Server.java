package application;

import java.io.IOException;
import java.util.Scanner;

import distribution.QueueManager;
import distribution.QueueServer;
import infrastructure.ServerRequestHandler;

public class Server {
	private static boolean shouldContinue = true;
	private static boolean serverIsUp = false;
	private static QueueServer queueServer;
	private static ServerRequestHandler srh; 
	public static void main(String [] args) throws IOException{
		Scanner in = new Scanner(System.in);
		String choice = "";
		String serverHintString = "Welcome to MOM Middleware!\n"
				+ "Here are some functions you can invoke:\n"
				+ "s : StartServer \n"
				+ "h : Shows intructions\n"
				+ "q : Shutdown server\n";
		System.out.println(serverHintString);
		while(shouldContinue){
			choice = in.nextLine();
			if (choice.equals("h")){
				System.out.println(serverHintString);
			}else if (choice.equals("q")){
				killServer();
			}else if (choice.equals("s")){
				startServerThreadOrErrorMessage(choice);
			}else{
				System.out.println("\"Wrong case in Server Main\"");
			}
		}

	}

	private static void startServerThreadOrErrorMessage(String choice) {
		String parts = choice;
		if(parts.equals("s")){
			try{
				if (!serverIsUp){
					startServerThread();
				}else{
					System.out.println("Server is already UP!");
				}	
			}catch (NumberFormatException nfe){
				System.out.println("We didn't really catch that :T");	    
			}
		}else{
			System.out.println("We didn't really catch that :T");
		}
	}

	private static void startServerThread() {
		new Thread(){
			public void run(){
				try {
					startServer();
				} catch (ClassNotFoundException e) {
					System.out.println("Wrong case in startServerThreadOrErrorMessage");
				} catch (IOException e) {
					System.out.println("Wrong case in startServerThreadOrErrorMessage");
				}
			}
		}.start();
	}

	public static void startServer() throws ClassNotFoundException, IOException{
		QueueManager queueManager = QueueManager.getInstance();
		srh = new ServerRequestHandler(queueManager.getPort());
		queueServer = new QueueServer();
		System.out.println("Server is UP!\n");
		serverIsUp = true;
		while(shouldContinue){
			srh.receive();

		}
	}

	public static void killServer() throws IOException{
		if (serverIsUp){
			shouldContinue = false;
			serverIsUp = false;
			srh.close();

			System.out.println("Shutting down the server!");
		}else{
			System.out.println("Server is not UP");
		}
	}

	public static QueueServer getQueueServer(){
		return queueServer;
	}

	public static ServerRequestHandler getSRH(){
		return srh;
	}

	public static boolean getShouldContinue(){
		return shouldContinue;
	}

	public static void setQueueServer(QueueServer queueServer) {
		Server.queueServer = queueServer;
	}


}
