import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class host {
	host(int portToHost) {
		portNumber = portToHost;
	}

	host(int portToHostOn, String passwordToSet) {
		portNumber = portToHostOn;
		password = passwordToSet;
	}

	
	public void bind() throws InterruptedException {
		while (!clientConnected) {
			try {
				serverSocket = new ServerSocket(portNumber);
				clientSocket = serverSocket.accept();

				System.out.println(clientSocket.getRemoteSocketAddress() + " just connected !");

				inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

				if (!inputStream.readLine().equals(password)) {
					System.out.println("Client was refused a connection\nReason: wrong password");
					outputStream.println("refused");

					clientSocket.close();
					serverSocket.close();
				} else {
					outputStream.println("accepted");
					clientConnected = true;
				}
			} catch (IOException e) {
				System.out.println("Error listening to incoming requests\n" + e);
			}
		}
	}

	public void sendMessage(String message) {
		if (clientConnected) {
			outputStream.println(message);
		}
	}

	public String receiveMessage() {
		try {
			return inputStream.readLine();
		} catch (IOException e) {
			System.out.println("Error receiveing message\n" + e);
		}

		return null;
	}

	public void kickClient() {
		try {
			clientConnected = false;
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error closing sockets\n" + e);
		}
	}

	public void close() {
		try {
			serverSocket.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error closing sockets\n" + e);
		}
	}

	public void changePassword(String newPassword) {
		password = newPassword;
	}

	public void changePort(int newPortNumber) {
		portNumber = newPortNumber;
	}


	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private ServerSocket serverSocket;
	private Socket clientSocket;

	private boolean clientConnected = false;
	private String password = "";
	private int portNumber;
}