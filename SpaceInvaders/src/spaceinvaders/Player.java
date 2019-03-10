/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author charles
 */
public class Player extends Item implements Commons {
    
    private Game game;
    private int speed;
    private Shot shot;
    
    /**
     * To create a new player object
     * @param x
     * @param y
     * @param width
     * @param height
     * @param game 
     */
    public Player(int x, int y, int width, int height, Game game) {
        super(x, y, width, height);
        
        shot = new Shot(getX(), getY(), SHOT_WIDTH, SHOT_HEIGHT);
        
        this.game = game;
        speed = 2;
    }
    
    /**
     * To update the item each frame
     */
    @Override
    public void tick() {
        //Check left key
        if (game.getKeyManager().left) {
            setX(getX() - speed);
        }
        
        //Check right key
        if (game.getKeyManager().right) {
            setX(getX() + speed);
        }
        
        //Shoot
        if (game.getKeyManager().space && !shot.isActive()) {
            shoot();
            Assets.laser.play();
        }
        
        //Left boundary
        if (getX() <= 2) {
            setX(2);
        }
        
        //Right boundary
        if (getX() >= game.getWidth() - getWidth()) {
            setX(game.getWidth() - getWidth());
        }
        
        shot.tick();
    }
    
    /**
     * To paint the item each frame
     */
    @Override
    public void render(Graphics g) {
        shot.render(g);
        g.drawImage(Assets.player, getX(), getY(), getWidth(), getHeight(), null);
    }
    
    /**
     * To shoot the shot object of the player
     */
    private void shoot() {
        shot.shoot(getX() + getWidth()/2, getY());
    }
    
    /**
     * To get the shot variable
     * @return shot
     */
    public Shot getShot() {
        return shot;
    }
    
    /**
     * To get speed
     * @return 
     */
    public int getSpeed() {
        return speed;
    }
    
    /**
     * To set speed
     * @param speed 
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    /**
     * This class is contained in the player item because it is a helper class and
     * it is required by the player class
     */
    public class Shot extends Item {
    
        private boolean active;
        private int speed;
        
       /**
        * To create a shot object
        * @param x
        * @param y
        * @param width
        * @param height 
        */
        public Shot(int x, int y, int width, int height) {
            super(x, y, width, height);
            active = false;
            speed = 4;
        }
        
        /**
         * To activate the shot object
         * @param x
         * @param y 
         */
        public void shoot(int x, int y) {
            active = true;
            setX(x - getWidth()/2);
            setY(y);
        }
        
        /**
         * To reset the shot position
         */
        public void reset() {
            setY(GROUND);
        }
        
        /**
        * To update the item each frame
        */
        @Override
        public void tick() {
            if (active) {
                //Move the shot up when active
                setY(getY() - 4);
            }
            
            if (getY() <= 0) {
                //If it reaches the upper boundary, deactivate it
                active = false;
            }
        }
        
        /**
        * To paint the item each frame
        */
        @Override
        public void render(Graphics g) {
            if (active) {
                g.setColor(Color.GREEN);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            }
        }
        
        /**
         * To get active
         * @return 
         */
        public boolean isActive() {
            return active;
        }
        
        /**
         * To set active
         * @param active 
         */
        public void setActive(boolean active) {
            this.active = active;
        }
        
        /**
         * To get speed
         * @return 
         */
        public int getSpeed() {
            return speed;
        }
        
        /**
         * To set speed
         * @param speed 
         */
        public void setSpeed(int speed) {
            this.speed = speed;
        }
    }
}
