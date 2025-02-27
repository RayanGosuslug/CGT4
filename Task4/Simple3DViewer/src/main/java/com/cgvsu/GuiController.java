package com.cgvsu;

import com.cgvsu.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    private enum RenderMode {
        SIMPLE,
        PAINTED,
        TEXTURED,
        LIGHTED
    }

    private RenderMode currentRenderMode = RenderMode.SIMPLE;

    final private float TRANSLATION = 1F;

    public File TextureKostil;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                if (mesh != null) {
                    switch (currentRenderMode) {
                        case SIMPLE:
                            RenderEngine.renderConstruct(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                            break;
                        case PAINTED:
                            RenderEngine.renderColor(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                            break;
                        case TEXTURED:
                            Image textureImage = null;
                            try {
                                textureImage = new Image(TextureKostil.toURI().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            RenderEngine.renderWithTexture(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height, textureImage, 0);
                            break;
                        case LIGHTED:
                            Image textureImageL = null;
                            try {
                                textureImageL = new Image(TextureKostil.toURI().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            RenderEngine.renderWithTexture(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height, textureImageL, 1);
                            break;
                    }
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent, null);
        } catch (IOException exception) {

        }
    }

    @FXML
    private void onOpenModelWTMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model For Texture");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        FileChooser fileChooserTex = new FileChooser();
        fileChooserTex.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texture Image (*.jpg, *.png)", "*.jpg", "*.png"));
        fileChooserTex.setTitle("Load Texture");

        TextureKostil = fileChooserTex.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (TextureKostil == null) {
            return;
        }

        Path texPath = Path.of(TextureKostil.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent, String.valueOf(texPath));
        } catch (IOException exception) {

        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }


    @FXML
    private void onRenderConstructMenuItemClick(ActionEvent event) {
        currentRenderMode = RenderMode.SIMPLE;
    }

    @FXML
    private void onRenderPaintedMenuItemClick(ActionEvent event) {
        currentRenderMode = RenderMode.PAINTED;
    }
    @FXML
    private void onRenderTexturedMenuItemClick(ActionEvent event) {
        currentRenderMode = RenderMode.TEXTURED;
    }
    @FXML
    private void onRenderTexturedLightedMenuItemClick(ActionEvent event) {
        currentRenderMode = RenderMode.LIGHTED;
    }
    @FXML
    private void handleCameraRotateRight(ActionEvent event) {
        camera.Rotate(0, 1, 0);
    }
    @FXML
    private void handleCameraRotateLeft(ActionEvent event) {
        camera.Rotate(0, -1, 0);
    }
}