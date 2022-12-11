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
import android.graphics.drawable.shapes.RectShape;
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
  private List<Graphic> missiles;
  private int asteroidCount = 5;
  private int fragmentCount = 3;

  // DRAWABLES
  private Drawable drawable_ship;
  private Drawable drawable_roid;
  private Drawable drawable_missile;

  // PLAYER SHIP
  private Graphic ship;
  private int shipAngle;
  private float shipAccel;
  private boolean shoot;
  private static final int SHIP_ANGLE_TICK = 5;
  private static final float SHIP_ACCEL_TICK = 0.5f;
  private static final double SHIP_MAX_VELOCITY = 50;

  // MISILES
  private static int MISSILE_VELOCITY_TICK = 12;
  private List<Integer> missileTimes;

  // THREAD + TIMINGS
  private GameThread thread = new GameThread();
  private static int REFRESH_RATE = 50;
  private long lastMs = 0;

  // MOVEMENTS
  private float mX;
  private float mY;
  private int choosenControl;
  private static final int CONTROLS_KEYBOARD = 1;
  private static final int CONTROLS_TOUCHPAD = 2;
  private static final int CONTROLS_SENSORS = 3;

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

    // Preferencias de usuario
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

    handlePreferences(prefs);

    if(prefs.getString("graphics", "1").equals("1")){
      // Vectorial
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
      setBackgroundColor(Color.BLACK);
      drawable_roid = createShapedAsteroid(1f,1f, 50,50);
      drawable_ship = createShapedShip(1f,1f,50,40);
      drawable_missile = createShapedMissile(15, 3);
    }else{
      // Bitmap
      setLayerType(View.LAYER_TYPE_HARDWARE, null);
      drawable_roid = context.getResources().getDrawable(R.drawable.asteroid_1);
      drawable_ship = context.getResources().getDrawable(R.drawable.ship_1_normal);
      drawable_missile = context.getResources().getDrawable(R.drawable.missile1);
    }

    // SHIP
    ship = new Graphic(this, drawable_ship);

    // MISSILE
    missiles = new ArrayList<Graphic>();
    missileTimes = new ArrayList<Integer>();

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

    if(choosenControl == CONTROLS_SENSORS){
      // Registramos los sensores de movimiento solo si el usuario los ha elegido como tipo de entrada
      sensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
      List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
      if(!sensors.isEmpty()){
        Sensor accelSensor = sensors.get(0);
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
      }
    }
  }

  private void handlePreferences (SharedPreferences prefs){
    switch (prefs.getString("controls", "1")){
      case "1": // TECLADO
        choosenControl = CONTROLS_KEYBOARD;
        break;
      case "2": // TOUCHPAD
        choosenControl = CONTROLS_TOUCHPAD;
        break;
      case "3": // SENSORES
        choosenControl = CONTROLS_SENSORS;
        break;
      default:
        break;
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

    float diff_z = z - sensor_z;
    Log.i("[MOV]", "Diferencia inclinación: " + diff_z);

    if(diff_z > 0){
      shipAccel = SHIP_ACCEL_TICK / 3;
    } else if(diff_z < 0){
      shipAccel = -SHIP_ACCEL_TICK / 3;
    }else{
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

  private ShapeDrawable createShapedMissile(int width, int height){
    ShapeDrawable dMissile = new ShapeDrawable(new RectShape());
    dMissile.getPaint().setColor(Color.WHITE);
    dMissile.getPaint().setStyle(Paint.Style.STROKE);
    dMissile.setIntrinsicWidth(width); // 15
    dMissile.setIntrinsicHeight(height); // 3
    return dMissile;
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

    for(Graphic missile:missiles){
      missile.drawGraphic(canvas);
    }


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

    for(int x = 0; x < missiles.size(); x++){
      Graphic missile = missiles.get(x);
      int missileMs = missileTimes.get(x);

      missile.incrementPosition(delay);
      missileMs -= delay;
      missileTimes.set(x, missileMs);
      if (missileMs < 0){ // < 0
        destroyMissile(x);
      } else{
        for (int i = 0; i < asteroids.size(); i++)
          if (missile.verifyCollision(asteroids.get(i))) {
            destroyAsteroid(i);
            destroyMissile(x);
            break;
          }
      }
    }

    for (Graphic roid: asteroids) {
      roid.incrementPosition(delay);
    }
  }

  public boolean onTouchEvent(MotionEvent event){
    super.onTouchEvent(event);

    float x = event.getX();
    float y = event.getY();
    switch (event.getAction()){
      case MotionEvent.ACTION_DOWN:
        shoot = true;
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
    if(choosenControl == CONTROLS_TOUCHPAD){
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
  }

  public void onTouchEventUp(){
    shipAngle = 0;
    shipAccel = 0;
    if(shoot){
      activateMisile();
    }
  }

  public void activateMisile(){
    Graphic missile = new Graphic(this, drawable_missile);
    missile.setPosX(ship.getPosX() + ship.getWidth() / 2 - missile.getWidth() / 2);
    missile.setPosY(ship.getPosY() + ship.getHeight() / 2 - missile.getHeight() / 2);
    missile.setAngle(ship.getAngle());
    missile.setIncX(Math.cos(Math.toRadians(missile.getAngle())) *  MISSILE_VELOCITY_TICK);
    missile.setIncY(Math.sin(Math.toRadians(missile.getAngle())) *  MISSILE_VELOCITY_TICK);

    int missileMs = (int) Math.min(this.getWidth() / Math.abs(missile.getIncX()), this.getHeight() / Math.abs(missile.getIncY())) - 2;

    missiles.add(missile);
    missileTimes.add(missileMs);
  }

  public void destroyAsteroid(int i){
    Log.i("[ASTEROIDS]", "Asteroid destroyed");
    asteroids.remove(i);
  }

  public void destroyMissile(int i){
    Log.i("[MISSILES]", "Missile destroyed");
    missiles.remove(i);
    missileTimes.remove(i);
  }

  public void onTouchEventDown(){
    shoot = true;
  }

  public boolean onKeyDown(int keyCode, KeyEvent event){
    super.onKeyDown(keyCode, event);

    boolean processed = true;
    if(choosenControl == CONTROLS_KEYBOARD){
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
    }

    return processed;
  }

  public  boolean onKeyUp(int keyCode, KeyEvent event){
    super.onKeyUp(keyCode, event);

    boolean processed = true;
    if(choosenControl == CONTROLS_KEYBOARD){
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
