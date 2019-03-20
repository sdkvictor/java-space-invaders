/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

/**
 *
 * @author charles
 */
public class NetworkData {
    private int x;
    private int y;
    
    private boolean ready;
    private boolean shoot;
    
    public NetworkData(int x, int y, boolean ready, boolean shoot) {
        this.x = x;
        this.y = y;
        this.ready = ready;
        this.shoot = shoot;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isShoot() {
        return shoot;
    }
}
