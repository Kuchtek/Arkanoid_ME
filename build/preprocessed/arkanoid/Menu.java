/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author kucht
 */
public class Menu extends Canvas implements CommandListener{
    
    private List list;
    
    private Command exit;

    Player player;
    Alert alert;
    Image image;
    
    public Menu()
    {

        
            Midlet.getDisplay().setCurrent(this);
            final InputStream is = getClass().getResourceAsStream("audio/intro.mp3");
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                         player = Manager.createPlayer(is,"audio/mpeg");
            
                        //  if "audio/mpeg" doesn't work try "audio/mp3"

                        player.realize();  
                        player.prefetch();
                        player.setLoopCount(-1);
                        player.start();
                        }
                        catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (MediaException ex) {
                            ex.printStackTrace();
                        }
            }});
            t.start();
            setCommandListener(this);

    }

    public void commandAction(Command c, Displayable d) {
        if(c==exit)
        {
            System.out.println("Exit");
        }
    }

    protected void paint(Graphics g) {
        try {
            g.setColor(0x00000000);
            g.fillRect(0,0, getWidth(), getHeight());
            g.drawImage(Image.createImage(getClass().getResourceAsStream("img/arkanoidLogo.png")), getWidth()/2, getWidth()/2, Graphics.TOP | Graphics.HCENTER);
            g.setColor(0x00FFFFFF);
            g.drawString("Press any key to play", getWidth()/2, getHeight()/5*4, Graphics.BASELINE | Graphics.HCENTER);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void keyPressed(int keyCode) {
        Midlet.getDisplay().setCurrent(new Draw());
        try {
            player.stop();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
}
