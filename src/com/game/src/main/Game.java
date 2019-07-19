package com.game.src.main;

import java.awt.Canvas;
import com.game.src.main.AudioPlayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
//VARIABLES
	
	//SCREEN
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 990;
	private static final int HEIGHT = 800;
	private final String TITLE = "Chicken Attack";
	
	//RUN
	private boolean running = false;
	private Thread thread;
	
	//AUDIO
	private AudioPlayer menuMusic = new AudioPlayer("/menuAudio.mp3");
	private AudioPlayer gameMusic = new AudioPlayer("/gameAudio.mp3");
	private AudioPlayer eggSound = new AudioPlayer("/eggAudio.mp3");
	private AudioPlayer catSound = new AudioPlayer("/catAudio.mp3");
	private AudioPlayer levelUpSound = new AudioPlayer("/levelUpAudio.mp3");
	private AudioPlayer scoreSound = new AudioPlayer("/scoreAudio.mp3");
	private AudioPlayer gameOverMusic = new AudioPlayer("/gameOverAudio.mp3");

	//MUSIC & SOUND
	private boolean music = true;
	private boolean sound = true;
	private boolean music_control = true;
	private boolean sound_control = true;
	
	//KEYBOARD INPUT
	private boolean right_pressed = false;
	private boolean left_pressed = false;
	
	//LEVEL & SCORE
	private int level = 10;
	private int score = 0;
	private int totalScore = 0;
	
	//RANDOM, EGGS & PLAYER
	private Object player =  new Object(WIDTH/2, HEIGHT/2, 0, 120, 110);
	private int eggOffset = 0;
	private int cat_random = 0;
	private int house_random = 0;
	
	private Random rand = new Random();
	
	//IMAGES
	BufferedImageLoader loader = new BufferedImageLoader();
	
	//Menu Images
	private BufferedImage menuBackground = loader.loadImage("/menu/menu.png");
	private BufferedImage help_image = loader.loadImage("/menu/help.png");
	
	//Options Images
	
	private BufferedImage options_background= loader.loadImage("/menu/options_background.png");
	
	private BufferedImage music_button = loader.loadImage("/menu/volume_on_button.png");
	private BufferedImage sound_button = loader.loadImage("/menu/volume_on_button.png");
	private BufferedImage on_button= loader.loadImage("/menu/volume_on_button.png");
	private BufferedImage off_button= loader.loadImage("/menu/volume_off_button.png");
	
	//Credits Images
	private BufferedImage credits_background= loader.loadImage("/menu/credits_background.png");
	private BufferedImage credits_flood= loader.loadImage("/menu/credits.png");
	
	private int credits_height = HEIGHT;
	
	//Game Over Image
	private BufferedImage gameOver = loader.loadImage("/end/GameOver.png");
	
	//GAME IMAGES
	
	//Background
	private BufferedImage background = loader.loadImage("/game/background.png");
	
	//Cloud
	private BufferedImage cloud = loader.loadImage("/game/cloud.png");
	private int cloud_width = rand.nextInt(2450) - 1700;
	private int cloud_height = HEIGHT+50;
	
	//Chicken Images
	private BufferedImage chicken = loader.loadImage("/game/chickens/chicken_front.png");
	private BufferedImage chicken_happy = loader.loadImage("/game/chickens/happy_chicken.png");
	private BufferedImage chicken_front = loader.loadImage("/game/chickens/chicken_front.png");
	private BufferedImage chicken_left = loader.loadImage("/game/chickens/chicken_left.png");
	private BufferedImage chicken_right = loader.loadImage("/game/chickens/chicken_right.png");
	
	private BufferedImage chicken_left_hat1 = loader.loadImage("/game/chickens/chicken_left_hat1.png");
	private BufferedImage chicken_right_hat1 = loader.loadImage("/game/chickens/chicken_right_hat1.png");
	private BufferedImage chicken_happy_hat1 = loader.loadImage("/game/chickens/happy_chicken_hat1.png");
	
	private BufferedImage chicken_left_hat2 = loader.loadImage("/game/chickens/chicken_left_hat2.png");
	private BufferedImage chicken_right_hat2 = loader.loadImage("/game/chickens/chicken_right_hat2.png");
	private BufferedImage chicken_happy_hat2 = loader.loadImage("/game/chickens/happy_chicken_hat2.png");
	
	private BufferedImage chicken_left_hat3 = loader.loadImage("/game/chickens/chicken_left_hat3.png");
	private BufferedImage chicken_right_hat3 = loader.loadImage("/game/chickens/chicken_right_hat3.png");
	private BufferedImage chicken_happy_hat3 = loader.loadImage("/game/chickens/happy_chicken_hat3.png");
	
	//Egg Images
	private LinkedList<Object> egg = new LinkedList<Object>();
	
	private BufferedImage Egg_right = loader.loadImage("/game/egg/egg_right.png");
	private BufferedImage Egg_left = loader.loadImage("/game/egg/egg_left.png");
	
	//Cat Image
	private LinkedList<Object> cat = new LinkedList<Object>();
	
	private BufferedImage cat_img = loader.loadImage("/game/cat.png");
	
	//House Images
	private LinkedList<Object> house = new LinkedList<Object>();
	
	private BufferedImage house_left_large = loader.loadImage("/game/houses/house_left_large.png");
	private BufferedImage house_left_mid = loader.loadImage("/game/houses/house_left_mid.png");
	private BufferedImage house_left_small = loader.loadImage("/game/houses/house_left_small.png");
	private BufferedImage house_right_large = loader.loadImage("/game/houses/house_right_large.png");
	private BufferedImage house_right_mid = loader.loadImage("/game/houses/house_right_mid.png");
	private BufferedImage house_right_small = loader.loadImage("/game/houses/house_right_small.png");
	
	private BufferedImage score_text = loader.loadImage("/game/score_text.png");
	private BufferedImage level_text = loader.loadImage("/game/level_text.png");
	private BufferedImage level_text_up = loader.loadImage("/game/level_text_up.png");
	private BufferedImage level_text_normal = loader.loadImage("/game/level_text.png");
	
	//STATES OF THE GAME
	 private enum STATE{
			MENU,
			GAME,
			OPTIONS,
			CREDITS,
			HELP,
			END
	};
	
	//FIRST STATE
	private static STATE State = STATE.MENU;

