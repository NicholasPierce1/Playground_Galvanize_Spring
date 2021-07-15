package com.example.demo.Repository;

import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class UserGraphRepositoryImpl implements UserGraphRepository{

    private final EntityManagerFactory _entityManagerFactory;

    private final EntityManager _entityManager;

    @Autowired
    public UserGraphRepositoryImpl(EntityManagerFactory entityManager){
        this._entityManagerFactory = entityManager;

        this._entityManager = this._entityManagerFactory.createEntityManager();
    }

    @Override
    public List<User> findAllCustom() {

        final Query query = this._entityManager.createQuery(
                "SELECT new user(u.id, u.name, a.address_path) FROM user u, address a WHERE u.id = a.id"
                //, User.class
        ); // .setHint("javax.persistence.fetchgraph", this._entityManager.getEntityGraph("UserEntityGraph"));

        query.getResultList().forEach(System.out::println);

        return null;
    }
}
