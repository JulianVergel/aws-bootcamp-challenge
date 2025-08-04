package com.bootcamp_aws.monolith_api.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonRequestDto {
    private String name;
    private String email;
}
