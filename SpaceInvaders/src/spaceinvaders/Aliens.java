/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author charles
 */
public class Aliens implements Commons {
    
    /**
     * All aliens are encapsulated in a class Aliens so that it becomes easier to 
     * deal with all aliens from the Game class, abstracting aliens behaviours in the new
     * Aliens class methods
     */
    private ArrayList<Alien> aliens;
    private int direction = 1;
    
    private int amountDestroyed;
    
    /**
     * To create an aliens object
     */
    public Aliens() {
        aliens = new ArrayList<>();
        
        amountDestroyed = 0;
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i, ALIEN_HEIGHT, ALIEN_WIDTH, -1, i);
                aliens.add(alien);
            }
        }
    }
    
    /**
     * To check if any bomb intersects with the player (item)
     * @param item
     * @return 
     */
    public boolean bombIntersects(Item item) {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getBomb().intersects(item) && aliens.get(i).getBomb().isActive()) {
                System.out.println("BOOM");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * To check if the shot (item) collides with any alien, and if so, kill the alien
     * @param item
     * @return 
     */
    public boolean checkShot(Item item) {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).intersects(item) && !aliens.get(i).isDead()) {
                aliens.get(i).setDead(true);
                aliens.get(i).setRecentlyDead(true);
                aliens.get(i).setRecentlyDeadCounter(0);
                amountDestroyed++;
                Assets.expSound.play();
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * To check if any alien has reached the ground
     * @return 
     */
    public boolean haveInvaded() {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getY() > GROUND - ALIEN_HEIGHT && !aliens.get(i).isDead()) {
                return true;
            }
        }
        
        return false;
        
    }
    
    /**
     * To check if all aliens have been killed
     * @return 
     */
    public boolean allDead() {
        return amountDestroyed >= aliens.size();
    }
    
    /**
     * To reset all aliens to their initial state
     */
    public void reset() {
        amountDestroyed = 0;
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                aliens.get(count).setX(ALIEN_INIT_X + 18 * j);
                aliens.get(count).setY(ALIEN_INIT_Y + 18 * i);
                aliens.get(count).setDirection(-1);
                aliens.get(count).setDead(false);
                aliens.get(count).getBomb().setActive(false);
                aliens.get(count).getBomb().setX(0);
                aliens.get(count).getBomb().setY(0);
                count++;
            }
        }
    }
    
    /**
     * To save the values of all aliens to the file
     * @param pw 
     */
    public void save(PrintWriter pw) {
        pw.println(Integer.toString(amountDestroyed));
        pw.println(Integer.toString(aliens.get(0).getDirection()));
        
        for (int i = 0; i < aliens.size(); i++) {
            pw.println(Integer.toString(aliens.get(i).isDead() ? 0 : 1));
            pw.println(Integer.toString(aliens.get(i).getX()));
            pw.println(Integer.toString(aliens.get(i).getY()));
            pw.println(Integer.toString(aliens.get(i).getBomb().isActive() ? 1 : 0));
            pw.println(Integer.toString(aliens.get(i).getBomb().getX()));
            pw.println(Integer.toString(aliens.get(i).getBomb().getY()));
      
        }
    }
    
    /**
     * to load the values of all aliens into the game
     * @param br
     * @throws IOException 
     */
    public void load(BufferedReader br) throws IOException {
        amountDestroyed = Integer.parseInt(br.readLine());
        int direction = Integer.parseInt(br.readLine());
        
        for (int i = 0; i < aliens.size(); i++) {
            aliens.get(i).setDead(Integer.parseInt(br.readLine()) == 0);
            aliens.get(i).setX(Integer.parseInt(br.readLine()));
            aliens.get(i).setY(Integer.parseInt(br.readLine()));
            aliens.get(i).setDirection(direction);
            aliens.get(i).getBomb().setActive(Integer.parseInt(br.readLine()) == 1);
            aliens.get(i).getBomb().setX(Integer.parseInt(br.readLine()));
            aliens.get(i).getBomb().setY(Integer.parseInt(br.readLine()));
        }
    }
    
    /**
     * To update all aliens in a frame
     */
    public void tick() {     
        for (int i = 0; i < aliens.size(); i++) {
            boolean moveFromLeft = aliens.get(i).getX() <= BORDER_LEFT && aliens.get(i).getDirection() != 1 && !aliens.get(i).isDead();
            boolean moveFromRight = aliens.get(i).getX() >= BOARD_WIDTH - BORDER_RIGHT && aliens.get(i).getDirection() != -1 && !aliens.get(i).isDead();
            
            if (moveFromLeft || moveFromRight) {
                for (int j = 0; j < aliens.size(); j++) {
                    aliens.get(j).setDirection(aliens.get(j).getDirection() * -1);
                    aliens.get(j).goDown();
                }
            }
            
            aliens.get(i).tick();
        }
    }
    
    /**
     * To render all aliens to the canvas
     * @param g 
     */
    public void render(Graphics g) {
        for (int i = 0; i < aliens.size(); i++) {
            aliens.get(i).render(g);
        }
    }
    
    /**
     * The class Alien is nested inside the class Aliens because it is a helper class
     * to Aliens and it is only used by the class Aliens
     */
    private class Alien extends Item {
        
        private int direction;
        private Bomb bomb;
        private boolean dead;
        private int image;
        private boolean recentlyDead;
        private int recentlyDeadCounter;
        
        /**
         * To create a new alien
         * @param x
         * @param y
         * @param width
         * @param height
         * @param direction
         * @param image 
         */
        public Alien(int x, int y, int width, int height, int direction, int image) {
            super(x, y, width, height);
            bomb = new Bomb(getX(), getY(), BOMB_WIDTH, BOMB_HEIGHT);
            dead = false;
            recentlyDead = false;
            recentlyDeadCounter = 0;
            this.direction = direction;
            this.image = image;
            
        }
        
        /**
         * To update the object each frame
         */
        @Override
        public void tick() {
            setX(getX() + direction);
            
            if (recentlyDead) {
                recentlyDeadCounter++;
                
                if (recentlyDeadCounter >= ANIMATION_FRAMES) {
                    recentlyDead = false;
                }
            }
            
            //Check if the alien has not dropped a bomb and is alive
            if (!bomb.isActive() && !isDead()) {
                //The alien has 1/15 probability of dropping a bomb
                int chance = (int) (Math.random() * 15);
                
                //1/15 chance
                if (chance == 0) {
                    bomb.drop(getX(), getY());
                }
            }
            
            bomb.tick();
            
        }
        
        /**
         * To paint the object each frame
         */
        @Override
        public void render(Graphics g) {
            
            if (!isDead()) {
                g.drawImage(Assets.alien[image], getX(), getY(), getWidth(), getHeight(), null);
            }
            
            if (recentlyDead) {
                g.drawImage(Assets.explosion, getX(), getY(), getWidth(), getHeight(), null);
            }
            
            bomb.render(g);
        }
        
        /**
         * To move the alien down a constant amount
         */
        public void goDown() {
            setY(getY() + GO_DOWN);
        }
        
        /**
         * To get the direction of the alien
         * @return 
         */
        public int getDirection() {
            return direction;
        }

        /**
         * To set the direction of the alien
         * @param direction 
         */
        public void setDirection(int direction) {
            this.direction = direction;
        }

        /**
         * To get the bomb
         * @return 
         */
        public Bomb getBomb() {
            return bomb;
        }

        /**
         * to check if the alien is dead
         * @return 
         */
        public boolean isDead() {
            return dead;
        }
        
        /**
         * To set dead
         * @param dead 
         */
        public void setDead(boolean dead) {
            this.dead = dead;
        }
        
        /**
         * to get recentlyDead
         * @return 
         */
        public boolean isRecentlyDead() {
            return recentlyDead;
        }

        /**
         * to set recentlyDead
         * @param recentlyDead 
         */
        public void setRecentlyDead(boolean recentlyDead) {
            this.recentlyDead = recentlyDead;
        }
        
        /**
         * To get recentlyDeadCounter
         * @return 
         */
        public int getRecentlyDeadCounter() {
            return recentlyDeadCounter;
        }

        /**
         * To set recentlyDeadCounter
         * @param recentlyDeadCounter 
         */
        public void setRecentlyDeadCounter(int recentlyDeadCounter) {
            this.recentlyDeadCounter = recentlyDeadCounter;
        }
        
        /**
         *  The class Bomb is nested inside the class Alien because it is only used 
         * by the class alien and it follows the rules of encapsulation
         */
        public class Bomb extends Item {
            
            private boolean active;
            
            /**
             * To create a new bomb
             * @param x
             * @param y
             * @param width
             * @param height 
             */
            public Bomb(int x, int y, int width, int height) {
                super(x, y, width, height);
                active = false;
            }
            
            /**
             * To activate the bomb and drop it
             * @param x
             * @param y 
             */
            public void drop(int x, int y) {
                setX(x);
                setY(y);
                active = true;
            }
            
            /**
             * To update the object in a frame
             */
            @Override
            public void tick() {
                if (active) {
                    setY(getY() + 1);
                    if (getY() >= GROUND - BOMB_HEIGHT) {
                        setY(0);
                        setActive(false);    
                    }
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
             * To paint the object in the canvas
             * @param g 
             */
            @Override
            public void render(Graphics g) {
                if (active) {
                    g.drawImage(Assets.bomb, getX(), getY(), getWidth(), getHeight(), null);
                }
            }
        }
    }
}
