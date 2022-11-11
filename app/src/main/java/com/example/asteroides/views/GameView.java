package com.example.asteroides.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.example.asteroides.R;
import com.example.asteroides.models.Graphic;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class GameView extends View {
  private List<Graphic> asteroids;
  private int asteroidCount = 5;
  private int fragmentCount = 3;

  // SHIP
  private Graphic ship;
  private int shipTurn;
  private float shipAccel;

  private static final int SHIP_TURN_TICK = 5;
  private static final float SHIP_ACCEL_TICK = 0.5f;
  private static final double SHIP_MAX_VELOCITY = 50;

  public GameView(Context context, AttributeSet attrs){
    super(context, attrs);
    Drawable drawableShip, drawableAsteroid, drawableRocket;

    // GRAPHICS QUALITY HANDLING
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

    if(prefs.getString("graphics", "1").equals("1")){
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
      setBackgroundColor(Color.BLACK);

      // ASTEROID DRAWABLE
      Path asteroidPath = new Path();
      asteroidPath.moveTo((float) 0.3, (float) 0.0);
      asteroidPath.lineTo((float) 0.6, (float) 0.0);
      asteroidPath.lineTo((float) 0.6, (float) 0.3);
      asteroidPath.lineTo((float) 0.8, (float) 0.2);
      asteroidPath.lineTo((float) 1.0, (float) 0.4);
      asteroidPath.lineTo((float) 0.8, (float) 0.6);
      asteroidPath.lineTo((float) 0.9, (float) 0.9);
      asteroidPath.lineTo((float) 0.8, (float) 1.0);
      asteroidPath.lineTo((float) 0.4, (float) 1.0);
      asteroidPath.lineTo((float) 0.0, (float) 0.6);
      asteroidPath.lineTo((float) 0.0, (float) 0.2);
      asteroidPath.lineTo((float) 0.3, (float) 0.0);
      ShapeDrawable dAsteroid = new ShapeDrawable(new PathShape(asteroidPath, 1, 1));
      dAsteroid.getPaint().setColor(Color.WHITE);
      dAsteroid.getPaint().setStyle(Paint.Style.STROKE);
      dAsteroid.setIntrinsicWidth(50);
      dAsteroid.setIntrinsicHeight(50);
      drawableAsteroid = dAsteroid;

      // PLAYER DRAWABLE
      Path playershipPath = new Path();
      playershipPath.moveTo((float) 0.0, (float) 0.0);
      playershipPath.lineTo((float) 0.0, (float) 1.0);
      playershipPath.lineTo((float) 1.0, (float) 0.5);
      playershipPath.lineTo((float) 0.0, (float) 0.0);

      ShapeDrawable dShip = new ShapeDrawable(new PathShape(playershipPath, 1, 1));
      dShip.getPaint().setColor(Color.WHITE);
      dShip.getPaint().setStyle(Paint.Style.STROKE);
      dShip.setIntrinsicWidth(40); // 20
      dShip.setIntrinsicHeight(30); // 15
      drawableShip = dShip;
    }else{
      setLayerType(View.LAYER_TYPE_HARDWARE, null);
      drawableAsteroid = context.getResources().getDrawable(R.drawable.asteroid_1);
      drawableShip = context.getResources().getDrawable(R.drawable.ship_1_normal);
    }

    // SHIP
    ship = new Graphic(this, drawableShip);

    // ASTEROIDS
    asteroids = new ArrayList<Graphic>();
    for(int i = 0; i < asteroidCount; i++){
      Graphic asteroid = new Graphic(this, drawableAsteroid);
      asteroid.setIncY(Math.random() * 4 - 2);
      asteroid.setIncX(Math.random() * 4 - 2);
      asteroid.setAngle((int)Math.random() * 360);
      asteroid.setRotation((int)Math.random() * 8 - 4);
      asteroids.add(asteroid);
    }
  }

  private void HandleGraphicsQualityMode(){

  }

  @Override
  protected void onSizeChanged(int w, int h, int prev_w, int prev_h){
    super.onSizeChanged(w,h, prev_w, prev_h);
    // PLAYER SHIP
    ship.setPosX(w / 2 - ship.getWidth() / 2);
    ship.setPosY(h / 2 - ship.getHeight() / 2);

    // ASTEROIDS
    for(Graphic asteroid: asteroids){
      do{
        // collision check
        asteroid.setPosX(Math.random()* (w - asteroid.getWidth()));
        asteroid.setPosY(Math.random()* (h - asteroid.getHeight()));
      }
      while(asteroid.distance(ship) < (w + h) / 5);
    }
  }

  @Override
  protected void onDraw(Canvas canvas){
    super.onDraw(canvas);
    // PLAYER SHIP
    ship.drawGraphic(canvas);

    // ASTEROIDS
    for(Graphic asteroid:asteroids){
      asteroid.drawGraphic(canvas);
    }
  }
}
