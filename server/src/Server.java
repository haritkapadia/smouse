import java.net.*;
import java.io.*;

public class Server extends Thread {
	private ServerSocket serverSocket;
	InetAddress addr = InetAddress.getByName("192.168.1.4");

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port, 50, addr);
		serverSocket.setSoTimeout(100000);
	}

	public void run() {
		while(true) {
			try {
				System.out.println("Waiting for client on port " + 
						serverSocket.getInetAddress() + "...");
				Socket server = serverSocket.accept();

				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				String ina = "";
				while(!ina.equals("exit")) {
					ina = in.readUTF();
					System.out.println(ina);
				}

				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
						+ "\nGoodbye!");
				server.close();

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String [] args) {
		try {
			Thread t = new Server(6066);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
