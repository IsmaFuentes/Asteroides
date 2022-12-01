package com.example.asteroides.models;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Graphic {
  private Drawable drawable;
  private double posX, posY;
  private double incX, incY;
  private int angle, rotation;
  private int width, height;
  private int collisionRadius;
  private View view;

  public static final int MAX_VELOCITY = 20;

  public Graphic(View view, Drawable drawable){
    this.view = view;
    this.drawable = drawable;
    width = drawable.getIntrinsicWidth();
    height = drawable.getIntrinsicHeight();
    collisionRadius = (height / width) / 4;
  }

  public void drawGraphic(Canvas canvas){
    canvas.save();
    int x = (int)(posX + width / 2);
    int y = (int)(posY + height / 2);

    canvas.rotate((float)angle, (float)x, (float)y);
    drawable.setBounds((int)posX, (int)posY, (int)(posX + width), (int)(posY + height));
    drawable.draw(canvas);
    canvas.restore();

    int rInval = (int)(Math.hypot(width, height) / 2 + MAX_VELOCITY);
    view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);
  }

  public int getWidth(){
    return this.width;
  }

  public int getHeight(){
    return this.height;
  }
  public int getAngle() {
    return this.angle;
  }
  public double getIncX() {
    return this.incX;
  }
  public double getIncY() {
    return this.incY;
  }

  public void setPosX(double x){
    this.posX = x;
  }

  public void setPosY(double y){
    this.posY = y;
  }

  public void setIncX(double incX){
    this.incX = incX;
  }

  public void setIncY(double incY){
    this.incY = incY;
  }

  public void setAngle(int angle){
    this.angle = angle;
  }

  public void setRotation(int rotation){
    this.rotation = rotation;
  }

  public void incrementPosition(double factor){
    posX += incX * factor;

    if(posX < -width / 2){
      posX = view.getWidth() - width / 2;
    }

    if(posX > view.getWidth() - width / 2){
      posX = -width / 2;
    }

    posY += incY * factor;

    if(posY < -height / 2){
      posY = view.getHeight() - height / 2;
    }

    if(posY > view.getHeight() - height / 2){
      posY = -height / 2;
    }

    angle += rotation * factor;
  }

  public double distance(Graphic g){
    return Math.hypot(posX - g.posX, posY - g.posY);
  }

  public boolean verifyCollision(Graphic g){
    return (distance(g) < (collisionRadius + g.collisionRadius));
  }

}
