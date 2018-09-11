/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;


/**
 *
 * @author kucht
 */
public class Victory extends Canvas implements CommandListener{
    Command again;
    Player player;
    String score;
    String time;
    boolean even=false;
    protected void paint(Graphics g) {
        if(even)
        {
            g.setColor(0x00FFFFFF);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(0x00000000);
            g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
            g.drawString("Zdobyłeś "+score+ " punktów!", 20, getHeight()/5*1, Graphics.LEFT | Graphics.TOP);
                    g.setColor(0x00000000);
            g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
            g.drawString("Grałeś "+ time+ " sekund!", 20, getHeight()/5*3, Graphics.LEFT | Graphics.TOP);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
            g.setColor(0x00000000);
            g.fillRect(0,0, getWidth(), getHeight());
            try {
                g.drawImage(Image.createImage(getClass().getResourceAsStream("img/victory.png")), getWidth()/2, 0, Graphics.TOP | Graphics.HCENTER);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        even=!even;
        repaint();
    }
    
    public Victory()
    {
//        score = Draw.getInstance().getScore();
//        time = Draw.getInstance().getTime();
        score="235";
        time="675";
        try {
            again = new Command("Zagraj", Command.ITEM, 0);
            addCommand(again);
            this.setCommandListener(this);
            InputStream is = getClass().getResourceAsStream("audio/gameWin.mp3");
            player = Manager.createPlayer(is,"audio/mpeg");
            
            //  if "audio/mpeg" doesn't work try "audio/mp3"
            
            player.realize();  
            player.prefetch();
            player.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if(c==again)
        {
            
            Midlet.getDisplay().setCurrent(new Draw());
            System.out.println("Komenda !");
            
        }
    }
    
}
