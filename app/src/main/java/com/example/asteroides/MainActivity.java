package com.example.asteroides;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.asteroides.models.ScoreStore;

public class MainActivity extends AppCompatActivity {
  private Button quitButton;
  private Button aboutButton;
  private Button playButton;

  public static ScoreStore _store;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Puntuaciones
    _store = new ScoreStore();

    // Configuración
    HandleButtonsRegistration();
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
      }
    });

    // PLAY BUTTON
    playButton = findViewById((R.id.StartButton));
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActionStartGame();
      }
    });
  }
  private void ActionStartGame(){
    Toast.makeText(this, "Work in progres.s.", Toast.LENGTH_SHORT).show();
  }
  private void ActionStartActivity(Class instance) {
    startActivity(new Intent(this, instance));
  }
  private void ActionFinishActivity(){
    this.finish();
  }
}