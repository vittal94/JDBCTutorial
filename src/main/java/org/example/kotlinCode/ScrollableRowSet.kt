package org.example.kotlinCode

import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Types

fun rowSetReading() {
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
            ResultSet.TYPE_SCROLL_INSENSITIVE, //without changes to DB
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

fun rowSetModifying() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(30) NOT NULL)")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('MAN')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('JAVA')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('SPRING')")

            conn.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE, //with changes to DB
                ResultSet.CONCUR_UPDATABLE).use { modifyingStat ->
                modifyingStat.executeQuery("SELECT * FROM Books").use { resultSet ->
                    println("Original data from Books")
                    while (resultSet.next()) {
                        println("${resultSet.getInt("id")} " +
                                resultSet.getString("name"))
                    }
                    println("\nModified data from Books")
                    resultSet.apply {
                        //updating the existing row
                        last()
                        updateString("name","SPRING SECURITY")
                        updateRow()

                        //inserting a new row
                        moveToInsertRow()
                        updateString("name","HYBERNATE")
                        insertRow()

                        //deleting row
                        absolute(1)
                        deleteRow()
                    }
                    resultSet.beforeFirst()
                    while (resultSet.next()) {
                        println("${resultSet.getInt("id")} " +
                                resultSet.getString("name"))
                    }
                }
            }
        }
    }
}
fun main() {
//rowSetReading()
    rowSetModifying()
}