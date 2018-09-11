/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author kucht
 */
public class Draw extends Canvas{
    static long duration;
    //wymiary ekranu
    int screenWidth=getWidth();
    int screenHeight=getHeight();
    //wymiary paletki
    int deskWidth=50;
    int deskHeight=10;
    //pozycja początkowa paletki
    int deskXStartPositon;
    int deskYStartPosition;
    //aktualna pozycja paletki
    int deskXPosition;
    int deskYPosition;

    //punkt rysowania bloczka
    int blockXPosition;
    int blockYPosition;
    //wymiary piłki
    int ballWidth=8,ballHeight=8;
    //kolory
    int deskColor = 0x00ACCAB0;
    int ballColor = 0x00FFFFFF;
    
    int[] blockColors = new int[4];
    //pozycja piłki
    int ballXPosition;
    int ballYPosition;
    //kierunek poruszania się piłki
    // x - true = w prawo, false w lewo, y - true = w dół, false w górę
    boolean ballXDirection=true,ballYDirection=false;
    //częstotliwość odświeżania
    private static final int UPDATE_INTERVAL = 15;
    //tworzenie siatki bloczków
    int blockColumns = (int) (screenWidth*0.02);
    int blockRows=2;
    //wymiary bloczków
    int blockWidth;
    int blockHeight=(int)(screenHeight*0.05);
    Block[][] blocks;
    int[] blockRowYPosition;
    //odbicie piłki, zmiana szybkości kierunku
    int ballAngle=0;
    boolean even=false;
    int[] deskParts= new int[8];
    Random generator = new Random();
    //określa szybkość przemieszczania się piłki oraz ]deski
    int level=3;
    String levelText;
    //ilość pozostałych bloków
    int numberOfBlocks;
    //odtwarzacz muzyki
    Player player;
    //ilość punktów
    int score=0;
    String scoreText;
    //czas gry
    int gameTime;
    String gameTimeText;
    //Timer
    Timer timer;
    long startTime;
    
    //instancja
    static Draw instance;
    
    protected void paint(Graphics g) 
    {
        blockXPosition=0;
        blockYPosition=0;
        g.setColor(0,0,0);
        g.fillRect(0,0,screenWidth,screenHeight);
        g.setColor(deskColor);
        g.fillRoundRect(deskXPosition, deskYStartPosition, deskWidth, deskHeight,10,10);
        for(int i=0;i<blockRows;i++)
        {
            for(int j=0;j<blockColumns;j++)
            {
                if(blocks[i][j]!=null)
                {
                g.setColor(blocks[i][j].getRGB());
                g.fillRect(blockXPosition, blockYPosition, blockWidth, blockHeight);
                g.setColor(0x00000000);
                g.drawRect(blockXPosition, blockYPosition, blockWidth, blockHeight);
                blocks[i][j].setX(blockXPosition);
                blocks[i][j].setY(blockYPosition);
                blockXPosition+=blockWidth;
                }
                else
                {
                    g.setColor(0x00000000);
                    g.fillRect(blockXPosition, blockYPosition, blockWidth, blockHeight);
                    blockXPosition+=blockWidth;
                }
            }
            blockXPosition=0;
            blockYPosition+=blockHeight;
        }
        g.setColor(ballColor);
        g.fillArc(ballXPosition, ballYPosition, ballWidth, ballHeight,0,360);
        //wykrywanie ruchu piłki
        boolean isCollide = checkCollisionWithDesk();
        if(isCollide)
        {
            ballYDirection=false;
        }
        checkCollisionWithWall();
        checkCollisionWithBlock();
        checkIfGameOver();
        g.setColor(0x00FFFFFF);
        if(scoreText==null)
        {
            scoreText="Wynik: 0";
        }
        g.drawString(scoreText, 5, getHeight()-10, Graphics.BASELINE | Graphics.LEFT);
        g.setColor(0x00FFFFFF);
        g.drawString("Time: "+gameTime, getWidth()-30, getHeight()-10, Graphics.BASELINE | Graphics.HCENTER);
        if(numberOfBlocks==0)
        {
            nextLevel();
        }
            if(ballXDirection)
            {
                if(even)
                ballXPosition+=ballAngle;
            }
            else
            {
                if(even)
                ballXPosition-=ballAngle; 
            }
            if(!ballYDirection)
            {
                ballYPosition-=1*level;
            }
            else
            {
                ballYPosition+=1*level;
            }
            try
            {
                Thread.sleep(UPDATE_INTERVAL);
            }
            catch(InterruptedException e){}
            even=!even;
            repaint();
    }
    
