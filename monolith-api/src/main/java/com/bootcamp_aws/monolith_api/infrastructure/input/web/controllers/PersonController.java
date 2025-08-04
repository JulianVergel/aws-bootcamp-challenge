package com.bootcamp_aws.monolith_api.infrastructure.input.web.controllers;

import com.bootcamp_aws.monolith_api.application.dto.request.PersonRequestDto;
import com.bootcamp_aws.monolith_api.application.dto.response.PersonResponseDto;
import com.bootcamp_aws.monolith_api.application.handler.IPersonHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;

import static com.bootcamp_aws.monolith_api.domain.utils.constants.DomainConstants.PERSON_CREATED_SUCCESSFULLY;
import static com.bootcamp_aws.monolith_api.infrastructure.input.web.constants.SwaggerConstants.*;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final IPersonHandler personHandler;

    @Operation(summary = PERSON_SAVE_OPERATION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = RESPONSE_201_DESCRIPTION),
            @ApiResponse(responseCode = "400", description = RESPONSE_400_DESCRIPTION, content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> savePerson(@RequestBody PersonRequestDto personRequestDto) {
        personHandler.savePerson(personRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", PERSON_CREATED_SUCCESSFULLY));
    }

    @Operation(summary = PERSON_GET_BY_ID_OPERATION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION),
            @ApiResponse(responseCode = "404", description = RESPONSE_404_DESCRIPTION, content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personHandler.getPersonById(id));
    }
}
