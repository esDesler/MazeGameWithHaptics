package Haptics;

import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

public class test {

    private static SerialPort serialPort;

    public static void main(String[] args) {
        SerialConnector serialConnector = new SerialConnector();

        while (true) {
            serialConnector.write("M0,1,91,500,100;M1,1,16,500,100;M2,1,14,500,100;M3,1,92,500,100;\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static void write(char motorData) {
        if (serialPort != null) {
            System.out.println("Writing " + motorData + " to " + serialPort.getSystemPortName() + "...");
            PrintWriter output = new PrintWriter(serialPort.getOutputStream());
            output.print(motorData);
        } else {
            System.out.println("Something went wrong in write method...");
        }
    }
}
