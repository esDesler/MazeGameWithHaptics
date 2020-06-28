package Haptics;


import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

public class SerialConnector {

    private SerialPort serialPort;

    public SerialConnector() {
        SerialPort[] serialPortList = SerialPort.getCommPorts();

        serialPort = null;

        for (SerialPort sp : serialPortList) {
            if (sp.getSystemPortName().equals("COM7")) serialPort = sp;
        }


        if (serialPort != null) {

            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

            if (serialPort.openPort()) {
                System.out.println("Serial port " + serialPort.getSystemPortName() + " is now open...");
            }
        }
    }


    void write(String motorData) {
        if (serialPort != null) {
            System.out.println("Writing " + motorData + " to " + serialPort.getSystemPortName() + "...");
            PrintWriter output = new PrintWriter(serialPort.getOutputStream());
            output.print(motorData);
        } else {
            System.out.println("Something went wrong in write method...");
        }
    }
}
