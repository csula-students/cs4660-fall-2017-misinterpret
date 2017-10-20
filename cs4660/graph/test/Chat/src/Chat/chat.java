package Chat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
public class chat 
{
	static ServerSocket sSocket = null;
	static Socket clientSock = null;
	static ArrayList<User> clients = new ArrayList<User>();
	static Integer port = 0;
	static String cIP;
	static Integer cPort;
	static Scanner scanner = new Scanner(System.in);
	
	// Get the local IP address
		public static String getIP() 
		{
			String localIP = " ";
			try 
			{
				localIP = InetAddress.getLocalHost().getHostAddress();
			} 
			catch (UnknownHostException e) 
			{
				System.out.println("Host not found");
			}
			return localIP;
		}
		
		public static void connects()
		{
			Client client = new Client();
			Thread clientStart = new Thread(client);
			clientStart.start();
		}
		public static void list()
		{
			System.out.println("id:	IP Address	Port No.");
			for (int x = 0; x < clients.size(); x++) {
				System.out.println(x + ":	" + clients.get(x).getIp() + "	" + clients.get(x).getPort());
			}
		}
		public static void exit()
		{
			System.out.println("Closing Program...");
			//closes all the connections that the server is connected to and the client is connected to
			while (!clients.isEmpty()) {
				clients.get(0).getWriter().println("bye");
				clients.get(0).getWriter().flush();
				System.out.println("\"bye\" sent to: " + clients.get(0).getIp() + ":"
						+ clients.get(0).getPort());
				clients.remove(0);
			}
			System.exit(0);
		}
		public static void myport()
		{
			System.out.println("Listening port: " + port);
		}
	
	//Checks if port specified in parameter is available. 
		public static boolean serverAvailable(int port) {
			ServerSocket testS = null;
			try {
				testS = new ServerSocket(port);
				return true;
			} catch (Exception e) {
				System.out.println("Port is in use or is invalid!");
				return false;
			} finally {
				try {
					testS.close();
				} catch (Exception e2) {
					System.out.println("Failed to close test socket!");
				}
			}
		}
		
		//Handles new client connections to the server. 
		public static class Server implements Runnable {
			@Override
			public void run() {
				InetAddress ip;
				try {
					sSocket = new ServerSocket(port);
					ip = InetAddress.getLocalHost();
					System.out.println("Server is live at " + ip.getHostAddress() + " and is listening at port " + port);
					while (true) {
						Socket incomingSock = sSocket.accept();
						PrintWriter writer = new PrintWriter(incomingSock.getOutputStream());
						String[] sockString = incomingSock.getRemoteSocketAddress().toString().replace("/", "").split(":");
						clients.add(new User(writer, sockString[0], incomingSock.getPort()));
						Thread serverMessages = new Thread(new ServerMessages(incomingSock, writer));
						serverMessages.start();
						System.out.println("Connection from " + sockString[0] + ":" + sockString[1] + " established!");
					}
				} catch (Exception e) {
					System.out.println("Error connecting to port!");
				}
			}
		}
		
		//Handles messages sent to the server. 
		public static class ServerMessages implements Runnable {
			BufferedReader bf;
			Socket cSocket;
			PrintWriter cWriter;
			String[] sockString;

			public ServerMessages(Socket cSocket, PrintWriter cWriter) {
				this.cWriter = cWriter;
				try {
					this.cSocket = cSocket;
					InputStreamReader cIS = new InputStreamReader(cSocket.getInputStream());
					bf = new BufferedReader(cIS);
					sockString = cSocket.getRemoteSocketAddress().toString().replace("/", "").split(":");
				} catch (Exception e) {
					System.out.println("Something went wrong with the socket.");
				}
			}

			@Override
			public void run() {
				String message;
				try {
					while ((message = bf.readLine()) != null) {
						if (message.equals("bye")) {
							//removes client from server's list of clients
							for (int x = 0; x < clients.size(); x++) {
								if (clients.get(x).getIp().equals(sockString[0])
										&& clients.get(x).getPort() == Integer.parseInt(sockString[1])) {
									clients.remove(x);
									break;
								}
							}
						}
						System.out.println("Message received from: " + sockString[0]);
						System.out.println("Sender's port: " + sockString[1]);
						System.out.println("Message: " + message);
					}
				} catch (Exception e) {
					System.out.println("Connection with " + sockString[0] + ":" + sockString[1] + " lost.");
					for (int x = 0; x < clients.size(); x++) {
						if (clients.get(x).getIp().equals(sockString[0])) {
							clients.remove(x);
							break;
						}
					}
				}
			}
		}
		
		//Handles creation of new connections using the connect command. 
		public static class Client implements Runnable {
			@Override
			public void run() {
				try {
					clientSock = new Socket(cIP, cPort);
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					clients.add(new User(writer, cIP, cPort));
					Thread listener = new Thread(new ClientMessages(clientSock));
					listener.start();
					System.out.println("Connection to " + cIP + ":" + cPort + " successful!");
				} catch (Exception e) {
					System.out.println("Error connecting to specified IP and port!");
				}
			}
		}

		//Handles messages sent to the client. 
		public static class ClientMessages implements Runnable {
			BufferedReader bf;
			Socket socket;
			String[] sockString;

			public ClientMessages(Socket socket) {
				this.socket = socket;
				sockString = socket.getRemoteSocketAddress().toString().replace("/", "").split(":");
			}

