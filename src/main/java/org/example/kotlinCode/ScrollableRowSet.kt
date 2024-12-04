package org.example.kotlinCode

import java.sql.DriverManager
import java.sql.ResultSet

fun rowSet() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL)")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('MAN')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('JAVA')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('SPRING')")
        }
        conn.prepareStatement(
            "SELECT * FROM Books",
            ResultSet.TYPE_SCROLL_INSENSITIVE, //with changes to DB
            ResultSet.CONCUR_READ_ONLY).use { preparedStatement ->  //readOnly or updatable
            preparedStatement.executeQuery().use { resultSet ->
                //reading the next element
                if(resultSet.next()) println(resultSet.getString("name"))
                if(resultSet.next()) println(resultSet.getString("name"))
                //reading the previous element
                if (resultSet.previous()) println(resultSet.getString("name"))
                // reading relative to the current position forward
                if(resultSet.relative(2)) println(resultSet.getString("name"))
                //reading relative to the current position backward
                if(resultSet.relative(-2)) println(resultSet.getString("name"))
                //reading shifted from beginning
                if(resultSet.absolute(2)) println(resultSet.getString("name"))
                //reading first row
                if(resultSet.first()) println(resultSet.getString("name"))
                //reading last row
                if(resultSet.last()) println(resultSet.getString("name"))
                //if (resultSet.isBeforeFirst) //when using in cycle
            }
        }
    }
}
fun main() {
rowSet()
}