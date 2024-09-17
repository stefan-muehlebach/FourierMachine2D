/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fourierAnim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Disc
        extends Group {

    private final static double F = 200.0;
    private final static Color BORDER_COLOR = Color.rgb(255, 255, 255, 0.5);
    private final static Color LINE_COLOR = Color.rgb(161, 121, 203, 0.5);
    private final static Color DISC_COLOR = Color.rgb(255, 255, 255, 0.1);
    private static final double BORDER_WIDTH = 2.0;
    private static final double LINE_WIDTH = 4.0;
    private static final Duration REVOLUTION_TIME = Duration.seconds(20.0);

    private double displ;
    Rotate rotComp, rotation;
    private Timeline anim;
    private Circle circle;
    private Line line;

    public Disc(FourierCoeff coeff, Disc parent) {
        super();
        Complex factor = coeff.getFactor();
        double radius = F * factor.abs();
        int freq = Math.abs(coeff.getFreq());
        int sign = Integer.signum(coeff.getFreq());
        double angle = Math.toDegrees(factor.arg());
        Duration duration = REVOLUTION_TIME.divide(freq);

        if (parent != null) {
            parent.getChildren().add(this);
            displ = parent.displ;
        }
        this.setDepthTest(DepthTest.ENABLE);
        rotComp = new Rotate(0.0, displ, 0.0);
        if (parent != null) {
            rotComp.angleProperty().bind(parent.rotation.angleProperty().multiply(-1.0));
        }
        rotation = new Rotate(angle, displ, 0.0);
        getTransforms().addAll(rotation, rotComp);

        circle = new Circle(displ, 0.0, radius);
        circle.setFill(DISC_COLOR);
        circle.setStroke(BORDER_COLOR);
        circle.setStrokeWidth(BORDER_WIDTH);
        getChildren().add(circle);

        line = new Line(displ, 0.0, displ + radius, 0.0);
        line.setStroke(LINE_COLOR);
        line.setStrokeWidth(LINE_WIDTH);
        getChildren().add(line);

        System.out.printf("duration: %f\n", duration.toSeconds());

        anim = new Timeline();
        anim.setCycleCount(Animation.INDEFINITE);
        anim.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), angle)),
                new KeyFrame(duration, new KeyValue(rotation.angleProperty(), angle + sign * 360.0))
        );
        displ += radius;
    }

    public Point2D getPoint() {
        return line.localToScene(line.getEndX(), line.getEndY());
    }

    public Timeline getAnim() {
        return anim;
    }
}
