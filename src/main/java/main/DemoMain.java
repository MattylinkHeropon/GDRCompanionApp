package main;

import hero.Unit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;


public class DemoMain extends Application {
    double windowsSize = 600;
    ImageView imageView = new ImageView();


    public static Unit randomPG (){
        int[] as = new int[6];
        Random random = new Random();
        for (int i = 0; i < as.length; i++){
            as[i] = random.nextInt(20) + 1;
        }
        File demo = new File("images/sakurafish.jpg");
        return new Unit(demo.getPath(), "Random", "Test", as, random.nextInt(1000));
    }



    @Override
    public void start(Stage stage) {

        HBox box = new HBox(100);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(windowsSize/2);

        VBox buttonBox = new VBox(50);

        Button cloneButton = new Button("clone");
        Button viewButton = new Button("View");


        System.out.println("TEST PATH");
        //Lancia NullPo
        Path dir = Paths.get(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(".")).getPath());

        cloneButton.setOnAction(actionEvent -> {
                    //Open window looking for an image
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select a image");
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image file", "*.png", "*.jpg"));
                    File image = fileChooser.showOpenDialog(new Stage());
                    FileInputStream imageStream = null;
                    try {
                        imageStream = new FileInputStream(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        assert imageStream != null;
                        Files.copy(imageStream, dir.resolve("TestFolder"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Copia effettuata");
                    System.out.println(dir.toString());
                });

        buttonBox.getChildren().addAll(cloneButton);
        box.getChildren().addAll(buttonBox, imageView);

        Scene mainScene = new Scene(box, windowsSize, windowsSize);
        stage = new Stage();
        stage.setScene(mainScene);
        stage.setTitle("Test");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.show();

    }
}
