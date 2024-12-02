package org.example.kotlinCode

import java.sql.DriverManager

fun injection() {
    DriverManager.getConnection(url, username, password).use { connection ->
        connection.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE Employees")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Employees("+
                    "id MEDIUMINT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL," +
                    "salary DOUBLE(5,1))")
            stat.executeUpdate("INSERT INTO Employees(name,salary) VALUES('MAX',234.5)")
            stat.executeUpdate("INSERT INTO Employees(name,salary)  VALUES('KOSTYA', 435.4)")

            val userId = "1' or 1 = '1" //this is sql injection that allows get access to all data in DB
            stat.executeQuery("SELECT * FROM Employees WHERE id = '$userId'").use { resultSet ->
                while (resultSet.next()) {
                    println("name: ${resultSet.getString("name")}")
                    println("salary: ${resultSet.getDouble("salary")}")
                }
            }
        }
    }
}

//but if you use prepared statement with parameters, it secure query from sql injection
fun preparedStatement() {
    DriverManager.getConnection(url, username, password).use { connection ->
        connection.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE Employees")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Employees("+
                    "id MEDIUMINT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL," +
                    "salary DOUBLE(5,1))")
            stat.executeUpdate("INSERT INTO Employees(name,salary) VALUES('MAX',234.5)")
            stat.executeUpdate("INSERT INTO Employees(name,salary)  VALUES('KOSTYA', 435.4)")
        }
        val userId = "1' or 1 = '1" //this is sql injection that allows get access to all data in DB
        connection.prepareStatement("SELECT * FROM Employees WHERE id = ?").use { preparedStatement ->
            preparedStatement.setString(1,userId)
            preparedStatement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    println("name: ${resultSet.getString("name")}")
                    println("salary: ${resultSet.getDouble("salary")}")
                }
            }
        }
    }
}
fun main() {
    //injection()
    preparedStatement()
}