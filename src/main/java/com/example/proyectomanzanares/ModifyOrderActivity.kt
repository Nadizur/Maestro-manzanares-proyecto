package com.example.proyectomanzanares

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ModifyOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_main_xml)

        val dbHelper = DbHelper(this)

        val buttonSearchOrder = findViewById<Button>(R.id.buttonSearchOrder)
        val editTextOrderID = findViewById<EditText>(R.id.editTextOrderID)
        val buttonEditOrder = findViewById<Button>(R.id.buttonSaveChanges)

        val editTextCustomerID = findViewById<EditText>(R.id.editTextCustomerID)
        val editTextOrderDate = findViewById<EditText>(R.id.editTextOrderDate)
        val editTextProductID = findViewById<EditText>(R.id.editTextProductID)
        val editTextQuantity = findViewById<EditText>(R.id.editTextQuantity)
        val editTextUnitPrice = findViewById<EditText>(R.id.editTextUnitPrice)

        buttonSearchOrder.setOnClickListener {
            val orderId = editTextOrderID.text.toString().toIntOrNull()
            if (orderId != null) {
                val orderDetails = dbHelper.getOrderDetails(orderId)
                if (orderDetails != null) {
                    // Llenar los EditText con los detalles de la orden recuperados
                    editTextCustomerID.setText(orderDetails.customerId.toString())
                    editTextOrderDate.setText(orderDetails.orderDate)
                    editTextProductID.setText(orderDetails.productId.toString())
                    editTextQuantity.setText(orderDetails.quantity.toString())
                    editTextUnitPrice.setText(orderDetails.unitPrice.toString())
                } else {
                    // La orden con el ID dado no existe
                    Toast.makeText(this, "Order with ID $orderId not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                // ID de orden inválido
                Toast.makeText(this, "Please enter a valid Order ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}