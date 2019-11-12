package logical;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 4567);
        while(true) {
            Map<String, String> map = stringToMap(client.readLine());
            System.out.println(map.get("1"));
            System.out.println(map);

            try {
                Thread.sleep(Long.parseLong(map.get("TThinking")));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> stringToMap(String value){
        Map<String, String> map = new HashMap<String, String>();
        String[] list = value.split(",");
        for (String string : list) {
            String[] strings = string.split(":");
            map.put(strings[0].trim(), strings[1].trim());
        }
        return map;
    }
}