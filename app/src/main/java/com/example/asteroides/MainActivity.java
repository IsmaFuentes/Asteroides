package com.example.asteroides;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {
  private Button quitButton;
  private Button aboutButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //...
    HandleButtonsRegistration();
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.application_menu, menu);
    return true;
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item){
    switch(item.getItemId()){
      case R.id.preferences:
        ActionStartActivity(PreferencesActivity.class);
        break;
      case R.id.about:
        // Arranca la actividad "Sobre" (por ahora muestra los valores guardados en preferencias)
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
      public void onClick(View view) { ActionFinishActivity(); }
    });

    // ABOUT BUTTON
    aboutButton = findViewById(R.id.AboutButton);
    aboutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) { ActionStartActivity(AboutActivity.class); }
    });
  }

  private void ActionStartActivity(Class instance) {
    startActivity(new Intent(this, instance));
  }
  private void ActionFinishActivity(){
    this.finish();
  }

}