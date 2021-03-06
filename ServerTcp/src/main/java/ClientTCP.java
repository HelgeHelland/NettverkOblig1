import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ClientTCP {
    public static void main(String[] args) throws IOException {

        String hostName = "10.253.16.47"; // Default host, localhost
        int portNumber = 5555; // Default port to use
        if (args.length > 0) {
            hostName = args[0];
            if (args.length > 1) {
                portNumber = Integer.parseInt(args[1]);
                if (args.length > 2) {
                    System.err.println("Usage: java ClientTCP [<host name>] [<port number>]");
                    System.exit(1);
                }
            }
        }


        try (
                // create TCP socket for the given hostName, remote port PortNumber
                Socket clientSocket = new Socket(hostName, portNumber);

                // Stream writer to the socket
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                // Stream reader from the socket
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                // Keyboard input reader
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {
            String inputURL;


            System.out.print("Type in the URL that you want to check, e.g. www.vg.no (Stop by typing exit): ");

            // Loop until user input exit
            while (!(inputURL = stdIn.readLine()).equals("exit")) {

                // write keyboard input to the socket
                out.println(inputURL);

                // read from the socket and display
                String receivedCode = in.readLine();

                String receivedEmails;

                List<String> emails = new ArrayList<>();

                System.out.println();

                //email(s) was found
                if (receivedCode.equals("0")) {

                    // read the received emails
                    while (!(receivedEmails = in.readLine()).equals(null) && !receivedEmails.equals("")){
                            emails.add(receivedEmails);
                    }

                    // print the email(s) that was/were found
                    System.out.println("The following email(s) was/were found:");
                    for(String email : emails) System.out.println(email);

                // URL exists but no email was found
                } else if (receivedCode.equals("1")) {
                    System.out.println("!!!No email address found on the page!!!");

                // URL does not exists
                } else if (receivedCode.equals("2")) {
                    System.out.println("Server couldn’t find the web page!!!");
                // Error in server
                } else {
                    System.err.println("!!!Respond message should be 0, 1 or 2!!!");
                }

                System.out.println();
                System.out.print("Type in the URL that you want to check, e.g. www.vg.no (Stop by typing exit): ");
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
