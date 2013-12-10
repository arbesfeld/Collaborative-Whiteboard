package client;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import models.BoardModel;
import name.BoardIdentifier;
import name.Identifiable;
import stroke.StrokeProperties;
import stroke.StrokeType;
import stroke.StrokeTypeBasic;
import stroke.StrokeTypeFill;
import stroke.StrokeTypeFur;
import stroke.StrokeTypePressure;
import stroke.StrokeTypeProc1;
import stroke.StrokeTypeProc2;
import stroke.StrokeTypeProc3;
import stroke.StrokeTypeProc4;
import stroke.StrokeTypeProc5;
import stroke.StrokeTypeProc6;
import stroke.StrokeTypeProc7;
import stroke.StrokeTypeSpray;
import stroke.StrokeTypeSquares;
import util.MineralNames;
import util.Utils;


class ClientGUI extends JFrame{
    private static final long serialVersionUID = -8313236674630578250L;
    
    private final Container container;
    private final GroupLayout layout;
    private JPanel canvas;
    
    private final boolean isWindows;
    
    private final JMenuItem save;
    
    private final JMenu joinGameSubmenu;
    private final JButton colorButton;
    private final JToggleButton dropperToggle;
    private final JColorChooser colorChooser;
    private final JButton strokeButton;
    private final JSlider strokeSlider;
    private final JToggleButton eraseToggle;
    private final JToggleButton fillToggle;
    private final JToggleButton brushToggle;
    private final JToggleButton cloneToggle;
    private final JComboBox<StrokeType> strokeDropdown;
    private final JSlider symetrySlider;
    
    private final StrokeType[] strokeTypes;
    
    private Cursor brushCursor;
    private Cursor fillCursor;
    private Cursor dropperCursor;
    private Cursor eraserCursor;
    private final Image iconImage;
    
    private final JPanel sidebar;
    private final JPanel chatBar;
    private String title="Whiteboard: Interactive Drawing Tool";
    
    private final JTable userTable;
    private final JTextArea chatText;
    private final UserTableModel tableModel;
    
    private static final int STROKE_MAX = 20;
    private static final int STROKE_MIN = 1;
    private static final int STROKE_INIT = StrokeProperties.DEFAULT_STROKE_WIDTH;
    
