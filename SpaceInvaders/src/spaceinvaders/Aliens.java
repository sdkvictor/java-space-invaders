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
    
    private ArrayList<Alien> aliens; //arraylist of objects of class alian
    private int direction = 1; //movement direction of the aliens
    
    private int amountDestroyed;
    /**
     * To create an arraylist of  enemy aliens
     */
    public Aliens() {
        aliens = new ArrayList<>();
        
        amountDestroyed = 0;
        //adds 24 enemy aliens in their different initial positions
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i, ALIEN_HEIGHT, ALIEN_WIDTH, -1, i);
                aliens.add(alien);
            }
        }
    }
    /**
     * To check if the bomb intersects with the player
     * @param item
     * @return 
     */
    public boolean bombIntersects(Item item) {
        //checks for every bomb
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getBomb().intersects(item) && aliens.get(i).getBomb().isActive()) {
                System.out.println("BOOM");
                return true;
            }
        }
        
        return false;
    }
    /**
     * Returns true if the alien is in contact with the player's laser
     * @param item
     * @return 
     */
    public boolean checkShot(Item item) {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).intersects(item) && !aliens.get(i).isDead()) {
                //change the destroyed alien as dead
                aliens.get(i).setDead(true);
                //change the status as recently dead
                aliens.get(i).setRecentlyDead(true);
                aliens.get(i).setRecentlyDeadCounter(0);
                //increase the counter for destroyed aliens by one
                amountDestroyed++;
                //play the explosion sound
                Assets.expSound.play();
                return true;
            }
        }
        
        return false;
    }
    /**
     * To check if the aliens have reached the ground
     * @return 
     */
    public boolean haveInvaded() {
        //check for every alien
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getY() > GROUND - ALIEN_HEIGHT && !aliens.get(i).isDead()) {
                return true;
            }
        }
        
        return false;
        
    }
    /**
     * To check if all enemies have been destroyed
     * @return 
     */
    public boolean allDead() {
        return amountDestroyed >= aliens.size();
    }
    
    public void reset() {
        //set aliens destroyed as 0 
        amountDestroyed = 0;
        int count = 0;
        //return every alien to their original positions, direction and set them as not dead
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
    
    public void save(PrintWriter pw) {
        pw.println(Integer.toString(amountDestroyed));
        pw.println(Integer.toString(aliens.get(0).getDirection()));
        //save the status and positions of every alien and bomb
        for (int i = 0; i < aliens.size(); i++) {
            pw.println(Integer.toString(aliens.get(i).isDead() ? 0 : 1));
            pw.println(Integer.toString(aliens.get(i).getX()));
            pw.println(Integer.toString(aliens.get(i).getY()));
            pw.println(Integer.toString(aliens.get(i).getBomb().isActive() ? 1 : 0));
            pw.println(Integer.toString(aliens.get(i).getBomb().getX()));
            pw.println(Integer.toString(aliens.get(i).getBomb().getY()));
      
        }
    }
    
    public void load(BufferedReader br) throws IOException {
        amountDestroyed = Integer.parseInt(br.readLine());
        int direction = Integer.parseInt(br.readLine());
        //read the status and positions of every alien and bomb
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
     
    public void tick() { 
        //for every alien in the array list
        for (int i = 0; i < aliens.size(); i++) {
            //check if the aliens at the left edge have reached the left border
            boolean moveFromLeft = aliens.get(i).getX() <= BORDER_LEFT && aliens.get(i).getDirection() != 1 && !aliens.get(i).isDead();
            //check if the aiens at the right edge have reached the right border
            boolean moveFromRight = aliens.get(i).getX() >= BOARD_WIDTH - BORDER_RIGHT && aliens.get(i).getDirection() != -1 && !aliens.get(i).isDead();
            //if the aliens reached a border
            if (moveFromLeft || moveFromRight) {
                //change the direction of all the aliens
                for (int j = 0; j < aliens.size(); j++) {
                    aliens.get(j).setDirection(aliens.get(j).getDirection() * -1);
                    aliens.get(j).goDown();
                }
            }
            aliens.get(i).tick();
        }
    }
    
    public void render(Graphics g) {
        //render for every alien
        for (int i = 0; i < aliens.size(); i++) {
            aliens.get(i).render(g);
        }
    }
    
    private class Alien extends Item {
        
        private int direction; //direction of the alien's movement
        private Bomb bomb; //bomb that the alien throws
        private boolean dead; //whether the alien has been destroyed
        private int image; //image of the alien
        private boolean recentlyDead; //whether the alien has just been destroyed
        private int recentlyDeadCounter; //counter to make the explosion animation last 20 frames

        public Alien(int x, int y, int width, int height, int direction, int image) {
            super(x, y, width, height);
            bomb = new Bomb(getX(), getY(), BOMB_WIDTH, BOMB_HEIGHT);
            dead = false;
            recentlyDead = false;
            recentlyDeadCounter = 0;
            this.direction = direction;
            this.image = image;
            
        }

        @Override
        public void tick() {
            //move the aliens in x 
            setX(getX() + direction);
            //wait 20 frames for the explosion animation
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

        @Override
        public void render(Graphics g) {
            //render the alien if its not dead yet
            if (!isDead()) {
                g.drawImage(Assets.alien[image], getX(), getY(), getWidth(), getHeight(), null);
            }
            //render the explosion if the alien has just been destroyed
            if (recentlyDead) {
                g.drawImage(Assets.explosion, getX(), getY(), getWidth(), getHeight(), null);
            }
            
            bomb.render(g);
        }
        
        public void goDown() {
            //move down in y direction
            setY(getY() + GO_DOWN);
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public Bomb getBomb() {
            return bomb;
        }

        public boolean isDead() {
            return dead;
        }

        public void setDead(boolean dead) {
            this.dead = dead;
        }

        public boolean isRecentlyDead() {
            return recentlyDead;
        }

        public void setRecentlyDead(boolean recentlyDead) {
            this.recentlyDead = recentlyDead;
        }
        
        public int getRecentlyDeadCounter() {
            return recentlyDeadCounter;
        }

        public void setRecentlyDeadCounter(int recentlyDeadCounter) {
            this.recentlyDeadCounter = recentlyDeadCounter;
        }
        
        
        
        public class Bomb extends Item {
            
            private boolean active;

            public Bomb(int x, int y, int width, int height) {
                super(x, y, width, height);
                active = false; //set the bomb as not active
            }

            public void drop(int x, int y) {
                setX(x);
                setY(y);
                active = true; //set the bomb as active when it is dropped
            } 
            
            @Override
            public void tick() {
                //set the bomb as not active again if it touches the ground
                if (active) {
                    setY(getY() + 1);
                    if (getY() >= GROUND - BOMB_HEIGHT) {
                        setY(0);
                        setActive(false);    
                    }
                } 
            }
            
            public boolean isActive() {
                return active;
            }

            public void setActive(boolean active) {
                this.active = active;
            }

            @Override
            public void render(Graphics g) {
                if (active) {
                    g.drawImage(Assets.bomb, getX(), getY(), getWidth(), getHeight(), null);
                }
            }
        }
    }
}
