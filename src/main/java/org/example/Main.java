package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
      String url = "jdbc:mysql://localhost:3306/bkts";
      String username = "root";
      String password = "Mys25892589.";
      Class.forName("com.mysql.cj.jdbc.Driver");

      try(Connection conn = DriverManager.getConnection(url, username,password);
          Statement s = conn.createStatement()) {
          s.executeUpdate("");
          s.executeUpdate("dsfd");
      }
    }
}