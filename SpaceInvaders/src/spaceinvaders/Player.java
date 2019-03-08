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
public class Player extends Item {
    
    private Game game;
    private int speed;

    public Player(int x, int y, int width, int height, Game game) {
        super(x, y, width, height);
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
        
        
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
    
}
