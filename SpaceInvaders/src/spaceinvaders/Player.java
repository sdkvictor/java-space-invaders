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

    public Player(int x, int y, int width, int height, Game game) {
        super(x, y, width, height);
        
        shot = new Shot(getX(), getY(), SHOT_WIDTH, SHOT_HEIGHT);
        
        this.game = game;
        speed = 2;
    }
    
    @Override
    public void tick() {
        if (game.getKeyManager().left) {
            setX(getX() - speed);
        }
        
        if (game.getKeyManager().right) {
            setX(getX() + speed);
        }
        
        if (game.getKeyManager().space && !shot.isActive()) {
            shoot();
            Assets.laser.play();
        }
        
        if (getX() <= 2) {
            setX(2);
        }
        
        if (getX() >= game.getWidth() - getWidth()) {
            setX(game.getWidth() - getWidth());
        }
        
        shot.tick();
    }

    @Override
    public void render(Graphics g) {
        shot.render(g);
        g.drawImage(Assets.player, getX(), getY(), getWidth(), getHeight(), null);
    }
    
    private void shoot() {
        shot.shoot(getX() + getWidth()/2, getY());
    }

    public Shot getShot() {
        return shot;
    }
    
    public class Shot extends Item {
    
        private boolean active;
        private int speed;

        public Shot(int x, int y, int width, int height) {
            super(x, y, width, height);
            active = false;
            speed = 4;
        }
        
        public void shoot(int x, int y) {
            active = true;
            setX(x - getWidth()/2);
            setY(y);
        }
        
        public void reset() {
            setY(GROUND);
        }

        @Override
        public void tick() {
            if (active) {
                setY(getY() - 4);
            }
            
            if (getY() <= 0) {
                active = false;
            }
        }

        @Override
        public void render(Graphics g) {
            if (active) {
                g.setColor(Color.GREEN);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            }
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
