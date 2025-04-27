package com.danosoftware.spring_boot_logging.masking;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
public class MaskingTest {

    @Builder
    record User(
            String id,

            String name,

            @Sensitive
            String password,

            Address address
    ) {
        @Builder
        record Address(

                String road,

                @Sensitive
                String postCode
        ) {
        }
    }

    @Builder
    public record Box(
            String id
    ) {
    }

    @Test
    public void test() throws JsonProcessingException {
        User user = User.builder()
                .id("id")
                .name("my-name")
                .password("my-password")
                .address(User.Address
                        .builder()
                        .road("my-road")
                        .postCode("my-postcode")
                        .build())
                .build();

        log.info("User: {}", user);
//        log.info(user.toString());
//        log.info(mapper.writeValueAsString(user));

        log.info("Box: {}", Box.builder().id("123").build());

        List<String> list = List.of("a", "b", "c", "d");
        log.info("List: {}", list);

        User user2 = User.builder()
                .id("id2")
                .name("my-name2")
                .password("my-password2")
                .address(User.Address
                        .builder()
                        .road("my-road2")
                        .postCode("my-postcode2")
                        .build())
                .build();

        log.info("Multiple Users: {} - {}", user, user2);

        List<User> users = List.of(user, user2);
        log.info("List of users: {}", users);

        Set<User> usersSet = Set.of(user, user2);
        log.info("Set of users: {}", usersSet);

    }
}
