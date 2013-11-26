package BoardClient;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import models.ClientBoardModel;
import packet.BoardName;
import packet.InvalidPacketTypeException;
import packet.Packet;
import packet.PacketBoardState;
import packet.PacketDisconnectClient;
import packet.PacketDrawPixel;
import packet.PacketGameState;
import packet.PacketHandler;
import packet.PacketNewClient;
import packet.PacketType;
import packet.User;
import pixel.Pixel;
import util.Utils;

public class BoardClientController extends PacketHandler implements Runnable {
    
    // Communication.
    private final Socket socket;
    
    // write to the server
    private final PrintWriter out;
    
    // read from the server
    private final BufferedReader in;
    
    private final BoardClientGUI view;
    private final User user;
    private ClientBoardModel model;
    
    private ClientState clientState;
    
    public BoardClientController(BoardClientGUI view, String userName, 
                                 String hostName, int portNumber) throws IOException {
        this.view = view;
        this.user = new User(Utils.generateId(), userName);
        this.clientState = ClientState.ClientStateIdle;
        
        this.socket = new Socket(hostName, portNumber);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    /**
     * Start the thread.
     */
    @Override
    public void run() {
        try {
            handleServerPacket();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     *  Handles the input from the client.
     *  @throws IOException
     */
    private void handleServerPacket() throws IOException {
        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                Packet packet = Packet.createPacketWithData(line);
                receivedPacket(packet, null);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    @Override
    protected void receivedNewClientPacket(PacketNewClient packet, PrintWriter out) {
        User packetUser = packet.user();
        BoardName boardName = packet.boardName();
        
        assert model != null;
        
        // We should never receive our own new client packet.
        assert !user.equals(packetUser);
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;
        
        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.boardName());
        
        model.addUser(packetUser);
        
        view.updateUserList();
    }

    @Override
    protected void receivedDisconnectClientPacket(PacketDisconnectClient packet, PrintWriter out) {
        assert model != null;
        assert out == null;
        
        User packetUser = packet.user();
        BoardName boardName = packet.boardName();
        
        
        // We should never receive our own disconnect client packet.
        assert !user.equals(packetUser);
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;
        
        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.boardName());
        
        model.removeUser(packetUser);
        
        view.updateUserList();
    }

    @Override
    protected void receivedGameStatePacket(PacketGameState packet, PrintWriter out) {
        assert model == null;
        assert out == null;
        
        // We should only receive these packets if are loading
        assert clientState == ClientState.ClientStateLoading;
        clientState = ClientState.ClientStatePlaying;
        
        BoardName boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
        User[] clients = packet.clients();
        Pixel[] pixels = packet.pixels();
        
        model = new ClientBoardModel(width, height, boardName, clients, pixels);
        
        view.setModel(model);
        view.updateBoard();
        view.updateUserList();
    }

    @Override
    protected void receivedBoardStatePacket(PacketBoardState packet, PrintWriter out) {
        assert out == null;
        
        BoardName[] boards = packet.boards();
        
        view.updateBoardList(boards);
    }
    
    @Override
    protected void receivedDrawPixelPacket(PacketDrawPixel packet, PrintWriter out) {
        assert model != null;
        assert out == null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;

        Pixel pixel = packet.pixel();
        BoardName boardName = packet.boardName();

        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.boardName());
        
        model.putPixel(pixel);
    }
    
    /*
     * Methods for sending packets.
     */
    
    public void connectToBoard(BoardName boardName) {
        // Can only connect to a board if we are disconnected.
        assert clientState == ClientState.ClientStateIdle;
        
        sendConnectToBoard(boardName);
    }
    
    /**
     * Disconnect from the current board.
     */
    public void disconnectFromCurrentBoard() {
        assert model != null;
        assert clientState == ClientState.ClientStatePlaying;

        BoardName boardName = model.boardName();
        
        // Disconnect ourselves from the model and wait until we receive a new "game state" packet.
        clientState = ClientState.ClientStateIdle;
        model = null;
        
        view.clearModel();

        sendDisconnectToBoard(boardName);
    }
    
    public void drawPixel(int x, int y, Color color) {
        assert model != null;
        assert clientState == ClientState.ClientStatePlaying;
        
        BoardName boardName = model.boardName();
        Pixel pixel = new Pixel(x, y, color);
        
        sendDrawPixel(boardName, pixel);
    }
    
    private void sendPacket(Packet packet) {
        out.println(packet.data());
    }
    
    private void sendConnectToBoard(BoardName boardName) {
        PacketNewClient packet = new PacketNewClient(boardName, user);
        sendPacket(packet);
    }
    
    private void sendDisconnectToBoard(BoardName boardName) {
        PacketDisconnectClient packet = new PacketDisconnectClient(boardName, user);
        sendPacket(packet);
    }
    
    private void sendDrawPixel(BoardName boardName, Pixel pixel) {
        PacketDrawPixel packet = new PacketDrawPixel(boardName, pixel);
        sendPacket(packet);
    }
}
