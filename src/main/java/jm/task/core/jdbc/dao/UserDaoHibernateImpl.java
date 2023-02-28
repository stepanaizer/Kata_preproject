package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {

        String sql = "CREATE TABLE if not exists `mydbtest`.`users`"
                     + "(`ID` INT NOT NULL AUTO_INCREMENT,`NAME` VARCHAR(45) NOT NULL,`LASTNAME` VARCHAR(45) NOT NULL,"
                     + "`AGE` INT(3) NOT NULL,PRIMARY KEY (`ID`),UNIQUE INDEX `ID_UNIQUE` (`ID` ASC) VISIBLE)\n"
                     + "ENGINE = InnoDB\n DEFAULT CHARACTER SET = utf8;";
        commitTransaction(sql);
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE if exists users";
        commitTransaction(sql);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        User user = new User(name, lastName, age);
        Transaction tx = null;
        Session session = Util.getSessionFactory().getCurrentSession();

        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Ошибка взаимодействия с БД");
        }
    }

    @Override
    public void removeUserById(long id) {

        Transaction tx = null;
        Session session = Util.getSessionFactory().getCurrentSession();

        try {
            tx = session.beginTransaction();
            session.createQuery("delete from User where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Ошибка взаимодействия с БД");
        }

    }

    @Override
    public List<User> getAllUsers() {

        Transaction tx = null;
        Session session = Util.getSessionFactory().getCurrentSession();

        try {
            tx = session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            tx.commit();
            return users;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Ошибка взаимодействия с БД");
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {

        String sql = "truncate table users";
        Transaction tx = null;
        Session session = Util.getSessionFactory().getCurrentSession();

        try {
            tx = session.beginTransaction();
            session.createSQLQuery(sql)
                    .executeUpdate();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Ошибка взаимодействия с БД");
        }
    }

    private static void commitTransaction(String sql) {
        Session session = Util.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        session.createSQLQuery(sql)
                .addEntity(User.class)
                .executeUpdate();

        session.getTransaction()
                .commit();
    }
}