			@Override
			public void run() {
				String message;
				try {
					InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
					bf = new BufferedReader(streamReader);
					while ((message = bf.readLine()) != null) {
						System.out.println("Message received from: " + sockString[0]);
						System.out.println("Sender's port: " + sockString[1]);
						System.out.println("Message: " + message);
					}
				} catch (Exception e) {

				}
			}
		}
	public static void main(String[] args) {
		//if port isn't specified as a parameter, have user enter one
		if (args.length == 0) {
			while (port == 0) {
				System.out.println("Oops! Looks like you don't have a port number specified! Please type one in now.");
				port = Integer.parseInt(scanner.nextLine());
			}
		} else
			port = Integer.parseInt(args[0]);
		Boolean validConnection = false;
		//checks if port is available
		try {
			validConnection = serverAvailable(port);
		} catch (Exception e) {
			System.out.println("Testing connection failed!");
		}
		//if not available, have user enter a new one
		while (!validConnection) {
			System.out.println("Enter a valid port please.");
			port = Integer.parseInt(scanner.nextLine());
			try {
				validConnection = serverAvailable(port);
			} catch (Exception e) {
				System.out.println("Testing connection failed!");
			}
		}
		System.out.println("Welcome to the chat app!");
		//starts server using specified port
		Server server = new Server();
		Thread serverStart = new Thread(server);
		serverStart.start();
		boolean isRunning = true;
		while (isRunning) {
			System.out.println("Enter a command. Type in \"help\" for the commands to this program.");
			String input = scanner.nextLine();
			String[] userInput = input.split(" ");
			if (userInput[0].toLowerCase().equals("help")) {
				System.out.println("myip \nDisplays the IP address of this process.\n");
				System.out.println(
						"myport \nDisplays the port that this process is listening for incoming connections.\n");
				System.out.println(
						"connect <destination IP> <port no.> \nConnect to the specified IP address and port number.\n");
				System.out.println("list \nDisplays a numbered list of all the connections this process is part of.\n");
				System.out.println(
						"terminate <connection id> \nTerminate the connection in the given ID. Use the command \"list\" to obtain the IDs of connections.\n");
				System.out.println(
						"send <connection id> <message> \nSends a message to the given ID. Use the command \"list\" to obtain the IDs of connections.\n");
				System.out.println("exit \nClose all connections and exit the program.\n");
			}
			if (userInput[0].toLowerCase().equals("myip")) {
				try {
					String myip = chat.getIP();
					System.out.println(myip);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			if (userInput[0].toLowerCase().equals("myport"))
				chat.myport();
			if (userInput[0].toLowerCase().equals("connect")) {

				if (userInput.length == 3) {
					cIP = userInput[1];
					cPort = Integer.parseInt(userInput[2]);
					
					/*Client client = new Client();
					Thread clientStart = new Thread(client);
					clientStart.start(); */
					try {
						//checks if user is trying to connect to their own IP
						if (cIP.equals(chat.getIP())) {
							System.out.println("Connecting to your own IP is not allowed.");
						} else {
							//connects if user is not doing so
							chat.connects();
						}
					} catch (Exception e) {
						System.out.println("Error connecting to specified IP and port.");
					}
				}
			}
			if (userInput[0].toLowerCase().equals("list")) {
				chat.list();
			}
			if (userInput[0].toLowerCase().equals("terminate")) {
				if (userInput.length != 1) {
					for (int x = 0; x < clients.size(); x++) {
						if (x == Integer.parseInt(userInput[1])) {
							//when the server receives "bye", the server removes the client from its list of clients
							clients.get(x).getWriter().println("bye");
							clients.get(x).getWriter().flush();
							System.out.println("\"bye\" sent to: " + clients.get(x).getIp() + ":"
									+ clients.get(x).getPort());
							//the client then closes the connection and removes the server from its list of clients
							for (int y = 0; y < clients.size(); y++) {
								if (clients.get(y).getIp().equals(clients.get(x).getIp())
										&& clients.get(y).getPort() == clients.get(x).getPort()) {
									clients.get(y).getWriter().close();
									clients.remove(y);
									break;
								}
							}
							break;
						}
					}
				} else
					System.out.println("You didn't enter an ID!");
			}
			if (userInput[0].toLowerCase().equals("send")) {
				if (userInput.length >= 3) {
					StringBuilder sb = new StringBuilder();
					for (int y = 2; y < userInput.length; y++) {
						sb.append(userInput[y] + " ");
					}
					if (Integer.parseInt(userInput[1]) >= 0 && Integer.parseInt(userInput[1]) < clients.size()) {
						clients.get(Integer.parseInt(userInput[1])).getWriter().println(sb);
						clients.get(Integer.parseInt(userInput[1])).getWriter().flush();
						System.out.println("Message sent to: " + clients.get(Integer.parseInt(userInput[1])).getIp() + ":"
								+ clients.get(Integer.parseInt(userInput[1])).getPort());
					} else {
						System.out.println("Are you sure you typed in the right ID?");
					}
				}
			}
			if (userInput[0].toLowerCase().equals("exit")) {
				//closes all the connections that the server is connected to and the client is connected to
				chat.exit();
			}
		}
		System.exit(0);
	}
}
class User {
	private PrintWriter writer;
	private String ip;
	private int port;

	public User(PrintWriter writer, String ip, int port) {
		this.writer = writer;
		this.ip = ip;
		this.port = port;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

}
