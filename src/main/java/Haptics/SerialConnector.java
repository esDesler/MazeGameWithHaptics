package Haptics;


import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

public class SerialConnector {

    private SerialPort serialPort;
    PrintWriter output;

    public SerialConnector() {
        serialPort = SerialPort.getCommPort("COM7");
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Serial port " + serialPort.getSystemPortName() + " is now open...");
            output = new PrintWriter(serialPort.getOutputStream(), true);
        }
    }


    void write(String motorData) {
        System.out.println("Writing " + motorData + " to " + serialPort.getSystemPortName() + "...");
        if (serialPort != null && serialPort.isOpen()) {
            output.println(motorData);
        } else {
            //System.out.println("Something went wrong in write method...");
        }
    }
}
