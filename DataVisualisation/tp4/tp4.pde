float minX, maxX;
float minY, maxY;
int totalCount; // total number of places
int minPopulation, maxPopulation;
int minSurface, maxSurface;
int minAltitude, maxAltitude;
float minDensity, maxDensity;
int minPopulationToDisplay = 2048;
int X = 1;
int Y = 2;
City cities[];
Slider slider;
float seuilSlide = 1;

City lastPointedCity;

void readData() {
  String[] lines = loadStrings("../villes.tsv");
  parseInfo(lines[0]);
  
  cities = new City[totalCount];
  String[] columns = split(lines[2], TAB);
  minDensity = maxDensity = float(columns[5]) / float(columns[6]);
  for (int i = 2; i < totalCount + 2; ++i) {
    columns = split(lines[i], TAB);
    cities[i-2] = new City();
    cities[i-2].postalcode = int(columns[0]);
    cities[i-2].x = mapX(float (columns[1]));
    cities[i-2].y = mapY(float (columns[2]));
    cities[i-2].name = columns[4];
    cities[i-2].population = float(columns[5]);
    cities[i-2].density =  float(columns[6]) != 0 ? cities[i-2].population / float(columns[6]) : cities[i-2].population;
    if(cities[i-2].density < minDensity)
      minDensity = cities[i-2].density;
    if(cities[i-2].density > maxDensity)
      maxDensity = cities[i-2].density;
  }
}

void parseInfo(String line) {
  String infoString = line.substring(2); // remove the #
  String[] infoPieces = split(infoString, ',');
  totalCount = int(infoPieces[0]);
  minX = float(infoPieces[1]);
  maxX = float(infoPieces[2]);
  minY = float(infoPieces[3]);
  maxY = float(infoPieces[4]);
  minPopulation = int(infoPieces[5]);
  maxPopulation = int(infoPieces[6]);
  minSurface = int(infoPieces[7]);
  maxSurface = int(infoPieces[8]);
  minAltitude = int(infoPieces[9]);
  maxAltitude = int(infoPieces[10]);
}

float mapX(float x) {
  return map(x, minX, maxX, 0, 800);
}

float mapY(float y) {
  return map(y, minY, maxY, 800, 0);
}

void setup () {
  size(800, 800);
  readData();
  slider = new Slider();
  slider.x = 750;
}

void draw () {
  background(255);
  fill(0);
  text(minPopulationToDisplay, 10, 10);
  for (int i = 0; i < totalCount; i++) {
    if(cities[i].population > minPopulationToDisplay)
      cities[i].drawCity();
  }
  
  fill(255);
  stroke(0);
  rect(50, 725, 700, 50);
  
  slider.setY(750);
  
  minPopulationToDisplay = (int) exp(((slider.x - 50) / 700) * (float) log(2500000));
  
  fill(0);
  if(minPopulationToDisplay <= 0) minPopulationToDisplay = 1;
  if(minPopulationToDisplay > 2500000) minPopulationToDisplay = 2500000;
  float widthRect = slider.x - 50;
  rect(50, 725, widthRect, 50);
  slider.drawSlider();
  redraw();
}

void mouseMoved(){
  City city = pick(mouseX, mouseY);
  if(city != null && city.population > minPopulationToDisplay) {
    if(city != lastPointedCity)
      println(city.name); 
    if(lastPointedCity != null)
      lastPointedCity.isSelected = false;
    city.isSelected = true;
    lastPointedCity = city;
  }
  if(city==null && lastPointedCity != null) {
    lastPointedCity.isSelected = false;
    lastPointedCity.isClicked = false;
  }
  redraw();
}


void mouseDragged() {
  if(slider.isClicked && abs(slider.xDisplacement(mouseX)) > 0.5){
    slider.setX(slider.x + slider.xDisplacement(mouseX));
    if(slider.x > 750)
      slider.x = 750;
    if(slider.x < 50)
      slider.x = 50;
  }
}
void mousePressed() {
  if(lastPointedCity != null)
    lastPointedCity.isClicked = true;
  if(slider.contains(mouseX, mouseY)) {
    slider.setColor(color(255,0,0));
    slider.isClicked = true;
  }
}

void mouseReleased() {
  if(lastPointedCity != null)
    lastPointedCity.isClicked = false;  
  slider.isClicked = false;
}

City pick(int px, int py) {
  for(int i = cities.length - 1; i>=0; i--) {
    if(cities[i].contains(px, py))
      return cities[i];
  }
  return null;
}

float mapMinPop(float val) {
  return map(val, 1, 2500000, 0, 650); 
}