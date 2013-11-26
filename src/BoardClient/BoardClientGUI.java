package BoardClient;

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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import models.ClientBoardModel;
import packet.BoardName;
import util.Utils;


public class BoardClientGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    private static JMenuBar menuBar;
    private static JPanel contentPane;
    
    private BoardName[] boardNames;
    private ClientBoardModel model;
    private BoardClientController controller;
    
    public void setController(BoardClientController controller) {
        this.controller = controller;
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void display() {
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Create and set up the content pane.
        updateMenuBar();
        updateContentPane();
    
        //Display the window.
        setSize(450, 260);
        setVisible(true);
    }
    
    private void updateMenuBar() {
        JMenu menu, submenu;
        JMenuItem menuItem;
        
        // Create the menu bar.
        menuBar = new JMenuBar();
    
        // Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
    
        menuItem = new JMenuItem("New Board",
                                 KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Create a new Board");
        
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBoardAction();
            }
        });
        
        menu.add(menuItem);
    
        // Join Game submenu.
        submenu = new JMenu("Join Game");  
        if (boardNames != null) {
            for (BoardName boardName : boardNames) {
                JMenuItem subMenuItem = new JMenuItem(boardName.name());        

                final BoardName boardNameF = boardName;
                subMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        joinBoardAction(boardNameF);
                    }
                });
                
                submenu.add(subMenuItem);
            }
        }
        menu.add(submenu);
        
        final JButton colorButton = new JButton("Color");
        colorButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color c;
                        c = JColorChooser.showDialog((Component) (e.getSource()),"Color Picker", Color.blue);
                        colorButton.setForeground(c);
                    }
                });
        menuBar.add(colorButton);
        setJMenuBar(menuBar);
        this.pack();
    }
    
    private void updateContentPane() {
        // Create the content-pane-to-be.
        
        if (model == null) {
            contentPane = new JPanel(new BorderLayout());
        } else {
            contentPane = model.canvas();
        }
        contentPane.setOpaque(true);
    
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
        
    }

    /**
     * Update the board Image from the current model.
     */
    public void updateBoard() {
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
        System.out.println("Update board list");
    }

    /**
     * Clear the model and deactivate drawing to the canvas until
     * setModel() is called.
     */
    public void clearModel() {
        this.model = null;
        updateContentPane();
        pack();
    }
}
