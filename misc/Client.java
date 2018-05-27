import java.net.*;
import java.io.*;

public class Client {
	public static void main(String [] args) {
		String serverName = "100.64.165.195";
		int port = 6066;
		try {
			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);

			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);

			String ina = "";
			while(!ina.equals("exit")) {
				ina = br.readLine();
				out.writeUTF(ina);
			}
			System.out.println("Server says " + in.readUTF());
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
