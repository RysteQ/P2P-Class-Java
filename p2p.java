import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.math.BigInteger;
import java.util.Scanner;

/*
Name: P2P
Input: None
Output: None
Purpose: This is a class meant for peer to peer communication, it will be expanted in the near future with the addition of encryption

Note: This is the first application I write in Java after learning the basics, so the code might not be the best way to do things !!!
*/

/*
TODO 1) Add encryption algorithms, ROT_X and RSA 
TODO 2) Create a CLI in a class called SimpleTerminal, this will be included in a seperate repository
TODO 3) Combine P2P and SimpleTerminal to create a P2P CLI communication application
*/

public class p2p {
    static Scanner input = new Scanner(System.in);

    public static void main(String args[]) {
        P2P okay = new P2P();

        if (args[0].equals("k")) {
            okay.setPassword("papagkaldso");
            okay.setPort(4444);
            okay.setAddress("127.0.0.1");
            
            okay.connect();
            if (okay.isConnected() == false) System.out.println("ERRRROROROORORO");
            okay.sendMessage("Hello !");
        } else {
            okay.setPassword("papagkalo");
            okay.setPort(4444);
            
            okay.host();
            
            System.out.println(okay.receiveMessage());
        }
    }
}

class P2P {
    public void sendMessage(String msg) {
        if (isConnected()) {
            outputStream.println(msg);
        } else {
            System.out.println("There is not an active connection");
            exit(-1);
        }
    }

    public String receiveMessage() {
        if (isConnected()) {
            try {
                return inputStream.readLine();
            } catch (IOException e) {
                System.out.println("Error: " + e.toString());
            }
        } else {
            System.out.println("There is not an active connection");
            exit(-1);
        }

        return null;
    }

    public void closeConnection() {
        if (isConnected()) {
            try {
                outputStream.close();
                inputStream.close();

                client.close();
                server.close();
            } catch (IOException e) {
                exit(0);
            }
        } else {
            System.out.println("There is not an active connection at the moment");
            exit(-1);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public void setAddress(String newAddress) {
        address = newAddress;
    }

    public void setPort(int port) {
        portNumber = port;
    }

    public void connect() {
        try {
            client = new Socket(address, portNumber);

            outputStream = new PrintWriter(client.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
 

            outputStream.println(password);

            if (inputStream.readLine().equals("accepted")) {
                System.out.println("Connected to " + address + ":" + portNumber);
                connected = true;
            } else {
                System.out.println("Host has refused the connection");
                
                closeConnection();
                exit(0);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
            exit(-1);
        }
    }

    public void host() {
        try {
            server = new ServerSocket(portNumber);

            System.out.println("Waiting for a connection.....");

            client = server.accept();
            outputStream = new PrintWriter(client.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));

            if (inputStream.readLine().equals(password)) {
                System.out.println("\nClient " + client.getInetAddress().toString() + " has connected");
                outputStream.println("accepted");
                connected = true;
            } else {
                System.out.println("Rejected client" + client.getInetAddress() + ", incorrect password");
                outputStream.println("refused");

                closeConnection();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
            exit(-1);
        }
    }

    private void exit(int status) {
        System.exit(status);
    }


    private ServerSocket server;
    private Socket client;

    private PrintWriter outputStream;
    private BufferedReader inputStream;

    private int portNumber = 4444;
    private String address;
    private String password = "";
    private boolean connected = false;
}