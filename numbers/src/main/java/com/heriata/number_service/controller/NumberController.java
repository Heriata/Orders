package com.heriata.number_service.controller;

import com.heriata.number_service.service.NumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/numbers")
@RequiredArgsConstructor
public class NumberController {

    @Autowired
    private NumberService numberService;

    @GetMapping
    public String number() {
        return numberService.getNumber();
    }
}
