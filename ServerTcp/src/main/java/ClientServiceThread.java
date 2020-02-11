

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ClientServiceThread extends Thread {
    Socket connectSocket;
    InetAddress clientAddr;
    int serverPort, clientPort;

    public ClientServiceThread(Socket connectSocket) {
        this.connectSocket = connectSocket;
        clientAddr = connectSocket.getInetAddress();
        clientPort = connectSocket.getPort();
        serverPort = connectSocket.getLocalPort();
    }

    public void run() {
        try (
                // Create server socket with the given port number
                PrintWriter out =
                        new PrintWriter(connectSocket.getOutputStream(), true);
                // Stream reader from the connection socket
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectSocket.getInputStream()));
        ) {

            String receivedURL;

            // read from the connection socket
            while (((receivedURL = in.readLine()) != null)) {
                System.out.println("Received URL from Client [" + clientAddr.getHostAddress() + ":" + clientPort + "]: " + receivedURL);
                Document doc = null;
                int code = 2;
                try {
                    doc = Jsoup.connect("https://" + receivedURL).get();
                } catch (IOException e) {
                    code = 2;
                    out.println(code);
                    System.out.println("Server [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] sending code: " + code + "\n");
                    continue;
                } catch (Exception e) {
                    code = 2;
                    out.println(code);
                    System.out.println("Server [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] sending code: " + code + "\n");
                    continue;
                }

              //  String[] words = doc.toString().split(">");
                String emails = "";

                Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
                Matcher matcher = p.matcher(doc.body().html());
                while (matcher.find()) {
                    emails += matcher.group() + "\n";
                }

                String outText = "";

                // if URL exists but no email was found, send code 1
                if (emails.equals("")) {
                    code = 1;
                    outText = code + "";
                    System.out.println("Server [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] sending code: " + code + "\n");
                // if URL exists and email(s) was/were found, send code 0
                } else {
                    code = 0;
                    outText = code + "\n" + emails;
                    System.out.println("Server [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] sending code: " + code + "\n");
                    System.out.println("and sending following email(s): \n" + emails );
                }

                // sending output to the client
                out.println(outText);
            }

            // close the connection socket
            connectSocket.close();

        } catch (IOException e) {
            System.out.println("Exception occurred when trying to communicate with the client " + clientAddr.getHostAddress());
            System.out.println(e.getMessage());
        }
    }

}