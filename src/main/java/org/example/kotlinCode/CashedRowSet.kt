package org.example.kotlinCode

import java.sql.DriverManager
import java.sql.ResultSet
import javax.sql.rowset.CachedRowSet
import javax.sql.rowset.RowSetFactory
import javax.sql.rowset.RowSetProvider

fun ordinaryRowSet() {
    fun getRowSet():ResultSet {
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
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
                ).use { modifiedStat ->
                    return modifiedStat.executeQuery("SELECT * FROM Books")
                }
            }
        }
    }

    //throw sql exception because resultSet were closed inside function
    //use instead cashedRowSet
    val resultSet = getRowSet()
    while (resultSet.next()) println(resultSet.getString("name"))
}

//the cashedRowSet you can pass between functions
fun cashedRowSet() {
    fun getCashRowSet(): CachedRowSet {
        DriverManager.getConnection(url, username, password).use { conn ->
            conn.createStatement().use { stat ->
                stat.executeUpdate("DROP TABLE IF EXISTS Books")
                stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "name VARCHAR(30) NOT NULL)")
                stat.executeUpdate("INSERT INTO Books(name) VALUES('MAN')")
                stat.executeUpdate("INSERT INTO Books(name) VALUES('JAVA')")
                stat.executeUpdate("INSERT INTO Books(name) VALUES('SPRING')")

                val factory: RowSetFactory = RowSetProvider.newFactory()
                val cashedRowSet = factory.createCachedRowSet()

                val statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)
                val resultSet = statement.executeQuery("SELECT * FROM Books")
                cashedRowSet.populate(resultSet)
                return cashedRowSet
            }
        }
    }

    val cashedRowSet = getCashRowSet()
    cashedRowSet.url = url
    cashedRowSet.username = username
    cashedRowSet.password = password
    cashedRowSet.command = "SELECT * FROM Books WHERE id = ?"
    cashedRowSet.setInt(1,1)
    cashedRowSet.pageSize = 20
    cashedRowSet.execute()
    do {
        while (cashedRowSet.next()) {
            println(cashedRowSet.getString("name"))
        }
    } while (cashedRowSet.nextPage())

    println("------------------")
    //modifying data

    getCashRowSet().apply {
        tableName = "Books"
        absolute(1)
        deleteRow()
        beforeFirst()
        while (next()) {
            println("${getInt(1)} ${getString(2)}")
        }
        acceptChanges(DriverManager.getConnection(
            org.example.kotlinCode.url,org.example.kotlinCode.username,org.example.kotlinCode.password))
    }

}

fun main() {
    //ordinaryRowSet()
    cashedRowSet()
}