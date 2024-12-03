package org.example.kotlinCode

import java.sql.DriverManager
import java.sql.Types

fun bookCount(){
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("DROP PROCEDURE IF EXISTS BooksCount")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL)")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('MAN')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('JAVA BOOKS')")

            stat.executeUpdate("CREATE PROCEDURE BooksCount (OUT cnt INT) " +
                    "BEGIN " +
                    "SELECT COUNT(*) INTO cnt FROM Books; " +
                    "END")
        }
//        conn.prepareCall("CREATE PROCEDURE BooksCount (OUT cnt INT) " +
//                "BEGIN " +
//                "SELECT COUNT(*) INTO cnt FROM Books; " +
//                "END").use { callableStatement -> callableStatement.execute() }

        //fetching data from procedure
        conn.prepareCall("{call BooksCount(?)}").use { callableStatement ->
            callableStatement.registerOutParameter(1,Types.INTEGER)
            callableStatement.execute()
            println(callableStatement.getInt(1))
        }
    }
}

fun getBooks() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("DROP PROCEDURE IF EXISTS getBooks")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL)")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('MAN')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('JAVA BOOKS')")

            stat.executeUpdate("CREATE PROCEDURE getBooks (bookId INT) " +
                    "BEGIN " +
                    "SELECT * FROM Books WHERE id = bookId; " +
                    "END")
        }
        //fetching data from procedure
        conn.prepareCall("{call getBooks(?)}").use { callableStatement ->
            callableStatement.setInt(1,1)
            if(callableStatement.execute()) {
                callableStatement.resultSet.use { resultSet ->
                    while (resultSet.next()) {
                        println(resultSet.getInt("id"))
                        println(resultSet.getString("name"))
                    }
                }
            }
        }
    }
}

//procedures with multiple tables
fun getCount() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("DROP PROCEDURE IF EXISTS getCount")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL)")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('MAN')")
            stat.executeUpdate("INSERT INTO Books(name) VALUES('JAVA BOOKS')")

            stat.executeUpdate("DROP TABLE IF EXISTS Users")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Users(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL)")
            stat.executeUpdate("INSERT INTO Users(name) VALUES('Peter')")
            stat.executeUpdate("INSERT INTO Users(name) VALUES('Max')")
            stat.executeUpdate("INSERT INTO Users(name) VALUES('Fill')")

            stat.executeUpdate("CREATE PROCEDURE getCount () " +
                    "BEGIN " +
                    "SELECT COUNT(*) FROM Books; " +
                    "SELECT COUNT(*) FROM Users; " +
                    "END")
        }
        conn.prepareCall("{call getCount()}").use { callableStatement ->
            var hasResult = callableStatement.execute()
            while (hasResult) {
                callableStatement.resultSet.use { resultSet ->
                    while (resultSet.next()) {
                        println(resultSet.getInt(1))
                    }
                    hasResult = callableStatement.moreResults
                }
            }
        }
    }
}

fun main() {
    //bookCount()
    //getBooks()
    getCount()
}