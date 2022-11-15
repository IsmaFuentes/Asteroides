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

  private Drawable drawable_ship;
  private Drawable drawable_roid;
  private Drawable drawable_rocket;

  private Graphic ship;
  private int shipTurn;
  private float shipAccel;

  private static final int SHIP_TURN_TICK = 5;
  private static final float SHIP_ACCEL_TICK = 0.5f;
  private static final double SHIP_MAX_VELOCITY = 50;

  public GameView(Context context, AttributeSet attrs){
    super(context, attrs);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

    if(prefs.getString("graphics", "1").equals("1")){
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
      setBackgroundColor(Color.BLACK);
      drawable_roid = createShapedAsteroid(0.5f,0.5f, 50,50);
      drawable_ship = createShapedShip(0.5f,0.5f,40,40);
    }else{
      setLayerType(View.LAYER_TYPE_HARDWARE, null);
      drawable_roid = context.getResources().getDrawable(R.drawable.asteroid_1);
      drawable_ship = context.getResources().getDrawable(R.drawable.ship_1_normal);
    }

    // SHIP
    ship = new Graphic(this, drawable_ship);

    // ASTEROIDS
    asteroids = new ArrayList<Graphic>();
    for(int i = 0; i < asteroidCount; i++){
      Graphic asteroid = new Graphic(this, drawable_roid);
      asteroid.setIncY(Math.random() * 4 - 2);
      asteroid.setIncX(Math.random() * 4 - 2);
      asteroid.setAngle((int)Math.random() * 360);
      asteroid.setRotation((int)Math.random() * 8 - 4);
      asteroids.add(asteroid);
    }
  }

  private ShapeDrawable createShapedAsteroid(float sW, float sH, int width, int height){
    Path path = new Path();
    path.moveTo((float) 0.3, (float) 0.0);
    path.lineTo((float) 0.6, (float) 0.0);
    path.lineTo((float) 0.6, (float) 0.3);
    path.lineTo((float) 0.8, (float) 0.2);
    path.lineTo((float) 1.0, (float) 0.4);
    path.lineTo((float) 0.8, (float) 0.6);
    path.lineTo((float) 0.9, (float) 0.9);
    path.lineTo((float) 0.8, (float) 1.0);
    path.lineTo((float) 0.4, (float) 1.0);
    path.lineTo((float) 0.0, (float) 0.6);
    path.lineTo((float) 0.0, (float) 0.2);
    path.lineTo((float) 0.3, (float) 0.0);

    ShapeDrawable shape = new ShapeDrawable(new PathShape(path, sW, sH));
    shape.getPaint().setColor(Color.WHITE);
    shape.getPaint().setStyle(Paint.Style.STROKE);
    shape.setIntrinsicWidth(width);
    shape.setIntrinsicHeight(height);

    return shape;
  }

  private ShapeDrawable createShapedShip(float sW, float sH, int width, int height){
    Path path = new Path();
    path.moveTo((float) 0.0, (float) 0.0);
    path.lineTo((float) 0.0, (float) 1.0);
    path.lineTo((float) 1.0, (float) 0.5);
    path.lineTo((float) 0.0, (float) 0.0);

    ShapeDrawable shape = new ShapeDrawable(new PathShape(path, sW, sH));
    shape.getPaint().setColor(Color.WHITE);
    shape.getPaint().setStyle(Paint.Style.STROKE);
    shape.setIntrinsicWidth(width); // 20
    shape.setIntrinsicHeight(height); // 15

    return shape;
  }

  @Override
  protected void onSizeChanged(int w, int h, int prev_w, int prev_h){
    super.onSizeChanged(w,h, prev_w, prev_h);
    ship.setPosX(w / 2 - ship.getWidth() / 2);
    ship.setPosY(h / 2 - ship.getHeight() / 2);
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
    ship.drawGraphic(canvas);
    for(Graphic asteroid:asteroids){
      asteroid.drawGraphic(canvas);
    }
  }
}
