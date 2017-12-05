/**
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 */

import java.awt.Label;
import java.util.Vector;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DTWGUI extends Application{
	GraphicsContext gc;
	Canvas canvas;
	Vector<Point2D> userGesture = new Vector<Point2D>();
	Template detectedTemplate;
	DTW dtw = new DTW();
	
	public void start(Stage stage) {
		VBox root = new VBox();
		canvas = new Canvas (600, 700);
		gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		canvas.setOnMousePressed(e -> {
			userGesture.clear();
			redrawMyCanvas();			
		});
		
		canvas.setOnMouseDragged(e -> {
			userGesture.add(new Point2D(e.getX(), e.getY()));
			redrawMyCanvas();
		});
		
		canvas.setOnMouseReleased(e -> {
			detectedTemplate = dtw.findTemplate(userGesture);
			System.out.println(detectedTemplate.getName());
			redrawMyCanvas();
			drawMyTemplate();
			
		});

		Scene scene = new Scene(root);
		stage.setTitle("Université Lille 1 - M2 IVI - NIHM - Dynamic Time Warping - G. Casiez");
		stage.setScene(scene);
		stage.show();
	}
	
	public void redrawMyCanvas() {
		double r = 5.0;
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for (int i=1; i<userGesture.size(); i++) {
			gc.setStroke(Color.BLACK);
			gc.strokeLine(userGesture.elementAt(i-1).getX(), userGesture.elementAt(i-1).getY(),
					userGesture.elementAt(i).getX(), userGesture.elementAt(i).getY());
			gc.strokeOval(userGesture.elementAt(i-1).getX() - r, userGesture.elementAt(i-1).getY() - r, 2*r, 2*r);
		}		
	}
	
	public void drawMyTemplate() {
		double r = 5.0;

		Vector<Point2D> tPoints = detectedTemplate.getPoints(); 
		for (int i=1; i<tPoints.size(); i++) {
			gc.setStroke(Color.ORANGE);
			gc.strokeLine(tPoints.elementAt(i-1).getX(), tPoints.elementAt(i-1).getY(),
					tPoints.elementAt(i).getX(), tPoints.elementAt(i).getY());
			gc.strokeOval(tPoints.elementAt(i-1).getX() - r, tPoints.elementAt(i-1).getY() - r, 2*r, 2*r);
		}		
	}
	
	
	

	

	public static void main(String[] args) {
		Application.launch(args);
	}
}
