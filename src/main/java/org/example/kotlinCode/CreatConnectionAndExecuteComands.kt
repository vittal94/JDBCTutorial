package org.example.kotlinCode
import java.sql.DriverManager

fun createConnection() {
    val connection = DriverManager.getConnection(url, username, password)
    connection.use { println("we are connected") }
}

fun executeSimpleCommands() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use {  stat ->
            stat.executeUpdate("DROP TABLE Books")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books " +
                    "(id INT AUTO_INCREMENT," +
                    "name CHAR(30) NOT NULL," +
                    "PRIMARY KEY (id))")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('Inferno')")
            stat.executeUpdate("INSERT INTO Books set name = 'Man in labirint'")

            stat.executeQuery("SELECT * FROM Books").use { resultSet ->
                while (resultSet.next()) {
                    println(resultSet.getInt("id"))
                    println(resultSet.getString("name"))
                    println("------------------------")
                }
            }
            println("========================================")
            stat.executeQuery("SELECT name FROM Books WHERE id = 1").use { resultSet ->
                while (resultSet.next()) {
                    println(resultSet.getString(1))
                }
            }
        }
    }
}


fun main() {
    createConnection()
    executeSimpleCommands()
}