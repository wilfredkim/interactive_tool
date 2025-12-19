package com.interactivetool.wilfred.interactivetool.services;

public interface UssdService {
    String processInput(String msisdn, String serviceCode, String sessionId, String imsi, String text);
}
