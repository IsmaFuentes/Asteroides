package com.example.asteroides.interfaces;

import java.util.List;

public interface ScoreStoreInterface {
  public void SaveScore(int points, String name, long data);
  public List<String> GetScore(int quantity);
}
