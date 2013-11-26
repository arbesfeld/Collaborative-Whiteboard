package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

import name.BoardName;
import name.User;

import models.ClientBoardModel;
import packet.Packet;
import packet.PacketBoardState;
import packet.PacketExitBoard;
import packet.PacketDrawPixel;
import packet.PacketGameState;
import packet.PacketHandler;
import packet.PacketJoinBoard;
import packet.PacketNewClient;
import pixel.Pixel;
import server.BoardServer;
import util.Utils;

public class BoardClientController extends PacketHandler implements Runnable {
    
    // Communication.
    private final Socket socket;
    
    // write to the server
    private final PrintWriter out;
    
    // read from the server
    private final BufferedReader in;
    
    private BoardClientGUI view;
    private final User user;
    private ClientBoardModel model;
    
    private ClientState clientState;
    
    public BoardClientController(String userName, String hostName, int portNumber) throws IOException {
        this.user = new User(Utils.generateId(), userName);
        this.clientState = ClientState.ClientStateIdle;
        
        this.socket = new Socket(hostName, portNumber);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Send a NewClientPacket to announce yourself.
        sendPacket(new PacketNewClient(user));
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
            	// Should not receive a packet before setting our view.
            	assert view != null;
            	
                Packet packet = Packet.createPacketWithData(line);
                receivedPacket(packet);
            }
        } finally {
            in.close();
            out.close();
        }
    }


    @Override
    protected void receivedNewClientPacket(PacketNewClient packet) {
    	// Only client to server.
    	assert false;
    }
    
    @Override
    protected void receivedJoinBoardPacket(PacketJoinBoard packet) {
        User packetUser = packet.senderName();
        BoardName boardName = packet.boardName();
        
        if (user.equals(packetUser)) {
            // We are about to receive a GameStatePacket, we should have a null model.
            assert model == null;
            assert clientState == ClientState.ClientStateIdle;
            clientState = ClientState.ClientStateLoading;
        } else {
            assert model != null;
            
            // We should only receive these packets if we have loaded.
            assert clientState == ClientState.ClientStatePlaying;
            
            // We should only receive these packets if the board is our current board.
            assert boardName.equals(model.boardName());
            
            model.addUser(packetUser);
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    view.updateUserList();
                }
            });
        }
    }

    @Override
    protected void receivedExitBoardPacket(PacketExitBoard packet) {
        assert model != null;
        assert out == null;
        
        final User packetUser = packet.senderName();
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;

        // We should only receive these packets if the board is our current board.
        BoardName boardName = packet.boardName();
        assert boardName.equals(model.boardName());
        
        if (user.equals(packetUser)) {
            // If it is ourselves, we must disconnect ourselves from the game.
            clientState = ClientState.ClientStateIdle;
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model = null;
                    view.setModel(null);
                }
            });
        } else {
            // Otherwise, remove the user and update the user list.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.removeUser(packetUser);
                    view.updateUserList();
                }
            });
        }
    }

    @Override
    protected void receivedGameStatePacket(PacketGameState packet) {
        assert model == null;
        assert out == null;
        assert packet.senderName().equals(BoardServer.SERVER_NAME);
        
        // We should only receive these packets if are loading
        assert clientState == ClientState.ClientStateLoading;
        clientState = ClientState.ClientStatePlaying;
        
        final BoardName boardName = packet.boardName();
        final int width = packet.width();
        final int height = packet.height();
        final User[] clients = packet.clients();
        final Pixel[] pixels = packet.pixels();
        final BoardClientController controller = this;
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model = new ClientBoardModel(controller, width, height, boardName, clients, pixels);
                view.setModel(model);
                view.updateUserList();
            }
        });
    }

    @Override
    protected void receivedBoardStatePacket(PacketBoardState packet) {
        assert out == null;
        
        final BoardName[] boards = packet.boards();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.updateBoardList(boards);
            }
        });
    }
    
    @Override
    protected void receivedDrawPixelPacket(PacketDrawPixel packet) {
        assert model != null;
        assert out == null;
        assert packet.senderName().equals(BoardServer.SERVER_NAME);
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;

        BoardName boardName = packet.boardName();

        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.boardName());

        final Pixel pixel = packet.pixel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model.putPixel(pixel);
            }
        });
    }
    
    /*
     * Methods for sending packets.
     */
    
    public void connectToBoard(BoardName boardName) {
        PacketJoinBoard packet = new PacketJoinBoard(boardName, user);
        sendPacket(packet);
    }
    
    /**
     * Disconnect from the current board.
     */
    public void disconnectFromCurrentBoard() {
        assert model != null;
        
        BoardName boardName = model.boardName();

        PacketExitBoard packet = new PacketExitBoard(boardName, user);
        sendPacket(packet);
    }
    
    public void drawPixel(int x, int y, Color color) {
        assert model != null;
        
        BoardName boardName = model.boardName();
        Pixel pixel = new Pixel(x, y, color);

        PacketDrawPixel packet = new PacketDrawPixel(boardName, user, pixel);
        sendPacket(packet);
    }
    
    private void sendPacket(Packet packet) {
        out.println(packet.data());
    }
    
    public void setView(BoardClientGUI view) {
    	this.view = view;
    }
}
