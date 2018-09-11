/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

/**
 *
 * @author kucht
 */
public class Ball {
    
    int width;
    int height;
    int r;
    int Nx,Ny,Sx,Sy,Ex,Ey,Wx,Wy,NEx,NEy,SEx,SEy,SWx,SWy,NWx,NWy;
    
    
    public Ball(int width,int height)
    {
        this.width=width;
        this.height=height;
        r=height/2;
        Nx=0;
        Ny=height/2;
        Sx=width/2;
        Sy=height;
        Ex=width;
        Ey=height/2;
        Wx=0;
        Wy=height/2;
        NEx=(int)(width/4*3);
        NEy=(int)(height/4);
        SEx=(int)(width/4*3);
        SEy=(int)(height/4*3);
        SWx=(int)(width/4);
        SWy=(int)(height/4*3);
        NWx=(int)(width/4);
        NWy=(int)(height/4);
    }

    public int getNx() {
        return Nx;
    }

    public int getNy() {
        return Ny;
    }

    public int getSx() {
        return Sx;
    }

    public int getSy() {
        return Sy;
    }

    public int getEx() {
        return Ex;
    }

    public int getEy() {
        return Ey;
    }

    public int getWx() {
        return Wx;
    }

    public int getWy() {
        return Wy;
    }

    public int getNEx() {
        return NEx;
    }

    public int getNEy() {
        return NEy;
    }

    public int getSEx() {
        return SEx;
    }

    public int getSEy() {
        return SEy;
    }

    public int getSWx() {
        return SWx;
    }

    public int getSWy() {
        return SWy;
    }

    public int getNWx() {
        return NWx;
    }

    public int getNWy() {
        return NWy;
    }
    
    
    
}
