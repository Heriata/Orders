package com.heriata.number_service;

import com.heriata.number_service.service.NumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class NumberServiceTest {

    @InjectMocks
    private NumberServiceImpl numberService;

    @Test
    public void shouldNotBeNull() {
        String number = numberService.getNumber();
        assertNotNull(number);
        assertEquals(13, number.length());
    }
}
