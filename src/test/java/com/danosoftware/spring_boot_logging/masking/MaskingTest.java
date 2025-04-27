package com.danosoftware.spring_boot_logging.masking;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MaskingTest {

    @Autowired
    UserService service;

    @Test
    public void test() throws JsonProcessingException {
        service.log();
    }
}
