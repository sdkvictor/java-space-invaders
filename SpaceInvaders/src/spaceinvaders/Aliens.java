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
    
    public Aliens() {
        aliens = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i, ALIEN_HEIGHT, ALIEN_WIDTH, -1);
                aliens.add(alien);
            }
        }
    }
    
    public boolean intersectsBomb(Item item) {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getBomb().intersects(item)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void tick() {
        
        boolean moveFromLeft = aliens.get(6).getX() <= BORDER_LEFT && aliens.get(6).getDirection() != 1;
        boolean moveFromRight = aliens.get(aliens.size() - 1).getX() >= BOARD_WIDTH - BORDER_RIGHT && aliens.get(aliens.size() - 1).getDirection() != -1;
        
        for (int i = 0; i < aliens.size(); i++) {
            if (moveFromLeft) {
                aliens.get(i).setDirection(1);
                aliens.get(i).goDown();
            }
            
            if (moveFromRight) {
                aliens.get(i).setDirection(-1);
                aliens.get(i).goDown();
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

        public Alien(int x, int y, int width, int height, int direction) {
            super(x, y, width, height);
            bomb = new Bomb(getX(), getY(), BOMB_WIDTH, BOMB_HEIGHT);
            this.direction = direction;
        }

        @Override
        public void tick() {
            setX(getX() + direction);
            
            //Check if the alien has not dropped a bomb
            if (!bomb.isActive()) {
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
            g.setColor(Color.CYAN);
            g.fillRect(getX(), getY(), width, height);
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
                    g.setColor(Color.WHITE);
                    g.fillOval(getX(), getY(), getWidth(), getHeight());
                }
            }
        }
    }
}
