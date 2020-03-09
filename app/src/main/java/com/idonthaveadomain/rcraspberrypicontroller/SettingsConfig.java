package com.idonthaveadomain.rcraspberrypicontroller;

public class SettingsConfig {

    private String ipAddress;

    private int volume = 0;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "SettingsConfig{" +
                "ipAddress='" + ipAddress + '\'' +
                ", volume=" + volume +
                '}';
    }
}
