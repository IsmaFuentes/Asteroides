package com.example.asteroides;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.asteroides.models.ScoreStore;

public class MainActivity extends AppCompatActivity {
  private Animation zoomRotate;
  private Animation fadeIn;
  private Animation moveRigth;
  private Animation moveBottom;
  private Animation bounce;
  private Button quitButton;
  private Button aboutButton;
  private Button configButton;
  private Button playButton;

  public static ScoreStore _store;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Puntuaciones
    _store = new ScoreStore();
  }

  @Override
  protected void onStart() {
    super.onStart();

    // Configuración
    HandleButtonsRegistration();

    // Animations
    HandleAnimations();
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.application_menu, menu);
    return true;
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item){
    // Menú preferencias
    switch(item.getItemId()){
      case R.id.preferences:
        ActionStartActivity(PreferencesActivity.class);
        break;
      case R.id.about:
        // Arranca la actividad "Sobre
        break;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void HandleAnimations(){
    // ANIMATIONS
    zoomRotate = AnimationUtils.loadAnimation(this, R.anim.zoom_rotation);
    fadeIn= AnimationUtils.loadAnimation(this, R.anim.fade_in);
    moveRigth = AnimationUtils.loadAnimation(this, R.anim.move_rigth);
    bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
    moveBottom = AnimationUtils.loadAnimation(this, R.anim.move_bottom);

    // TITLE
    TextView mainTitle = (TextView)findViewById(R.id.main_title);
    mainTitle.startAnimation(zoomRotate);

    playButton.startAnimation(fadeIn);
    configButton.startAnimation(moveRigth);
    aboutButton.startAnimation(bounce);
    quitButton.startAnimation(moveBottom);
  }

  private void HandleButtonsRegistration(){
    // QUIT BUTTON
    quitButton = findViewById(R.id.QuitButton);
    quitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // ActionFinishActivity();
        ActionStartActivity(ScoresActivity.class);
      }
    });

    // ABOUT BUTTON
    aboutButton = findViewById(R.id.AboutButton);
    aboutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActionStartActivity(AboutActivity.class);
        aboutButton.startAnimation(zoomRotate);
      }
    });

    // PLAY BUTTON
    playButton = findViewById((R.id.StartButton));
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActionStartActivity(Game.class);
      }
    });

    // CONFIG BUTTON
    configButton = findViewById((R.id.ConfigButton));
  }

  private void ActionStartActivity(Class instance) {
    startActivity(new Intent(this, instance));
  }
  private void ActionFinishActivity(){
    this.finish();
  }
}