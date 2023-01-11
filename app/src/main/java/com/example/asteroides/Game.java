package com.example.asteroides;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.asteroides.views.GameView;

public class Game extends AppCompatActivity {

  private GameView gameView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);

    gameView = (GameView)findViewById(R.id.gameView);
  }

  @Override
  protected void onPause(){
    super.onPause();
    gameView.getThread().Pause();
  }

  @Override
  protected void onResume(){
    super.onResume();
    gameView.getThread().Resume();
  }

  @Override
  protected void onDestroy(){
    gameView.getThread().Stop();
    super.onDestroy();
  }

}