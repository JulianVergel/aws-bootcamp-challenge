package com.bootcamp_aws.monolith_api.infrastructure.out.jpa.repository;

import com.bootcamp_aws.monolith_api.infrastructure.out.jpa.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonRepository extends JpaRepository<PersonEntity, Long> {
}
