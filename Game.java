import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas implements Runnable{
	private JPanel p;
	private Graphics g;
	public static int width = 600;
	public static int height = 500;
	private JFrame frame;
	Thread thread;
	
	int mX = 0;
	int mY = 0;
	boolean down = false;
	boolean down2 = false;
	boolean down3 = false;
	
	private BufferedImage image = new BufferedImage(width, height, 1);
	public int[] pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
	public byte[] boxes = new byte[width * height];
	public int[] movement = new int[width * height];
	
	
	
	
	
	
	public void draw() {
		BufferStrategy bs = getBufferStrategy();
	    if (bs == null){
	      createBufferStrategy(3);
	      return;
	    }
	    Graphics g = bs.getDrawGraphics();
	    g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
	    
	    g.dispose();
	    bs.show();
	}

	
	public enum boxType{
		WALL((byte)200,0x555555),
		SNOW((byte)1,0xffffff),
		
		WATER((byte)3,0x5555FF),
		LAVA((byte)5,0xFF5555)
		;
		
		int color;
		byte id;
		
		boxType(byte id,int color){
			this.color = color;
			this.id = id;
		}
	}
	
	
	public Game(int width, int height){
		thread = new Thread(this,"thread");
		JFrame gamescreen = new JFrame();
		frame = gamescreen;
		
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				mX = e.getX();
				mY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mX = e.getX();
				mY = e.getY();
			}
			
		});
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getButton());
				if(e.getButton() == 1)
					down = true;

				if(e.getButton() == 3)
					down2=true;

				if(e.getButton( )== 2)
					down3 = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == 1)
					down = false;

				if(e.getButton() == 3)
					down2 = false;

				if(e.getButton() == 2)
					down3 = false;
			}
			
		});
		
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()=='s'){
					for (int y = 0; y < 15; y++) {
						for (int x = 0; x < 15; x++) {
							if (x+1+mX < Game.width && y+1+mY < Game.height&&y+mY>0&&x+mX>0) {
								boxes[x+mX+(y+mY)*Game.width] = boxType.WALL.id;
							}
						}
					}
				}
				else if(e.getKeyChar() == 'a'){
					for (int y = 0; y < 15; y++) {
						for (int x = 0; x < 15; x++) {
							if (x+1+mX < Game.width && y+1+mY < Game.height&&y+mY>0&&x+mX>0) {
								boxes[x+mX+(y+mY)*Game.width] = boxType.LAVA.id;
							}
						}
					}
				}
				else if(e.getKeyChar()=='r')
					snowRain =! snowRain;
				
			}
			
		});
		this.setSize(width,height);

	    this.frame.setTitle("Sandbox game test");
	    this.frame.add(this);
	    this.frame.pack();
	    this.frame.setDefaultCloseOperation(3);
	    this.frame.setLocationRelativeTo(null);
	    this.frame.setVisible(true);
	    this.thread.start();
	
		boxes[5+4*Game.width] = 1;

	}


	@Override
	public void run() {
		while (true) {

			for(int i = 0;i < boxes.length; i++){
				switch(boxes[i]){
					case 0:
						//Background
						pixels[i]=0x333366;
						break;

					case 1:
					case 2:
						pixels[i]=boxType.SNOW.color;
						break;

					case 3:
					case 4:
						pixels[i]=boxType.WATER.color;
						break;

					case 5:
					case 6:
						pixels[i]=boxType.LAVA.color;
						break;

				case (byte)200:
					pixels[i]=0x555555;
					break;
				}
			}

			draw();
			update();

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("ERROR");
			}
		}
		
	}
	
	
	Random rand = new Random();
	
	double rotation = 0;
	boolean snowRain = false;
	
	
	private void rotate(){
		
		rotation += 0.01;
		if(rotation >= 4)
			rotation = 0;
		//0-1
		if(rotation <= 1){
			for(int i = 0;i < 20; i++){
				for(int j = 0; j < 3; j++){
					boxes[i+25+(j+25+(int)(i*rotation))*Game.width] = boxType.WALL.id;
				}
				
			}
		}
		//]1,2]
		else if(rotation > 1 && rotation <= 2){
			
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < 3; j++){
					boxes[((int)(i-i*(rotation%1)))+25+(j+25+i)*Game.width] = boxType.WALL.id;
				}
				
			}
		}
		else if(rotation > 2 && rotation <= 3){
			for(int i = 0;i<20;i++){
				for(int j = 0; j < 3; j++){
					boxes[25-((int)(i*(rotation%1)))  +  (j+25+i)*Game.width] = boxType.WALL.id;
				}
				
			}
		}
		//]1,2]
		else if(rotation > 3 && rotation <= 4){
			for(int i = 0;i < 20; i++){
				for(int j = 0; j < 3; j++){
					boxes[i+25+(j+25+(int)(i*(rotation%1)))*Game.width] = boxType.WALL.id;
				}
				
			}
		}
	}
	
	private void update() {

			for(int i = 0; i < 100; i++){
				for(int j = 0; j < 100; j++){
					boxes[25+i+(j+25)*Game.width] = 0;
				}
			}
		
		rotate();
		
		//snow
		if(snowRain)
		{
			boxes[rand.nextInt(Game.width)+(5)*Game.width] = boxType.SNOW.id;
			boxes[rand.nextInt(Game.width)+(5)*Game.width] = boxType.SNOW.id;
		}
			
		//Create snow when mousekey 1 is down
		if(down){
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (x+1+mX < Game.width && y+1+mY < Game.height&&y+mY>0&&x+mX>0) {
						boxes[x+mX+(y+mY)*Game.width] = boxType.SNOW.id;
					}
				}
			}
		}
		//Create rain when mousekey 2 is down
		else if(down2){
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (x+1+mX < Game.width && y+1+mY < Game.height&&y+mY>0&&x+mX>0) {
						boxes[x+mX+(y+mY)*Game.width]=boxType.WATER.id;
					}
				}
			}
		}
		
		//Clear underneath the cursor if middlekey is down
		else if(down3){
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (x+1+mX < Game.width && y+1+mY < Game.height&&y+mY>0&&x+mX>0) {
						boxes[x+mX+(y+mY)*Game.width] = 0;
					}
				}
			}
		}
		
		
		
		
		for (int y = 0; y < Game.height; y++) {
			for (int x = 0; x < Game.width; x++) {
				if (x+1 < Game.width && y+1 < Game.height&&y>0&&x>0) {
					
					if (boxes[x+(y*Game.width)] == boxType.SNOW.id ||
						boxes[x+(y*Game.width)] == boxType.WATER.id ||
						boxes[x+(y*Game.width)] == boxType.LAVA.id){


						//Snow movement
						if (boxes[x+((y+1)*Game.width)] == 0) {
								boxes[x+((y+1)*Game.width)] = (byte) (boxes[x+(y*Game.width)]+1);
								boxes[x+y*Game.width] = 0;
						}
						else{
							//if no snow on right and left
							boolean left = boxes[(x-1)+((y+1)*Game.width)] == 0;
							boolean right = boxes[(x+1)+((y+1)*Game.width)] == 0;
							
							if(left && right){
								//if empty on both sides then random chance to move right or left
								if(Math.random() >= 0.5){

									boxes[(x-1)+((y+1)*Game.width)] = (byte) (boxes[x+y*Game.width]+1);
									boxes[x+y*Game.width] = 0;
								}
								else {
									
									boxes[(x+1)+((y+1)*Game.width)] = (byte) (boxes[x+y*Game.width]+1);
									boxes[x+y*Game.width] = 0;
								}
							}
							else if(right){
								
								boxes[(x+1)+((y+1)*Game.width)] = (byte) (boxes[x+y*Game.width]+1);
								boxes[x+y*Game.width] = 0;
							}
							else if(left){
								
								boxes[(x-1)+((y+1)*Game.width)] = (byte) (boxes[x+y*Game.width]+1);
								boxes[x+y*Game.width] = 0;
							}
						}
						
						//lava properties (changes snow to lava)
						if(boxes[x+(y*Game.width)] == boxType.LAVA.id){

							if(boxes[x+((y+1)*Game.width)] == boxType.SNOW.id){
								boxes[x+((y+1)*Game.width)] = (byte) (boxType.LAVA.id);
							}
							else if(boxes[x+((y-1)*Game.width)] == boxType.SNOW.id){
								boxes[x+((y-1)*Game.width)] = (byte) (boxType.LAVA.id);
							}
							else if(boxes[(x+1)+(y*Game.width)] == boxType.SNOW.id){
								boxes[(x+1)+(y*Game.width)]=(byte) (boxType.LAVA.id);
							}
							else if(boxes[(x-1)+(y*Game.width)] == boxType.SNOW.id){
								boxes[(x-1)+(y*Game.width)] = (byte) (boxType.LAVA.id);
							}
						}
						
						//liquid movement
						double rand = Math.random();
						if(boxes[x+(y*Game.width)] == boxType.WATER.id ||
								boxes[x+(y*Game.width)] == boxType.LAVA.id) {

							//50% chance to move 5 pixels
							if(rand <= 0.5){
								if(boxes[x+5+y*Game.width] == 0 && (x+5) < width){

									boxes[x+5+y*Game.width] = (byte) (boxes[x+y*Game.width]+1);
									boxes[x+y*Game.width] = 0;
									
								}
							
							}
						else {
							if(boxes[x-5+y*Game.width] == 0 && (x-5) > 0){
								boxes[x-5+y*Game.width] = (byte) (boxes[x+y*Game.width]+1);
								boxes[x+y*Game.width] = 0;
							}
						}
					}
				}
			}
		}
	}
		//changing pixel type so it will get updated next tick
		for(int i = 0;i<Game.width*Game.height;i++){

			if(boxes[i]==boxType.SNOW.id+1||
					boxes[i]==boxType.WATER.id+1||
					boxes[i]==boxType.LAVA.id+1)
				boxes[i]--;
		}
	}
}
