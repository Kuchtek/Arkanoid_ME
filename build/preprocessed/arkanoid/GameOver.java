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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author kucht
 */
public class GameOver extends Canvas implements CommandListener{
    
    Command again;
    Player player;
    String score;
    String time;
    protected void paint(Graphics g) {
        g.setColor(0x00FFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x00000000);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
        g.drawString("Niestety, \nale odniosłeś porażkę", 20, getHeight()/5, Graphics.LEFT | Graphics.TOP);
        g.setColor(0x00000000);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
        g.drawString(score+ " punktów!", 20, getHeight()/5*3, Graphics.LEFT | Graphics.TOP);
                g.setColor(0x00000000);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
        g.drawString("Grałeś "+ time+ " sekund!", 20, getHeight()/5*4, Graphics.LEFT | Graphics.TOP);
    }

    public GameOver()
    {
        score = Draw.getInstance().getScore();
        time = Draw.getInstance().getTime();
        try {
            again = new Command("Zagraj", Command.ITEM, 0);
            addCommand(again);
            this.setCommandListener(this);
            InputStream is = getClass().getResourceAsStream("audio/game_over.mp3");
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
