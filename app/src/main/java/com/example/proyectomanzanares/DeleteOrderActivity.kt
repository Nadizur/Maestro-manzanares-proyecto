package com.example.proyectomanzanares

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DeleteOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete_main_xml)

        val dbHelper = DbHelper(this)
        val db = dbHelper.writableDatabase
        val buttonDeleteOrder = findViewById<Button>(R.id.buttonDeleteOrder)
        val editTextOrderID = findViewById<EditText>(R.id.editTextOrderID)

        buttonDeleteOrder.setOnClickListener {
            val orderIDToDelete = editTextOrderID.text.toString().toIntOrNull()

            if (orderIDToDelete != null) {
                // Realizar la eliminación de la orden de la base de datos
                val rowsAffected = db.delete(
                    "SalesOrders",
                    "OrderID = ?",
                    arrayOf(orderIDToDelete.toString())
                )

                if (rowsAffected > 0) {
                    // La eliminación fue exitosa
                    Toast.makeText(this, "Order deleted successfully", Toast.LENGTH_SHORT).show()
                    // Limpiar los campos después de eliminar la orden
                    editTextOrderID.setText("")

                } else {
                    // No se pudo eliminar la orden
                    Toast.makeText(this, "Failed to delete order", Toast.LENGTH_SHORT).show()
                }
            } else {
                // El ID de la orden no es válido
                Toast.makeText(this, "Please enter a valid Order ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}