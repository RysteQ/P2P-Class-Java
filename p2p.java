import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

class P2P {
    public P2P(String addressToConnect_OPTIONAL, int portNumber, boolean connect_Host) {
        if (connect_Host == false) {
            client_or_host = true;
            connect(addressToConnect_OPTIONAL, portNumber);
        } else {
            client_or_host = false;
            host(portNumber);
        }
    }

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

                if (client_or_host == false) {
                    client.close();
                } else {
                    server.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.toString());
                exit(-1);
            }
        } else {
            System.out.println("There is not an active connection at the moment");
            exit(-1);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    private void connect(String address, int portNumber) {
        try {
            client = new Socket(address, portNumber);

            System.out.println("Connected to " + address + ":" + portNumber);

            outputStream = new PrintWriter(client.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));

            connected = true;
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
            exit(-1);
        }
    }

    private void host(int portNumber) {
        try {
            server = new ServerSocket(portNumber);

            System.out.println("Waiting for a connection.....");
            client = server.accept();
            System.out.println("\nClient " + client.getInetAddress().toString() + " has connected");

            outputStream = new PrintWriter(client.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));

            connected = true;
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
            exit(-1);
        }
    }

    private void exit(int status) {
        System.exit(status);
    }

    private boolean client_or_host;
    private ServerSocket server;
    private Socket client;

    private PrintWriter outputStream;
    private BufferedReader inputStream;

    private boolean connected = false;
}