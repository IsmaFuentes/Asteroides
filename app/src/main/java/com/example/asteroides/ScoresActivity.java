package com.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import com.example.asteroides.models.ScoreAdapter;

public class ScoresActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scores);
    // setListAdapter(new ArrayAdapter(this, R.layout.element_list, R.id.ListTitle,MainActivity._store.GetScore(10)));
    setListAdapter(new ScoreAdapter(this, MainActivity._store.GetScore(10)));
  }
}

//public class ScoresActivity extends Activity {
//
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_scores);
//    ListView view = findViewById(R.id.ScoresList);
//    view.setAdapter(new ArrayAdapter(this, R.layout.element_list, R.id.ListTitle, MainActivity._store.GetScore(10)));
//  }
//}