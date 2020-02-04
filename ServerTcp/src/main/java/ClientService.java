package main.java;

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
                System.out.println("Client [" + clientAddr.getHostAddress() + ":" + clientPort + "] > " + receivedText);
                Document doc = null;
                int feilmelding = 2;
                try {
                    doc = Jsoup.connect("https://" + receivedText).get();
                } catch (IOException e) {
                    feilmelding = 2;
                    out.println(feilmelding);
                    System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] > " + feilmelding);
                    continue;
                } catch (Exception e) {
                    feilmelding = 2;
                    out.println(feilmelding);
                    System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] > " + feilmelding);
                    continue;
                }

                String[] words = doc.toString().split(">");
                String emails = "";

                Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
                Matcher matcher = p.matcher(doc.body().html());
                while (matcher.find()){
                    emails += matcher.group() + "\n";
                }

                String outText = "";
                if(emails.equals("")){
                    feilmelding = 1;
                    outText = feilmelding + "";
                }else{
                    feilmelding = 0;
                    outText = feilmelding + "\n" + emails;
                }



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