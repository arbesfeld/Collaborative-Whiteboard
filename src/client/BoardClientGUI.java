package client;

import java.awt.BorderLayout;

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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
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
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Group;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import models.BoardModel;
import name.BoardIdentifier;
import stroke.StrokeProperties;
import util.Utils;


class BoardClientGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    private final JMenuBar menuBar;
    private final JMenu menu;
    
    private final Container container;
    private final GroupLayout layout;
    private JPanel canvas;
    
    private final JMenu joinGameSubmenu;
    private final JMenuItem newBoard;
    private final JButton colorButton;
    private final JButton strokeButton;
    private final JSlider strokeSlider;
    private final JToggleButton eraseToggle;
    
    private final JPanel sidebar;
    
    
    private static final int STROKE_MAX = 10;
    private static final int STROKE_MIN = 1;
    private static final int STROKE_INIT = StrokeProperties.DEFAULT_STROKE_WIDTH;
    
    private BoardIdentifier[] boardNames;
    private BoardModel model;
    private BoardClientController controller;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public BoardClientGUI() {
        setResizable(false);
        this.container = this.getContentPane();
        this.layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        
        // Create the menu bar.
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("File");
        this.newBoard = new JMenuItem("New Board", KeyEvent.VK_T);
        this.colorButton = new JButton("Color");
        this.colorButton.setIcon(new ColorIcon(10, Color.black));
        //this.colorButton.setBorder(BorderFactory.createLineBorder(Color.black));
        this.colorButton.setFocusPainted(false);
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
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        setSideBar();
        
        //Create and set up the content pane.
        setMenuBar();
        updateContentPane();
        
    }

    public void setController(BoardClientController controller) {
        assert this.controller == null;
        this.controller = controller;
    }
    
    private void setSideBar() {
        
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
                // get the preferred size of the menu...
                Dimension size = popup.getPreferredSize();
                // Adjust the x position so that the left side of the popup
                // appears at the center of  the component
                pos.x = (strokeButton.getWidth()/2 - size.width/2);
                // Adjust the y position so that the y postion (top corner)
                // is positioned so that the bottom of the popup
                // appears in the center
                pos.y = (strokeButton.getHeight());
                popup.show(strokeButton, pos.x, pos.y);
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
                
        sidebar.add(strokeButton);
        sidebar.add(colorButton);
        sidebar.add(eraseToggle);
        
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
        strokeButton.setIcon(new ColorIcon(strokeSlider.getValue(), Color.black));
        controller.setStrokeWidth(strokeSlider.getValue());
    }
    
    private void updateContentPane() {
        if (model == null) {
            canvas = new JPanel(new BorderLayout());
        } else {
            canvas = model.panel();
        }
        
        canvas.setVisible(true);
        canvas.revalidate();
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
    public void updateBoardList(BoardIdentifier[] boards) {
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
