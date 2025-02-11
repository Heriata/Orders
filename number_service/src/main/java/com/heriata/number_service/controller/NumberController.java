package com.heriata.number_service.controller;

import com.heriata.number_service.service.NumberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/numbers")
@AllArgsConstructor
public class NumberController {

    private NumberService numberService;

    @GetMapping
    public String number() {
        return numberService.getNumber();
    }
}
