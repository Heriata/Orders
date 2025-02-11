package com.heriata.number_service.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class NumberServiceImpl implements NumberService {
    private final static String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()+=_-";
    private final Random random = new Random();

    @Override
    public String getNumber() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            number.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = LocalDateTime.now().format(formatter);
        number.append(now);
        return number.toString();
    }
}
