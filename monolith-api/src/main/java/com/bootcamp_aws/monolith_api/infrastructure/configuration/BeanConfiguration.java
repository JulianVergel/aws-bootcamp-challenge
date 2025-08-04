package com.bootcamp_aws.monolith_api.infrastructure.configuration;

import com.bootcamp_aws.monolith_api.domain.api.IPersonServicePort;
import com.bootcamp_aws.monolith_api.domain.spi.IPersonPersistencePort;
import com.bootcamp_aws.monolith_api.domain.usecase.PersonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IPersonServicePort personServicePort(IPersonPersistencePort personPersistencePort) {
        return new PersonUseCase(personPersistencePort);
    }
}
