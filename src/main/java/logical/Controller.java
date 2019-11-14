package logical;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
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
    private Image imageEating;
    private Image imageHungryNoFork;
    private Image imageHungryFork;
    private Image imageThinkingNoFork;
    private Image imageThinkingFork;

    private Controller() {
        createImages();
        client = new Client(serverAddress, port);
        Map<String, String> map = getInfo();
        initConfig = new InitConfig(Integer.parseInt(map.get("NPhil")), Integer.parseInt(map.get("SEating")), Integer.parseInt(map.get("SHungry")), Integer.parseInt(map.get("SThinking")), Integer.parseInt(map.get("TThinking")), Integer.parseInt(map.get("TEating")));
        philosophers = new ArrayList<>();
        createPhilosophers(map);
    }

    public static Controller getInstance() {
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

    public Image getImageEating() {
        return imageEating;
    }

    public void setImageEating(Image imageEating) {
        this.imageEating = imageEating;
    }

    public Image getImageHungryNoFork() {
        return imageHungryNoFork;
    }

    public void setImageHungryNoFork(Image imageHungryNoFork) {
        this.imageHungryNoFork = imageHungryNoFork;
    }

    public Image getImageHungryFork() {
        return imageHungryFork;
    }

    public void setImageHungryFork(Image imageHungryFork) {
        this.imageHungryFork = imageHungryFork;
    }

    public Image getImageThinkingNoFork() {
        return imageThinkingNoFork;
    }

    public void setImageThinkingNoFork(Image imageThinkingNoFork) {
        this.imageThinkingNoFork = imageThinkingNoFork;
    }

    public Image getImageThinkingFork() {
        return imageThinkingFork;
    }

    public void setImageThinkingFork(Image imageThinkingFork) {
        this.imageThinkingFork = imageThinkingFork;
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
            Point2D centerPoint = new Point2D.Double(Main.screenSize.getWidth()/2, Main.screenSize.getHeight()/2 - 28);
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
        //System.out.println("Pont X"+ point.getX() + "Point to X"+ to.getX() +"Point Y" + point.getY() +"To Y" +to.getY() );
        newPoint.setLocation(point.getX()+ (to.getX() ), point.getY() + (to.getY()));
        return newPoint;
    }

    public void updatePhilosophers(){
        Map<String, String> map = getInfo();
        boolean flag = false;
        for (int i = initConfig.getNPhil() - 1; i >= 0 ;i--) {
            Philosopher aux = philosophers.get(i);
            aux.setState(Integer.parseInt(map.get(aux.getId() + "")));
            if (aux.getState() == initConfig.getSEating()) {
                aux.getCircle().setFill(new ImagePattern(imageEating));
            } else if(aux.getState() == initConfig.getSThinking()) {
                if (philosophers.get(right(i)).getState() == initConfig.getSEating()) {
                    aux.getCircle().setFill(new ImagePattern(imageThinkingNoFork));
                } else {
                    aux.getCircle().setFill(new ImagePattern(imageThinkingFork));
                }
            } else {
                if (philosophers.get(right(i)).getState() == initConfig.getSEating()) {
                    aux.getCircle().setFill(new ImagePattern(imageHungryNoFork));
                } else {
                    aux.getCircle().setFill(new ImagePattern(imageHungryFork));
                }
            }

            if(flag) {
                break;
            }

            if (i == 0) {
                i = initConfig.getNPhil();
                flag = true;
            }
        }
    }

    private int left(int pos) {
        return (pos+ (initConfig.getNPhil() -1 )) % initConfig.getNPhil();
    }

    private int right(int pos){
        return (pos+ 1 ) % initConfig.getNPhil();
    }

    private void createImages() {
        try {
            FileInputStream inputStreamEating = new FileInputStream("src/main/img/Eating.png");
            FileInputStream inputStreamHungryFork = new FileInputStream("src/main/img/HungryFork.png");
            FileInputStream inputStreamHungryNoFork = new FileInputStream("src/main/img/HungryNoFork.png");
            FileInputStream inputStreamThinkingNoFork = new FileInputStream("src/main/img/ThinkingNoFork.png");
            FileInputStream inputStreamThinkingFork = new FileInputStream("src/main/img/ThinkingFork.png");
            imageEating = new Image(inputStreamEating);
            imageHungryNoFork = new Image(inputStreamHungryNoFork);
            imageHungryFork = new Image(inputStreamHungryFork);
            imageThinkingNoFork = new Image(inputStreamThinkingNoFork);
            imageThinkingFork = new Image(inputStreamThinkingFork);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final double delay = (initConfig.getTEating() >= initConfig.getTThinkig())?(initConfig.getTThinkig() * 1000):(initConfig.getTEating() * 1000);
        while (true) {
            updatePhilosophers();
            try {
                sleep((delay > 2500)?2500: (long) delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
