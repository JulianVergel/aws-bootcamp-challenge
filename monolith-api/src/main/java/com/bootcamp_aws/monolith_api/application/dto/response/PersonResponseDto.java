package com.bootcamp_aws.monolith_api.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponseDto {
    private Long id;
    private String name;
    private String email;
}
