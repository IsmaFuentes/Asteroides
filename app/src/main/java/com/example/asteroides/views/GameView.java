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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.preference.PreferenceManager;
import com.example.asteroides.R;
import com.example.asteroides.models.Graphic;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View implements SensorEventListener {
  private List<Graphic> asteroids;
  private int asteroidCount = 5;
  private int fragmentCount = 3;

  private Drawable drawable_ship;
  private Drawable drawable_roid;
  private Drawable drawable_rocket;

  private Graphic ship;
  private int shipAngle;
  private float shipAccel;
  private boolean shoot;
  private static final int SHIP_ANGLE_TICK = 5;
  private static final float SHIP_ACCEL_TICK = 0.5f;
  private static final double SHIP_MAX_VELOCITY = 50;

  // THREAD + TIMINGS
  private GameThread thread = new GameThread();
  private static int REFRESH_RATE = 50;
  private long lastMs = 0;

  // MOVEMENTS
  private float mX;
  private float mY;

  // SENSORS
  private SensorManager sensorManager;
  private boolean sensor_y_exists = false;
  private boolean sensor_z_exists = false;

  private float sensor_y = 0;
  private float sensor_z = 0;

  public GameView(Context context, AttributeSet attrs){
    super(context, attrs);

    // Mantiene la pantalla en marcha mientras el juego esté en marcha
    this.setKeepScreenOn(true);

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
      asteroid.setAngle((int)(Math.random() * 360));
      asteroid.setRotation((int)((Math.random() * 8) - 4));
      asteroids.add(asteroid);
    }

    sensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
    List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    if(!sensors.isEmpty()){
      Sensor accelSensor = sensors.get(0);
      sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
    }
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent){
    // x = 0, y = 1, z = 2
    float y = sensorEvent.values[1];
    float z = sensorEvent.values[2];

    if(!sensor_y_exists){ // Valor inicial y
      sensor_y = y;
      sensor_y_exists = true;
    }

    if(!sensor_z_exists){ // Valor inicial z
      sensor_z = z;
      sensor_z_exists = true;
    }

    // ángulo de rotación
    shipAngle = (int)(y - sensor_y);

    float diff_z = sensor_z - z;
    float thresh = 1.5f; // margen inclinación eje z
    float df_abs = Math.abs(diff_z);

    if(diff_z < 0 && df_abs > thresh){ // Aceleración
      Log.i("[MOV]", "UP difference: " + diff_z);
      shipAccel = SHIP_ACCEL_TICK / 3;
    }

    if(diff_z > 0 && df_abs > thresh){ // Desaceleración
      Log.i("[MOV]", "DP difference: " + diff_z);
      shipAccel = -SHIP_ACCEL_TICK / 3;
    }

    if(df_abs < thresh){ // Punto muerto
      shipAccel = 0;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i){

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
    shape.setIntrinsicWidth(width);
    shape.setIntrinsicHeight(height);

    return shape;
  }

  @Override
  protected void onSizeChanged(int w, int h, int prev_w, int prev_h){
    super.onSizeChanged(w,h, prev_w, prev_h);
    ship.setPosX(w / 2 - ship.getWidth() / 2);
    ship.setPosY(h / 2 - ship.getHeight() / 2);
    for(Graphic asteroid: asteroids){
      do{ // check distancia nave / asteroides
        asteroid.setPosX(Math.random() * (w - asteroid.getWidth()));
        asteroid.setPosY(Math.random() * (h - asteroid.getHeight()));
      }
      while(asteroid.distance(ship) < (w + h) / 5);
    }

    lastMs = System.currentTimeMillis();
    thread.start();
  }

  @Override
  protected synchronized void onDraw(Canvas canvas){
    super.onDraw(canvas);
    ship.drawGraphic(canvas);
    for(Graphic asteroid:asteroids){
      asteroid.drawGraphic(canvas);
    }
  }

  protected synchronized void updatePhysics(){
    long now = System.currentTimeMillis();

    if(lastMs + REFRESH_RATE > now){
      return;
    }

    double delay = (now - lastMs) / REFRESH_RATE;
    lastMs = now;

    ship.setAngle((int)(ship.getAngle() + shipAngle * delay));
    double nIncX = ship.getIncX() + shipAccel * Math.cos(Math.toRadians(ship.getAngle())) * delay;
    double nIncY = ship.getIncY() + shipAccel * Math.sin(Math.toRadians(ship.getAngle())) * delay;

    if (Math.hypot(nIncX, nIncY) <= SHIP_MAX_VELOCITY){
      ship.setIncX(nIncX);
      ship.setIncY(nIncY);
    }

    ship.incrementPosition(delay);
    for (Graphic roid : asteroids) {
      roid.incrementPosition(delay);
    }
  }

  public boolean onTouchEvent(MotionEvent event){
    super.onTouchEvent(event);
    float x = event.getX();
    float y = event.getY();
    switch (event.getAction()){
      case MotionEvent.ACTION_DOWN:
        onTouchEventDown();
        break;
      case MotionEvent.ACTION_MOVE:
        onTouchEventMove(x, y);
        break;
      case MotionEvent.ACTION_UP:
        onTouchEventUp();
        break;
    }
    mX = x;
    mY = y;
    return true;
  }

  private void onTouchEventMove(float x, float y){
    int drag_distance = 6;
    float diff_x = Math.abs(x - mX);
    float diff_y = mY - y; // float dy = Math.abs(y - mY);
    if(diff_y < drag_distance && diff_x > drag_distance){
      shipAngle = Math.round((x - mX) / 2);
      shoot = false;
    }else if(diff_x < drag_distance && diff_y > drag_distance){
      shipAccel = Math.round((mY - y) / 25);
      shoot = false;
    }
  }

  public void onTouchEventUp(){
    shipAngle = 0;
    shipAccel = 0;
    if(shoot){
      // activateMisile();
    }
  }

  public void onTouchEventDown(){
    shoot = true;
  }

  public boolean onKeyDown(int keyCode, KeyEvent event){
    super.onKeyDown(keyCode, event);
    boolean processed = true;

    switch (keyCode){
      case KeyEvent.KEYCODE_DPAD_UP:
        shipAccel = +SHIP_ACCEL_TICK;
        break;
      case KeyEvent.KEYCODE_DPAD_LEFT:
        shipAngle = -SHIP_ANGLE_TICK;
        break;
      case KeyEvent.KEYCODE_DPAD_RIGHT:
        shipAngle = +SHIP_ANGLE_TICK;
        break;
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER:
        // TODO: shotMisile
        break;
      default:
        processed = false;
        break;
    }

    return processed;
  }

  public  boolean onKeyUp(int keyCode, KeyEvent event){
    super.onKeyUp(keyCode, event);
    boolean processed = true;

    switch (keyCode){
      case KeyEvent.KEYCODE_DPAD_UP:
        shipAccel = 0;
        break;
      case KeyEvent.KEYCODE_DPAD_LEFT:
      case KeyEvent.KEYCODE_DPAD_RIGHT:
        shipAngle = 0;
        break;
      case KeyEvent.KEYCODE_ENTER:
        // TODO: shotMisile
        break;
      default:
        processed = false;
        break;
    }

    return processed;
  }

  public class GameThread extends Thread {
    @Override
    public void run(){
      while(true){
        updatePhysics();
      }
    }
  }
}
