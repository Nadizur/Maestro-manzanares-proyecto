package com.example.proyectomanzanares


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.proyectomanzanares.data.model.OrderDetails

class DbHelper (context: Context): SQLiteOpenHelper(context, dbname, factory, version) {
    companion object {
        internal val dbname = "SqlDB"
        internal val factory = null
        internal val version = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """CREATE TABLE IF NOT EXISTS Customers (
                CustomerID INTEGER PRIMARY KEY,
                FirstName TEXT,
                LastName TEXT,
                Email TEXT,
                Phone TEXT
            );"""
        )

        db?.execSQL(
            """CREATE TABLE IF NOT EXISTS Products (
                ProductID INTEGER PRIMARY KEY,
                ProductName TEXT,
                UnitPrice REAL
            );"""
        )

        db?.execSQL(
            """CREATE TABLE IF NOT EXISTS SalesOrders (
                OrderID INTEGER PRIMARY KEY,
                CustomerID INTEGER,
                OrderDate DATE,
                FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
            );"""
        )

        db?.execSQL(
            """CREATE TABLE IF NOT EXISTS SalesOrderDetails (
                OrderDetailID INTEGER PRIMARY KEY,
                OrderID INTEGER,
                ProductID INTEGER,
                Quantity INTEGER,
                UnitPrice REAL,
                FOREIGN KEY (OrderID) REFERENCES SalesOrders(OrderID),
                FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
            );"""
        )

        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS SalesOrderDetails")
        db?.execSQL("DROP TABLE IF EXISTS SalesOrders")
        db?.execSQL("DROP TABLE IF EXISTS Products")
        db?.execSQL("DROP TABLE IF EXISTS Customers")
        onCreate(db)
    }

    private fun insertSampleData(db: SQLiteDatabase?) {
        // Insertar datos de clientes
        db?.execSQL(
            """INSERT INTO Customers (FirstName, LastName, Email, Phone)
                VALUES ('Orlando', 'Gee', 'orlando0@adventure-works.com', '245-555-0112'),
                       ('Keith', 'Harris', 'keith0@adventure-works.com', '245-555-0123'),
                       ('Donna', 'Carreras', 'donna0@adventure-works.com', '245-555-0148'),
                       ('Janet', 'Gates', 'janet1@adventure-works.com', '245-555-0173');"""
        )

        // Insertar datos de productos
        db?.execSQL(
            """INSERT INTO Products (ProductName, UnitPrice)
                VALUES ('Adjustable Race', 217.22),
                       ('Bearing Ball', 15.75),
                       ('BB Ball Bearing', 10.65),
                       ('Headset Ball Bearings', 5.32),
                       ('Blade', 203.49),
                       ('LL Crankarm', 77.50);"""
        )

        // Insertar datos de órdenes de venta
        db?.execSQL(
            """INSERT INTO SalesOrders (CustomerID, OrderDate)
                VALUES (1, '2024-05-01'),
                       (2, '2024-05-15'),
                       (3, '2024-05-20'),
                       (4, '2024-05-25');"""
        )

        // Insertar detalles de órdenes de venta
        db?.execSQL(
            """INSERT INTO SalesOrderDetails (OrderID, ProductID, Quantity, UnitPrice)
                VALUES (1, 1, 2, 217.22),
                       (1, 2, 5, 15.75),
                       (2, 3, 3, 10.65),
                       (2, 4, 10, 5.32),
                       (3, 5, 1, 203.49),
                       (3, 6, 2, 77.50),
                       (4, 1, 4, 217.22),
                       (4, 2, 3, 15.75);"""
        )
    }


    fun getOrderDetails(orderId: Int): OrderDetails? {
        val db = this.readableDatabase
        val query = """
        SELECT SO.OrderID, SO.OrderDate, SO.Quantity, SO.UnitPrice, 
               P.ProductID, P.ProductName, 
               C.CustomerID, C.CustomerName 
        FROM SalesOrders SO
        INNER JOIN Products P ON SO.ProductID = P.ProductID
        INNER JOIN Customers C ON SO.CustomerID = C.CustomerID
        WHERE SO.OrderID = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(orderId.toString()))

        var orderDetails: OrderDetails? = null

        try {
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndexCustomerID = cursor.getColumnIndexOrThrow("CustomerID")
                val columnIndexOrderDate = cursor.getColumnIndexOrThrow("OrderDate")
                val columnIndexProductID = cursor.getColumnIndexOrThrow("ProductID")
                val columnIndexQuantity = cursor.getColumnIndexOrThrow("Quantity")
                val columnIndexUnitPrice = cursor.getColumnIndexOrThrow("UnitPrice")

                val customerId = cursor.getInt(columnIndexCustomerID)
                val orderDate = cursor.getString(columnIndexOrderDate)
                val productId = cursor.getInt(columnIndexProductID)
                val quantity = cursor.getInt(columnIndexQuantity)
                val unitPrice = cursor.getDouble(columnIndexUnitPrice)

                orderDetails = OrderDetails(customerId, orderDate, productId, quantity, unitPrice)
            }
        } catch (e: IllegalArgumentException) {
            // Manejar la excepción si una columna no existe en el cursor
            e.printStackTrace()
            println(e.printStackTrace().toString())

        } finally {
            cursor?.close()
        }

        return orderDetails
    }

}
