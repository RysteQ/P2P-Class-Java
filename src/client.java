import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
	client(String addressToConnectTo, int portNumberToConnectTo) throws InterruptedException {
		connected = connect(addressToConnectTo, portNumberToConnectTo);
	}
	
	client(String addressToConnectTo, int portNumberToConnectTo, String password) throws InterruptedException {
		connected = connect(addressToConnectTo, portNumberToConnectTo, password);
	}

	
	public void sendMessage(String message) throws IOException {
		if (connected) {
			outputStream.println(message);	
		} else {
			throw new IOException("There is not an active connection");
		}
	}
	
	public String receiveMessage() throws IOException {
		if (connected) {
			try {
				return inputStream.readLine();	
			} catch (IOException e) {
				System.out.println("Error receiveing message\n" + e);
				return null;
			}	
		} else {
			throw new IOException("There is not an active connection");
		}
	}
	
	public void leave() throws IOException {
		if (connected) {
			try {
				clientSocket.close();
				connected = false;	
			} catch (IOException e) {
				System.out.println("Error closing connectiong\n" + e);
			}	
		} else {
			throw new IOException("There is not an active connection");
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
	

	private boolean connect(String addressToConnectTo, int portNumberToConnectTo) throws InterruptedException {
		try {
			// connect to the specified host
			clientSocket = new Socket(addressToConnectTo, portNumberToConnectTo);
			
			// get the input and output streams
			inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

			// since this constructor has no passwords this will just send an empty string
			outputStream.println("fds");
			
			// check if the host accepted our request, if not, inform the user why
			if (inputStream.readLine().equals("refused")) {
				throw new IOException("Connection refused");
			} else {
				return true;
			}
		} catch (IOException e) {
			// if there is an error while connecting to the specified host, display this message
			System.out.println("Error connecting to " + addressToConnectTo + ":" + portNumberToConnectTo);
			System.out.println(e);
		}
		
		return false;
	}
	
	private boolean connect(String addressToConnectTo, int portNumberToConnectTo, String password) throws InterruptedException {
		try {
			// connect to the specified host
			clientSocket = new Socket(addressToConnectTo, portNumberToConnectTo);
			
			// get the input and output streams
			inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

			// send the password to the host
			outputStream.println(password);

			// check if the host accepted our request, if not, inform the user why
			if (inputStream.readLine().equals("refused")) {
				throw new IOException("Connection refused");
			} else {
				return true;
			}
		} catch (IOException e) {
			// if there is an error while connecting to the specified host, display this message
			System.out.println("Error connecting to " + addressToConnectTo + ":" + portNumberToConnectTo);
			System.out.println(e);
		}
		
		return false;
	}
	

	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private Socket clientSocket;
	
	private boolean connected = false;
}