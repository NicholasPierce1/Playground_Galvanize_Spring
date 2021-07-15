package com.example.demo.Repository;

import com.example.demo.domain.UserCustom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomRepository extends CrudRepository<UserCustom, Long>, UserCustomDefinitionRepository {

}
