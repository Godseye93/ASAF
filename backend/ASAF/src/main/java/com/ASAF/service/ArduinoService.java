package com.ASAF.service;

import com.ASAF.dto.RfidDTO;
import com.ASAF.entity.RfidEntity;
import com.ASAF.repository.RfidRepository;
import com.fazecast.jSerialComm.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ArduinoService {

    private final String PORT_NAME = "COM6";
    private final int BAUD_RATE = 9600;
    private final int TIMEOUT_READ_BLOCKING = 100;

    @Autowired
    private RfidRepository rfidRepository;

    private AtomicBoolean isListening = new AtomicBoolean(false);

    public RfidDTO startListeningForUid() {
        isListening.set(true);

        try {
            SerialPort serialPort = getConnectedArduino();

            if (serialPort != null) {
                while (isListening.get()) {
                    byte[] readBuffer = new byte[1024];
                    int numRead = serialPort.readBytes(readBuffer, readBuffer.length);

                    if (numRead > 0) {
                        String uid = new String(readBuffer).trim();
                        System.out.println("UID: " + uid);

                        Optional<RfidEntity> optionalRfidEntity = rfidRepository.findByRfidNumber(uid);

                        if (optionalRfidEntity.isPresent()) {
                            return RfidDTO.toRfidDTO(optionalRfidEntity.get());
                        }
                    }
                }

                serialPort.closePort();
            } else {
                System.err.println("No Arduino device found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void stopListeningForUid() {
        isListening.set(false);
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
