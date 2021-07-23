package com.example.demo.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
public class UserCustomDefinitionRepositoryImpl implements UserCustomDefinitionRepository{

    private final EntityManagerFactory _entityManagerFactory;

    private final EntityManager _entityManager;

    private final DataSource dataSource;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserCustomDefinitionRepositoryImpl(EntityManagerFactory entityManager, DataSource dataSource, ObjectMapper objectMapper){
        this._entityManagerFactory = entityManager;

        this._entityManager = this._entityManagerFactory.createEntityManager();

        this.dataSource = dataSource;

        this.objectMapper = objectMapper;
    }

    @Override
    public void getAllUsers() {
        try {
            final Connection connection = this.dataSource.getConnection();

            System.out.println(connection == null);

            assert(connection != null);

            final Statement statement = connection.createStatement();


            final Query query = this._entityManager.createNativeQuery(
                    "SELECT * FROM USER_CUSTOM, ADDRESS_CUSTOM WHERE " +
                            "USER_CUSTOM.ADDRESS = ADDRESS_CUSTOM.ADDRESS_ID"
            );

            final ResultSet resultSet = statement.executeQuery("SELECT * FROM USER_CUSTOM, ADDRESS_CUSTOM WHERE " +
                    "USER_CUSTOM.ADDRESS = ADDRESS_CUSTOM.ADDRESS_ID;" +
                    "SELECT * FROM USER_CUSTOM;");

            /*
             +
                    " SELECT * FROM USER_CUSTOM, ADDRESS_CUSTOM WHERE" +
                    " USER_CUSTOM.ADDRESS = ADDRESS_CUSTOM.ADDRESS_ID;"
             */

            while (resultSet.next())
            {
                //System.out.println(resultSet.getMetaData().getColumnCount());

                for (int column = 1; column < resultSet.getMetaData().getColumnCount(); column++)
                {
                    final Object value = resultSet.getObject(column);
                    System.out.println(objectMapper.writeValueAsString(value));
                }
            }

            final List<Object[]> users = (List<Object[]>) query.getResultList();

            users.forEach(
                    (user) -> {
                        for (final Object object : user)
                            System.out.println(object);
                    }

            );
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

}
