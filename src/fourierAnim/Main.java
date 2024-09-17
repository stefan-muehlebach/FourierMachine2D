/*
 * Main-Attraktor (interaktiv, animiert)
 *
 *    Implementiert ein Applet, das den Verlauf dreier Punkte auf einem
 *    Main-Attraktor darstellt. Die Darstellung kann mit der Maus im
 *    3D-Raum gedreht werden.
 */
package fourierAnim;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.animation.AnimationTimer;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Scale;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;
import static javafx.scene.input.KeyCode.C;
import javafx.scene.layout.VBox;

public class Main
        extends Application {

    /*
     * Konstanten
     */
    static final int FRAME_WIDTH  = 2000;
    static final int FRAME_HEIGHT = 1200;
    static final long DRAW_TASK_PERIOD = 100_000_000;
    
    static final double AXIS_LENGTH = 1000.0;
    static final double AXIS_WIDTH = 2.0;
    static final Color X_AXIS_COLOR = Color.rgb(255, 0, 0, 0.5);
    static final Color Y_AXIS_COLOR = Color.rgb(0, 255, 0, 0.5);

    static final Color CURVE_COLOR = Color.rgb(255, 255, 255, 0.5);
    static final double CURVE_WIDTH = 4.0;
    
    /* -----------------------------------------------------------------------
    *
    * Ab hier geht es ums Eingemachte.
     */
//    Pane root;
    Group root;
    Group world;
    Group discGroup;
    Group axisGroup;
    Group drawGroup;
    Scale camZoom;

    Stage dialogStage;

    final Polyline curve = new Polyline();

    FourierCoeffList coeffList;
    int maxFrequency;
    int visibleDiscs;

    boolean isRunning = false;
    boolean isDrawing = false;

    ArrayList<Disc> discList = new ArrayList<>();

    double mousePosX;
    double mousePosY;
    double zoomFactor = 1.0;

    // This is the task, which gets called every so ms and continues
    // the path, drawn by the last wheel.
    //
    AnimationTimer drawTask = new AnimationTimer() {
        private long ts = 0;
        private Point2D p;

        @Override
        public void handle(long now) {
            if (now - ts < DRAW_TASK_PERIOD) {
                return;
            }
            ts = now;
            p = drawGroup.sceneToLocal(discList.get(visibleDiscs - 1).getPoint());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    curve.getPoints().addAll(new Double[]{p.getX(), p.getY()});
                }
            });
        }
    };

    void startDrawing() {
        drawTask.start();
    }

    void stopDrawing() {
        drawTask.stop();
    }

    void startAnimation() {
        for (Disc disc : discList) {
            disc.getAnim().play();
        }
    }

    void stopAnimation() {
        for (Disc disc : discList) {
            disc.getAnim().pause();
        }
    }

    void resetAnimation() {
        for (Disc disc : discList) {
            disc.getAnim().jumpTo("start");
        }
    }

    void initCoeffList() {
        try {
            coeffList = new FourierCoeffList("./cat.csv");
        } catch (Exception e) {
            System.err.println(e);
        };
        maxFrequency = coeffList.getMaxFreq();
        visibleDiscs = (2 * maxFrequency) + 1;
    }

    private void buildSceneGraph(Group root) {
        world = new Group();
        root.getChildren().add(world);
        discGroup = new Group();
        world.getChildren().add(discGroup);
        drawGroup = new Group();
        world.getChildren().add(drawGroup);
        curve.setStroke(CURVE_COLOR);
        curve.setStrokeWidth(CURVE_WIDTH);
        drawGroup.getChildren().add(curve);
    }

    private void buildCamera(Group root) {
        root.setTranslateX(FRAME_WIDTH / 2.0);
        root.setTranslateY(FRAME_HEIGHT / 2.0);
        camZoom = new Scale(0.75, -0.75, 0.0, 0.0);
        root.getTransforms().add(camZoom);
    }

    private void buildAxes(Group root) {
        axisGroup = new Group();

        Line xAxis = new Line(-AXIS_LENGTH, 0.0, AXIS_LENGTH, 0.0);
        xAxis.setStroke(X_AXIS_COLOR);
        xAxis.setStrokeWidth(AXIS_WIDTH);
        Path xArrow = new Path(new MoveTo(AXIS_LENGTH - 15.0, 5.0), new LineTo(AXIS_LENGTH, 0.0), new LineTo(AXIS_LENGTH - 15.0, -5.0));
        xArrow.setStroke(X_AXIS_COLOR);
        xArrow.setStrokeWidth(0.0);
        xArrow.setFill(X_AXIS_COLOR);

        Line yAxis = new Line(0.0, -AXIS_LENGTH, 0.0, AXIS_LENGTH);
        yAxis.setStroke(Y_AXIS_COLOR);
        yAxis.setStrokeWidth(AXIS_WIDTH);
        Path yArrow = new Path(new MoveTo(-5.0, AXIS_LENGTH - 15.0), new LineTo(0.0, AXIS_LENGTH), new LineTo(5.0, AXIS_LENGTH - 15.0));
        yArrow.setStroke(Y_AXIS_COLOR);
        yArrow.setStrokeWidth(0.0);
        yArrow.setFill(Y_AXIS_COLOR);

        axisGroup.getChildren().addAll(xAxis, xArrow, yAxis, yArrow);
        root.getChildren().add(axisGroup);
    }

    private void buildDiscs(Group root) {
        Disc disc = null;
        Iterator<FourierCoeff> iter;
        iter = coeffList.freqIterator();
        while (iter.hasNext()) {
            FourierCoeff coeff = iter.next();
            disc = new Disc(coeff, disc);
            System.out.printf("freq: %d\n", coeff.getFreq());
            System.out.printf("angle: %f\n", disc.rotation.getAngle());
            discList.add(disc);
        }
        root.getChildren().add(discList.get(0));
    }

    private void buildDialog() throws IOException {
        dialogStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dialog.fxml"));
        VBox root = (VBox) fxmlLoader.load();
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.setTitle("Settings");
    }

    private void handleMouse(Scene scene) {
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                mousePosX = ev.getX();
                mousePosY = ev.getY();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                double mouseDeltaX = (ev.getX() - mousePosX);
                double mouseDeltaY = (ev.getY() - mousePosY);
                root.setTranslateX(root.getTranslateX() + mouseDeltaX);
                root.setTranslateY(root.getTranslateY() + mouseDeltaY);
                mousePosX += mouseDeltaX;
                mousePosY += mouseDeltaY;
            }
        });

        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent ev) {
                zoomFactor /= Math.pow(1.2, ev.getDeltaY() / 40);
                camZoom.setX(1.0 / zoomFactor);
                camZoom.setY(-1.0 / zoomFactor);
            }
        });
    }

    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ev) {
                switch (ev.getCode()) {
                    
                    // Toggle the visibility of the axis.
                    case A:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                       
                    // Toggle the visibility of the discs.
                    case C:
                        discGroup.setVisible(!discGroup.isVisible());
                        break;
                        
                    // Toggle the animation
                    case ENTER:
                        if (isRunning) {
                            stopDrawing();
                            stopAnimation();
                        } else {
                            startAnimation();
                            if (isDrawing) {
                                startDrawing();
                            }
                        }
                        isRunning = !isRunning;
                        break;
                        
                    // Toggle the drawing of the curve
                    case SPACE:
                        if (!isRunning) {
                            break;
                        }
                        if (isDrawing) {
                            stopDrawing();
                        } else {
                            startDrawing();
                        }
                        isDrawing = !isDrawing;
                        break;
                        
                    // Shows more or less discs, i.e. frequencies
                    case UP:
                        if (visibleDiscs < discList.size()) {
                            discList.get(visibleDiscs).setVisible(true);
                            visibleDiscs++;
                            curve.getPoints().clear();
                        }
                        break;
                    case DOWN:
                        if (visibleDiscs > 0) {
                            visibleDiscs--;
                            discList.get(visibleDiscs).setVisible(false);
                            curve.getPoints().clear();
                        }
                        break;
                        
                    // Stops the animation and resets everything
                    case R:
                        if (isRunning) {
                            stopDrawing();
                            stopAnimation();
                            isRunning = false;
                            isDrawing = false;
                        }
                        resetAnimation();
                        drawGroup.getChildren().clear();
                        break;
                        
                    // Show the settings dialog
                    case D:
                        if (dialogStage.isShowing()) {
                            dialogStage.hide();
                        } else {
                            dialogStage.show();
                        }
                        break;

                    // Quit the application
                    case Q:
                    case ESCAPE:
                        stopDrawing();
                        Platform.exit();
                        break;
                        
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initCoeffList();

        root = new Group();

        buildSceneGraph(root);
        buildCamera(world);
        buildAxes(world);
        buildDiscs(discGroup);
        buildDialog();

        Scene scene = new Scene(root, FRAME_WIDTH, FRAME_HEIGHT, true,
                SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        handleMouse(scene);
        handleKeyboard(scene);

        primaryStage.setTitle("Fun with Fourier");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Use the following keys:");
        System.out.println("-----------------------");
        System.out.println(" [Enter]: toggles the animation");
        System.out.println(" [Space]: toggles the bubble system");
        System.out.println(" a      : turns the axis on/off");
        System.out.println(" r      : stops the animation and resets all values");
        System.out.println(" q      : quit the application");
        System.out.println(" d      : show settings dialog");
        System.out.println(" [Up]   : add two discs (one frequency");
        System.out.println(" [Down] : remove two discs (one frequency");
        System.out.println(" (currently " + coeffList.getMaxFreq() + " frequencies defined)");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
