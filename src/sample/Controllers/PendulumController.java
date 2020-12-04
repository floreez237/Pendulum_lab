package sample.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class PendulumController {

    private static final double PIXELS_TO_METRES = (1.5 / 430.0);

    private static final double PROTRACTOR_RADIUS = 120;
    private static final double MAJOR_DIVISION_LENGTH = 20;
    private static final double MINOR_DIVISION_LENGTH = 10;
    private static final double GRAVITY = 9.8;
    private static final double RECTANGLE_HEIGHT = 250;
    private static final double MIN_RECTANGLE_Y = 395;


    private double lengthOfRope = 1.0;
    private double lengthOfRopeInPixels = lengthOfRope / PIXELS_TO_METRES;
    private double mass = .1;


    private double thetaMax = 0.0;

    private double bobTime = 0.0;

    @FXML
    private JFXButton btnStartStop;
    @FXML
    private Slider ropeLengthSlider;

    @FXML
    private Label lblRopeLength;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox<String> cmbGraph;


    @FXML
    private Line horizontalAxisLine;

    @FXML
    private Line verticalAxisLine;

    @FXML
    private AnchorPane optionsAnchorPane;

    @FXML
    private Label lblMass;

    @FXML
    private Label lblAngleMax;

    @FXML
    private Slider massSlider;

    @FXML
    private Rectangle recKE;

    @FXML
    private Rectangle recPE;

    @FXML
    private Rectangle recTE1;

    @FXML
    private Rectangle recTE2;

    @FXML
    private Line ropeLine;

    @FXML
    private Circle axleCircle;

    @FXML
    private Circle bobCircle;

    @FXML
    private TextField tfTimer;

    private final Polyline keCurve = new Polyline();

    private final Polyline peCurve = new Polyline();

    private final Line totalEnergyLine = new Line();

    private Line leftMarker = new Line();

    private Line rightMarker = new Line();

    private double stopWatchTime = 0.0;

    private final Timeline stopwatchAnimation = new Timeline(new KeyFrame(Duration.millis(1), e -> {
        stopWatchTime += 0.001;
        tfTimer.setText(String.format("%.3f", stopWatchTime));
    }));

    private final Timeline bobAnimation = new Timeline(new KeyFrame(Duration.millis(1.0), e -> {
        bobTime += 0.001;
        double theta = newTheta();
        double deltaX = lengthOfRopeInPixels * Math.sin(theta);
        double deltaY = lengthOfRopeInPixels * Math.cos(theta);
        bobCircle.setCenterX(axleCircle.getCenterX() + deltaX);
        bobCircle.setCenterY(axleCircle.getCenterY() + deltaY);
//        barEnergyGraph();
        energyCurve();
    }));



    public PendulumController() {
        stopwatchAnimation.setCycleCount(Timeline.INDEFINITE);
        bobAnimation.setCycleCount(Timeline.INDEFINITE);


    }

    //called each time the position of the ball is changed
    void barEnergyGraph() {
        double maxEnergy = totalEnergy();
        double heightKE = (newKineticEnergy() / maxEnergy) * RECTANGLE_HEIGHT;
        double heightPE = RECTANGLE_HEIGHT - heightKE;
        double newYKE = MIN_RECTANGLE_Y + (RECTANGLE_HEIGHT - heightKE);
        double newYPE = MIN_RECTANGLE_Y + (RECTANGLE_HEIGHT - heightPE);
        recKE.setY(newYKE);
        recPE.setY(newYPE);
        recKE.setHeight(heightKE);
        recPE.setHeight(heightPE);
        recTE1.setY(newYKE);
        recTE1.setHeight(heightKE);
        recTE2.setY(newYKE - heightPE);
        recTE2.setHeight(heightPE);
    }

    void energyCurve() {
        final int MAX_HEIGHT = 200;
        final int MAX_WIDTH = 190;

        if (!optionsAnchorPane.getChildren().contains(totalEnergyLine)) {
            Point2D startTotalEnergy = new Point2D(verticalAxisLine.getStartX(), horizontalAxisLine.getStartY() - MAX_HEIGHT);
            Point2D endTotalEnergy = new Point2D(startTotalEnergy.getX() + MAX_WIDTH, startTotalEnergy.getY());
            totalEnergyLine.setStartX(startTotalEnergy.getX());
            totalEnergyLine.setStartY(startTotalEnergy.getY());
            totalEnergyLine.setEndX(endTotalEnergy.getX());
            totalEnergyLine.setEndY(endTotalEnergy.getY());
//            totalEnergyLine.setFill(Color.RED);
            totalEnergyLine.setStroke(Color.RED);
            optionsAnchorPane.getChildren().add(totalEnergyLine);
        }

        double pe = newPotentialEnergy();

        double ke = newKineticEnergy();
        if (Math.abs(ke) < 1e-14) {
            keCurve.getPoints().clear();
        } else {

        }

    }

    @FXML
    void changeBallPosition(MouseEvent event) {
        bobAnimation.stop();
        ropeLengthSlider.setDisable(true);
        massSlider.setDisable(true);
        bobTime = 0.0;

        double mouseX = event.getSceneX();
        double mouseY = event.getSceneY();
        double base = Math.abs(mouseX - axleCircle.getCenterX());
        double height = Math.abs(mouseY - axleCircle.getCenterY());
        thetaMax = mouseX < axleCircle.getCenterX() ? -Math.atan(base / height) : Math.atan(base / height);
        double bobDeltaY = lengthOfRopeInPixels * Math.cos(thetaMax);
        double bobDeltaX = lengthOfRopeInPixels * Math.sin(thetaMax);
        bobCircle.setCenterX(bobDeltaX + axleCircle.getCenterX());
        bobCircle.setCenterY(bobDeltaY + axleCircle.getCenterY());


        lblAngleMax.setText(String.format("%.1f°", Math.abs(Math.toDegrees(thetaMax))));

        placeMarkers();

    }

    private void placeMarkers() {
        double absoluteThetaMax = Math.abs(thetaMax);
        double deltaX = PROTRACTOR_RADIUS * Math.sin(absoluteThetaMax);
        double deltaY = PROTRACTOR_RADIUS * Math.cos(absoluteThetaMax);
        leftMarker.setStartX(axleCircle.getCenterX() - deltaX);
        rightMarker.setStartX(axleCircle.getCenterX() + deltaX);
        leftMarker.setStartY(axleCircle.getCenterY() + deltaY);
        rightMarker.setStartY(axleCircle.getCenterY() + deltaY);

        deltaX = (PROTRACTOR_RADIUS + MAJOR_DIVISION_LENGTH + 10.0) * Math.sin(absoluteThetaMax);
        deltaY = (PROTRACTOR_RADIUS + MAJOR_DIVISION_LENGTH + 10.0) * Math.cos(absoluteThetaMax);
        leftMarker.setEndX(axleCircle.getCenterX() - deltaX);
        rightMarker.setEndX(axleCircle.getCenterX() + deltaX);
        leftMarker.setEndY(axleCircle.getCenterY() + deltaY);
        rightMarker.setEndY(axleCircle.getCenterY() + deltaY);
    }

    @FXML
    void handleBallRelease() {
        /*double mouseX = event.getSceneX();
        double base = Math.abs(mouseX - axleCircle.getCenterX());*/
        bobAnimation.play();
    }

    @FXML
    void handleStartStopStopWatch() {
        if (btnStartStop.getText().equals("Pause")) {
            btnStartStop.setText("Play");
            stopwatchAnimation.pause();
        } else {
            stopwatchAnimation.play();
            btnStartStop.setText("Pause");
        }
    }

    @FXML
    public void reInitialize() {
        bobTime = 0.0;
        stopWatchTime = 0.0;
        stopwatchAnimation.stop();
        bobAnimation.stop();
        handleRestartStopWatch();
        lblAngleMax.setText("0.0°");

        bobCircle.setCenterX(axleCircle.getCenterX());
        bobCircle.setCenterY(axleCircle.getCenterY() + lengthOfRopeInPixels);
        removeMarkers();
        ropeLengthSlider.setDisable(false);
        massSlider.setDisable(false);

        initializeEnergyGraph();
    }

    @FXML
    void handleRestartStopWatch() {
        stopwatchAnimation.stop();
        tfTimer.setText("0.000");
        stopWatchTime = 0.0;
        btnStartStop.setText("Start");
    }

    private void removeMarkers() {
        anchorPane.getChildren().removeAll(leftMarker, rightMarker);
        rightMarker = new Line();
        leftMarker = new Line();
        leftMarker.setFill(Color.BLUE);
        rightMarker.setFill(Color.BLUE);
        leftMarker.setStrokeWidth(3.0);
        rightMarker.setStrokeWidth(3.0);
        anchorPane.getChildren().addAll(leftMarker, rightMarker);
    }

    @FXML
    public void initialize() {
        bobTime = 0.0;
        stopWatchTime = 0.0;
        lblAngleMax.setText(String.format("%.1f°", Math.abs(thetaMax)));
        lblRopeLength.setText(String.format("%.2f m", lengthOfRope));

        bobCircle.setCenterX(axleCircle.getCenterX());
        bobCircle.setCenterY(axleCircle.getCenterY() + lengthOfRopeInPixels);

        ropeLine.endXProperty().bind(bobCircle.centerXProperty());
        ropeLine.endYProperty().bind(bobCircle.centerYProperty());
        ropeLine.setStartX(axleCircle.getCenterX());
        ropeLine.setStartY(axleCircle.getCenterY());

        drawProtractor();
        initializeEnergyGraph();
        leftMarker.setFill(Color.BLUE);
        rightMarker.setFill(Color.BLUE);
        leftMarker.setStrokeWidth(3.0);
        rightMarker.setStrokeWidth(3.0);
        anchorPane.getChildren().addAll(leftMarker, rightMarker);

        massSlider.valueProperty().addListener(o1 -> {
            mass = massSlider.getValue();
            lblMass.setText((int) mass + "g");
            bobCircle.setRadius((massSlider.getValue() * 17) / 100);
        });

        ropeLengthSlider.valueProperty().addListener(o -> {
            lengthOfRope = ropeLengthSlider.getValue();
            lblRopeLength.setText(String.format("%.2f m", lengthOfRope));
            lengthOfRopeInPixels = lengthOfRope / PIXELS_TO_METRES;
            bobCircle.setCenterY(axleCircle.getCenterY() + lengthOfRopeInPixels);
        });

        cmbGraph.getItems().addAll("Energy Bar", "Energy Curve");

    }


    @FXML
    void changeGraph(ActionEvent event) {

    }

    private void initializeEnergyGraph() {
        Rectangle[] rectangles = {recTE1, recTE2, recKE, recPE};
        for (Rectangle rectangle : rectangles) {
            rectangle.setHeight(0);
        }
    }

    private double newTheta() {
        double omega = Math.sqrt(GRAVITY / lengthOfRope);
        return thetaMax * Math.cos(omega * bobTime);
    }

    private double newKineticEnergy() {

        return totalEnergy() - newPotentialEnergy();
    }

    private double newPotentialEnergy() {
        return mass * GRAVITY * lengthOfRope * (1 - Math.cos(newTheta()));
    }

    private double totalEnergy() {
        return mass * GRAVITY * lengthOfRope * (1 - Math.cos(thetaMax));
    }

    private void drawProtractor() {
        final double axleCircleCenterX = axleCircle.getCenterX();
        final double axleCircleCenterY = axleCircle.getCenterY();
        for (int i = -90; i <= 90; i++) {
            double angleInRadians = Math.toRadians(i);
            double startX = PROTRACTOR_RADIUS * Math.sin(angleInRadians) + axleCircleCenterX;
            double startY = PROTRACTOR_RADIUS * Math.cos(angleInRadians) + axleCircleCenterY;
            double endX;
            double endY;
            if (i % 5 == 0) {
                endX = startX + MAJOR_DIVISION_LENGTH * Math.sin(angleInRadians);
                endY = startY + MAJOR_DIVISION_LENGTH * Math.cos(angleInRadians);
            } else {
                endX = startX + MINOR_DIVISION_LENGTH * Math.sin(angleInRadians);
                endY = startY + MINOR_DIVISION_LENGTH * Math.cos(angleInRadians);
            }
            anchorPane.getChildren().add(new Line(startX, startY, endX, endY));

        }
    }


}
