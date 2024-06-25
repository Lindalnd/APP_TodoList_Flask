package com.lindahasanah.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lindahasanah.todolist.R
import com.lindahasanah.todolist.data.network.ApiConfig
import com.lindahasanah.todolist.data.response.CatatanResponseItem
import com.lindahasanah.todolist.databinding.ActivityHomeBinding
import com.lindahasanah.todolist.ui.adapter.CatatanAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var catatanAdapter: CatatanAdapter
    private lateinit var rvTask: RecyclerView
    private var catatanList: MutableList<CatatanResponseItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
        }
        rvTask = binding.rvData
        rvTask.layoutManager = LinearLayoutManager(this)
        catatanAdapter = CatatanAdapter(this, catatanList)
        rvTask.adapter = catatanAdapter
        catatanAdapter.notifyDataSetChanged()

        showTask()
    }

    private fun showTask(){
        val client =  ApiConfig.getApiService().getCatatan()
        client.enqueue(object : Callback<List<CatatanResponseItem>>{
            override fun onResponse(
                call: Call<List<CatatanResponseItem>>,
                response: Response<List<CatatanResponseItem>>
            ) {
                if (response.isSuccessful){
                    val catatanResponseItems = response.body()
                    catatanResponseItems?.let {
                        catatanList.clear()
                        catatanList.addAll(it)
                        catatanAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Gagal memuat catatan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CatatanResponseItem>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}



