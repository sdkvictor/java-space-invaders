/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.image.BufferedImage;

/**
 *
 * @author charles
 */
public class Assets {
    
    public static BufferedImage background;
    public static BufferedImage player;
    public static BufferedImage bomb;
    public static BufferedImage explosion;
    public static BufferedImage alien[];
    
    public static SoundClip laser;
    public static SoundClip expSound;
    
    public static void init() {
        background = ImageLoader.loadImage("/images/background.jpg");
        player = ImageLoader.loadImage("/images/player.png");
        bomb = ImageLoader.loadImage("/images/bomb.png");
        explosion = ImageLoader.loadImage("/images/explosion.png");
        
        alien = new BufferedImage[4];
        
        laser = new SoundClip("/sounds/laser.wav");
        expSound = new SoundClip("/sounds/explosion.wav");
        
        alien[0] = ImageLoader.loadImage("/images/alienorange.png");
        alien[1] = ImageLoader.loadImage("/images/alienred.png");
        alien[2] = ImageLoader.loadImage("/images/alienblue.png");
        alien[3] = ImageLoader.loadImage("/images/alienpink.png");
    }
}
