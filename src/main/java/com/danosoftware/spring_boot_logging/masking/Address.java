package com.danosoftware.spring_boot_logging.masking;

import lombok.Builder;

@Builder
public record Address(

        String road,

        @Sensitive
        String postCode
) {
}
