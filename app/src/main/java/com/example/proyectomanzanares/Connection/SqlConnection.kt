package com.example.proyectomanzanares.Connection

import java.sql.Connection
import java.sql.DriverManager

object SqlConnection {
    fun getConnection(): Connection {
        Class.forName("net.sourceforge.jtds.jdbc.Driver")

        return DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.100.4:1433/AdventureWorks2022", "sa", "hexa")
    }
}