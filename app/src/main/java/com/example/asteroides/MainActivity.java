package com.example.asteroides;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {
  private Button quitButton;
  private Button aboutButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    HandleButtonsRegistration();
  }

  private void HandleButtonsRegistration(){
    // QUIT BUTTON
    quitButton = findViewById(R.id.QuitButton);
    quitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) { QuitAction(); }
    });

    // ABOUT BUTTON
    aboutButton = findViewById(R.id.AboutButton);
    aboutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) { AboutAction(); }
    });
  }

  private void AboutAction(){
    startActivity(new Intent(this, AboutActivity.class));
  }

  private void QuitAction(){
    this.finish();
  }

}