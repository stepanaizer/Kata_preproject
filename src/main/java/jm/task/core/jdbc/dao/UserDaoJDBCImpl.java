package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = """
                CREATE TABLE if not exists `mydbtest`.`users`(`id` INT NOT NULL AUTO_INCREMENT,`name` VARCHAR(45) NOT NULL,`lastName` VARCHAR(45) NOT NULL,`age` INT(3) NOT NULL,PRIMARY KEY (`id`),UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)\s
                 ENGINE = InnoDB\s
                 DEFAULT CHARACTER SET = utf8;""";

        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Не удалось создать таблицу!");
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE if exists users";

        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Не удалось удалить таблицу!");
            throw new RuntimeException(e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных ");

        } catch (SQLException e) {
            System.err.println("Не удалось добавить пользователя в БД!");
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id =" + id + ";";

        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Не удалось удалить User из БД!");
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        ResultSet rst;

        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            rst = statement.executeQuery(sql);

            while (rst.next()) {
                User user = new User(rst.getString("name"), rst.getString("lastName"), rst.getByte("age"));
                user.setId(rst.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Не удалось получить список User'ов из БД!");
            throw new RuntimeException(e);
        }
        return users;
    }
    // copy to new repo
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";

        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Не удалось очистить таблицу Users в БД!");
            throw new RuntimeException(e);
        }
    }
}
