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
public class CoopPlayer extends Player {
    
    public CoopPlayer(int x, int y, int width, int height, Game game) {
        super(x, y, width, height, game);
    }
    
    @Override
    public void tick() {
        shot.tick();
    }
}
