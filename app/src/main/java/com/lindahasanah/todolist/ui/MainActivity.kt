package com.lindahasanah.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.lindahasanah.todolist.R
import com.lindahasanah.todolist.data.network.ApiConfig
import com.lindahasanah.todolist.data.response.CatatanResponseItem
import com.lindahasanah.todolist.data.response.MessageResponse
import com.lindahasanah.todolist.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var selectStatus: RadioGroup
    private var status: String = ""
    private lateinit var judulTask: TextInputEditText
    private lateinit var deskTask: TextInputEditText
    private lateinit var dateTask: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        judulTask = binding.edtJudul
        deskTask = binding.edtDescription
        selectStatus = binding.rgStatus
        selectStatus.setOnCheckedChangeListener { radioGroup, checkedId ->
            status = findViewById<RadioButton>(checkedId)?.text.toString()
        }

        val itemId = intent.getIntExtra("item_id", 0)
        if (itemId != 0) {
            loadCatatan(itemId)
        }

        binding.btSimpan.setOnClickListener {
            addTask()
        }
        binding.btEdit.setOnClickListener {
            editTask()
        }

        val dateTimeTextView: TextView = binding.txtTanggal
        val currentDateTime = getCurrentDateTime()
        dateTimeTextView.text = currentDateTime
        dateTask = dateTimeTextView.text.toString()
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy HH.mm", Locale("id", "ID"))
        return dateFormat.format(Date())
    }

    private fun addTask() {
        val judul = judulTask.text.toString()
        val deskripsi = deskTask.text.toString()

        val client = ApiConfig.getApiService().addcatatan(judul, deskripsi, status, dateTask)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>, response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        this@MainActivity, "Sukses menambahkan catatan", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity, "Catatan gagal ditambahkan", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editTask() {
        val itemId = intent.getIntExtra("item_id", 0)

        if (itemId != 0) {
            val judul = judulTask.text.toString()
            val deskripsi = deskTask.text.toString()

            val client = ApiConfig.getApiService().editcatatan(itemId, judul, deskripsi, status)
            client.enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>, response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            this@MainActivity, "Sukses mengedit catatan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity, "Gagal mengedit catatan", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadCatatan(itemId: Int) {
        val client = ApiConfig.getApiService().detailcatatan(itemId)
        client.enqueue(object : Callback<CatatanResponseItem> {
            override fun onResponse(
                call: Call<CatatanResponseItem>, response: Response<CatatanResponseItem>
            ) {
                if (response.isSuccessful) {
                    val catatan = response.body()
                    Toast.makeText(this@MainActivity, "$catatan", Toast.LENGTH_SHORT).show()
                    if (catatan != null) {
                        judulTask.setText(catatan.task)
                        deskTask.setText(catatan.deskripsi)
                        when (catatan.status) {
                            "Belum Tuntas" -> binding.rbBelumTuntas.isChecked = true
                            "Tuntas" -> binding.rbTuntas.isChecked = true
                            else -> binding.rbBelumTuntas.isChecked = true
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Gagal memuat catatan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CatatanResponseItem>, t: Throwable) {
                Log.e("MainActivity", "API call failed: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}