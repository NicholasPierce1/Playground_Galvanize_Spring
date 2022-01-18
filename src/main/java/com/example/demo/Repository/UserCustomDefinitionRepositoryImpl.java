package com.example.demo.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

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

            assert(connection != null);

            connection.setAutoCommit(true); // default

            System.out.println(connection == null);

            EntityTransaction entityTransaction = null;

            try {

                entityTransaction = _entityManager.getTransaction();

                entityTransaction.begin();

               // _entityManager.createStoredProcedureQuery("")

                entityTransaction.commit();
            }
            catch(JDBCException e){}
            catch (RuntimeException e) {
                if ( entityTransaction != null) entityTransaction.rollback();
                throw e; // or display error message
            }
//            finally {
//                em.close();
//            }

            final Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            final Query query = this._entityManager.createNativeQuery(
                    "SELECT * FROM USER_CUSTOM, ADDRESS_CUSTOM WHERE " +
                            "USER_CUSTOM.ADDRESS = ADDRESS_CUSTOM.ADDRESS_ID"
            );

            final ResultSet resultSet = statement.executeQuery("SELECT * FROM USER_CUSTOM, ADDRESS_CUSTOM WHERE " +
                    "USER_CUSTOM.ADDRESS = ADDRESS_CUSTOM.ADDRESS_ID;" +
                    "SELECT * FROM USER_CUSTOM;");
            resultSet.beforeFirst();
            resultSet.relative(1);
            //resultSet.first(); breaks
            resultSet.next();
            resultSet.absolute(1000);
            System.out.println(resultSet.isAfterLast());
            resultSet.afterLast();

            // shows how to update a row
            if(!resultSet.isAfterLast()){

                resultSet.updateString(2, "new name here");

                // if commit has auto commit than when you update row this will take effect
                resultSet.updateRow(); // resultSet.cancelRowUpdates();

                // else you need to commit the transaction
                connection.commit(); // connection.rollback();

            }

           //resultSet.absolute(-90);
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

//            users.forEach(
//                    (user) -> {
//                        for (final Object object : user)
//                            System.out.println(object);
//                    }
//
//            );
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void testJDBC() throws SQLException {
        /*
        String url = "jdbc:derby:zoo";
        try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select name from animal")) {
        while (rs.next())
        System.out.println(rs.getString(1));
        }
         */

        try {
            final Connection connection = this.dataSource.getConnection();

            System.out.println(connection == null);

            assert(connection != null);

            final Statement statement = connection.createStatement();

            connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            /*
            result set type:
            forward only: cursor goes forward only (DEFAULT)
            scroll sensitive/insensitive: cursor goes anywhere and is/isn't responsive (gets new data)
             to changes to the data in the database
             Sensitive is not well supported.
             */
            /*
            result concurrency type:
            read only: can't change the result set. That's done with insert, update, delete statements. (DEFAULT)
            updatable: can change the result set too. Not supported well
             */
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }

}
