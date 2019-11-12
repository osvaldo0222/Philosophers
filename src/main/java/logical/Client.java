package logical;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private Socket connect() {
        Socket socket = null;
        try {
            socket = new Socket(address, port);
        } catch(UnknownHostException e) {
            System.out.println(e);
        } catch(IOException e) {
            System.out.println(e);
        }
        return socket;
    }

    public void write(String message) {
        Socket socket = connect();
        if (socket != null) {
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.write(message.getBytes());
            } catch(IOException i) {
                System.out.println(i);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readLine() {
        Socket socket = connect();
        String result = "";
        if (socket != null) {
            try {
                DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                BufferedReader stream = new BufferedReader(new InputStreamReader(input));
                result = stream.readLine();
            } catch(IOException i) {
                System.out.println(i);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static Map<String, String> stringToMap(String value){
        Map<String, String> map = new HashMap<>();
        String[] list = value.split(",");
        for (String string : list) {
            String[] strings = string.split(":");
            map.put(strings[0].trim(), strings[1].trim());
        }
        return map;
    }
}