/**
 *
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MultiTouch3D extends Application{
	BlobQueue bqueue;
	
	GraphicsContext gc;
	
	double sceneWidth = 800;
	double sceneHeight = 800;
	
	boolean fullscreen = false;
	boolean touchscreen = false;
	
	double lastMouseX;
	double lastMouseY;
	
	int nbCursorOnBox = 0;
	
	String debugInfo = "";

	public void start(Stage stage) {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            throw new RuntimeException("*** ERROR: SCENE3D is not supported on your machine !!");
        }
        
        if (fullscreen) {
	        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	        sceneWidth = primaryScreenBounds.getWidth();
	        sceneHeight = primaryScreenBounds.getHeight();
        }
        
		Canvas canvas = new Canvas(sceneWidth,sceneHeight);
		canvas.setMouseTransparent(true);
		Group rootCanvas = new Group(canvas);
		gc = canvas.getGraphicsContext2D();
        bqueue = new BlobQueue(gc);
		
		// Create a Box
        Box box = new Box(200, 200, 200);
        box.setTranslateX(sceneWidth/2);
        box.setTranslateY(sceneHeight/2);
        box.setTranslateZ(100);
        box.setRotationAxis(new Point3D(1,1,0));
        box.setRotate(45);
        box.setId("box");
        
        // Set the material for the box
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.rgb(191, 155, 58));
        material.setSpecularColor(Color.rgb(160, 142, 93));
        box.setMaterial(material);
        
        // Set up a light
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(400);
        pointLight.setTranslateY(400);
        pointLight.setTranslateZ(-300);

        AmbientLight ambientLight = new AmbientLight(Color.rgb(150, 150, 150));
        
        Group group3Dscene = new Group(box, ambientLight, pointLight);
		Group wholeGroup = new Group(group3Dscene, rootCanvas);
		
		Scene scene = new Scene(wholeGroup, sceneWidth, sceneHeight);
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFarClip(1000);
		scene.setCamera(camera);
            		
		scene.setOnMousePressed(e -> {
			if (!touchscreen) {
				Point2D p = new Point2D(e.getX(), e.getY());
				if (e.isSecondaryButtonDown()) 
					bqueue.remove(p);
				else 
		    		bqueue.add(p);
			}
    		
            PickResult res = e.getPickResult();		
            System.out.println(res);
            if (res.getIntersectedNode() != null && res.getIntersectedNode().getId() != null) {
            	System.out.println("Le pointeur est sur le cube");
            	nbCursorOnBox++;
            }
            
            if(res.getIntersectedNode() != null && e.isSecondaryButtonDown()) {
            	nbCursorOnBox--;
            }
            
            System.out.println(nbCursorOnBox);
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
            
            e.consume(); 
            redrawMyCanvas();
        });
		
		scene.setOnMouseDragged(e -> {
			if (!touchscreen) {
	    		Point2D p = new Point2D(e.getX(), e.getY());
	    		bqueue.update(p);
			}

			PickResult res = e.getPickResult();
			Point3D p = box.localToParent(res.getIntersectedPoint());
			
			if(res.getIntersectedNode() != null && res.getIntersectedNode().getId() != null) {
				double x = p.getX() - lastMouseX + box.getTranslateX();
				double y = p.getY() - lastMouseY + box.getTranslateY();
				box.setTranslateX(x);
				box.setTranslateY(y);

				lastMouseX = p.getX();
				lastMouseY = p.getY();
			}else{
				if(nbCursorOnBox > 0) {
					System.out.println("boxSelected");
					double z = e.getY() - lastMouseY + box.getTranslateZ();
					box.setTranslateZ(z);
					
					lastMouseY = e.getY();
				}
			}
        	
			redrawMyCanvas();
        });
        
		scene.setOnMouseReleased(e -> {
    		
        });
		
		scene.setOnTouchPressed(e -> {
			System.out.println("Touch pressed " + e);
		});
		
		scene.setOnTouchMoved(e -> {
			System.out.println("Touch moved " + e);
		});
		
		scene.setOnTouchReleased(e -> {
			System.out.println("Touch released " + e);
		});
        		
		stage.setScene(scene);
		stage.setTitle("Université Lille 1 - M2 IVI - NIHM - 3D MultiTouch interaction - G. Casiez");
		stage.show();
	}
	
	void redrawMyCanvas() {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.fillText(debugInfo, 10, 20);
		
		if (!touchscreen) bqueue.draw();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