// VARIABLES END
	
	public void init() {
		requestFocus();
		addKeyListener(new KeyInput(this));
		addMouseListener(new MouseInput(this));
	}
	
	private synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + "Ticks, Fps " + frames + " SCORE: " + score + " STATE = " + State);
				updates = 0;
				frames = 0;
			
			}
		}
		stop();
	}
	
	private void tick() {
		
		//MUSIC ADJUSTMENTS
		if(State == STATE.MENU || State == STATE.OPTIONS) {
			
			if(music == true) {
				music_button = on_button;
				music_control = true;
				if (!menuMusic.check())
				menuMusic.play();
			}
			
			else if(music == false) {
				music_button = off_button;
				music_control = false;
				menuMusic.stop();
			}
		}
		
		if(State == STATE.MENU || State == STATE.HELP || State == STATE.GAME 
				|| State == STATE.END || State == STATE.OPTIONS || State == STATE.CREDITS) {
			if(sound == true) {
				sound_button = on_button;
				sound_control= true;
			}
			
			else if(sound == false) {
				sound_button = off_button;
				sound_control=false;
			}
			
			if(State == STATE.END) gameMusic.stop();
		}
		
		//GAME STATE
		if(State == STATE.GAME) {
			menuMusic.stop();
			
			if(music == true) {
				music_button = on_button;
				music_control = true;
				
				if (!gameMusic.check()) gameMusic.play();
			}
			
			else if(music == false) {
				music_button = off_button;
				music_control = false;
				gameMusic.stop();
			}
		//MUSIC ADJUSTMENTS END
			
		//MOVEMENTS
			
			//CLOUDS
			if (cloud_height <= -700) {
				cloud_height = HEIGHT + 50;
				cloud_width = rand.nextInt(2450) - 1700;
			}
			cloud_height = cloud_height - (level + 5);
			
			//PLAYER
			player.tick();
			
			if (!right_pressed && !left_pressed || right_pressed && left_pressed) player.SetVelX(0);
			
			if (player.GetX() <= WIDTH - 233) {
				if (right_pressed && !left_pressed) {
					player.SetVelX(level + 5);
					player.SetDirection(1);
					
					if(level <= 0) chicken = chicken_right;
					else if(level >=1 & level <=3) chicken = chicken_right_hat1;
					else if(level >=4 & level <=7) chicken = chicken_right_hat2;
					else if(level >=8) chicken = chicken_right_hat3;
				}
			}	
			else right_pressed = false;
			
			if (player.GetX() >= 233) {
				if (!right_pressed && left_pressed) {
					player.SetVelX(-level - 5);
					player.SetDirection(3);
					
					if(level <= 0) chicken = chicken_left;
					else if(level >=1 & level <=3) chicken = chicken_left_hat1;
					else if(level >=4 & level <=7) chicken = chicken_left_hat2;
					else if(level >=8) chicken = chicken_left_hat3;
				}
			}	
			else left_pressed = false;
			
			//EGG
			for (int i = 0; i < egg.size(); i++) {
	
				if (egg.get(i).GetDirection() == 1) egg.get(i).SetX(egg.get(i).GetX() + (level + 10));
				
				if (egg.get(i).GetDirection() == 3)  egg.get(i).SetX(egg.get(i).GetX() - (level + 10));
				
				if (egg.get(i).GetX() > WIDTH || egg.get(i).GetX() < 0) egg.remove(i);
			}
			
			//CAT
			cat_random = rand.nextInt(1000);
			
			if (cat_random < level + 5)
				cat.add(new Object(rand.nextInt(WIDTH - 300) + 150, HEIGHT + 60, 0, 67, 60));
			
			for (int i = 0; i < cat.size(); i++) {
				cat.get(i).SetY(cat.get(i).GetY() - (level*0.5 + 2.5));
				
				if (cat.get(i).GetY() < 0) cat.remove(i);
			}
			
			//HOUSE
			house_random = rand.nextInt(1000);
			
			if (house_random < level + 5) {
				
				house_random = rand.nextInt(3);
				
				if (house_random == 0) {
					house_random = rand.nextInt(2);
					
					if (house_random == 0) house.add(new Object(50, -50, 0, 55, 50));
					else house.add(new Object(WIDTH - 50, -50, 1, 55, 50));
				}
			
				else if (house_random == 1){
					house_random = rand.nextInt(2);
					
					if (house_random == 0) house.add(new Object(60, -50, 2, 109, 100));					
					else house.add(new Object(WIDTH - 60, -50, 3, 109, 100));					
					
				}
				else {
					house_random = rand.nextInt(2);
					if (house_random == 0) house.add(new Object(80, -100, 4, 164, 150));
					
					else house.add(new Object(WIDTH - 75, -100, 5, 164, 150));
				}
			}
			
			for (int i = 0; i < house.size(); i++) {
				
				house.get(i).SetY(house.get(i).GetY() + (level*0.5 + 2));
				
				if (house.get(i).GetY() > HEIGHT + 100) house.remove(i);
			}
			
		//MOVEMENTS END
			
		//COLLISIONS
			
			//EGG AND HOUSE
			for (int i = 0; i < egg.size(); i++) {
				for (int j = 0; j < house.size(); j++) {
					if (egg.get(i).GetDirection() == 1 
						&& egg.get(i).GetX() < house.get(j).GetX() + house.get(j).GetWidth() / 2 
						&& egg.get(i).GetX() > house.get(j).GetX() - house.get(j).GetWidth() / 2 
						&& egg.get(i).GetY() > house.get(j).GetY() - house.get(j).GetHeight() / 2 
						&& egg.get(i).GetY() < house.get(j).GetY() + house.get(j).GetHeight() / 2){
						
							if (house.get(j).GetDirection() == 1) {
								score += 300;
								totalScore += score;
							}
							
							if (house.get(j).GetDirection() == 3) {
								score += 200;
								totalScore += score;
							}
							
							if (house.get(j).GetDirection() == 5) {
								score += 100;
								totalScore += score;
							}
							
							house.remove(j);
							egg.remove(i);
							
							if(sound_control == true) scoreSound.play();
							if(sound_control == false) scoreSound.stop();
							
							break;
					}
					
					if (egg.get(i).GetDirection() == 3 
						&& egg.get(i).GetX() > house.get(j).GetX() - house.get(j).GetWidth() / 2 
						&& egg.get(i).GetX() < house.get(j).GetX() + house.get(j).GetWidth() / 2 
						&& egg.get(i).GetY() > house.get(j).GetY() - house.get(j).GetHeight() / 2 
						&& egg.get(i).GetY() < house.get(j).GetY() + house.get(j).GetHeight() / 2) {
						
							if (house.get(j).GetDirection() == 0) {
								score += 300;
								totalScore += score;
							}
							
							if (house.get(j).GetDirection() == 2) {
								score += 200;
								totalScore += score;
							}
							
							if (house.get(j).GetDirection() == 4) {
								score += 100;
								totalScore += score;
							}
							
							house.remove(j);
							egg.remove(i);
							
							if(sound_control == true) scoreSound.play();
							if(sound_control == false) scoreSound.stop();
							
							break;
					}
				}
			}
			
			//CHICKEN AND CAT
			for (int i = 0; i < cat.size(); i++) {
				if (cat.get(i).GetX() < player.GetX() + player.GetWidth()/2 
					&& cat.get(i).GetX() > player.GetX() - player.GetWidth()/2
					&& cat.get(i).GetY() < player.GetY() + player.GetHeight() / 2
					&& cat.get(i).GetY() > player.GetY() - player.GetHeight() / 2) {
					
					if(sound_control == true) {
						catSound.play();
					}
					
					else if(sound_control == false) {
						catSound.stop();
					}
					
					if(music_control == true) gameOverMusic.play();
					else if(music_control == false) gameOverMusic.stop();
					
					State = STATE.END;
					System.out.println("Game Over.");
				}
			}
			
		//COLLISIONS END
			
			//LEVEL UP
			if (score > level * 500 + 500 && level < 10) levelUp();
		}
		//GAME STATE END
		
		//CREDITS STATE
		if(State == STATE.CREDITS) {
			if(credits_height >= -3100) credits_height -= 2; 
			else State = STATE.MENU;
		}
		
		if(State != STATE.CREDITS) credits_height = HEIGHT; 
		//CREDITS STATE END
	}
	
	private void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		if (State == STATE.MENU) {
			g.drawImage(menuBackground, 0, 0, this);	
		}
		
		if (State == STATE.GAME) {
			
			//BACKGROUND
			g.drawImage(background, 0, 0, this);
			g.drawImage(cloud, cloud_width, cloud_height, this);
		
			//PLAYER
			g.drawImage(chicken, (int)(player.GetX()-player.GetWidth()/2), (int)(player.GetY()-player.GetHeight()/2), this);
			
			//EGG
			for (int i = 0; i < egg.size(); i++) {
				
				if (egg.get(i).GetDirection() == 1) 
					g.drawImage(Egg_right, (int)(egg.get(i).GetX()-egg.get(i).GetWidth()/2), (int)(egg.get(i).GetY()-egg.get(i).GetHeight()/2), this);
				
				if (egg.get(i).GetDirection() == 3) 
					g.drawImage(Egg_left, (int)(egg.get(i).GetX()-egg.get(i).GetWidth()/2), (int)(egg.get(i).GetY()-egg.get(i).GetHeight()/2), this);
			}
			
			//CAT
			for (int i = 0; i < cat.size(); i++) 
				g.drawImage(cat_img, (int)(cat.get(i).GetX()-cat.get(i).GetWidth()/2), (int)(cat.get(i).GetY()-cat.get(i).GetHeight()/2), this);
			
			//HOUSE
			for (int i = 0; i < house.size(); i++) {
				
				if (house.get(i).GetDirection() == 0) 
					g.drawImage(house_left_small, (int)(house.get(i).GetX()-house.get(i).GetWidth()/2), (int)(house.get(i).GetY()-house.get(i).GetHeight()/2), this);
				
				if (house.get(i).GetDirection() == 1) 
					g.drawImage(house_right_small, (int)(house.get(i).GetX()-house.get(i).GetWidth()/2), (int)(house.get(i).GetY()-house.get(i).GetHeight()/2), this);
				
				if (house.get(i).GetDirection() == 2) 
					g.drawImage(house_left_mid, (int)(house.get(i).GetX()-house.get(i).GetWidth()/2), (int)(house.get(i).GetY()-house.get(i).GetHeight()/2), this);
				
				if (house.get(i).GetDirection() == 3) 
					g.drawImage(house_right_mid, (int)(house.get(i).GetX()-house.get(i).GetWidth()/2), (int)(house.get(i).GetY()-house.get(i).GetHeight()/2), this);
				
				if (house.get(i).GetDirection() == 4) 
					g.drawImage(house_left_large, (int)(house.get(i).GetX()-house.get(i).GetWidth()/2), (int)(house.get(i).GetY()-house.get(i).GetHeight()/2), this);
				
				if (house.get(i).GetDirection() == 5) 
					g.drawImage(house_right_large, (int)(house.get(i).GetX()-house.get(i).GetWidth()/2), (int)(house.get(i).GetY()-house.get(i).GetHeight()/2), this);
			}
			
			//SCOREBOARD
			Font myFont = new Font("HELVATICA", Font.BOLD, 30);
			Font myFont2 = new Font("HELVATICA", Font.BOLD, 36);
			g.setColor(Color.BLACK);
			g.setFont(myFont);
			g.drawString(String.valueOf(totalScore), 840, 70);
			g.drawImage(score_text, 670,40, this);
			if(chicken == chicken_happy) g.setColor(Color.RED);
			else g.setColor(Color.BLACK);
			g.drawImage(level_text, 50, 40, this);
			g.setFont(myFont2);
			g.drawString(String.valueOf(level+1), 250, 70);
		}
		
		if(State == STATE.HELP) {
			g.drawImage(help_image, 0,0,null);
		}
		
		if(State == STATE.OPTIONS) {
			g.drawImage(options_background, 0, 0, this);
			g.drawImage(music_button, 310, 310, this);
			g.drawImage(sound_button, 310, 380, this);
		}
		
		if(State == STATE.CREDITS) {
			g.drawImage(credits_background, 0,0,null);
			g.drawImage(credits_flood, 0, credits_height, this);
		}
		
		if(State == STATE.END) {
			
			//GAME OVER SCREEN 
			g.drawImage(gameOver,0,0, this);
			
			//SCORE
			Font myFont = new Font("HELVATICA", Font.BOLD, 36);
			g.setColor(Color.BLACK);
			g.setFont(myFont);
			g.drawImage(score_text, 100, 520, this);
			g.drawString(String.valueOf(totalScore), 300, 550);
		}
		
		g.dispose();
		bs.show();
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_RIGHT && State == STATE.GAME) {
			level_text = level_text_normal;
			right_pressed = true;
		}
		
		else if (key == KeyEvent.VK_LEFT && State == STATE.GAME) {
			level_text = level_text_normal;
			left_pressed = true;
		}
		
		if (key == KeyEvent.VK_SPACE && State == STATE.GAME) {
			
			level_text = level_text_normal;
			
			if(sound_control == true) eggSound.play();
			
			else if(sound_control == false) eggSound.stop();
			
			if (player.GetDirection() == 3) eggOffset = -30;
			
			if (player.GetDirection() == 1) eggOffset = 30;
			
			egg.add(new Object(player.GetX() + eggOffset, player.GetY()-20, player.GetDirection(), 25, 18 ));
			
			if (score > 0) score -= 50;
			
			if (totalScore > 0) totalScore -= 50;
		}
		
		if(key == KeyEvent.VK_ESCAPE) {
			State = Game.STATE.MENU;
			gameMusic.stop();
			resetGame();
		}
		
		if(key == KeyEvent.VK_ESCAPE && State == STATE.CREDITS) {
			State = Game.STATE.MENU;
			gameMusic.stop();
			resetGame();
		}
		
		if(key == KeyEvent.VK_H && State == STATE.MENU)	State = STATE.HELP;
		
		if (key == KeyEvent.VK_ENTER && State == STATE.MENU) State = STATE.GAME;
		
		if (key == KeyEvent.VK_ENTER && State == STATE.END)	{
			State = STATE.MENU;
			resetGame();
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_RIGHT && State == STATE.GAME) right_pressed = false;
		
		else if (key == KeyEvent.VK_LEFT && State == STATE.GAME) left_pressed = false;
	}
	
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		//START
		if(mx >= 690 && mx <= 830 && my >= 400 && my <= 440 && Game.State == Game.STATE.MENU) State = STATE.GAME;
		
		//OPTIONS
		if(mx >= 670 && mx <= 850 && my >= 480 && my <= 520 && Game.State == Game.STATE.MENU) State = STATE.OPTIONS;
		
		//HELP
		if(mx >= 700 && mx <= 815 && my >= 560 && my <= 595 && Game.State == Game.STATE.MENU) State = Game.STATE.HELP;
		
		//CREDITS
		if(mx >= 670 && mx <= 850 && my >= 620 && my <= 660 && Game.State == Game.STATE.MENU) State = Game.STATE.CREDITS;
		
		//EXIT
		if(mx >= 720 && mx <= 810 && my >= 690 && my <= 725 && Game.State == Game.STATE.MENU) System.exit(1);	
		
		//END
		if(mx>=650 && mx<=900 && my>=500 && my<=800 && Game.State == Game.STATE.END) {
			State = Game.STATE.GAME;
			resetGame();
		}
		
		//MUSIC
		if(mx >= 310 && mx <= 360 && my >= 310 && my <= 360 && Game.State == Game.STATE.OPTIONS) {
			if(music == true) {
				music_control = false;
				music = false;
			}
			
			else if(music == false) {
					music_control = true;
					music = true;
			}
		}
			
		//SOUND
		if(mx >= 310 && mx <= 360 && my >= 380 && my <= 430 && Game.State == Game.STATE.OPTIONS) {
			if(sound == true) {
				sound = false;
				sound_control = false;
			}
			
			else if(sound == false) {
				sound = true;
				sound_control = false;
			}
		}
	}
	
	public void resetGame () {
		level = 0;
		score = 0;
		totalScore = 0;
		cloud_height = HEIGHT+50;
		
		chicken = chicken_front;
		player.SetDirection(2);
		player.SetX(WIDTH/2);
		player.SetY(HEIGHT/2);
		
		egg.removeAll(egg);
		house.removeAll(house);
		cat.removeAll(cat);
		level_text = level_text_normal;
		left_pressed = false;
		right_pressed = false;
	}
	
	public void levelUp () {
		gameMusic.stop();
		
		if(sound_control == true) levelUpSound.play();
		
		else if(sound_control == false) levelUpSound.stop();

		
		level += 1;
		score = 0;
		cloud_height = HEIGHT+50;
		player.SetDirection(2);
		level_text = level_text_up;
		if(level <= 0)
			chicken = chicken_happy;
			else if(level <=3 && level >=1){
				chicken = chicken_happy_hat1;
			}
			else if(level <=7 && level >=4){
				chicken = chicken_happy_hat2;
			}
			else if(level >=8) {
				chicken = chicken_happy_hat3;
			}
				
		
		egg.removeAll(egg);
		house.removeAll(house);
		cat.removeAll(cat);		
	}
	
	public static void main(String args[]) throws IOException {
		
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JFrame frame = new JFrame(game.TITLE);

		String imagePath = "/game/chickens/logo.png";
		InputStream imgStream = Game.class.getResourceAsStream(imagePath);
		BufferedImage myImg = ImageIO.read(imgStream);
		frame.setIconImage(myImg);
		
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	
	}	
}