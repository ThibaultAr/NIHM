float minX, maxX;
float minY, maxY;
int totalCount; // total number of places
int minPopulation, maxPopulation;
int minSurface, maxSurface;
int minAltitude, maxAltitude;
float minDensity, maxDensity;
int X = 1;
int Y = 2;
City cities[];

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
    if(float(columns[6]) != 0)
      cities[i-2].density = cities[i-2].population / float(columns[6]);
    else
      cities[i-2].density = cities[i-2].population;
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
}

void draw () {
  background(255);
  for (int i = 0; i < totalCount; i++)
    cities[i].drawCity();
}