import java.net.*;
import java.io.*;

public class Client {
    public static void main(String [] args) {
        String serverName = "100.64.165.195";
        OutputStream outToServer;
        BufferedReader br;
        InputStream inFromServer;
        DataInputStream in;
        String ina = "";
        DataOutputStream out;
        int port = 6066;
        Socket client;

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            br = new BufferedReader(new InputStreamReader(System.in));
            inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);

            ina = "";
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
