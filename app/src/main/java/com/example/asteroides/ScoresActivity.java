package com.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.example.asteroides.models.ScoreAdapter;

public class ScoresActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scores);
    setListAdapter(new ScoreAdapter(this, MainActivity._store.GetScore(10)));
  }

  @Override
  protected void onListItemClick(ListView listView, View view, int position, long id){
    super.onListItemClick(listView, view, position, id);
  }
}