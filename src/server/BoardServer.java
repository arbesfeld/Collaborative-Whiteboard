package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import models.ServerBoardModel;
import packet.Packet;
import packet.PacketDisconnectClient;
import packet.PacketDrawPixel;
import packet.PacketGameState;
import packet.PacketNewClient;


public class BoardServer {
	private final ServerSocket serverSocket;
	private int numClients=0; // TODO use random int
	private HashMap<BoardName,ServerBoardModel> serverBoards;
	private HashMap<Integer, OutputStream> userMap;

	public BoardServer(int port) throws IOException // TODO add more args here
	{
		serverSocket=new ServerSocket(port);
		this.serverBoards=new HashMap<BoardName, ServerBoardModel>();
		this.userMap=new HashMap<Integer, OutputStream>();
	}

	public void serve() throws IOException {
		while(true) {
			// block until a client connects
			final Socket socket = serverSocket.accept();

			//Starting a new thread to handle the connection
			Thread thread = new Thread(new Runnable() {
				public void run() {
					//handle the client
					try {
						handleConnection(socket);
					} catch (IOException e) {
						e.printStackTrace(); // but don't terminate the serve()
					} finally {
						// close the socket at the end
						if (socket !=null) {
							try {
								socket.close();
							} catch (IOException e) {
								e.printStackTrace();

							}
						}
					}

				}
			});
			thread.start();
		}
	}

	private void handleConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		this.numClients++;
		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				String output= handleRequest(line, out);
				// Case of New Packet Client
				if (output.equals("goodbye"))
				{
					return;
				}
			}
		} finally {
			out.close();
			in.close();
		}

	}

	private String handleRequest(String input, PrintWriter out) {
		// TODO add a check for valid string input using regex?

		//Create packet with each request
		Packet packet=Packet.createPacketWithData(input);
		switch (packet.packetType()) {

		case PacketTypeNewClient:
			packet = (PacketNewClient) packet;
			ServerBoardModel board = serverBoards.get(packet.boardName());
			PacketGameState game = new PacketGameState (packet.boardID(), board.getUsers() , board.getPixels());
			out.println(x);
			break;

		case PacketTypeDisconnectClient:
			packet= (PacketDisconnectClient) packet;
			break;

		case PacketTypeGameState:
			packet= (PacketGameState) packet;
			break;

		case PacketTypeDrawPixel:
			packet= (PacketDrawPixel) packet;
			break;

		}
		return ""; // TODO Add appropriate return statement or change to void
		
		// TODO Handle all interactions with appropriate boards for each 



	}
}


	//
	//public class BoardClientController implements Runnable {
	//
	//    private BufferedReader in;
	//    private PrintWriter out;
	//    private Socket socket;
	//    private BoardClientView view;
	//    
	//    BoardClientController(Socket socket, BoardClientView view) throws IOException {
	//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	//        out = new PrintWriter(socket.getOutputStream(), true);
	//        this.socket = socket;
	//        this.view = view;
	//    }
	//    
	//    /**
	//     *  Handles the input and output to the client.
	//     *  @throws IOException
	//     */
	//    private void handleConnection() throws IOException {
	//        try {
	//            for (String line = in.readLine(); line != null; line = in.readLine()) {
	//                
	//            }
	//        } finally {
	//            in.close();
	//            out.close();
	//        }
	//    }
	//    
	//
	//    /**
	//     * Start the thread.
	//     */
	//    @Override
	//    public void run() {
	//        try {
	//            handleConnection();
	//        } catch (IOException e) {
	//            e.printStackTrace();
	//        } finally {
	//            try {
	//                socket.close();
	//            } catch (IOException e) {
	//                e.printStackTrace();
	//            }
	//        }
	//    }
	//}
