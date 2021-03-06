package com.company;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class Transformations extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
        Box box = prepareBox();

        SmartGroup group = new SmartGroup();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1500);

        primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            initMouseControl(group, scene);
            });

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case U:
                    group.translateXProperty().set(group.getTranslateX() + 10);
                    break;
                case I:
                    group.translateXProperty().set(group.getTranslateX() - 10);
                    break;
                case P:
                    group.translateYProperty().set(group.getTranslateY() + 10);
                    break;
                case O:
                    group.translateYProperty().set(group.getTranslateY() - 10);
                    break;
                case K:
                    group.translateZProperty().set(group.getTranslateZ() + 100);
                    break;
                case L:
                    group.translateZProperty().set(group.getTranslateZ() - 100);
                    break;
                case V:
                    group.rotateProperty().set(group.getRotate() + 10);
                    break;
                case Q:
                    group.rotateByX(10);
                    break;
                case E:
                    group.rotateByX(-10);
                    break;
                case A:
                    group.rotateByY(10);
                    break;
                case D:
                    group.rotateByY(-10);
                    break;
                case Z:
                    group.rotateByZ(10);
                    break;
                case C:
                    group.rotateByZ(-10);
                    break;
            }
        });

        primaryStage.setTitle("3D Transformations");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Box prepareBox() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.DARKBLUE);
        material.setSpecularColor(Color.RED);
        Box box = new Box(100, 20, 50);
        box.setMaterial(material);
        return box;
    }

    private SmartGroup initMouseControl(SmartGroup group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

//        scene.setOnMousePressed(event -> {
//            anchorX = event.getSceneX();
//            anchorY = event.getSceneY();
//            anchorAngleX = angleX.get();
//            anchorAngleY = angleY.get();
//        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        return group;
    }

    public static void main(String[] args) {
        launch(args);
    }

    class SmartGroup extends Group {

        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByZ(int ang) {
            r = new Rotate(ang, Rotate.Z_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }

}
