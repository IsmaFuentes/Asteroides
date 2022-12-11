package com.example.asteroides.models;

public class Missile {
  private Graphic graphic;
  private int ms;

  public Missile(Graphic graphic, int ms){
    this.graphic = graphic;
    this.ms  = ms;
  }

  public Missile(){

  }

  public int getMs(){
    return ms;
  }

  public Graphic getGraphic(){
    return graphic;
  }

  public void setMs(int ms){
    this.ms = ms;
  }

  public void setGraphic(Graphic graphic){
    this.graphic = graphic;
  }
}
