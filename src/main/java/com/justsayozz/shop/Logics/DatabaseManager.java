package com.justsayozz.shop.Logics;

import com.justsayozz.shop.Models.Category;
import com.justsayozz.shop.Models.DatabaseResult;
import com.justsayozz.shop.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by justs on 12.04.2017.
 */
public class DatabaseManager {
    private static final String DB_CONNECTION_STRING = "jdbc:sqlite:data/database.db";

    //Singleton
    private static DatabaseManager instance;

    public static DatabaseManager sharedInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }
    //

    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.out.println("[FATAL] Error loading sqlite JDBC driver! App wouldn't work");
        }
    }

    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_CONNECTION_STRING);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            System.out.println("[ERROR] Error creating database connection. Message: " + ex.getMessage());
        }
        return null;
    }

    private int getRecordsCount(Connection connection, String sql) {
        try {
            ResultSet resultSet = executeQueryStatement(connection, sql);
            int cnt = 0;
            while (resultSet.next())
                cnt++;
            resultSet.close();
            resultSet.getStatement().close();
            return cnt;
        } catch (SQLException ex) {
            return 0;
        }
    }

    private DatabaseResult<Boolean> executeUpdateStatement(Connection connection, String sql) {
        if (connection == null)
            return new DatabaseResult<Boolean>(false, "Error occurred. Please try again");

        DatabaseResult<Boolean> result = new DatabaseResult<>();
        try {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
                result.data = true;
                result.message = "User created";
            }
            connection.commit();
            statement.close();
        } catch (SQLException ex) {
            result.data = false;
            result.message = "Error occurred. Please try again";
            System.out.print(ex.getMessage());
        }

        return result;
    }

    private ResultSet executeQueryStatement(Connection connection, String sql) {
        if (connection == null)
            return null;

        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }

        return resultSet;
    }

    public DatabaseResult<User> createUser(String login, String password) {
        User user = null;
        DatabaseResult<User> result = new DatabaseResult<>(null, "Error creating user. Please try again");
        Connection connection = getConnection();

        if (connection != null) {
            //First, check for user already exists
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM users WHERE login='%s';", login);
            if (getRecordsCount(connection, sql) != 0) {
                result.message = "User already exists";
            } else {
                //If user doesn't exists, create it
                sql = String.format(Locale.ENGLISH, "INSERT INTO users (login, password) VALUES ('%s', '%s');",
                        login, password);
                DatabaseResult<Boolean> updateResult = executeUpdateStatement(connection, sql);
                result.message = updateResult.message;
                if (updateResult.data) {
                    sql = String.format(Locale.ENGLISH, "SELECT * FROM users WHERE login='%s';", login);
                    ResultSet resultSet = executeQueryStatement(connection, sql);
                    if (resultSet != null) {
                        try {
                            if (resultSet.next()) {
                                user = new User();
                                user.id = resultSet.getInt("id");
                                user.login = resultSet.getString("login");
                                user.password = resultSet.getString("password");
                                result.data = user;
                            }
                            resultSet.close();
                            resultSet.getStatement().close();
                        } catch (SQLException ex) {
                            System.out.print(ex.getMessage());
                        }
                    }
                }
            }

            //Close connection
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.print(ex.getMessage());
            }
        }

        return result;
    }

    public DatabaseResult<Boolean> signIn(String login, String password) {
        DatabaseResult<Boolean> result = new DatabaseResult<>(false, "Error occurred. Please try again");
        Connection connection = getConnection();

        if (connection != null) {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM users WHERE login='%s';", login);
            ResultSet resultSet = executeQueryStatement(connection, sql);
            try {
                if (resultSet != null) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        if (password.equals(storedPassword)) {
                            result.data = true;
                            result.message = "Authenticated";
                        } else {
                            result.message = "Invalid password";
                        }
                    } else {
                        result.message = "User not found";
                    }
                    resultSet.close();
                    resultSet.getStatement().close();
                }
            } catch (SQLException ex) {
                System.out.print(ex.getMessage());
            }

            //Close connection
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.print(ex.getMessage());
            }
        }

        return result;
    }

    public List<Category> getCategories() {
        List<Category> categories = null;
        Connection connection = getConnection();

        if (connection != null) {
            String sql = "SELECT * FROM categories;";
            ResultSet resultSet = executeQueryStatement(connection, sql);
            try {
                if (resultSet != null) {
                    categories = new ArrayList<>();
                    while (resultSet.next()) {
                        Category category = new Category();
                        category.id = resultSet.getInt("id");
                        category.title = resultSet.getString("title");
                        category.iconUrl = String.format(Locale.ENGLISH, "http://localhost:666/images/%s",
                                resultSet.getString("icon"));
                        categories.add(category);
                    }
                    resultSet.close();
                    resultSet.getStatement().close();
                }
            } catch (SQLException ex) {
                System.out.print(ex.getMessage());
            }

            //Close connection
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.print(ex.getMessage());
            }
        }

        return categories;
    }
}
