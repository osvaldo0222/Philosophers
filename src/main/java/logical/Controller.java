package logical;

import javafx.scene.paint.Color;
import visual.Main;

import java.awt.*;
import java.awt.geom.Point2D;
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

    private Controller() {
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
            philosopher.getRectangle().setLayoutX(pointTo.getX());
            philosopher.getRectangle().setLayoutY(pointTo.getY());
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
        newPoint.setLocation(point.getX()+ (to.getX() - 30), point.getY() + (to.getY()-35));
        return newPoint;
    }

    public void updatePhilosophers(){
        Map<String, String> map = getInfo();
        for (Philosopher aux : philosophers) {
            aux.setState(Integer.parseInt(map.get(aux.getId() + "")));
            if (aux.getState() == initConfig.getSHungry()) {
                //hungry
                aux.getRectangle().setFill(Color.RED);
            } else if(aux.getState() == initConfig.getSEating()) {
                //eating
                aux.getRectangle().setFill(Color.GREEN);
            } else {
                //Thinking
                aux.getRectangle().setFill(Color.BLUE);
            }
        }
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
