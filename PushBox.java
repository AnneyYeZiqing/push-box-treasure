import javax.imageio.ImageIO;
import java.lang.Math;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

@SuppressWarnings("serial")
public class PushBox extends JPanel implements ActionListener {
	
	//GUI-related variables
	private JLabel picLabel;			//where the drawing happens
	private JLabel mouseLabel; 			//show tile coordinates of mouse
	private JButton startBtn;
	private JButton stopBtn;
	private JButton resetBtn;
	private JButton imageBtn;
	private JTextField sizeTxt;
	private JTextField neighborTxt;
	private Image pic;					//var to display the tiles grid
	private Color[] colors;				//the different colors to use
	private int colorsToUse;			//for more than 2 colors
	private int vOffset;				//for mouse
	private int hOffset;				//for mouse
	private int vMax;					//vertical size of picture space 
	private int hMax;					//horizontal size of picture space
	private int[] mouseCoords;			//where the mouse is
	private JLabel moves;			//states number of moves the player has made
	
	//input-related variables
	//TODO: add keyboard-input related stuff here!
	
	//game-related variables
	private int movCount;				//number of moves already conducted
	private int tilesPerRow;
	private int tilesPerCol;
	private int size;					//how big each tile is in pixels
	private Tile[][] tiles;				//the tiles array
	private int playerX; //x-coordinate of player
	private int playerY; //y-coordinate of player
//	private int stage;
	private int genSkip;
	int pshbxDstX = 5;
	int pshbxDstY = 0;
	
	//system-related variables
	private Timer timer;
	private boolean isRunning;
	
    public PushBox(int xDim, int yDim, int numColors) {
        super(new GridBagLayout());                       				// set up graphics window
        setBackground(Color.LIGHT_GRAY);
		addMouseListener(new MAdapter());
		addMouseMotionListener(new MAdapter());
		addKeyListener(new KeyInput());
		setFocusable(true);
		setDoubleBuffered(true);
		//initialize the colors using rgb
		colors = new Color[] { //TODO: designate what each color symbolizes in terms of tile's status
				new Color(255, 255, 255), //white //TODO: change color palatte based on theme
				new Color(241, 239, 208), //beige
				new Color(209, 241, 248), //light blue
				new Color(215, 215, 249), //light purple/violet
				new Color(193, 230, 173), //light green
				new Color(254, 223, 128), //orange
				new Color(248, 209, 209), //light red
				new Color(199, 211, 226) //light navy blue
		}; 
		colorsToUse = numColors + 1;		//+1 for the empty tile color
		initBtns();
		initTxt();
		initLabels();
		initGameVars();
		pic = new BufferedImage(xDim, yDim, BufferedImage.TYPE_INT_RGB);
		picLabel = new JLabel(new ImageIcon(pic));
		addThingsToPanel();
		//a lot of initialization
		genSkip = 1;
		vMax = yDim;
		hMax = xDim;
		size = Integer.parseInt(sizeTxt.getText());
		isRunning = true;
		tiles = new Tile[vMax / size][hMax / size];		//initialize the tiles
		resetSim();										//initialize the simulation
		updateGraphics(pic.getGraphics());					//draw the initial set up
		timer = new Timer(1, this);	//initialize the timer
		timer.start();									//start up the sim
    }

    public void initGameVars(){ //initializes game-related variables
    	movCount = 0;
		playerX = 0;
		playerY = 0;
    }
    
