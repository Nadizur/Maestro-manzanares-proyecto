package com.example.proyectomanzanares

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        // Permitir la política de red en el hilo principal (no recomendado para producción)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        val tableLayout: TableLayout = findViewById(R.id.tableLayout)
        val buttonGoToInsertOrder = findViewById<Button>(R.id.buttonGoToInsertOrder)
        val buttonDeleteOrder = findViewById<Button>(R.id.buttonDeleteOrder)
        val buttonEditOrder = findViewById<Button>(R.id.buttonEditOrder)

        val dbHelper = DbHelper(this)
        val db = dbHelper.readableDatabase

        // Consulta a la base de datos para obtener las órdenes de venta
        val cursor: Cursor = db.rawQuery(
            """SELECT SalesOrders.OrderID, SalesOrders.OrderDate, 
                      SUM(SalesOrderDetails.Quantity * SalesOrderDetails.UnitPrice) AS TotalDue 
               FROM SalesOrders 
               INNER JOIN SalesOrderDetails ON SalesOrders.OrderID = SalesOrderDetails.OrderID 
               GROUP BY SalesOrders.OrderID, SalesOrders.OrderDate""",
            null
        )

        // Recorre los resultados y agrégalos al TableLayout
        if (cursor.moveToFirst()) {
            do {
                val orderId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderID"))
                val orderDate = cursor.getString(cursor.getColumnIndexOrThrow("OrderDate"))
                val totalDue = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalDue"))

                val tableRow = TableRow(this)
                val textViewOrderId = TextView(this)
                val textViewOrderDate = TextView(this)
                val textViewTotalDue = TextView(this)

                textViewOrderId.text = orderId.toString()
                textViewOrderDate.text = orderDate
                textViewTotalDue.text = String.format("%.2f", totalDue)

                tableRow.addView(textViewOrderId)
                tableRow.addView(textViewOrderDate)
                tableRow.addView(textViewTotalDue)

                tableLayout.addView(tableRow)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        // Navegar a com.example.proyectomanzanares.InsertOrderActivity
        buttonGoToInsertOrder.setOnClickListener {
            val intent = Intent(this, InsertOrderActivity::class.java)
            startActivity(intent)
        }

        // Navegar a com.example.proyectomanzanares.InsertOrderActivity
        buttonDeleteOrder.setOnClickListener {
            val intent = Intent(this, DeleteOrderActivity::class.java)
            startActivity(intent)
        }

        // Navegar a com.example.proyectomanzanares.InsertOrderActivity
        buttonEditOrder.setOnClickListener {
            val intent = Intent(this, ModifyOrderActivity::class.java)
            startActivity(intent)
        }
    }
}