package com.heriata.number_service;


import com.heriata.number_service.service.NumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest
@ContextConfiguration(classes = {NumberServiceApplication.class, NumberServiceImpl.class})
public class NumberServiceIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testNumberService() throws Exception {

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/numbers"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        assertEquals(13, contentAsString.length());
    }

}