    private ClientController controller;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public ClientGUI() {
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");  
        setResizable(false);
        this.container = this.getContentPane();
        this.layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        this.save = new JMenuItem("Save to png", KeyEvent.VK_T);
        
        // Set the title
        setTitle(title);
        
        // Create Cursors
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.iconImage = toolkit.getImage("resources/cursor.png");
        Point hotSpot = new Point(16,16);
        this.brushCursor = toolkit.createCustomCursor(iconImage, hotSpot, "circleBrush");
        
        Image fillImage = toolkit.getImage("resources/fillCursor.png");
        hotSpot = new Point(2,30);
        this.fillCursor = toolkit.createCustomCursor(fillImage, hotSpot, "fillCursor");
        
        fillImage = toolkit.getImage("resources/eyedropperCursor.png");
        hotSpot = new Point(2,30);
        this.dropperCursor = toolkit.createCustomCursor(fillImage, hotSpot, "dropperCursor");

        fillImage = toolkit.getImage("resources/eraserCursor.png");
        hotSpot = new Point(2,2);
        this.eraserCursor = toolkit.createCustomCursor(fillImage, hotSpot, "eraserCursor");
        
        this.colorButton = new JButton("Color");
        this.colorButton.setIcon(new ColorIcon(10, Color.black));
        this.colorButton.setFocusPainted(false);

        this.colorButton.setHorizontalAlignment(SwingConstants.LEFT);
        this.colorChooser = new JColorChooser();
        this.strokeButton = new JButton("Stroke");
        this.strokeButton.setFocusPainted(false);
        this.strokeButton.setIcon(new ColorIcon(STROKE_INIT, Color.black));

        this.strokeButton.setHorizontalAlignment(SwingConstants.LEFT);
        this.strokeSlider = new JSlider(JSlider.HORIZONTAL, STROKE_MIN, STROKE_MAX, STROKE_INIT);
        
        this.dropperToggle = new JToggleButton(new ImageIcon(((new ImageIcon("resources/eyedropperIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.dropperToggle.setDisabledIcon(new ImageIcon(((new ImageIcon("resources/eyedropperSelectedIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH))); 
        this.dropperToggle.setFocusPainted(false);
        this.dropperToggle.setPreferredSize(new Dimension(40, 40));
        this.eraseToggle = new JToggleButton(new ImageIcon(((new ImageIcon("resources/eraserIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.eraseToggle.setDisabledIcon(new ImageIcon(((new ImageIcon("resources/eraserSelectedIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.eraseToggle.setPreferredSize(new Dimension(40,40));
        this.eraseToggle.setFocusPainted(false);
        this.fillToggle = new JToggleButton(new ImageIcon(((new ImageIcon("resources/fillIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.fillToggle.setDisabledIcon(new ImageIcon(((new ImageIcon("resources/fillSelectedIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.fillToggle.setFocusPainted(false);
        this.fillToggle.setPreferredSize(new Dimension(40,40));
        this.cloneToggle = new JToggleButton(new ImageIcon(((new ImageIcon("resources/stampIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.cloneToggle.setDisabledIcon(new ImageIcon(((new ImageIcon("resources/stampSelectedIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.cloneToggle.setFocusPainted(false);
        this.cloneToggle.setPreferredSize(new Dimension(40,40));
        this.brushToggle = new JToggleButton(new ImageIcon(((new ImageIcon("resources/brushIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.brushToggle.setDisabledIcon(new ImageIcon(((new ImageIcon("resources/brushSelectedIcon.png")).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));  
        this.brushToggle.setPreferredSize(new Dimension(40,40));
        this.brushToggle.setFocusPainted(false);
        this.brushToggle.setEnabled(false);
        this.brushToggle.setSelected(true);
        
        this.symetrySlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        this.symetrySlider.setMinorTickSpacing(1);
        this.symetrySlider.setPaintTicks(true);
        this.symetrySlider.setSnapToTicks(true);
        
        //Build stroke dropdown
        this.strokeTypes = new StrokeType[6];
            strokeTypes[0] = new StrokeTypeBasic();
            strokeTypes[1] = new StrokeTypePressure();
            //strokeTypes[2] = new StrokeTypeSpray();
            //strokeTypes[3] = new StrokeTypeProc2();
            //strokeTypes[4] = new StrokeTypeProc1();
            //strokeTypes[5] = new StrokeTypeProc3();
            //strokeTypes[6] = new StrokeTypeProc4();
            //strokeTypes[7] = new StrokeTypeProc5();
            strokeTypes[2] = new StrokeTypeProc6();
            strokeTypes[3] = new StrokeTypeSquares(); 
            strokeTypes[4] = new StrokeTypeFur(); 
            strokeTypes[5] = new StrokeTypeProc7();

            
       this.strokeDropdown = new JComboBox<StrokeType>(strokeTypes);
       this.strokeDropdown.setFocusable(false);
        
        // Join Game submenu.
        this.joinGameSubmenu = new JMenu("Join Game");
        
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Build Sidebar
        this.sidebar = new JPanel();
        this.chatBar = new JPanel();
        this.chatText = new JTextArea();
        this.chatText.setEditable(false);
        this.tableModel = new UserTableModel();
        this.userTable = new JTable(this.tableModel);
        setSideBar();
        
        if (isWindows) {
            this.brushCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            this.colorButton.setPreferredSize(new Dimension(80,30));
            this.strokeButton.setPreferredSize(new Dimension(90, 30));
            this.strokeDropdown.setPreferredSize(new Dimension(120,30));
        }
        else {
            this.colorButton.setPreferredSize(new Dimension(60,30));
            this.strokeButton.setPreferredSize(new Dimension(70, 30));
            this.strokeDropdown.setPreferredSize(new Dimension(100,30));
        }
        
        
        // Create and set up the content pane.
        buildMenuBar();
        setContentPaneGUI(null);

    }

    public void setController(ClientController controller) {
        assert this.controller == null;
        this.controller = controller;

    }
    
    private void setSideBar() {
        
        
        JPanel brushPanel = new JPanel();
        brushPanel.add(strokeButton);
        brushPanel.add(colorButton);
        brushPanel.add(strokeDropdown);
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.add(new JLabel("Symetry"));
        sliderPanel.add(symetrySlider);
        
        JPanel allBrushPanel = new JPanel();
        allBrushPanel.setLayout(new BoxLayout(allBrushPanel, BoxLayout.Y_AXIS));
        allBrushPanel.add(brushPanel);
        allBrushPanel.add(sliderPanel);
        TitledBorder BrushPanelBorder = BorderFactory.createTitledBorder("Brush Settings");
        allBrushPanel.setBorder(BrushPanelBorder);
        
        JPanel toolBar = new JPanel();
        toolBar.add(brushToggle);
        toolBar.add(fillToggle);
        toolBar.add(cloneToggle);
        toolBar.add(eraseToggle);
        toolBar.add(dropperToggle);
        TitledBorder toolBorder = BorderFactory.createTitledBorder("Tools");
        toolBar.setBorder(toolBorder);
        
        setChatClient();
        TitledBorder chatBorder = BorderFactory.createTitledBorder("Chat Client");
        chatBar.setBorder(chatBorder);
        
        sidebar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(5,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        sidebar.add(allBrushPanel, c);
        
        c.gridx = 0;
        c.gridy = 1;
        sidebar.add(toolBar, c); 
        
        c.gridx = 0;
        c.gridy = 2;
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
        
        strokeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setStrokeType((StrokeType)strokeDropdown.getSelectedItem());
            }
        });
        
        symetrySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.setSymetry(symetrySlider.getValue());;
            }
        });
        
        brushToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (brushToggle.isSelected()) {
                    setBrushToggles(true,false,false,false,false);
                    brushToggle.setEnabled(false);
                    updateCursor();
                }
                else {
                    brushToggle.setEnabled(true);
                }
            }
        });
        
        cloneToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cloneToggle.isSelected()) {
                    cloneToggle.setEnabled(false);
                    setBrushToggles(false,false,true,false,false);
                    //TODO implement clone
                }
                else {
                    cloneToggle.setEnabled(true);
                }
            }
        });
        
        eraseToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (eraseToggle.isSelected()) {
                    eraseToggle.setEnabled(false);
                    setBrushToggles(false,false,false,true,false);
                    controller.setEraserOn(true);
                    canvas.setCursor(eraserCursor);
                }
                else {
                    eraseToggle.setEnabled(true);
                    controller.setEraserOn(false);
                }
            }
        });
        
        fillToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (fillToggle.isSelected()) {
                    fillToggle.setEnabled(false);
                    setBrushToggles(false,true,false,false,false);
                    controller.setFillOn(true);
                    canvas.setCursor(fillCursor);
                }
                else {
                    fillToggle.setEnabled(true);
                    controller.setFillOn(false);
                }
            }
        });
        
        dropperToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (dropperToggle.isSelected()) {
                    dropperToggle.setEnabled(false);
                    setBrushToggles(false,false,false,false,true);
                    try {
                        final Robot robot = new Robot();
                        canvas.setCursor(dropperCursor);

                        controller.setStrokeWidth(-1);
                        MouseListener mouseListener = new MouseListener() {
                            
                        	@Override
                            public void mouseClicked(MouseEvent e) {
                                PointerInfo pointer;
                                pointer = MouseInfo.getPointerInfo();
                                Point coord = pointer.getLocation();
                                Color color = robot.getPixelColor((int)coord.getX(), (int)coord.getX());
                                colorButton.setIcon(new ColorIcon(10, color));
                                controller.setStrokeColor(color);
                                canvas.removeMouseListener(this); 
                                brushToggle.setSelected(true);
                                updateCursor();
                                setStroke();
                            }
                            @Override
                            public void mouseEntered(MouseEvent arg0) { }
    
                            @Override
                            public void mouseExited(MouseEvent arg0) { }
    
                            @Override
                            public void mousePressed(MouseEvent arg0) { }
    
                            @Override
                            public void mouseReleased(MouseEvent arg0) { }
                        };
                        canvas.addMouseListener(mouseListener);
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                } 
                else {
                    dropperToggle.setEnabled(true);
                }  
            }
        });
        
    }
    
    private void setBrushToggles(boolean brush, boolean fill, boolean clone, boolean eraser, boolean dropper) {
        brushToggle.setSelected(brush);
        cloneToggle.setSelected(clone);
        eraseToggle.setSelected(eraser);
        fillToggle.setSelected(fill);
        dropperToggle.setSelected(dropper);
    }
       
    private void setChatClient() {
        chatBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final JTextField inputTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        JScrollPane scroll = new JScrollPane (chatText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        chatBar.setLayout(new GridBagLayout());
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,0,0,0);
        c.ipadx = 120;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        chatBar.add(userTable, c);
        
        c.insets = new Insets(10,0,0,0);
        c.ipady = 60;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        chatBar.add(scroll, c);

        c.insets = new Insets(5,0,0,0);
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 2;
        chatBar.add(inputTextField, c);
        
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0,0,0,0);
        c.weightx = 0.2;
        c.gridx = 0;
        c.gridy = 3;
        chatBar.add(sendButton, c);


        
        ActionListener enterText = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendMessage(inputTextField.getText());
                inputTextField.setText("");
            }
        };
        
