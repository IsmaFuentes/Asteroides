package com.example.asteroides.models;

import com.example.asteroides.interfaces.ScoreStoreInterface;

import java.util.ArrayList;
import java.util.List;

public class ScoreStore implements ScoreStoreInterface {

  private List<String> scores;

  public ScoreStore(){
    scores = new ArrayList<String>();
    scores.add("123000 Pepe Domínguez");
    scores.add("111000 Pedro Martínez");
    scores.add("011000 Paco Pérez");
  }

  @Override
  public void SaveScore(int points, String name, long data) {
    scores.add(0, String.format("%s %s", points, name));
  }

  @Override
  public List<String> GetScore(int quantity) {
    return scores;
  }
}
