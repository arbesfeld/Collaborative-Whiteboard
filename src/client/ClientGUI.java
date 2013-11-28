package client;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
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
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import models.BoardModel;
import name.BoardIdentifier;
import stroke.StrokeProperties;
import util.Utils;


class ClientGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    private final JMenuBar menuBar;
    private final JMenu menu;
    
    private final Container container;
    private final GroupLayout layout;
    private JPanel canvas;
    
    private final JMenu joinGameSubmenu;
    private final JMenuItem newBoard;
    private final JButton colorButton;
    private final JColorChooser colorChooser;
    private final JButton strokeButton;
    private final JSlider strokeSlider;
    private final JToggleButton eraseToggle;
    
//    private Cursor brushCursor;
//    private final Image iconImage;
    
    private final JPanel sidebar;
    private final JPanel chatBar;
    
    private final JTextArea chatText;
    
    private static final int STROKE_MAX = 10;
    private static final int STROKE_MIN = 1;
    private static final int STROKE_INIT = StrokeProperties.DEFAULT_STROKE_WIDTH;
    
    private BoardIdentifier[] boardNames;
    private BoardModel model;
    private ClientController controller;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public ClientGUI() {
        setResizable(false);
        this.container = this.getContentPane();
        this.layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // Create Cursor
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        this.iconImage = toolkit.getImage("resources/cursor.gif");
//        Point hotSpot = new Point(16,16);
//        this.brushCursor = toolkit.createCustomCursor(iconImage, hotSpot, "circleBrush");
        
        // Create the menu bar.
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("File");
        this.newBoard = new JMenuItem("New Board", KeyEvent.VK_T);
        this.colorButton = new JButton("Color");
        this.colorButton.setIcon(new ColorIcon(10, Color.black));
        this.colorButton.setFocusPainted(false);
        this.colorChooser = new JColorChooser();
        this.strokeButton = new JButton("Stroke");
        this.strokeButton.setFocusPainted(false);
        this.strokeButton.setIcon(new ColorIcon(STROKE_INIT, Color.black));
        this.strokeSlider = new JSlider(JSlider.HORIZONTAL, STROKE_MIN, STROKE_MAX, STROKE_INIT);
        this.eraseToggle = new JToggleButton(new ImageIcon("resources/eraserIcon.gif"));
        this.eraseToggle.setFocusPainted(false);
        
        // Join Game submenu.
        this.joinGameSubmenu = new JMenu("Join Game");
        
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Build Sidebar
        this.sidebar = new JPanel();
        this.chatBar = new JPanel();
        this.chatText = new JTextArea();
        this.chatText.setEditable(false);
        setSideBar();
        
        //Create and set up the content pane.
        setMenuBar();
        updateContentPane();
        
    }

    public void setController(ClientController controller) {
        assert this.controller == null;
        this.controller = controller;
    }
    
    private void setSideBar() {
        
        sidebar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        sidebar.add(strokeButton, c);
        
        c.gridx = 0;
        c.gridy = 1;
        sidebar.add(colorButton, c);
        
        c.gridx = 0;
        c.gridy = 2;
        sidebar.add(eraseToggle, c);
        setChatClient();
        
        c.gridx = 0;
        c.gridy = 3;
        sidebar.add(chatBar, c);
        
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColor(e);
            }
        });

        strokeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.add(strokeSlider);
                popup.pack();
                Point pos = new Point();
                Dimension size = popup.getPreferredSize();
                pos.x = (strokeButton.getWidth()/2 - size.width/2);
                pos.y = (strokeButton.getHeight());
                popup.show(strokeButton, pos.x, pos.y);
            }
        });
        
        strokeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateCursor();
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
                
    }
       
    private void setChatClient() {
        chatBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JTextField inputTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        chatText.append("\n \n \n \n");
        
        chatBar.setLayout(new GridBagLayout());
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        chatBar.add(inputTextField, c);
     
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.2;
        c.gridx = 2;
        c.gridy = 1;
        chatBar.add(sendButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,0,0,0);
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        chatBar.add(chatText, c);
        
        
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

        menu.add(newBoard);
        menu.add(joinGameSubmenu);
        setJMenuBar(menuBar);
    }
    
    private void updateMenuBar() {
    	joinGameSubmenu.removeAll();
        if (boardNames == null) {
        	return;
        }
        
        for (final BoardIdentifier boardName : boardNames) {
            JMenuItem subMenuItem = new JMenuItem(boardName.name());        

            subMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    joinBoardAction(boardName);
                }
            });
            
            joinGameSubmenu.add(subMenuItem);
        }
    }
    
    private void setColor(ActionEvent e) {
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
        
        JDialog dialog = JColorChooser.createDialog((Component) (e.getSource()), "Color Picker", false, colorChooser, okListener, null);
        dialog.setVisible(true);
        
    }
    
    private void setStroke() {
        strokeButton.setIcon(new ColorIcon(strokeSlider.getValue(), Color.black));
        controller.setStrokeWidth(strokeSlider.getValue());
    }
    
    private void updateContentPane() {
        if (model == null) {
            this.canvas = new JPanel();
        } else {
            this.canvas = model.canvas();
        }

        Group horizontal = layout.createSequentialGroup();
        horizontal.addGroup(layout.createParallelGroup(LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(canvas)
                        .addComponent(sidebar)));
        layout.setHorizontalGroup(horizontal);
        
        Group vertical = layout.createSequentialGroup();
        vertical.addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(canvas)
                .addComponent(sidebar));
        layout.setVerticalGroup(vertical);
        this.pack();
    }
    
    private void updateCursor() {
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        int strokeWidth = strokeSlider.getValue();
//        Image scaledIcon = iconImage.getScaledInstance(5, 5, Image.SCALE_DEFAULT);
//        Point hotSpot = new Point(strokeWidth/2, strokeWidth/2);
//        brushCursor = toolkit.createCustomCursor(scaledIcon, hotSpot, "circleBrush");
//        this.canvas.setCursor(brushCursor);
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
            BoardIdentifier boardName = new BoardIdentifier(Utils.generateId(), inputBoardName.getText());

            if (model != null) {
                controller.disconnectFromCurrentBoard();
            }
            controller.generateNewBoard(boardName, Integer.parseInt(widthName.getText()),
                						Integer.parseInt(heightName.getText()));

        } 
        
    }
    
    private void joinBoardAction(BoardIdentifier boardName) {
        if (model != null) {
            controller.disconnectFromCurrentBoard();
        }
        controller.connectToBoard(boardName);
    }
    
    /**
     * Set the current model and allow the user to draw to the screen.
     * @param model
     */
    public void setModel(BoardModel model) {
        this.model = model;
        updateContentPane();
        updateUserList();
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
    public void updateBoardList(BoardIdentifier[] boards) {
        this.boardNames = boards;
        updateMenuBar();
    }
    
    /**
     * Add new chat message
     * @param string
     */
    public void addChatLine(String string) {
        this.chatText.append("\n" + string);
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
            g2d.setColor(Color.black);
            g2d.fillRect(x, y, size, size);
            g2d.setColor(color);
            g2d.fillRect(x+1, y+1, size - 2, size -2);
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
