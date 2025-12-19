package com.interactivetool.wilfred.interactivetool.controllers;

import com.interactivetool.wilfred.interactivetool.services.UssdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/interactions")
@RequiredArgsConstructor
@Slf4j
public class UssdController {
    private final UssdService ussdService;


    @PostMapping
    public ResponseEntity<String> handleInput(@RequestParam(value = "MSISDN") String msisdn,
                                              @RequestParam(value = "SERVICE_CODE") String serviceCode, @RequestParam(value = "SESSION_ID") String sessionId,
                                              @RequestParam(value = "imsi") String imsi, @RequestParam(value = "USSD_STRING", required = false) String text) {
        log.info("inputs received ::::{}", sessionId);
        return ResponseEntity.ok(ussdService.processInput(msisdn, serviceCode, sessionId, imsi, text));
    }

    @GetMapping
    public ResponseEntity<String> testController() {
        return ResponseEntity.ok("Hello Interactive tool!!");
    }
}