    public Draw() 
    {
        instance=this;
        if(timer==null)
            timer = new Timer();
        startTime=System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                gameTime++;
            }
        }, new Date(), 1000);
        setColorsForBlocks();
        configureFields();
    }

    protected void keyPressed(int keyCode) 
    {
    switch(keyCode)
        {
            case KEY_NUM6:
                if(deskXPosition+3<screenWidth)
                    deskXPosition+=3*level;
                else if(deskXPosition+2<screenWidth)
                    deskXPosition+=2;
                else if(deskXPosition+1<screenWidth)
                    deskXPosition++;
                break;
            case -4:
                if(deskXPosition+3<screenWidth)
                    deskXPosition+=3*level;
                else if(deskXPosition+2<screenWidth)
                    deskXPosition+=2;
                else if(deskXPosition+1<screenWidth)
                    deskXPosition++;
                break;
            case KEY_NUM4:
                if(deskXPosition-3>0)
                    deskXPosition-=3*level;
                else if(deskXPosition-2>0)
                    deskXPosition-=2;
                else if(deskXPosition-1>0)
                    deskXPosition--;
                break;
            case -3:
                if(deskXPosition-3>0)
                    deskXPosition-=3*level;
                else if(deskXPosition-2>0)
                    deskXPosition-=2;
                else if(deskXPosition-1>0)
                    deskXPosition--;
                break;
        } 
    }

    protected void keyRepeated(int keyCode) 
    {
    switch(keyCode)
        {
            case KEY_NUM6:
                if(deskXPosition+3<screenWidth)
                    deskXPosition+=3*level;
                else if(deskXPosition+2<screenWidth)
                    deskXPosition+=2;
                else if(deskXPosition+1<screenWidth)
                    deskXPosition++;
                break;
            case -4:
                if(deskXPosition+3<screenWidth)
                    deskXPosition+=3*level;
                else if(deskXPosition+2<screenWidth)
                    deskXPosition+=2;
                else if(deskXPosition+1<screenWidth)
                    deskXPosition++;
                break;
            case KEY_NUM4:
                if(deskXPosition-3>0)
                    deskXPosition-=3*level;
                else if(deskXPosition-2>0)
                    deskXPosition-=2;
                else if(deskXPosition-1>0)
                    deskXPosition--;
                break;
            case -3:
                if(deskXPosition-3>0)
                    deskXPosition-=3*level;
                else if(deskXPosition-2>0)
                    deskXPosition-=2;
                else if(deskXPosition-1>0)
                    deskXPosition--;
                break;
        } 
    }
    
    
    private void setColorsForBlocks()
    {
        blockColors[0]=0x00FF0000;
        blockColors[1]=0x0000FF00;
        blockColors[2]=0x0000FFFF;
        blockColors[3]=0x00999999;
    }
    
    private void setAngleForBall(int ballXRelativePosition) 
    {
        int angle = -3;
//        System.out.println("Środek pilki:"+(ballXRelativePosition+ballWidth/2));
//        System.out.println(deskParts[7]);
        for(int i=0;i<deskParts.length-1;i++)
        {
            if((ballXRelativePosition+ballWidth/2)>=deskParts[i]&&(ballXRelativePosition+ballWidth/2)<deskParts[i+1])
            {
//                System.out.println("DeskParts["+i+"]="+deskParts[i]);
//                System.out.println("Actual angle:"+angle);
                ballAngle=angle;
                if(ballAngle>0)
                {
                    ballXDirection=true;
                }
                else
                {
                    ballXDirection=false;
                    ballAngle*=(-1);
                }
                
                break;
                
            }
            angle++;
            if(ballXRelativePosition+(ballWidth/2)>deskParts[7])
            {
                ballAngle=3;
                ballXDirection=true;
                break;
            }
            if(ballXRelativePosition+(ballWidth)/2<0)
            {
                ballAngle=-3;
                ballXDirection=false;
                ballAngle*=(-1);
                break;
            }
        }
//        System.out.println("Angle: "+ballAngle);
    }

    private void configureFields() 
    {
        even=false;
        levelText=Integer.toString(level);
        
        blockRows*=level;
        blockColumns*=level;
        blockWidth=(int)(screenWidth/blockColumns);
        if(blocks!=null)
            blocks=null;
        blocks= new Block[blockRows][blockColumns];
        blockRowYPosition = new int[blockRows]; 
                //pozycja początkowa paletki
        deskXStartPositon=screenWidth/2-deskWidth/2;
        deskYStartPosition=(int) (screenHeight-screenHeight*0.05);
        //aktualna pozycja paletki
        deskXPosition=deskXStartPositon;
        deskYPosition=deskYStartPosition;
            //pozycja piłki
        ballXPosition=deskXPosition+deskWidth/2-ballWidth/2;
        ballYPosition=deskYPosition-ballWidth+1;
        blockXPosition=0;
        blockYPosition=0;
        ballXPosition=deskXPosition+deskWidth/2-ballWidth/2;
        ballYPosition=deskYPosition-ballWidth+1;
        ballXDirection=true;ballYDirection=false;
        numberOfBlocks=blockRows*blockColumns;
            //podzielenie deski na części
            for(int i=0;i<deskParts.length;i++)
            {
                if(i==0)
                {
                    deskParts[i]=0;
                }
                else if(i==7)
                {
                    deskParts[i]=deskWidth;
                }
                else
                {
                    deskParts[i]=(int) (deskWidth*(i+1) /deskParts.length);
                    
                }
                
            }
            for(int i=0;i<blockRows;i++)
            {
                for(int j=0;j<blockColumns;j++)
                {
                    blocks[i][j] = new Block(blockColors[generator.nextInt(4)]);
                }
                if(i==0)
                    blockRowYPosition[i]=0;
                else
                {
                    blockYPosition+=blockHeight;
                    blockRowYPosition[i]=blockYPosition;
                }
            }
            //muzyka
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try
                    {
//                        System.out.println(player);
//                                if(player!=null)
//                player=null;
            InputStream is = getClass().getResourceAsStream("audio/Level"+level+".mp3");
            player = Manager.createPlayer(is,"audio/mpeg");
            
            player.realize();
            player.setLoopCount(-1);
            player.prefetch();
            player.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
                }
            });
        t.start();
    }

    private boolean checkCollisionWithDesk() 
    {
        if(ballYPosition+ballWidth==deskYPosition||(ballYPosition+ballWidth>=deskYPosition&&ballYPosition+ballWidth<=deskYPosition+(deskHeight/2)))
        {
            if(
            (ballXPosition+ballWidth>=deskXPosition&&ballXPosition<=deskXPosition+deskWidth)) //środek piłki dotyka góry deskis
            {
//                System.out.println("Kolizja!");
                setAngleForBall(ballXPosition-deskXPosition);
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    private void checkCollisionWithWall() 
    {
        if(ballXPosition<=0)
        {
            ballXDirection=true;
        }
        if(ballXPosition+ballWidth>=screenWidth)
        {
            ballXDirection=false;
        }
        if(ballYPosition<=0)
        {
            ballYDirection=true;
        }
    }

    private void checkIfGameOver() 
    {

        //sprawdzamy, czy piłka dotyka deski
        if(ballYPosition+ballWidth>=deskYPosition+deskHeight)
        {
            try {
                System.out.println("Przegrałeś !");
                player.stop();
                player=null;
                timer.cancel();
                Midlet.getDisplay().setCurrent(new GameOver());
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkCollisionWithBlock() 
    {
        for(int i=blockRows-1;i>=0;i--)
        {
            //sprawdzanie, czy linia pierwszych bloków nie została przekroczona
            if(!(blockRowYPosition[blockRows-1]+blockHeight>=ballYPosition))
            {
                break;
            }
            //przekroczenie pierwszej linii
            else
            {
                for(int j = 0;j<blockColumns;j++)
                {
                    //jeśli blok nie jest zniszczony, to wchodzimy
                    if(blocks[i][j]!=null)
                    {
                        //jeśli położenie środka piłki jest na długości krawędzi danego bloku oraz piłka się porusza do góry wchodzimy
                        if(
                        blocks[i][j].getX()<=ballXPosition+ballWidth/2
                        &&
                        ballXPosition+ballWidth/2<blocks[i][j].getX()+blockWidth
                        &&
                        ballYDirection==false)
                        {
                            //jeśli położenie górna krawędź piłki i dolna bloku stykają się, wchodzimy
                            if(blocks[i][j].getY()+blockHeight>=ballYPosition)
                            {
                                System.out.println("Blok["+i+"]["+j+"]\t Block x="+blocks[i][j].getX()+" block y="+blocks[i][j].getY()+"\tBall x="+ballXPosition+" ball y="+ballYPosition+" \t Screen width: "+getWidth());
                                System.out.println("Liczba bloków: "+numberOfBlocks);
                                //block zostaje zniszczony
                            blocks[i][j]=null;
                            ballYDirection=!ballYDirection;
                            //piłka porusza się w dół
                            i=-1;
                            score++;
                            scoreText="Wynik: "+String.valueOf(score);
                                System.out.println(scoreText);
                            //zmniejszamy liczbę bloków
                            numberOfBlocks--;
                            ballBlockCollisionSound();
                            //wychodzimy z obu pętl(stąd i=-1), żeby nie zabierać czasu procesorowi
                            break;
                            }
                            //sprawdzamy, czy dotyka sąsiada
                        }
                        else if(
                        blocks[i][j].getX()-ballHeight/2<=ballXPosition
                        &&
                        ballXPosition+ballWidth/2<blocks[i][j].getX()+blockWidth
                        &&
                        ballYDirection==false
                        &&
                        blocks[i][j+1]==null)
                        {
                            if(blocks[i][j].getY()+blockHeight>=ballYPosition)
                            {
                                System.out.println("Blok["+i+"]["+j+"]\t Block x="+blocks[i][j].getX()+" block y="+blocks[i][j].getY()+"\tBall x="+ballXPosition+" ball y="+ballYPosition+" \t Screen width: "+getWidth());
                                
                                //block zostaje zniszczony
                            blocks[i][j]=null;
                            ballYDirection=!ballYDirection;
                            //piłka porusza się w dół
                            i=-1;
                            score++;
                            scoreText="Wynik: "+String.valueOf(score);
                                System.out.println(scoreText);
                            //zmniejszamy liczbę bloków
                            numberOfBlocks--;
                            System.out.println("Liczba bloków do zbicia: "+numberOfBlocks);
                            ballBlockCollisionSound();
                            //wychodzimy z obu pętl(stąd i=-1), żeby nie zabierać czasu procesorowi
                            break;
                            }
                        }
//                        else if(
//                        blocks[i][j].getX()<=ballXPosition+ballWidth/2
//                        &&
//                        ballXPosition+ballWidth/2<blocks[i][j].getX()+blockWidth
//                        &&
//                        ballYDirection==false                                )
                        
                        //pilka "spada"
                        if(
                        blocks[i][j].getX()<=ballXPosition+ballWidth/2
                        &&
                        ballXPosition+ballWidth/2<blocks[i][j].getX()+blockWidth
                        &&
                        ballYDirection==true)
                        {
                            if(blocks[i][j].getY()==ballYPosition+ballWidth)
                            {
                                System.out.println("Blok["+i+"]["+j+"]");
                                //block zostaje zniszczony
                            blocks[i][j]=null;
                            ballYDirection=!ballYDirection;
                            //piłka porusza się w dół
                            i=-1;
                            //zmniejszamy liczbę bloków
                            score++;
                            scoreText="Wynik: "+String.valueOf(score);
                                System.out.println(scoreText);
                            numberOfBlocks--;
                            ballBlockCollisionSound();
                            //wychodzimy z obu pętl(stąd i=-1), żeby nie zabierać czasu procesorowi
                            break;
                            }
                        }
                        if(
                                blocks[i][j].getX()>=ballXPosition+ballWidth        //jeśli piłka jest na lewo
                                &&                                                  //i
                                blocks[i][j].getX()<ballXPosition+ballWidth/2       //jej powierzchnia po przesunięciu styka się maksymalnie do środka
                                &&                                                  //i
                                blocks[i][j].getY()<ballYPosition+ballWidth/2       //środek piłki znajduje się pomiędzy długością krawędzi bocznej bloczka
                                &&
                                blocks[i][j].getY()+blockHeight>ballYPosition+ballWidth/2)
                        {
                            System.out.println("Blok["+i+"]["+j+"]");
                            blocks[i][j]=null;
                            i=-1;
                            //zmniejszamy liczbę bloków
                            numberOfBlocks--;
                            score++;
                            scoreText="Wynik: "+String.valueOf(score);
                                System.out.println(scoreText);
                            ballBlockCollisionSound();
                            ballXDirection=!ballXDirection;
                            break;
                        }
                        if(
                                blocks[i][j].getX()+blockWidth<=ballXPosition                  //sytuacja analogiczna jak wyżej
                                &&
                                blocks[i][j].getX()+blockWidth>ballXPosition-ballWidth/2       //tylko dla prawej strony
                                &&
                                blocks[i][j].getY()<ballYPosition+ballWidth/2
                                &&
                                blocks[i][j].getY()+blockHeight>ballYPosition+ballWidth/2)
                        {
                            System.out.println("Blok["+i+"]["+j+"]");
                            blocks[i][j]=null;

                            //zmniejszamy liczbę bloków
                            numberOfBlocks--;
                            i=-1;
                            score++;
                            scoreText="Wynik: "+String.valueOf(score);
                                System.out.println(scoreText);
                            ballBlockCollisionSound();
                            ballXDirection=!ballXDirection;
                            break;
                        }
                    }
                }
                
            }
        }
    }
   

    private void nextLevel() 
    {
        try {
            score+=100;
           
            if(player!=null)
            player.stop();
            
            if(level==4)
            {
                arkanoidVictory();
            }
            else
            {
                levelVictory();
            }
            
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
        level++;
        configureFields();
    }
    
    void ballBlockCollisionSound()
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try{
                            InputStream is = getClass().getResourceAsStream("audio/ballBlockCollision.mp3");
            Player ballCollison;
            
            ballCollison = Manager.createPlayer(is,"audio/mpeg");
            
            //  if "audio/mpeg" doesn't work try "audio/mp3"
            
            ballCollison.realize();  
            ballCollison.prefetch();
            ballCollison.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
            }
            }});
        t.start();
    }

    private void levelVictory() 
    {

        try {
            player.stop();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
        Thread t = new Thread(new Runnable() {
            public void run() {
                try{
                            InputStream is = getClass().getResourceAsStream("audio/victory.mp3");
            Player victory;
            
            victory = Manager.createPlayer(is,"audio/mpeg");
            
            //  if "audio/mpeg" doesn't work try "audio/mp3"
            
            victory.realize();  
            victory.prefetch();
            victory.start();
            duration = victory.getDuration();
                    System.out.println(victory.getDuration());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
            }
            }});
        t.start();
        try {
            System.out.println(duration);
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void arkanoidVictory() 
    {
        Midlet.getDisplay().setCurrent(new Victory());
    }
    
    public String getScore()
    {
        return scoreText;
    }
    
    public String getTime()
    {
        return String.valueOf(gameTime);
    }

    public static Draw getInstance() 
    {
        return instance;
    }
}
