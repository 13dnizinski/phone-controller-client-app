package com.idonthaveadomain.rcraspberrypicontroller;

import java.io.*;
import java.net.*;

public class RCClient {
    public static final String SERVER_IP = "192.168.43.8";

    Socket socket;
    PrintWriter output;
    BufferedReader input;

    RCClient() {
        try {
            System.out.println("Setting up client connection...");
            socket = new Socket(InetAddress.getByName(SERVER_IP), 4141);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            write(output, "SYN"); //Synchronize
            System.out.println("Successfully set up client connection...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    void turn(int pulseWidth) {
        write(output, "pwAngle="+pulseWidth);
    }

    void write(PrintWriter output, String message) {
        System.out.println("Sending: "+message);
        output.println(message);
    }

    void closeUp() {
        try {
            write(output, "FIN");
            String in = "";
            while((in = input.readLine()) != null) {
                if(in.equals("FIN_ACK")) {
                    break;
                }
            }
            System.out.println("Closing Socket");
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}