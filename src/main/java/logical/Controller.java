package logical;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import visual.Main;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller extends Thread {
    private ArrayList<Philosopher> philosophers;
    private InitConfig initConfig;
    private Client client;
    private static Controller controller;
    private String serverAddress = "127.0.0.1";
    private int port = 4567;
    private FileInputStream inputStreamDish = new FileInputStream("/home/garco/IdeaProjects/Philosophers/src/main/java/img/hungry.png");
    private FileInputStream inputStreamEating = new FileInputStream("/home/garco/IdeaProjects/Philosophers/src/main/java/img/eating.png");
    private FileInputStream inputStreamThinking = new FileInputStream("/home/garco/IdeaProjects/Philosophers/src/main/java/img/thinking.png");
    private FileInputStream inputStreamDishNoFork = new FileInputStream("/home/garco/IdeaProjects/Philosophers/src/main/java/img/noForkDish.png");
    private FileInputStream inputStreamThinkingWFork = new FileInputStream("/home/garco/IdeaProjects/Philosophers/src/main/java/img/ThinkingFork.png");

    private Image imageDish = new Image(inputStreamDish);
    private Image imageeating = new Image(inputStreamEating);
    private Image imageThinking = new Image(inputStreamThinking);
    private Image imageDishNoFork = new Image(inputStreamDishNoFork);
    private Image getImageThinkingFork = new Image(inputStreamThinkingWFork);

    private Controller() throws FileNotFoundException {
        client = new Client(serverAddress, port);
        Map<String, String> map = getInfo();
        initConfig = new InitConfig(Integer.parseInt(map.get("NPhil")), Integer.parseInt(map.get("SEating")), Integer.parseInt(map.get("SHungry")), Integer.parseInt(map.get("SThinking")), Integer.parseInt(map.get("TThinking")), Integer.parseInt(map.get("TEating")));
        philosophers = new ArrayList<>();
        createPhilosophers(map);
    }

    public static Controller getInstance() throws FileNotFoundException {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<Philosopher> getPhilosophers() {
        return philosophers;
    }

    public void setPhilosophers(ArrayList<Philosopher> philosophers) {
        this.philosophers = philosophers;
    }

    public InitConfig getInitConfig() {
        return initConfig;
    }

    public void setInitConfig(InitConfig initConfig) {
        this.initConfig = initConfig;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static Controller getController() {
        return controller;
    }

    public static void setController(Controller controller) {
        Controller.controller = controller;
    }

    public Map<String, String> getInfo() {
        Map<String, String> map = null;
        while(map == null) {
            map = stringToMap(client.readLine());
        }
        return map;
    }

    public static Map<String, String> stringToMap(String value){
        Map<String, String> map = null;
        if (!value.equalsIgnoreCase("") && value != null) {
            map = new HashMap<>();
            String[] list = value.split(",");
            for (String string : list) {
                String[] strings = string.split(":");
                map.put(strings[0].trim(), strings[1].trim());
            }
        }
        return map;
    }

    private void createPhilosophers(Map<String, String> map){
        double angle = 360.0/initConfig.getNPhil();
        double startAngle = 0;
        for (int i = 0;i < initConfig.getNPhil();i++){
            Philosopher philosopher = new Philosopher();
            Point2D centerPoint = new Point2D.Double(Main.screenSize.getWidth()/2, Main.screenSize.getHeight()/2);
            Point pointTo = xyPoint(startAngle);
            pointTo = translate(pointTo,centerPoint);
            philosopher.setId(i);
            philosopher.setState(Integer.parseInt(map.get(i + "")));
            philosopher.getCircle().setLayoutX(pointTo.getX());
            philosopher.getCircle().setLayoutY(pointTo.getY());

            philosophers.add(philosopher);
            startAngle += angle;
        }
    }

    private Point xyPoint( double angle){
        int radius = 300;
        int x = (int) (radius * Math.cos(Math.toRadians(angle)));
        int y = (int) (radius * Math.sin(Math.toRadians(angle)));
        Math.toDegrees(x);
        Math.toDegrees(y);
        return new Point(x,y);
    }

    private Point translate(Point point, Point2D to){
        Point newPoint = new Point((int)point.getX(), (int) point.getY());
        System.out.println("Pont X"+ point.getX() + "Point to X"+ to.getX() +"Point Y" + point.getY() +"To Y" +to.getY() );
        newPoint.setLocation(point.getX()+ (to.getX() ), point.getY() + (to.getY()));
        return newPoint;
    }

    public void updatePhilosophers(){
        Map<String, String> map = getInfo();
        for (Philosopher aux : philosophers) {
            aux.setState(Integer.parseInt(map.get(aux.getId() + "")));
            if (aux.getState() == initConfig.getSHungry()) {
                //hungry
                if(philosophers.get(left(aux.getId())).getState()== initConfig.getSEating()){
                    aux.getCircle().setFill(new ImagePattern(imageDishNoFork));
                } else {
                    aux.getCircle().setFill(new ImagePattern(imageDish));
                }
            } else if(aux.getState() == initConfig.getSEating()) {
                //eating
                aux.getCircle().setFill(new ImagePattern(imageeating));
            } else if(aux.getState() == initConfig.getSThinking()){
                //Thinking
               if(philosophers.get(left(aux.getId())).getState()== initConfig.getSEating()){
                    aux.getCircle().setFill(new ImagePattern(imageDishNoFork));
                } else {
                    aux.getCircle().setFill(new ImagePattern(imageDish));
                }
            }

            if (aux.getId() == initConfig.getNPhil() - 1) {
                philosophers.get(0).setState(Integer.parseInt(map.get("0")));
                if (philosophers.get(0).getState() == initConfig.getSHungry()) {
                    //hungry
                    if(philosophers.get(left(0)).getState()== initConfig.getSEating()){
                        philosophers.get(0).getCircle().setFill(new ImagePattern(imageDishNoFork));
                    } else {
                        philosophers.get(0).getCircle().setFill(new ImagePattern(imageDish));
                    }
                } else if(philosophers.get(0).getState() == initConfig.getSEating()) {
                    //eating
                    philosophers.get(0).getCircle().setFill(new ImagePattern(imageeating));
                } else if(philosophers.get(0).getState()  == initConfig.getSThinking()){
                    //Thinking
                    if(philosophers.get(left(0)).getState()== initConfig.getSEating()){
                        philosophers.get(0).getCircle().setFill(new ImagePattern(imageDishNoFork));
                    } else {
                        philosophers.get(0).getCircle().setFill(new ImagePattern(imageDish));
                    }
                }
            }
        }
    }
    public int left(int pos){
        int position = (pos+ (initConfig.getNPhil() -1 )) % initConfig.getNPhil();
        return position;
    }
    public int right(int pos){
        int position = (pos+ 1 ) % initConfig.getNPhil();
        return position;
    }

    @Override
    public void run() {
        final long delay = (initConfig.getTEating() >= initConfig.getTThinkig())?(initConfig.getTThinkig() * 1000):(initConfig.getTEating() * 1000);
        while (true) {
            updatePhilosophers();
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
