class City { 
  int postalcode; 
  String name; 
  float x; 
  float y; 
  float population; 
  float density; 
  boolean isSelected = false;
  boolean isClicked = false;


  float mapPop() {
   return map(population, minPopulation, maxPopulation, 1, 200); 
  }
  
  float mapDensity(){
   return map(density, minDensity, maxDensity, 50, 200); 
  }

  void drawCity() {
    noStroke();
    color black = color(0, mapDensity());
    color red = color(255, 0, 0, mapDensity());
    color redTrans = color(255,0,0,50);
    color redTxt = color(255, 0, 0);
    color blackTxt = color(0);
    fill(black);
    if(isSelected) {
      fill(redTxt);
      if(isClicked)
        fill(blackTxt);
      textSize(14);
      textAlign(LEFT, CENTER);
      text(name, x + mapPop()/4, y);
      fill(redTrans);
      rect(x+mapPop()/4, y-5, textWidth(name), 14);
      fill(red);
    }
    ellipse(x, y, mapPop()/2, mapPop()/2);
    //set((int) x, (int) y, black);
  }
  
  boolean contains(int px, int py) {
     return dist(x, y, px, py) <= mapPop()/4; 
  }
}