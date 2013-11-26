package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import name.BoardName;

import models.ClientBoardModel;

import util.Utils;


public class BoardClientGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    private final JMenuBar menuBar;
    private final JMenu menu;
    
    private final JMenu joinGameSubmenu;
    private final JMenuItem newBoard;
    private final JButton colorButton;
    
    private BoardName[] boardNames;
    private ClientBoardModel model;
    private final BoardClientController controller;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public BoardClientGUI(BoardClientController controller) {
        this.controller = controller;

        // Create the menu bar.
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("File");
        this.newBoard = new JMenuItem("New Board", KeyEvent.VK_T);
        this.colorButton = new JButton("Color");
        
        // Join Game submenu.
        this.joinGameSubmenu = new JMenu("Join Game");
        
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Create and set up the content pane.
        setMenuBar();
        updateContentPane();
    }
    
    private void setMenuBar() {
        // Build the first menu.
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
    
        newBoard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newBoard.getAccessibleContext().setAccessibleDescription("Create a new Board");
        
        newBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBoardAction();
            }
        });
        
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setColor(e);
            }
        });

        menu.add(newBoard);
        menu.add(joinGameSubmenu);
        menu.add(colorButton);
        setJMenuBar(menuBar);
    }
    
    private void updateMenuBar() {
    	joinGameSubmenu.removeAll();
        if (boardNames == null) {
        	return;
        }
        
        for (BoardName boardName : boardNames) {
            JMenuItem subMenuItem = new JMenuItem(boardName.name());        

            final BoardName boardNameF = boardName;
            subMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    joinBoardAction(boardNameF);
                }
            });
            
            joinGameSubmenu.add(subMenuItem);
        }
    }
    
    private void setColor(ActionEvent e) {
        Color color = JColorChooser.showDialog((Component) (e.getSource()),"Color Picker", Color.blue);
        colorButton.setForeground(color);
        this.controller.setColor(color);
    }
    
    private void updateContentPane() {
    	JPanel contentPane;
        if (model == null) {
            contentPane = new JPanel(new BorderLayout());
        } else {
            contentPane = model.canvas();
        }
        contentPane.setVisible(true);
        setContentPane(contentPane);
    }
    
    private void newBoardAction() {
        BoardName boardName = new BoardName(Utils.generateId(), Integer.toString(Utils.generateId()));
        joinBoardAction(boardName);
    }
    
    private void joinBoardAction(BoardName boardName) {
        if (model != null) {
            controller.disconnectFromCurrentBoard();
        }
        controller.connectToBoard(boardName);
    }
    
    /**
     * Set the current model and allow the user to draw to the screen.
     * @param model
     */
    public void setModel(ClientBoardModel model) {
        this.model = model;
        updateContentPane();
        pack();
    }

    /**
     * Update the list of users from the current model.
     */
    public void updateUserList() {
        assert this.model != null;
        // TODO
    }
    
    /**
     * Update the list of boards.
     * @param boards
     */
    public void updateBoardList(BoardName[] boards) {
        this.boardNames = boards;
        updateMenuBar();
    }
}
