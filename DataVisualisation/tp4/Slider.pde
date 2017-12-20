class Slider {
  float x, y;
  color col;
  boolean isClicked = false;
  
  boolean contains(int px, int py) {
     return dist(x, y, px, py) <= 25; 
  }
  
  void drawSlider() {
    fill(126);
    if(isClicked)
      fill(col);
    ellipse(x, y, 50, 50);
    redraw();
  }
  
  void setX(float v) {
    x = v; 
  }
  
  void setY(float v) {
    y = v; 
  }
  
  void setColor(color c) {
    col = c; 
  }
  
  float xDisplacement(float xDisp) {
    return xDisp - x;
  }
}