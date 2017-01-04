package com.rbc.rbcone.hackaduck.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Application is up and running", HttpStatus.OK);
    }

}
