package com.example.demo.Repository;

import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Component
public class UserCustomDefinitionRepositoryImpl implements UserCustomDefinitionRepository{

    private final EntityManagerFactory _entityManagerFactory;

    private final EntityManager _entityManager;

    @Autowired
    public UserCustomDefinitionRepositoryImpl(EntityManagerFactory entityManager){
        this._entityManagerFactory = entityManager;

        this._entityManager = this._entityManagerFactory.createEntityManager();
    }

    @Override
    public void getAllUsers() {
        final Query query = this._entityManager.createNativeQuery(
                "SELECT * FROM USER_CUSTOM, ADDRESS_CUSTOM WHERE " +
                        "USER_CUSTOM.ADDRESS = ADDRESS_CUSTOM.ADDRESS_ID"
        );

        final List<Object[]> users = (List<Object[]>)query.getResultList();

        users.forEach(
                (user) -> {
                    for (final Object object : user)
                        System.out.println(object);
                }

        );
    }
}
