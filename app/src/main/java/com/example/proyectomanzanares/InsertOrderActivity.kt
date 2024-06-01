package com.example.proyectomanzanares

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InsertOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_main_xml)

        val dbHelper = DbHelper(this)
        val db = dbHelper.writableDatabase

        val editTextCustomerID = findViewById<EditText>(R.id.editTextCustomerID)
        val editTextOrderDate = findViewById<EditText>(R.id.editTextOrderDate)
        val editTextProductID = findViewById<EditText>(R.id.editTextProductID)
        val editTextQuantity = findViewById<EditText>(R.id.editTextQuantity)
        val editTextUnitPrice = findViewById<EditText>(R.id.editTextUnitPrice)
        val buttonInsertOrder = findViewById<Button>(R.id.buttonInsertOrder)

        buttonInsertOrder.setOnClickListener {
            val customerID = editTextCustomerID.text.toString().toIntOrNull()
            val orderDate = editTextOrderDate.text.toString()
            val productID = editTextProductID.text.toString().toIntOrNull()
            val quantity = editTextQuantity.text.toString().toIntOrNull()
            val unitPrice = editTextUnitPrice.text.toString().toDoubleOrNull()

            if (customerID != null && orderDate.isNotEmpty() && productID != null && quantity != null && unitPrice != null) {
                db.execSQL(
                    """INSERT INTO SalesOrders (CustomerID, OrderDate) VALUES (?, ?);""",
                    arrayOf(customerID, orderDate)
                )

                val cursor = db.rawQuery("SELECT last_insert_rowid()", null)
                var orderID: Int? = null
                if (cursor.moveToFirst()) {
                    orderID = cursor.getInt(0)
                }
                cursor.close()

                if (orderID != null) {
                    db.execSQL(
                        """INSERT INTO SalesOrderDetails (OrderID, ProductID, Quantity, UnitPrice)
                           VALUES (?, ?, ?, ?);""",
                        arrayOf(orderID, productID, quantity, unitPrice)
                    )

                    Toast.makeText(this, "Order inserted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to retrieve Order ID", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
