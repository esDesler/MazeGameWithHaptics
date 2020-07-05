package Haptics;

import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

public class test {

    private static SerialPort serialPort;

    public static void main(String[] args) throws InterruptedException {
        SerialPort[] serialPortList = SerialPort.getCommPorts();

        serialPort = SerialPort.getCommPort("COM7");
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Serial port " + serialPort.getSystemPortName() + " is now open...");
        }


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PrintWriter output = new PrintWriter(serialPort.getOutputStream());

                while (true) {
                    output.print(true);
                    output.flush();
                    System.out.println("output sent");
                }
            }
        };
        thread.start();
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
