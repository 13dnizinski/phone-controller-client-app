package com.idonthaveadomain.rcraspberrypicontroller;

import android.content.Context;

import java.io.*;
import java.net.*;

public class RCClient {
    String serverIp = "";//"192.168.43.8";

    Socket socket;
    PrintWriter output;
    BufferedReader input;

    private static RCClient rcClientInstance;

    public static RCClient getInstance(Context context) {
        if(rcClientInstance == null) {
            SettingsConfig settingsConfig = FileIOHandler.readFromFile(context);
            return new RCClient(settingsConfig.getIpAddress());
        }
        return rcClientInstance;
    }

    RCClient(String ipAddress) {
        setUpConnectionToIP(ipAddress);
    }

    void turn(int pulseWidth) {
        write(output, "pwAngle="+pulseWidth);
    }

    void emitBodyMovementAngle(double bodyMovementAngle) {
        //Angle is in degrees
        write(output, "bodyMovementAngle="+bodyMovementAngle);
    }

    void emitBodyMovementSpeed(double bodyMovementSpeed) {
        write(output, "bodyMovementSpeed="+bodyMovementSpeed);
    }

    void emitBodyRotation(double bodyRotation) {
        write(output, "bodyRotation="+bodyRotation);
    }

    void emitHeadMovementAngle(double headMovementAngle) {
        //Angle is in degrees
        write(output, "headMovementAngle="+headMovementAngle);
    }

    void emitHeadMovementSpeed(double headMovementSpeed) {
        write(output, "headMovementSpeed="+headMovementSpeed);
    }

    void emitHeadRotationAngle(double headRotationAngle) {
        //Angle is in degrees
        write(output, "headRotationAngle="+headRotationAngle);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Closing Socket");
            input.close();
            output.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setUpConnectionToIP(String ipAddress) {
        try {
            this.serverIp = ipAddress;
            System.out.println("Setting up client connection...");
            socket = new Socket(InetAddress.getByName(this.serverIp), 4141);
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
}