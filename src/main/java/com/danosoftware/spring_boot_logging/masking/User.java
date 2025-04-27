package com.danosoftware.spring_boot_logging.masking;

import lombok.Builder;

@Builder
public record User(
        String id,

        String name,

        @Sensitive
        String password,

        Address address
) {
}
