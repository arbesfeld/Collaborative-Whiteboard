package client;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import models.BoardModel;
import name.BoardIdentifier;
import name.ClientIdentifier;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketDrawCommand;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import packet.PacketNewClient;
import server.Server;
import server.SocketHandler;
import stroke.StrokeProperties;
import stroke.StrokeType;
import stroke.StrokeTypeBasic;
import stroke.StrokeTypeEraser;
import util.Utils;
import canvas.Canvas2d;
import canvas.DrawableBase;
import canvas.command.DrawCommand;

public class ClientController extends SocketHandler {
	private static final long serialVersionUID = 6673644308053728584L;
	
	private ClientGUI view;
    private final ClientIdentifier user;
    private BoardModel model;
    
    private ClientState clientState;
    private final StrokeProperties strokeProperties;
    
    public ClientController(String userName, String hostName, int portNumber) throws IOException {
        super(new Socket(hostName, portNumber));
        
        this.user = new ClientIdentifier(Utils.generateId(), userName);
        this.clientState = ClientState.ClientStateLoading;
        
        // Default stroke.
        this.strokeProperties = new StrokeProperties();
        
        // Send a NewClientPacket to announce yourself.
        sendPacket(new PacketNewClient(user));
    }
    
    public void setView(ClientGUI view) {
        assert this.view == null;
        
        this.view = view;
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
        
        ClientIdentifier packetUser = packet.senderName();
        BoardIdentifier boardName = packet.boardName();
        
        // This must be for another user that has just joined.
        assert !user.equals(packetUser);
        assert model != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;
        
        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.identifier());
        
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
        
        final ClientIdentifier packetUser = packet.senderName();
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;
        
        // We should only receive these packets if the board is our current board.
        BoardIdentifier boardName = packet.boardName();
        assert boardName.equals(model.identifier());
        
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
    protected void receivedBoardModelPacket(PacketBoardModel packet) {
        assert model == null;
        assert out != null;
        assert packet.senderName().equals(Server.SERVER_NAME);
        
        // We should only receive these packets if are loading
        assert clientState == ClientState.ClientStateLoading;
        clientState = ClientState.ClientStatePlaying;
        
        BoardModel model = packet.boardModel();
        DrawableBase drawableCanvas = ((Canvas2d) model.canvas()).makeDrawable(strokeProperties, this);
        final BoardModel newModel = new BoardModel(model.identifier(), drawableCanvas, model.users());
        this.model = newModel;
        
        try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
			        view.setModel(newModel);
			        view.updateUserList();
			    }
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet) {
        assert out != null;
        
        final BoardIdentifier[] boards = packet.boards();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.updateBoardList(boards);
            }
        });
    }
    
    @Override
    protected void receivedDrawCommandPacket(PacketDrawCommand packet) {
        assert model != null;
        assert out != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.ClientStatePlaying;

        BoardIdentifier boardName = packet.boardName();

        // We should only receive these packets if the board is our current board.
        assert boardName.equals(model.identifier());

        packet.drawCommand().drawOn(model);
    }
    
    /*
     * Methods for sending packets.
     */

	public void generateNewBoard(BoardIdentifier boardName, int width, int height) {
		PacketNewBoard packet = new PacketNewBoard(boardName, user, width, height);
		sendPacket(packet);
	}
	
    public void connectToBoard(BoardIdentifier boardName) {
        PacketJoinBoard packet = new PacketJoinBoard(boardName, user);
        sendPacket(packet);
    }
    
    /**
     * Disconnect from the current board.
     */
    public void disconnectFromCurrentBoard() {
        assert model != null;
        
        BoardIdentifier boardName = model.identifier();

        PacketExitBoard packet = new PacketExitBoard(boardName, user);
        sendPacket(packet);
    }
    
    public void sendDrawCommand(DrawCommand drawCommand) {
        assert model != null;
        
        BoardIdentifier boardName = model.identifier();

        PacketDrawCommand packet = new PacketDrawCommand(boardName, user, drawCommand);
        sendPacket(packet);
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
