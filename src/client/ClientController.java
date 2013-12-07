package client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import models.BoardModel;
import name.BoardIdentifier;
import name.ClientIdentifier;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketBoardUsers;
import packet.PacketClientReady;
import packet.PacketDrawCommand;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketMessage;
import packet.PacketNewBoard;
import packet.PacketNewClient;
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
    
    private ClientState clientState;
    private final StrokeProperties strokeProperties;
    
    public ClientController(String userName, String hostName, int portNumber) throws IOException {
        super(new Socket(hostName, portNumber));
        
        this.user = new ClientIdentifier(Utils.generateId(), userName);
        this.clientState = ClientState.IDLE;
        
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
    public void receivedBoardModelPacket(PacketBoardModel packet) {
        assert model == null;
        assert out != null;
        
        // We should only receive these packets if are loading
        assert clientState == ClientState.IDLE;
        clientState = ClientState.PLAYING;
        
        BoardModel model = packet.boardModel();
        DrawableBase drawableCanvas = ((Canvas2d) model.canvas()).makeDrawable(strokeProperties, this);
        BoardModel newModel = new BoardModel(model.identifier(), drawableCanvas, model.users());
        this.model = newModel;
        
        sendPacket(new PacketClientReady());

        view.setModel(newModel);
    }
    
	@Override
	public void receivedBoardUsersPacket(PacketBoardUsers packet) {
        assert model != null;
        assert out != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.PLAYING;
        
        view.setUserList(packet.boardUsers());
	}
	
    @Override
    public void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet) {
        assert out != null;
        
        view.updateBoardList(packet.boards());
    }
    
    @Override
    public void receivedDrawCommandPacket(final PacketDrawCommand packet) {
        assert model != null;
        assert out != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.PLAYING;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                packet.drawCommand().drawOn(model);
            }
        });
    }

	@Override
	public void receivedExitBoardPacket(PacketExitBoard packet) {
		assert model != null;
		assert clientState == ClientState.PLAYING;
		clientState = ClientState.IDLE;
		model = null;
	}

	@Override
	public void receivedMessagePacket(PacketMessage packet) {
		view.addChatLine(packet.text());
	}
	
    /*
     * Methods for sending packets.
     */

	public void generateNewBoard(BoardIdentifier boardName, int width, int height) {
    	if (model != null) {
            disconnectFromCurrentBoard();
    	}
    	
		PacketNewBoard packet = new PacketNewBoard(boardName, width, height);
		sendPacket(packet);
	}
	
    public void connectToBoard(BoardIdentifier boardName) {
    	if (model != null) {
            disconnectFromCurrentBoard();
    	}
    	
        PacketJoinBoard packet = new PacketJoinBoard(boardName);
        sendPacket(packet);
    }
    
    /**
     * Disconnect from the current board.
     */
    private void disconnectFromCurrentBoard() {
        assert model != null;
        
        PacketExitBoard packet = new PacketExitBoard();
        sendPacket(packet);
    }
    
    public void sendDrawCommand(DrawCommand drawCommand) {
        assert model != null;
        
        PacketDrawCommand packet = new PacketDrawCommand(drawCommand);
        sendPacket(packet);
    }

	public void sendMessage(String text) {
		assert model != null;
		
		PacketMessage packet = new PacketMessage(user.name()+ ": " + text);
		sendPacket(packet);
	}
	
    public void setStrokeColor(Color strokeColor) {
    	strokeProperties.setStrokeColor(strokeColor);
    }
    
    public void setStrokeWidth(int strokeWidth) {
        strokeProperties.setStrokeWidth(strokeWidth);
    }
    
    public void setStrokeType(StrokeType newStrokeType) {
        strokeProperties.setStrokeType(newStrokeType);
    }
    
    public void setEraserOn(boolean eraserOn) {
        strokeProperties.setEraserOn(eraserOn);
    }
    
    public void setFillOn(boolean fillOn) {
        strokeProperties.setFillOn(fillOn);
    }

	@Override
	public void receivedNewClientPacket(PacketNewClient packet) {
		assert false;
	}

	@Override
	public void receivedNewBoardPacket(PacketNewBoard packet) {
		assert false;
	}

	@Override
	public void receivedClientReadyPacket(PacketClientReady packet) {
		assert false;
	}

	@Override
	public void receivedJoinBoardPacket(PacketJoinBoard packet) {
		assert false;
	}

}
