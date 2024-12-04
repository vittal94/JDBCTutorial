package org.example.kotlinCode

import java.io.File
import java.sql.DriverManager
import javax.imageio.ImageIO

fun write(){
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.createStatement().use { stat ->
            stat.executeUpdate("DROP TABLE IF EXISTS Books")
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
                    "id MEDIUMINT AUTO_INCREMENT PRIMARY KEY," +
                    "name CHAR(30) NOT NULL," +
                    "img BLOB)")
        }
        val image = ImageIO.read(File("cat.jpg"))
        val blob = conn.createBlob()
        blob.setBinaryStream(1).use {
            outputStream -> ImageIO.write(image,"jpg",outputStream)
        }
        conn.prepareStatement("INSERT INTO Books (name, img) VALUES(?,?)").use { prepStat ->
            prepStat.setString(1,"Man in labirint.")
            prepStat.setBlob(2,blob)
            prepStat.execute()
        }
//        conn.createStatement().use { stat ->
//            stat.executeQuery("SELECT * FROM Books").use { resultSet ->
//                while(resultSet.next()) {
//                    val blob1 = resultSet.getBlob("img")
//                    val image1 = ImageIO.read(blob1.binaryStream)
//                    val outputFile = File("saved.png")
//                    ImageIO.write(image1,"png",outputFile)
//                }
//            }
//        }
    }
}

fun read() {
    DriverManager.getConnection(url, username, password).use { conn ->
        conn.prepareStatement("SELECT img FROM Books WHERE name = ?").use { prepStat ->
            prepStat.setString(1,"Man in labirint.")
            prepStat.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val blob = resultSet.getBlob("img")
                    val image = ImageIO.read(blob.binaryStream)
                    val outFile = File("saved1.png")
                    ImageIO.write(image,"png", outFile)
                }
            }
        }
    }
}

fun main() {

    read()
}