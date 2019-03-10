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
public interface Commons {
    
    public static final int BOARD_WIDTH = 358; //wifth of the whole board
    public static final int BOARD_HEIGHT = 350; //height of the whole beard
    public static final int GROUND = 290; //position of the ground
    public static final int BOMB_HEIGHT = 10; //height of the alien's bomb
    public static final int BOMB_WIDTH = 5; //width of the alien's width
    public static final int ALIEN_HEIGHT = 12; //height of each alien
    public static final int ALIEN_WIDTH = 12;//width of each alien
    public static final int BORDER_RIGHT = 30; //extra border at the right side of the board
    public static final int BORDER_LEFT = 5; //extra border at the left side of the board
    public static final int GO_DOWN = 15; //movement of the aliens when going down
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24; //total number of aliens
    public static final int PLAYER_WIDTH = 15; //width of the player's object
    public static final int PLAYER_HEIGHT = 10; //height of the player's object
    public static final int PLAYER_START_X = 270; //initial x position of the player
    public static final int PLAYER_START_Y = 280; //initial y position of the player
    public static final int ALIEN_INIT_X = 150;//initial x position of the aliens 
    public static final int ALIEN_INIT_Y = 5; //initial y position of the aliens
    public static final int SHOT_WIDTH = 2; //width of the player's shot
    public static final int SHOT_HEIGHT = 15; //height of the player's shot
    public static final int ANIMATION_FRAMES = 20; //numbers of frames for animation
}
