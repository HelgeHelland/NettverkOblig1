import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

class ClientService extends Thread {
    Socket connectSocket;
    InetAddress clientAddr;
    int serverPort, clientPort;

    public ClientService(Socket connectSocket) {
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

            String receivedText;
            // read from the connection socket
            while (((receivedText = in.readLine()) != null)) {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.javatpoint.com/jsoup-tutorial").get();
                } catch (IOException e) {
                    System.out.println("Code 2: !!!Server couldn't find the web page!!!");
                }

                String[] words = doc.toString().split(" ");
                ArrayList<String> emails = new ArrayList<String>();
                for(String word: words){
                    if (word.matches("[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+.[a-zA-Z0-9.,]")){
                        emails.add(word);
                    }
                }

                for (String email: emails){
                    if(email.charAt(email.length()-1) == '.' || email.charAt(email.length()-1) == ',') {
                        email = email.substring(0, email.length()-1);
                    }
                    System.out.println(email);
                }

                if(emails.size() == 0){
                    System.out.println("Code 1: !!!No email addresses found on the page!!!");
                }else{
                    System.out.println(0);
                }
                System.out.println("Client [" + clientAddr.getHostAddress() + ":" + clientPort + "] > " + receivedText);

                // Write the converted uppercase string to the connection socket
                String outText = ProcessString(receivedText);

                out.println(outText);
                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] > " + outText);
            }

            // close the connection socket
            connectSocket.close();

        } catch (IOException e) {
            System.out.println("Exception occurred when trying to communicate with the client " + clientAddr.getHostAddress());
            System.out.println(e.getMessage());
        }
    }

    /***
     * Process the input string and returns.
     * @param intext Input text
     * @return processed text
     */
    private String ProcessString(String intext) {
        // Convert to upper case
        String outtext = intext.toUpperCase();

        return outtext;
    }
}