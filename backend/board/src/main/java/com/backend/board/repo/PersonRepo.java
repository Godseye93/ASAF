package com.backend.board.repo;

import com.backend.board.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface PersonRepo extends JpaRepository<Person, Long> {
}
