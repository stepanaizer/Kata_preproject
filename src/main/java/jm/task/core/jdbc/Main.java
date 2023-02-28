package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Stepan", "Gusev", (byte) 29);
        userService.saveUser("Valentin", "Petrov", (byte) 18);
        userService.saveUser("Alena", "Ivanova", (byte) 24);
        userService.saveUser("Nikolai", "Fedorov", (byte) 37);

        List<User> users = userService.getAllUsers();
        System.out.println(users);

        userService.cleanUsersTable();
        userService.dropUsersTable();

        // не нашел более удачного расположения для закрытия SessionFactory,
        // где корректно вызывать закрытие фактори в контексте данного приложения?

        Util.closeSessionFactory();
    }
}
