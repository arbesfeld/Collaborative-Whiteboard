package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import canvas.Canvas;

import name.BoardName;
import models.ClientBoardModel;

import server.BoardServer;
import stroke.StrokeProperties;

import util.Utils;


public class BoardClientGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    private final JMenuBar menuBar;
    private final JMenu menu;
    
    private final JMenu joinGameSubmenu;
    private final JMenuItem newBoard;
    private ColorIcon icon;
    private final JButton colorButton;
    private final JMenu strokeMenu;
    private final JSlider strokeSlider;
    private final JToggleButton eraseToggle;
    
    private static final int STROKE_MAX = 10;
    private static final int STROKE_MIN = 1;
    private static final int STROKE_INIT = StrokeProperties.DEFAULT_STROKE_WIDTH;
    
    private BoardName[] boardNames;
    private ClientBoardModel model;
    private BoardClientController controller;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public BoardClientGUI() {
        // Create the menu bar.
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("File");
        this.newBoard = new JMenuItem("New Board", KeyEvent.VK_T);
        this.colorButton = new JButton(new ColorIcon(10, Color.black));
        this.colorButton.setBorder(BorderFactory.createLineBorder(Color.black));
        this.colorButton.setFocusPainted(false);
        this.strokeMenu = new JMenu("Stroke");
        this.strokeMenu.setIcon(new ColorIcon(STROKE_INIT, Color.black));
        this.strokeSlider = new JSlider(JSlider.HORIZONTAL, STROKE_MIN, STROKE_MAX, STROKE_INIT);
        this.eraseToggle = new JToggleButton(new ImageIcon("resources/eraserIcon.gif"));
        this.eraseToggle.setFocusPainted(false);
        
        // Join Game submenu.
        this.joinGameSubmenu = new JMenu("Join Game");
        
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Create and set up the content pane.
        setMenuBar();
        updateContentPane();
    }

    public void setController(BoardClientController controller) {
        this.controller = controller;
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
        
        strokeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setStroke();
            }
        });
        
        eraseToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eraseToggle.isSelected()) {
                    eraseToggle.setFocusPainted(true);
                    controller.setEraserOn(true);
                }
                else {
                    eraseToggle.setFocusPainted(false);
                    controller.setEraserOn(false);
                }
            }
        });

        menu.add(newBoard);
        menu.add(joinGameSubmenu);
        strokeMenu.add(strokeSlider);
        menuBar.add(colorButton);
        menuBar.add(eraseToggle);
        menuBar.add(strokeMenu);
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
        final JColorChooser colorChooser = new JColorChooser();
        colorChooser.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
        for (int i = 0; i < oldPanels.length; i++) {
          String clsName = oldPanels[i].getClass().getName();
          if (clsName.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel")) {
            colorChooser.removeChooserPanel(oldPanels[i]);
          }
        }
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Color color = colorChooser.getColor();
                colorButton.setIcon(new ColorIcon(10, color));
                controller.setStrokeColor(color);
            }
          };
        
        JDialog dialog = JColorChooser.createDialog((Component) (e.getSource()), "ColorPicker", false, colorChooser, okListener, null);
        dialog.setVisible(true);
        
    }
    
    private void setStroke() {
        strokeMenu.setIcon(new ColorIcon(strokeSlider.getValue(), Color.black));
        controller.setStrokeWidth(strokeSlider.getValue());
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
        JTextField inputBoardName = new JTextField("Whiteboard");
        JTextField widthName = new JTextField("256");
        JTextField heightName = new JTextField("256");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        panel.add(new JLabel("Whiteboard Name"));
        panel.add(inputBoardName);
        panel.add(new JLabel("Width (px)"));
        panel.add(widthName);
        panel.add(new JLabel("Height (px)"));
        panel.add(heightName);
        int result = JOptionPane.showConfirmDialog(null, panel, "Connect To Server",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            BoardName boardName = new BoardName(Utils.generateId(), inputBoardName.getText());

            if (model != null) {
                controller.disconnectFromCurrentBoard();
            }
            controller.generateNewBoard(boardName, Integer.parseInt(widthName.getText()),
                						Integer.parseInt(heightName.getText()));

        } 
        
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
        updateUserList();
        pack();
    }

    /**
     * Update the list of users from the current model.
     */
    public void updateUserList() {
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
    
    /**
     * Creates icon for color button
     */
    private static class ColorIcon implements Icon {

        private int size;
        private Color color;

        public ColorIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillRect(x, y, size, size);
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
