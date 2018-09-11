/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author kucht
 */
public class Midlet extends MIDlet {
    
    private static Display display;
    private static Midlet instance;
    public Midlet()
    {
        instance=this;
        display=Display.getDisplay(this);
    }
    public void startApp() {
        display.setCurrent(new Menu());
        // loads the InputStream for the sound
    }

    public static Display getDisplay() {
        return display;
    }

    public static Midlet getInstance() {
        return instance;
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    

    
    
    
}
