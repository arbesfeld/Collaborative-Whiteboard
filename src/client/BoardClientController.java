package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

import canvas.Canvas;

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
import packet.PacketNewBoard;
import packet.PacketNewClient;
import pixel.Pixel;
import server.BoardServer;
import stroke.StrokeProperties;
import stroke.StrokeType;
import stroke.StrokeTypeBasic;
import stroke.StrokeTypeEraser;
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
    private final StrokeProperties strokeProperties;
    
    public BoardClientController(String userName, String hostName, int portNumber) throws IOException {
        this.user = new User(Utils.generateId(), userName);
        this.clientState = ClientState.ClientStateLoading;
        
        this.socket = new Socket(hostName, portNumber);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Default stroke.
        this.strokeProperties = new StrokeProperties();
        
        // Send a NewClientPacket to announce yourself.
        sendPacket(new PacketNewClient(user));
    }
    
    public void setView(BoardClientGUI view) {
        this.view = view;
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
            // Should not receive a packet before setting our view.
            assert view != null;
            
            for (String line = in.readLine(); line != null; line = in.readLine()) {
            	
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
    protected void receivedNewBoardPacket(PacketNewBoard packet) {
        // Only client to server
        assert false;
    }
    
    @Override
    protected void receivedJoinBoardPacket(PacketJoinBoard packet) {
        assert out != null;
        
        User packetUser = packet.senderName();
        BoardName boardName = packet.boardName();
        
        // This must be for another user that has just joined.
        assert !user.equals(packetUser);
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

    @Override
    protected void receivedExitBoardPacket(PacketExitBoard packet) {
        assert model != null;
        assert out != null;
        
        final User packetUser = packet.senderName();
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;
        
        // We should only receive these packets if the board is our current board.
        BoardName boardName = packet.boardName();
        assert boardName.equals(model.boardName());
        
        if (user.equals(packetUser)) {
            // If it is ourselves, we must disconnect ourselves from the game.
            clientState = ClientState.ClientStateLoading;
            model = null;
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    view.setModel(null);
                }
            });
        } else {
            // Otherwise, remove the user and update the user list.
            model.removeUser(packetUser);
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    view.updateUserList();
                }
            });
        }
    }

    @Override
    protected void receivedGameStatePacket(PacketGameState packet) {
        assert model == null;
        assert out != null;
        assert packet.senderName().equals(BoardServer.SERVER_NAME);
        
        // We should only receive these packets if are loading
        assert clientState == ClientState.ClientStateLoading;
        clientState = ClientState.ClientStatePlaying;
        
        BoardName boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
        User[] clients = packet.clients();
        Pixel[] pixels = packet.pixels();

        model = new ClientBoardModel(this, strokeProperties, width, height, boardName, clients, pixels);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.setModel(model);
                view.updateUserList();
            }
        });
    }

    @Override
    protected void receivedBoardStatePacket(PacketBoardState packet) {
        assert out != null;
        
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
        assert out != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;

        BoardName boardName = packet.boardName();

        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.boardName());

        final Pixel pixel = packet.pixel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model.drawPixel(pixel);
            }
        });
    }
    
    /*
     * Methods for sending packets.
     */

	public void generateNewBoard(BoardName boardName, int width, int height) {
		PacketNewBoard packet = new PacketNewBoard(boardName, user, width, height);
		sendPacket(packet);
	}
	
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
    
    public void sendPixel(Pixel pixel) {
        assert model != null;
        
        BoardName boardName = model.boardName();

        PacketDrawPixel packet = new PacketDrawPixel(boardName, user, pixel);
        sendPacket(packet);
    }
    
    private void sendPacket(Packet packet) {
        out.println(packet.data());
    }
    
    public void setStrokeColor(Color strokeColor) {
    	strokeProperties.setStrokeColor(strokeColor);
    }
    
    public void setStrokeWidth(int strokeWidth) {
        strokeProperties.setStrokeWidth(strokeWidth);
    }
    
    public void setEraserOn(boolean eraserOn) {
        StrokeType newStroke = eraserOn ? new StrokeTypeEraser() : new StrokeTypeBasic();
        strokeProperties.setStrokeType(newStroke);
    }

}
