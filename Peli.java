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

public class Peli extends Canvas implements Runnable{
	private JPanel p;
	private Graphics g;
	public static int width=600;
	public static int height=500;
	private JFrame frame;
	Thread thread;
	
	int mX=0;
	int mY=0;
	boolean down=false;
	boolean down2=false;
	boolean down3=false;
	
	private BufferedImage image = new BufferedImage(width, height, 1);
	public int[] pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
	public byte[] laatikot = new byte[width*height];
	public int[] movement = new int[width*height];
	
	
	
	
	
	
	public void draw() {
		BufferStrategy bs = getBufferStrategy();
	    if (bs == null)
	    {
	      createBufferStrategy(3);
	      return;
	    }
	    Graphics g = bs.getDrawGraphics();
	    g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
	    
	    g.dispose();
	    bs.show();
		
	}

	
	public enum laatikkoTyyppi{
		SEINA((byte)200,0x555555),
		LUMI((byte)1,0xffffff),
		
		VESI((byte)3,0x5555FF),
		LAAVA((byte)5,0xFF5555)
		;
		
		int vari;
		byte id;
		
		laatikkoTyyppi(byte id,int vari){
			this.vari=vari;
			this.id=id;
		}
		
	}
	
	
	public Peli(int width, int height){
		thread=new Thread(this,"asd");
		JFrame peli=new JFrame();
		frame=peli;
		
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				mX=e.getX();
				mY=e.getY();
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {

				mX=e.getX();
				mY=e.getY();
			}
			
		});
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {

				

				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getButton());
				if(e.getButton()==1)
				down=true;
				if(e.getButton()==3)
				down2=true;
				if(e.getButton()==2)
					down3=true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton()==1)
				down=false;
				if(e.getButton()==3)
				down2=false;
				if(e.getButton()==2)
					down3=false;
			}
			
		});
		
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()=='s'){
					for (int y = 0; y < 15; y++) {
						for (int x = 0; x < 15; x++) {
							if (x+1+mX < Peli.width && y+1+mY < Peli.height&&y+mY>0&&x+mX>0) {
								laatikot[x+mX+(y+mY)*Peli.width]=laatikkoTyyppi.SEINA.id;
							}
						}
					}
				}
				else if(e.getKeyChar()=='a'){
					for (int y = 0; y < 15; y++) {
						for (int x = 0; x < 15; x++) {
							if (x+1+mX < Peli.width && y+1+mY < Peli.height&&y+mY>0&&x+mX>0) {
								laatikot[x+mX+(y+mY)*Peli.width]=laatikkoTyyppi.LAAVA.id;
							}
						}
					}
				}
				else if(e.getKeyChar()=='r')
					snowRain=!snowRain;
				
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
	
		laatikot[5+4*Peli.width]=1;

	}


	@Override
	public void run() {
		while (true) {


			for(int i = 0;i<laatikot.length;i++)
			{
				switch(laatikot[i])
				{
				case 0:
					//Background
					pixels[i]=0x333366;
					break;
				case 1:
				case 2:
					pixels[i]=laatikkoTyyppi.LUMI.vari;
					break;
				case 3:
				case 4:
					pixels[i]=laatikkoTyyppi.VESI.vari;
					break;
				case 5:
				case 6:
					pixels[i]=laatikkoTyyppi.LAAVA.vari;
					break;
				case (byte) 200:
					pixels[i]=0x555555;
				
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
	
	double rotation=0;
	boolean snowRain=false;
	
	
	private void rotate(){
		
		rotation+=0.01;
		if(rotation>=4)
			rotation=0;
		//0-1
		if(rotation<=1){
			for(int i = 0;i<20;i++){
				for(int j = 0;j<3;j++){
					laatikot[i+25+(j+25+(int)(i*rotation))*Peli.width]=laatikkoTyyppi.SEINA.id;
				}
				
			}
		}
		//]1,2]
		else if(rotation>1&&rotation<=2){
			
			for(int i = 0;i<20;i++){
				for(int j = 0;j<3;j++){
					laatikot[((int)(i-i*(rotation%1)))+25+(j+25+i)*Peli.width]=laatikkoTyyppi.SEINA.id;
				}
				
			}
		}
		else if(rotation>2&&rotation<=3){
			for(int i = 0;i<20;i++){
				for(int j = 0;j<3;j++){
					laatikot[25-((int)(i*(rotation%1)))  +  (j+25+i)*Peli.width]=laatikkoTyyppi.SEINA.id;
				}
				
			}
		}
		//]1,2]
		else if(rotation>3&&rotation<=4){
			for(int i = 0;i<20;i++){
				for(int j = 0;j<3;j++){
					laatikot[i+25+(j+25+(int)(i*(rotation%1)))*Peli.width]=laatikkoTyyppi.SEINA.id;
				}
				
			}
		}
	}
	
	private void update() {

		
		
			for(int i = 0;i<100;i++){
				for(int j = 0;j<100;j++){
					laatikot[25+i+(j+25)*Peli.width]=0;
				}
				
			}
		

		
		
		rotate();
		
		
		
		
		//snow
		if(snowRain)
		{
			laatikot[rand.nextInt(Peli.width)+(5)*Peli.width]=laatikkoTyyppi.LUMI.id;
			laatikot[rand.nextInt(Peli.width)+(5)*Peli.width]=laatikkoTyyppi.LUMI.id;
		}
			
			
	//Creating snow when mousedown
		if(down){
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (x+1+mX < Peli.width && y+1+mY < Peli.height&&y+mY>0&&x+mX>0) {
						laatikot[x+mX+(y+mY)*Peli.width]=laatikkoTyyppi.LUMI.id;
					}
				}
			}
		}
		else if(down2){
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (x+1+mX < Peli.width && y+1+mY < Peli.height&&y+mY>0&&x+mX>0) {
						laatikot[x+mX+(y+mY)*Peli.width]=laatikkoTyyppi.VESI.id;
					}
				}
			}
		}
		
		else if(down3){
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (x+1+mX < Peli.width && y+1+mY < Peli.height&&y+mY>0&&x+mX>0) {
						laatikot[x+mX+(y+mY)*Peli.width]=0;
					}
				}
			}
		}
		
		
		
		
		for (int y = 0; y < Peli.height; y++) {
			for (int x = 0; x < Peli.width; x++) {
				if (x+1 < Peli.width && y+1 < Peli.height&&y>0&&x>0) {
					
					if (laatikot[x+(y*Peli.width)] == laatikkoTyyppi.LUMI.id||
							laatikot[x+(y*Peli.width)] == laatikkoTyyppi.VESI.id||
							laatikot[x+(y*Peli.width)]==laatikkoTyyppi.LAAVA.id
							
							) {


						//Snow movement
						if (laatikot[x+((y+1)*Peli.width)] == 0) {
								laatikot[x+((y+1)*Peli.width)] = (byte) (laatikot[x+(y*Peli.width)]+1);
								laatikot[x+y*Peli.width] = 0;
						}
						else{
							//if no snow on right and left
							boolean vasen=laatikot[(x-1)+((y+1)*Peli.width)]==0;
							boolean oikea=laatikot[(x+1)+((y+1)*Peli.width)]==0;
							
							if(vasen&&oikea){
								//if empty on both sides then random chance to move right or left
								if(Math.random()>=0.5)
								{
									
									laatikot[(x-1)+((y+1)*Peli.width)] = (byte) (laatikot[x+y*Peli.width]+1);
									laatikot[x+y*Peli.width] = 0;
								}
								else{
									
									laatikot[(x+1)+((y+1)*Peli.width)] = (byte) (laatikot[x+y*Peli.width]+1);
									laatikot[x+y*Peli.width] = 0;
								}
							
							}
							else if(oikea){
								
								laatikot[(x+1)+((y+1)*Peli.width)] = (byte) (laatikot[x+y*Peli.width]+1);
								laatikot[x+y*Peli.width] = 0;
							}
							else if(vasen){
								
								laatikot[(x-1)+((y+1)*Peli.width)] = (byte) (laatikot[x+y*Peli.width]+1);
								laatikot[x+y*Peli.width] = 0;
								
							}

							
						}
						
						//lava properties (changes snow to lava)
						if(laatikot[x+(y*Peli.width)]==laatikkoTyyppi.LAAVA.id)
						{
							if(laatikot[x+((y+1)*Peli.width)]==laatikkoTyyppi.LUMI.id){
								laatikot[x+((y+1)*Peli.width)]=(byte) (laatikkoTyyppi.LAAVA.id);
							}
							else if(laatikot[x+((y-1)*Peli.width)]==laatikkoTyyppi.LUMI.id){
								laatikot[x+((y-1)*Peli.width)]=(byte) (laatikkoTyyppi.LAAVA.id);
							}
							else if(laatikot[(x+1)+(y*Peli.width)]==laatikkoTyyppi.LUMI.id){
								laatikot[(x+1)+(y*Peli.width)]=(byte) (laatikkoTyyppi.LAAVA.id);
							}
							else if(laatikot[(x-1)+(y*Peli.width)]==laatikkoTyyppi.LUMI.id){
								laatikot[(x-1)+(y*Peli.width)]=(byte) (laatikkoTyyppi.LAAVA.id);
							}
							
						}
						
						
						
						
						
						
						
						
						
						//liquid movement
						double rand = Math.random();
						if(laatikot[x+(y*Peli.width)]==laatikkoTyyppi.VESI.id||
								laatikot[x+(y*Peli.width)]==laatikkoTyyppi.LAAVA.id
								)
						{
							//50% chance to move 5 pixels
							if(rand<=0.5){
								if(laatikot[x+5+y*Peli.width]==0&&x+5<width){

									laatikot[x+5+y*Peli.width] = (byte) (laatikot[x+y*Peli.width]+1);
									laatikot[x+y*Peli.width] = 0;
									
								}
							
							}
						else{
								if(laatikot[x-5+y*Peli.width]==0&&x-5>0){
									laatikot[x-5+y*Peli.width] = (byte) (laatikot[x+y*Peli.width]+1);
									laatikot[x+y*Peli.width] = 0;
								}
						
							}
						}

						
						
					}
				}
			}
		}
		//changing pixel type so it will get updated next tick
		for(int i = 0;i<Peli.width*Peli.height;i++)
		{
			if(laatikot[i]==laatikkoTyyppi.LUMI.id+1||
					laatikot[i]==laatikkoTyyppi.VESI.id+1||
					laatikot[i]==laatikkoTyyppi.LAAVA.id+1
					
					)
				laatikot[i]--;
		}
		
	}
}
