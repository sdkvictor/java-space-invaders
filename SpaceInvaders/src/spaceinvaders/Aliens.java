/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author charles
 */
public class Aliens implements Commons {
    
    private ArrayList<Alien> aliens;
    private int direction = 1;
    
    private int amountDestroyed;
    
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
    
    public boolean bombIntersects(Item item) {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getBomb().intersects(item) && aliens.get(i).getBomb().isActive()) {
                System.out.println("BOOM");
                return true;
            }
        }
        
        return false;
    }
    
    public boolean checkShot(Item item) {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).intersects(item) && !aliens.get(i).isDead()) {
                aliens.get(i).setDead(true);
                aliens.get(i).setRecentlyDead(true);
                aliens.get(i).setRecentlyDeadCounter(0);
                amountDestroyed++;
                return true;
            }
        }
        
        return false;
    }
    
    public boolean haveInvaded() {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getY() > GROUND - ALIEN_HEIGHT && !aliens.get(i).isDead()) {
                return true;
            }
        }
        
        return false;
        
    }
    
    public boolean allDead() {
        return amountDestroyed >= aliens.size();
    }
     
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
    
    public void render(Graphics g) {
        for (int i = 0; i < aliens.size(); i++) {
            aliens.get(i).render(g);
        }
    }
    
    private class Alien extends Item {
        
        private int direction;
        private Bomb bomb;
        private boolean dead;
        private int image;
        private boolean recentlyDead;
        private int recentlyDeadCounter;

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
        
        public void goDown() {
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
                active = false;
            }

            public void drop(int x, int y) {
                setX(x);
                setY(y);
                active = true;
            }
            
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
