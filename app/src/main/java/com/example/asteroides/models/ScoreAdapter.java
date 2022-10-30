package com.example.asteroides.models;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.asteroides.R;

import java.util.List;

public class ScoreAdapter extends BaseAdapter {
  private final Activity _activity;
  private final List<String> _list;

  public ScoreAdapter(Activity _activity, List<String> _list){
    super();
    this._activity = _activity;
    this._list = _list;
  }

  public View getView(int position, View convertView, ViewGroup parent){
    LayoutInflater inflater = _activity.getLayoutInflater();
    View view = inflater.inflate(R.layout.element_list, null, true);
    TextView textView = view.findViewById(R.id.ListTitle);
    textView.setText(_list.get(position));
    ImageView imageView = view.findViewById(R.id.ListIcon);

    switch (Math.round((float)Math.random() * 3)){
      case 0:
        imageView.setImageResource(R.drawable.asteroid_1);
        break;
      case 1:
        imageView.setImageResource(R.drawable.asteroid_2);
        break;
      case 2:
        imageView.setImageResource(R.drawable.asteroid_3);
        break;
      default:
        imageView.setImageResource(R.drawable.asteroid_3);
        break;
    }

    return view;
  }

  public int getCount(){
    return _list.size();
  }

  public Object getItem(int position){
    return position;
  }

  public long getItemId(int position){
    return position;
  }

}