        sendButton.addActionListener(enterText);
        inputTextField.addActionListener(enterText);
        
        
    }
    
    private void buildMenuBar() {     
        // Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem newBoard = new JMenuItem("New Board", KeyEvent.VK_T);
        this.save.setEnabled(false);
        
        // Build the first menu.
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        
        
        this.save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        this.save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCanvas();
            }
        });
        
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
        menu.add(this.save);
        setJMenuBar(menuBar);
    }
    

    
    private void setBoardNames(BoardIdentifier[] boardNames) {
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
        strokeButton.setIcon(new ColorIcon(Math.max((int)(strokeSlider.getValue() * 3/5), 2), Color.black));
        controller.setStrokeWidth(strokeSlider.getValue());
    }
    
    private void setContentPaneGUI(BoardModel model) {
        if (model != null) {
            this.save.setEnabled(true);
            this.canvas = model.canvas();
            updateCursor();
            container.removeAll();
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
    }
    
    private void updateCursor() {
        if (!isWindows) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int strokeWidth = strokeSlider.getValue();
            Image scaledIcon = iconImage.getScaledInstance(strokeWidth, strokeWidth, Image.SCALE_SMOOTH);
            Point hotSpot = new Point(strokeWidth/2, strokeWidth/2);
            brushCursor = toolkit.createCustomCursor(scaledIcon, hotSpot, "circleBrush");
            this.canvas.setCursor(brushCursor);
        }
    }
    
    private void newBoardAction() {
        
        JTextField inputBoardName = new JTextField(MineralNames.getName());
        JTextField widthName = new JTextField("512");
        JTextField heightName = new JTextField("512");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        panel.add(new JLabel("Whiteboard Name"));
        panel.add(inputBoardName);
        panel.add(new JLabel("Width (px)"));
        panel.add(widthName);
        panel.add(new JLabel("Height (px)"));
        panel.add(heightName);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Create Board",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            BoardIdentifier boardName = new BoardIdentifier(Utils.generateId(), inputBoardName.getText());
            this.setTitle(title+ " - " + boardName.name());
            controller.generateNewBoard(boardName, Integer.parseInt(widthName.getText()),
                						Integer.parseInt(heightName.getText()));

        } 
        
    }
    
    private void saveCanvas() {
        BufferedImage bi = new BufferedImage(controller.getBoardWidth(), controller.getBoardHeight(), BufferedImage.TYPE_INT_ARGB); 
        Graphics g = bi.createGraphics();
        canvas.paint(g);
        g.dispose();
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try{
                ImageIO.write(bi,"png",file);
                }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void joinBoardAction(BoardIdentifier boardName) {
        this.setTitle(boardName.name());
        controller.connectToBoard(boardName);
    }
    
    /**
     * Set the current model and allow the user to draw to the screen.
     * @param model
     */
    public void setModel(final BoardModel model) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setContentPaneGUI(model);
         
            }
        });
    }

    /**
     * Update the list of users from the current model.
     */
	public void setUserList(Identifiable[] users) {
        assert users.length > 0;
        final Object[][] tableData = new Object[users.length][1];
        
        for (int i = 0; i < users.length; i++) {        
            tableData[i][0] = users[i].identifier().name();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tableModel.updateData(tableData);
                pack();
            }
        });
    }
    
    /**
     * Update the list of boards.
     * @param boards
     */
    public void updateBoardList(final BoardIdentifier[] boards) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setBoardNames(boards);
            }
        });
    }
    
    /**
     * Add new chat message
     * @param string
     */
    public void addChatLine(final String string) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	chatText.append(string + "\n");
            }
        });
    }
    
    /**
     * Creates icon for color button
     */
    private static class ColorIcon implements Icon {

        private int size;
        private int forcedSize;
        private Color color;

        public ColorIcon(int size, Color color) {
            this.size = size;
            this.color = color;
            this.forcedSize = 10;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.black);
            g2d.fillRect(x + (forcedSize - size) / 2, y + (forcedSize - size) / 2, size, size);
            g2d.setColor(color);
            g2d.fillRect(x + (forcedSize - size) / 2, y + (forcedSize - size) / 2, size - 2, size -2);
        }

        @Override
        public int getIconWidth() {
            return forcedSize;
        }

        @Override
        public int getIconHeight() {
            return forcedSize;
        }
    }
    
    public class UserTableModel extends AbstractTableModel{

        private String[] columnNames = {"Users"};      
        private Object[][] data = new Object[0][0];
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
      
        public void updateData(Object[][] newData) {
            data = newData;
            fireTableDataChanged();
        }

    }

}
