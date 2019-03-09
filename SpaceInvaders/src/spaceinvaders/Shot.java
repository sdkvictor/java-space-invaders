/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Graphics;

/**
 *
 * @author charles
 */
public class Shot extends Item {
    
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
