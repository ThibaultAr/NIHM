class City { 
  int postalcode; 
  String name; 
  float x; 
  float y; 
  float population; 
  float density; 


  float mapPop() {
   return map(population, minPopulation, maxPopulation, 1, 100); 
  }
  
  float mapDensity(){
   return map(density, minDensity, maxDensity, 50, 200); 
  }

  void drawCity() {
    noStroke();
    color black = color(0, mapDensity());
    fill(black);
    ellipse(x, y, mapPop(), mapPop());
    //set((int) x, (int) y, black);
  }
  // put a drawing function in here and call from main drawing loop }
}