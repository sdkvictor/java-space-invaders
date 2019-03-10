/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author charles
 */
public class Game implements Runnable, Commons {
    
    private BufferStrategy bs;
    private Graphics g;
    private Display display;
    
    String title;
    private int width;
    private int height;
    
    private Thread thread;
    private boolean running;
    
    private KeyManager keyManager;
    
    private Player player;
    private Aliens aliens;
    
    private boolean gameOver;
    private boolean gameWon;
    private boolean paused;
    
    private String message;
    private int score;
    
    /**
    * to create title, width and height and set the game is still not running
    * @param title to set the title of the window
    * @param width to set the width of the window
    * @param height to set the height of the window
    */
    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        
        aliens = new Aliens();

        running = false;
        keyManager = new KeyManager();
        gameOver = false;
        gameWon = false;
        paused = false;
        message = "Game Over!";
        score = 0;
    }
    
     /**
     * start main game thread
     */
    @Override
    public void run() {
        init();
        
        int fps = 60; //Current game requirements demand 60 fps
        double timeTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        
        while (running) {
            
            now = System.nanoTime();
            delta += (now - lastTime) / timeTick;
            lastTime = now;
            
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }
        
        stop();
    }
    
    /**
    * initializing the display window of the game
    */
    private void init() {
        display = new Display(title, width, height);
        Assets.init();
        
        display.getJframe().addKeyListener(keyManager);
        
        player = new Player(PLAYER_START_X, PLAYER_START_Y, PLAYER_WIDTH, PLAYER_HEIGHT, this);
    }
    
    /**
     * updates all objects on a frame
     */
    private void tick() {
        if (gameOver) {
            keyManager.tick();
            if (keyManager.r) {
                resetGame();
            }
            if (keyManager.c) {
                loadGame();
            }
            return;
        }
        
        if (paused) {
            keyManager.tick();
            if (keyManager.p) {
                paused = false;
            }
            if (keyManager.g) {
                saveGame();
            }

            if (keyManager.c) {
                loadGame();
            }

            if (keyManager.r) {
                resetGame();
            }
            return;
        }
        
        keyManager.tick();
        
        if (keyManager.p) {
            paused = true;
        }
        
        if (keyManager.g) {
            saveGame();
        }
        
        if (keyManager.c) {
            loadGame();
        }
        
        if (keyManager.r) {
            resetGame();
        }
        
        player.tick();
        
        aliens.tick();
        
        if (aliens.checkShot(player.getShot())) {
            player.getShot().setActive(false);
            player.getShot().reset();
            score += 100;
        }
        
        if (aliens.allDead()) {
            message = "Game Won!";
            gameOver = true;
        }
        
        if (aliens.haveInvaded()) {
            message = "Invasion!";
            gameOver = true;
        }
        
        if (aliens.bombIntersects(player)) {
            gameOver = true;
        }
    }
    
    /**
     * renders all objects in a frame
     */
    private void render() {
        Toolkit.getDefaultToolkit().sync(); //Linux
        bs = display.getCanvas().getBufferStrategy();
        
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        }
        else {
            g = bs.getDrawGraphics();
            g.clearRect(0, 0, width, height);
            
            if (gameOver) {
                g.setColor(Color.black);
                g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

                g.setColor(new Color(0, 32, 48));
                g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
                g.setColor(Color.white);
                g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

                Font small = new Font("Helvetica", Font.BOLD, 14);
                FontMetrics metr = display.getJframe().getFontMetrics(small);

                g.setColor(Color.white);
                g.setFont(small);
                g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);
            } else {
                g.drawImage(Assets.background, 0, 0, getWidth(), getHeight(), null);
                //g.setColor(Color.black);
                //g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

                g.setColor(Color.green);
                g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
                player.render(g);

                aliens.render(g);
                
                Font small = new Font("Helvetica", Font.BOLD, 13);

                g.setColor(Color.white);
                g.setFont(small);
                String sScore = ("Score: " + Integer.toString(score));
                g.drawString(sScore, BOARD_WIDTH - 100, BOARD_HEIGHT - 20);
                
                if (paused) {
                     g.setColor(new Color(0, 32, 48));
                    g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
                    g.setColor(Color.white);
                    g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

                    small = new Font("Helvetica", Font.BOLD, 14);
                    FontMetrics metr = display.getJframe().getFontMetrics(small);

                    g.setColor(Color.white);
                    g.setFont(small);
                    g.drawString("Paused", (BOARD_WIDTH - metr.stringWidth("Paused")) / 2, BOARD_WIDTH / 2);
                }
            }
            
            bs.show();
            g.dispose();     
        }
    }
    
    /**
     * Saves current game status into a text file
     * Each important variable to define the current status of the game is
     * stored in the file in a specific order
     */
    private void saveGame() {
        try {
            //Open text file
            PrintWriter pw = new PrintWriter(new FileWriter("game.txt"));
            
            pw.println(Integer.toString(player.getX()));
            pw.println(Integer.toString(player.getY()));
            pw.println(Integer.toString(player.getShot().getX()));
            pw.println(Integer.toString(player.getShot().getY()));
            pw.println(Integer.toString(player.getShot().isActive() ? 1 : 0));
            
            aliens.save(pw);
            
            pw.println(Integer.toString(score));
            
            pw.close();
            
            System.out.println("SAVED!");
        } catch(IOException e) {
            System.out.println("BEEP BEEP");
            System.out.println(e.toString());
        }
    }
    
    /**
     * Load game from text file
     * This method open the designated text file and reads its contents
     * and assigns them to their designated variables
     */
    private void loadGame() {
        try {
            //Open file to load game
            BufferedReader br = new BufferedReader(new FileReader("game.txt"));
            
            player.setX(Integer.parseInt(br.readLine()));
            player.setY(Integer.parseInt(br.readLine()));
            player.getShot().setX(Integer.parseInt(br.readLine()));
            player.getShot().setY(Integer.parseInt(br.readLine()));
            player.getShot().setActive(Integer.parseInt(br.readLine()) == 1);
            
            aliens.load(br);
            
            score = Integer.parseInt(br.readLine());
            br.close();
            
            System.out.println("LOADED!");
            gameOver = false;
           
        } catch (IOException e) {
            System.out.println("BEEP BEEP");
            System.out.println(e.toString());
        }
    }
    
    public void resetGame() {

        player.setX(PLAYER_START_X);
        player.setY(PLAYER_START_Y);
        
        player.getShot().setX(PLAYER_START_X);
        player.getShot().setY(PLAYER_START_Y);
        
        aliens.reset();
        
        gameOver = false;
        score = 0;
    }
    
    /**
     * to get width
     * @return width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * to get height
     * @return height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * to get key manager
     * @return keyManager
     */
    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    /**
     * To get aliens object
     * @return 
     */
    public Aliens getAliens() {
        return aliens;
    }
    
    /**
     * To set aliens object
     * @param aliens 
     */
    public void setAliens(Aliens aliens) {
        this.aliens = aliens;
    }
    
    /**
     * To get message
     * @return 
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * To set message
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * To get paused
     * @return 
     */
    public boolean isPaused() {
        return paused;
    }
    
    /**
     * To set paused
     * @param paused 
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    /**
     * To get gameOver
     * @return 
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * to set gameOver
     * @param gameOver 
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    /**
     * To get gameWon
     * @return 
     */
    public boolean isGameWon() {
        return gameWon;
    }
    
    /**
     * To set gameWon
     * @param gameWon 
     */
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }
    
    /**
     * start game
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * stop game
     */
    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
