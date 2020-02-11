# NettverkOblig1

Beskrive applikasjonen og applikasjon layer protocol:
* Meldinger og data utvekslet.
* Syntax/format/
* Betydning
* Regler

(Prepare a report describing your application, and application layer protocol you used,
which should include types of messages and data to be exchanged, syntax/format and semantics,
and rules (see example of Web application in the lecture slide N02a-Application layer).)

Kladd:
We have used TCP:
* The server creates a socket, welcoming clients.
* The client creates a socket (specifies the servers IP and port number) and establishes connection to the server.
* The server creates a new socket to communicate with the specific client. This is done to enable multiple clients to 
connect at the same time, and the port numbers can be used to recognise clients.
* The server listens for inputs.
* The client sends the server an input.
* The server receives the input.
* If the input is an URL, the server (using Jsoup) receives the HTML document and parses it into a doc.
* A pattern is created to help locate the emails.
* The pattern is then matched with the HTML document, and any matching pattern is kept.
* Duplicates gets removed.
* The client then receives the email as a string. 
* If no URL is found or no emails are found on the website, an appropriate code and message is sent to the client. 
  


