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
        
        if (game.getKeyManager())
        
        if (getX() <= 2) {
            setX(2);
        }
        
        if (getX() >= game.getWidth() - getWidth()) {
            setX(game.getWidth() - getWidth());
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public Shot getShot() {
        return shot;
    }
    
    private class Shot extends Item {
    
        private boolean active;

        public Shot(int x, int y, int width, int height) {
            super(x, y, width, height);
            active = false;
        }

        @Override
        public void tick() {
        }

        @Override
        public void render(Graphics g) {
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
}

    
}
