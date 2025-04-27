package com.danosoftware.spring_boot_logging.masking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    ObjectMapper mapper;

    public void log() throws JsonProcessingException {
        User user = User.builder()
                .id("id")
                .name("my-name")
                .password("my-password")
                .address(Address.builder().road("my-road").postCode("my-postoce").build())
                .build();

        log.info("User: {}", user);
        log.info(user.toString());
//        log.info(mapper.writeValueAsString(user));

        log.info("Name: {}", Box.builder().id("123").build());
    }
}