    public void addThingsToPanel() {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 0, 1);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		c.gridheight = 10;
		add(picLabel, c);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 2, 0, 2);
		c.gridx = 0;
		c.gridy = 0;
		add(startBtn, c);
		c.gridx = 1;
		add(stopBtn, c);
		c.gridx = 2;
		add(resetBtn, c);
		c.gridx = 3;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 4;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		add(moves, c);
		c.gridx = 6;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(imageBtn, c);
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 7;
		add(new JLabel("Tile size"), c);
		c.gridx = 7;
		c.gridy = 7;
		add(sizeTxt, c);
		c.gridy = 9;
		add(moves, c);
		c.gridwidth = 4;
		c.gridx = 1;
		c.gridy = 11;
		add(mouseLabel, c);
    }
    
    public void initTxt() {
    	sizeTxt = new JTextField("50", 4); //how large each tile in the grid is in pixels
    	sizeTxt.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			size = Integer.parseInt(sizeTxt.getText());
				resetSim();
				updateGraphics(pic.getGraphics());
    		}
    	});

    }

    public void initLabels() {
    	moves = new JLabel("Moves: " + movCount);
    	mouseCoords = new int[2];
    	mouseLabel = new JLabel("Mouse off-grid");
    }
    
    public void initBtns() { //similar stuff to game of life
		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = true;
			}
		});
		
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
			}
		});
		
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetSim();
				updateGraphics(pic.getGraphics());
			}
		});    	
		
		imageBtn = new JButton("Save Picture");
		imageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Calendar c = Calendar.getInstance();
					String fileName = ".\\" + moves + " moves @" + c.get(Calendar.HOUR) + "." + c.get(Calendar.MINUTE) + "." + c.get(Calendar.SECOND)+ ".png";
					System.out.println(fileName);
					File outputFile = new File(fileName);
					outputFile.createNewFile();
					ImageIO.write((RenderedImage) pic, "png", outputFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
    }
    
    public PushBox() {
        super();
        setBackground(Color.WHITE);
		addMouseListener(new MAdapter());
		addKeyListener(new KeyInput());
		setFocusable(true);
		setDoubleBuffered(true);
	}
 
    public void paintComponent(Graphics g) { 	                 // draw graphics in the panel
        super.paintComponent(g);                              	 // call superclass' method to make panel display correctly
    }
    
    
    //moves the player and all boxes
    public void playerMove(char c) { //ditch isPulling for rn
    	boolean isPulling = false;
    	switch(c) {
    	case 'r':
    		System.out.println("attempted right movement, player @ (" + playerX + "," + playerY + ").");
    		tryMovRight(isPulling);
    		break;
    	case 'l':
    		tryMovLeft(isPulling);
    		break;
    	case 'u':
    		tryMovUp(isPulling);
    		break;
    	case 'd':
    		tryMovDown(isPulling);
    		break;
    	}
    }

	
	  public void tryMovRight(boolean isPulling) { //move oneself first
		  int targX = playerX+1;
		  int targY = playerY;
		  if (targX >= tiles.length || targY > tiles[targX].length) {
			  System.out.println("hit the walls");
			  return;
			  }
		  if (tiles[targX][targY].isInactive() == false) { //space available
			  System.out.println("tile available @ (" + targX + "," + targY + ").");
			  if (tiles[targX][targY].getPushBox() == true) {
				  System.out.println("try pushing a box @ " + targX + "," + targY);
				  if ((targX+1) < tiles.length && tiles[(targX+1)][targY].isInactive() == false) {
					  //move box to right
					  tiles[(targX+1)][targY].setPushBox(true);
					  tiles[targX][targY].setPushBox(false);
					  System.out.println("Box moved to " + (targX+1) + ", " + targY);
					  playerX++;
					  tiles[targX][targY].setPlayer(true);
					  System.out.println("Player moved to " + targX + ", " + targY);
				  }
				  else { System.out.println("Box hit wall, cannot move box to " + (targX+1) + ", " + targY);
				  System.out.println("Player blocked by box, cannot move to " + targX + ", " + targY);}
			  }
			  else {
				  playerX++;
				  tiles[targX][targY].setPlayer(true);
				  System.out.println("Player moved to " + targX + ", " + targY);
			  }
		  }
		  checkWin();
	  }
	 
	  public void tryMovLeft(boolean isPulling) { //move oneself first
		  int targX = playerX-1;
		  int targY = playerY;
		  if (targX < 0 || targY > tiles[targX].length) {return;}
		  if (tiles[targX][targY].isInactive() == false) { //space available 
			  if (tiles[targX][targY].getPushBox() == true) {
				  if (targX-1 < 0 && tiles[targX+1][targY].isInactive() == false) {
					  //move box to right
					  tiles[targX+1][targY].setPushBox(true);
					  tiles[targX][targY].setPushBox(false);
					  tiles[targX][targY].setPlayer(true);
				  }
			  }
		  }
		  checkWin();
	  }
	  
	  public void tryMovUp(boolean isPulling) { //move oneself first
		  int targX = playerX+1;
		  int targY = playerY;
		  if (targX >= tiles.length || targY > tiles[targX].length) {return;}
		  if (tiles[targX][targY].isInactive() == false) { //space available 
			  if (tiles[targX][targY].getPushBox() == true) {
				  if (targX+1 < tiles.length && tiles[targX+1][targY].isInactive() == false) {
					  //move box to right
					  tiles[targX+1][targY].setPushBox(true);
					  tiles[targX][targY].setPushBox(false);
					  tiles[targX][targY].setPlayer(true);
				  }
			  }
		  }
		  checkWin();
	  }
    
	  public void tryMovDown(boolean isPulling) { //move oneself first
		  int targX = playerX+1;
		  int targY = playerY;
		  if (targX > tiles.length || targY > tiles[targX].length) {return;}
		  if (tiles[targX][targY].isInactive() == false) { //space available 
			  if (tiles[targX][targY].getPushBox() == true) {
				  if (targX+1 < tiles.length && tiles[targX+1][targY].isInactive() == false) {
					  //move box to right
					  tiles[targX+1][targY].setPushBox(true);
					  tiles[targX][targY].setPushBox(false);
					  tiles[targX][targY].setPlayer(true);
				  }
			  }
		  }
		  checkWin();
	  }
    
    public void updateGraphics(Graphics g) {
    	for (int i = 0; i < tiles.length; i++) {
    		for (int j = 0; j < tiles[i].length; j++) {
    			//color is an array containing preset colors,thus
    			g.setColor(colors[tiles[i][j].getColor()]); //get color based on the status of the tile
    			g.fillRect(i*size, j*size, size, size); 
    		}
    	}
    }

    //initializes the stage
    public void resetSim() {
    	tiles = new Tile[hMax / size][vMax / size]; //dump the old tiles array
    	int pushboxX = 1; //x coordinate of push box
    	int pushboxY = 0; //y coordinate of push box

		  System.out.println("starts new game");
    	//your double for loop goes here
    	for (int i = 0; i < tiles.length; i++) {//randomly assigns a tile's initial state of being either alive or dead
			for (int j = 0; j < tiles[i].length; j++) {
				if (i == pshbxDstX && j == pshbxDstY) {
					tiles[i][j] = new Tile(i, j, true, false, false);
				}
			else {
				tiles[i][j] = new Tile(i, j, false, false, false);//tiles are by default active and empty first
			}
			}
		}
		//TODO: "put" obstacles
		//TODO: put boxes
		//TODO: put player
    	tiles[pushboxX][pushboxY].setPushBox(true);
    	tiles[playerX][playerY].setPlayer(true);
    	updateGraphics(pic.getGraphics());
    	
    	//other initializations
		isRunning = false;
		movCount = 0;
    }
    
    public boolean checkWin() {
    	return tiles[pshbxDstX][pshbxDstY].getPushBox();
    }
    
    public void updateLabels() { //keep labels updated with the latest statistics!
    	moves.setText("Moves: " + movCount);
    	if ((mouseCoords[0] >= 0) && (mouseCoords[0] < tiles.length) && (mouseCoords[1] >= 0) && (mouseCoords[1] < tiles[0].length)) {
    		mouseLabel.setText("Mouse at (" + mouseCoords[0] + ", " + mouseCoords[1] + ").");    	    		
    	} else {
    		mouseLabel.setText("Mouse off-grid");
    	}
    }
    
    //fairly straightforward and similar to the actionPerformed methods in other projects
	@Override
	public void actionPerformed(ActionEvent e) {
		if (isRunning) {
			//TODO: get keyboard actions
	        updateGraphics(pic.getGraphics());
		}
		hOffset = picLabel.getLocationOnScreen().x - getLocationOnScreen().x;
		vOffset = picLabel.getLocationOnScreen().y - getLocationOnScreen().y;
		updateLabels();
		repaint();
	}
	
	
	class KeyInput extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			
			int key = e.getKeyCode();
			char dir;

		//different cases for each possibility : up down left right space 
			switch(key) {
			case KeyEvent.VK_LEFT:
				dir = 'l';
				
				playerMove(dir);
				break;
			
			case KeyEvent.VK_RIGHT:
				dir = 'r';
				
				playerMove(dir);
				break;
			case KeyEvent.VK_UP:
				dir = 'u';
				playerMove(dir);
				break;

			case KeyEvent.VK_DOWN:
				dir = 'd';
				playerMove(dir);
				break;
			

			case KeyEvent.VK_R:
				resetSim(); 
				break;
			
			default:
				break;
			}
		}
		}
	
	//where the mouse handler goes
	//lots of old stuff from game of life, maybe you'll use them, maybe not
	private class MAdapter extends MouseAdapter {
		
//		@Override
//		public void mousePressed(MouseEvent e) {
//			Point p = new Point((e.getX() - hOffset) / size, (e.getY() - vOffset) / size);
//			try {
//				tiles[p.x][p.y] = 1 - tiles[p.x][p.y];
//				updateGraphics(pic.getGraphics());
//				mouseDraw = !mouseDraw;
//			} catch (ArrayIndexOutOfBoundsException e2) {
//			}
//		}
//		
		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = new Point((e.getX() - hOffset) / size, (e.getY() - vOffset) / size);
//			System.out.println(hOffset + " " + e.getXOnScreen() + ", " + e.getYOnScreen() + " grid " + p.x*size + ", " + p.y*size);
			mouseCoords[0] = p.x;
			mouseCoords[1] = p.y;			
		}

