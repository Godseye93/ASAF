package com.ASAF.service;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

@Service
public class ArduinoService {

    private final String PORT_NAME = "COM6"; // 리눅스 기반이라면 /dev/ttyACM* 형식으로 되어있습니다.
    private final int BAUD_RATE =9600;
    private final int TIMEOUT_READ_BLOCKING=100;

    public void startListeningForUid(){
        try{
            SerialPort serialPort= getConnectedArduino();
            if(serialPort!=null){
                while(true){
                    byte[] readBuffer=new byte[1024];
                    int numRead=serialPort.readBytes(readBuffer,readBuffer.length);
                    if(numRead>0){
                        String uid=new String(readBuffer).trim();
                        System.out.println("UID: "+uid);
                    }
                }
            }else{
                System.err.println("No Arduino device found.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private SerialPort getConnectedArduino(){
        SerialPort[] commPorts = SerialPort.getCommPorts();

        for (SerialPort port : commPorts) {
            if (port.getSystemPortName().equals(PORT_NAME)) {
                port.setBaudRate(BAUD_RATE);
                port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, TIMEOUT_READ_BLOCKING, 0);

                if (port.openPort()) {
                    return port;
                } else {
                    throw new RuntimeException("Unable to open the serial connection");
                }
            }
        }

        return null;
    }
}
