package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Не удалось подключиться к БД!");
            throw new RuntimeException(e);
        }
        return connection;
    }

    private static SessionFactory buildSessionFactory() {

        try {
            Configuration configuration = new Configuration();
            Properties properties = new Properties();

            properties.put("hibernate.connection.driver_class", DB_DRIVER);
            properties.put("hibernate.connection.url", DB_URL);
            properties.put("hibernate.connection.username", DB_USERNAME);
            properties.put("hibernate.connection.password", DB_PASSWORD);
            properties.put("hibernate.current_session_context_class", "thread");
            properties.put("hibernate.show_sql", "true");

            configuration.setProperties(properties);
            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            System.out.println("Hibernate конфигурация ServiceRegistry успешно создана");

            return configuration.buildSessionFactory(serviceRegistry);

        } catch (HibernateException e) {
            throw new ExceptionInInitializerError("Не удалось инициализировать SessionFactory" + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSessionFactory(){
        sessionFactory.close();
    }

}
