import java.util.Vector;

import javafx.geometry.Point2D;

public class DTW {
	
	private Vector<Template> templates;
	
	DTW() {
		TemplateManager tm = new TemplateManager();
		tm.loadFile("gestures.xml");
		this.templates = tm.getTemplates();
	}
	
	public Matrix dtw(Vector<Point2D> gesture, Vector<Point2D> template) {
		int n = gesture.size();
		int m = template.size();
		
		Matrix dtw = new Matrix(n, m);
		
		dtw.items[0][0] = 0;
		dtw.couple[0][0] = new Couple(0, 0);
		
		for(int i = 1; i < n; i++) {			
			dtw.items[i][0] = dtw.items[i-1][0] + dist(gesture.get(i), template.get(0));
			dtw.couple[i][0] = new Couple(i-1, 0);
		}
		
		for (int i = 1; i < m; i++) {			
			dtw.items[0][i] = dtw.items[0][i-1] + dist(gesture.get(0), template.get(i));
			dtw.couple[0][i] = new Couple(0, i-1);
		}
		
		for(int i = 1; i < n; i++) {
			for(int j = 1; j < m; j++) {
				double min = Math.min(Math.min(dtw.items[i-1][j], dtw.items[i][j-1]), dtw.items[i-1][j-1]);
				Couple pred = new Couple();
				
				if(min == dtw.items[i-1][j])
					pred = new Couple(i-1, j);
				if(min == dtw.items[i][j-1])
					pred = new Couple(i, j-1);
				if(min == dtw.items[i-1][j-1])
					pred = new Couple(i-1, j-1);
				
				dtw.items[i][j] = dist(gesture.get(i), template.get(j)) + min;
				dtw.couple[i][j] = pred;
			}
		}
		
		return dtw;
	}
	
	public Template findTemplate(Vector<Point2D> gesture) {		
		gesture = boundingBox(gesture);
		gesture = centroid(gesture);
		
		Template mint = new Template("NoTemplate", new Vector<Point2D>());
		double min = Double.MAX_VALUE;
		
		for(Template temp : templates) {
			Template t = new Template("", temp.getPoints());
			t.setPoints(boundingBox(t.getPoints()));
			t.setPoints(centroid(t.getPoints()));
			
			Matrix d = dtw(gesture, t.getPoints());
			double mind = d.items[gesture.size()-1][t.getPoints().size()-1];
			if(mind < min) {
				min = mind;
				mint = temp;
			}
		}
		
		return mint;
	}
	
	private double dist(Point2D p1, Point2D p2) {
		return p1.distance(p2);
	}
	
	private Vector<Point2D> centroid(Vector<Point2D> points) {
		Point2D centroid = new Point2D(0,0);
		Vector<Point2D> nps = new Vector<Point2D>();
		for(Point2D p : points)
			centroid = centroid.add(p);
		centroid = centroid.multiply(1.0/points.size());
		
		for(Point2D p : points)
			nps.add(p.subtract(centroid));
		
		return nps;
	}
	
	private Vector<Point2D> boundingBox(Vector<Point2D> points) {
		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
		Vector<Point2D> newPoints = new Vector<Point2D>();
		
		for(Point2D p : points) {
			minX = Math.min(minX, p.getX());
			minY = Math.min(minY, p.getY());
			maxX = Math.max(maxX, p.getX());
			maxY = Math.max(maxY, p.getY());
		}
		
		for(Point2D p : points) {
			newPoints.add(new Point2D((p.getX() - minX) / (maxX - minX), (p.getY() - minY) / (maxY - minY)));
		}
		
		return newPoints;
	}
}
