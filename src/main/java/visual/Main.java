package visual;

import com.sun.jdi.IntegerType;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import logical.Client;
import logical.InitConfig;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;

public class Main extends Application {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ArrayList<Philosopher> philosophers = new ArrayList<>();
    private static InitConfig initConfig;
    private static final Client client = new Client("127.0.0.1", 4567);

    @Override
    public void start(Stage primaryStage) throws Exception{
       /* Rectangle r1 = new Rectangle();
        r1.setHeight(70);
        r1.setWidth(70);
        r1.setRotate(45);
        r1.setFill(Color.BLACK);
        r1.setLayoutX(50);
        r1.setLayoutY(screenSize.getHeight()/2);
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.seconds(1));
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(1000);
        translateTransition.setToX(540);
        translateTransition.setNode(r1);
        translateTransition.play();*/


         /* Rectangle r2 = new Rectangle();
        r2.setHeight(70);
        r2.setWidth(70);
        r2.setRotate(45);
        r2.setFill(Color.BLACK);
        r2.setLayoutX(1250);
        r2.setLayoutY(screenSize.getHeight()/2);
      TranslateTransition translateTransition2 = new TranslateTransition();
        translateTransition2.setDuration(Duration.seconds(1));
        translateTransition2.setAutoReverse(true);
        translateTransition2.setCycleCount(1000);
        translateTransition2.setToX(-540);
        translateTransition2.setNode(r2);
        translateTransition2.play();*/





        Pane root = new Pane();
        root.getChildren().add(createCircle());
        root.getChildren().add(createMenu());
        createPhilosophers();
        for (int i =0 ;i<5;i++){
            root.getChildren().add(philosophers.get(i).getRectangle());

        }
      //  refresh();

        Scene scene = new Scene(root,1000,1000);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Animation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        Map<String, String> map = null;
        while(map == null) {
            map = Client.stringToMap(client.readLine());
        }
        initConfig = new InitConfig(Integer.parseInt(map.get("NPhil")), map.get("SEating"), map.get("SHungry"), map.get("SThinking"), Integer.parseInt(map.get("TThinking")), Integer.parseInt(map.get("TEating")));
        launch(args);
    }
    public MenuBar createMenu(){
        Menu menu = new Menu("File");
        menu.getItems().add(new MenuItem("Close"));
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    public Circle createCircle(){
        Circle table = new Circle();
        table.setRadius(300);
        table.setLayoutX(screenSize.getWidth()/2);
        table.setLayoutY(screenSize.getHeight()/2);
        table.setFill(Color.YELLOW);
        return table;
    }

    public void createPhilosophers(){
        Map<String, String> map = null;

        while (map == null) {
            map = Client.stringToMap(client.readLine());
        }

        double angle = 360.0 / Integer.parseInt(map.get("NPhil"));
        double startAngle = 0;

        for (int i = 0;i < Integer.parseInt(map.get("NPhil")) ;i++){
            Philosopher philosopher = new Philosopher();

            Point2D centerPoint = new Point2D.Double(screenSize.getWidth()/2, screenSize.getHeight()/2);
            Point pointTo = xyPoint(startAngle);
            pointTo = translate(pointTo,centerPoint);

            philosopher.setId(i);
            philosopher.setState(Integer.parseInt(map.get(i + "")));

            philosopher.getRectangle().setLayoutX(pointTo.getX());
            philosopher.getRectangle().setLayoutY(pointTo.getY());
            philosophers.add(philosopher);
            startAngle+= angle;
        }
    }

    public Point xyPoint( double angle){
        int radius = 300;
        int x = (int) (radius * Math.cos(Math.toRadians(angle)));
        int y = (int) (radius * Math.sin(Math.toRadians(angle)));
        Math.toDegrees(x);
        Math.toDegrees(y);
        System.out.println("X->"+x);
        System.out.println("Y->"+y);
        return new Point(x,y);
    }
    public Point translate(Point point, Point2D to){
        Point newPoint = new Point((int)point.getX(), (int) point.getY());
        newPoint.setLocation(point.getX()+ to.getX(), point.getY() + to.getY());
        return newPoint;
    }

    public void refresh() throws InterruptedException {
        while (true) {
            Map<String, String> map = null;
            while(map == null){
                map = Client.stringToMap(client.readLine());
            }

            for (int i = 0;i < initConfig.getNPhil();i++) {
                if (map.get(i + "").equalsIgnoreCase(initConfig.getSHungry())){
                    //hungry
                    philosophers.get(i).getRectangle().setFill(Color.RED);
                }else if (map.get(i + "").equalsIgnoreCase(initConfig.getSEating())){
                    //eating
                    philosophers.get(i).getRectangle().setFill(Color.GREEN);

                }else if (map.get(i + "").equalsIgnoreCase(initConfig.getSThinking())){
                    //Thinking
                    philosophers.get(i).getRectangle().setFill(Color.BLUE);
                }
            }

            Thread.sleep(initConfig.getTThinkig() * 1000);
        }
    }
}
