package com.ASAF.controller;

import com.ASAF.dto.RfidDTO;
import com.ASAF.service.ArduinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/RFID")
public class ArduinoController {

    @Autowired
    private ArduinoService arduinoService;

    @GetMapping
    public ResponseEntity<?> getRfid() {
        RfidDTO result = arduinoService.startListeningForUid();

        if(result != null){
            return ResponseEntity.ok(result.getRfidNumber());
        }else{
            return ResponseEntity.ok(false);
        }


//        if(result == null){
//            return ResponseEntity.ok(false);
//        }else{
//            return ResponseEntity.ok(result.getRfidNumber());
//        }
    }

    @GetMapping("/off")
    public ResponseEntity<?> stopListeningForUid() {
        arduinoService.stopListeningForUid();
        return ResponseEntity.ok("RFID Listener stopped");
    }
}
