package org.example.kotlinCode

import java.sql.Date
import java.sql.DriverManager

fun date() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL," +
                    "dt DATE)")
            //this is an escape sequence when you insert date in table = {d '2024-01-10'}
            stat.executeUpdate("INSERT INTO Books(name,dt) VALUES('Java',{d '2024-01-10'})")
        }
        conn.prepareStatement("INSERT INTO Books(name,dt) VALUES('Man in labirint', ?)").use { prepStat ->
            prepStat.setDate(1,Date(1733244646598L))
            prepStat.execute()
            println(prepStat)

            //reading date from DB
            prepStat.executeQuery("SELECT dt FROM Books WHERE name = 'Java'").use { resulSet ->
                while (resulSet.next()) {
                    println(resulSet.getDate("dt"))
                }
            }
        }
    }
}

fun main(){
    date()
}