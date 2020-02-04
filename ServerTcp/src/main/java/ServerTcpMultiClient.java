import java.io.IOException;
import java.net.ServerSocket;

public class ServerTcpMultiClient {
    public static void main(String[] args) throws IOException {
        int portNumber = 5555; // Default port to use

        if (args.length > 0) {
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else {
                System.err.println("Usage: java EchoUcaseServerMutiClients [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am the EchoUCase Multi-client TCP server.");

        try (
                // Create server socket with the given port number
                ServerSocket serverSocket =
                        new ServerSocket(portNumber);
        ) {
            String receivedText;
            // continuously listening for clients
            while (true) {
                // create and start a new ClientServer thread for each connected client
                ClientServiceThead clientserver = new ClientServiceThead(serverSocket.accept());
                clientserver.start();
            }
        } catch (IOException e) {

            System.out.println("Exception occurred when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}