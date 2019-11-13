package visual;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logical.Controller;

import java.awt.*;

public class Main extends Application {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.getChildren().add(createCircle());
        root.getChildren().add(createMenu());
        for (int i = 0;i < Controller.getInstance().getInitConfig().getNPhil();i++){
            root.getChildren().add(Controller.getInstance().getPhilosophers().get(i).getRectangle());
        }
        Scene scene = new Scene(root,1000,1000);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Animation");
        primaryStage.setScene(scene);
        Controller.getInstance().start();
        primaryStage.show();
        //This is for application to stop with the exit button and kill and the Threads
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });
    }

    public Circle createCircle() {
        Circle table = new Circle();
        table.setRadius(300);
        table.setLayoutX(screenSize.getWidth()/2);
        table.setLayoutY(screenSize.getHeight()/2);
        table.setFill(Color.YELLOW);
        return table;
    }

    public javafx.scene.control.MenuBar createMenu(){
        javafx.scene.control.Menu menu = new javafx.scene.control.Menu("File");
        menu.getItems().add(new javafx.scene.control.MenuItem("Close"));
        javafx.scene.control.MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        return menuBar;
    }
}
