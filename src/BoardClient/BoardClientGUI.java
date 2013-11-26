package BoardClient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import models.ClientBoardModel;

import packet.BoardName;


public class BoardClientGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    
    private JTextArea output;
    private JScrollPane scrollPane;
    private static JMenuBar menuBar;
    
    private List<String> boardNames = new ArrayList<String>(); 
    private ClientBoardModel model;

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void display() {
        //Create and set up the window.
        JFrame frame = new JFrame("MenuLookDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Create and set up the content pane.
        updateMenuBar();
        frame.setJMenuBar(menuBar);
        frame.setContentPane(createContentPane());
    
        //Display the window.
        frame.setSize(450, 260);
        frame.setVisible(true);
    }
    
    private JMenuBar updateMenuBar() {
        JMenu menu, submenu;
        JMenuItem menuItem;
    
        //Create the menu bar.
        menuBar = new JMenuBar();
    
        //Build the first menu.
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
                display();
                pack();
            }});
        menu.add(menuItem);
    
        //join game submenu
        submenu = new JMenu("Join Game");       
        for (String boardName : boardNames) {
            menuItem = new JMenuItem(boardName);
            submenu.add(menuItem);
        }
        menu.add(submenu);
        return menuBar;
    }
    
    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
    
        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
    
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
    
        return contentPane;
    }
    
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BoardClientGUI view = new BoardClientGUI();
                view.display();
            }
        });
    }
    
    
    /**
     * Set the current model and allow the user to draw to the screen.
     * @param model
     */
    public void setModel(ClientBoardModel model) {
        this.model = model;
    }

    /**
     * Update the list of users from the current model.
     */
    public void updateUserList() {
        assert this.model != null;
        
        // TODO
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
        // TODO
    }

    /**
     * Clear the model and deactivate drawing to the canvas until
     * setModel() is called.
     */
    public void clearModel() {
        this.model = null;
        // TODO
    }
}
