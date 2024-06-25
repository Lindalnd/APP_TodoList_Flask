package com.lindahasanah.todolist.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lindahasanah.todolist.R
import com.lindahasanah.todolist.data.network.ApiConfig
import com.lindahasanah.todolist.data.response.CatatanResponseItem
import com.lindahasanah.todolist.data.response.MessageResponse
import com.lindahasanah.todolist.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatatanAdapter(private val context: Context, private val catatanList: MutableList<CatatanResponseItem>) :
    RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder>() {

    inner class CatatanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judulTask: TextView = itemView.findViewById(R.id.txtJudul)
        val statusTask: TextView = itemView.findViewById(R.id.txtStatus)
        val deskripsiTask: TextView = itemView.findViewById(R.id.txtKeterangan)
        val tanggalTask: TextView = itemView.findViewById(R.id.txtTanggal)
        val editTask: TextView = itemView.findViewById(R.id.txtEdit)
        val hapusTask: TextView = itemView.findViewById(R.id.txtHapus)

        init {
            editTask.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val catatan = catatanList[position]
                    val toEdit = Intent(context, MainActivity::class.java)
                    toEdit.putExtra("item_id", catatan.idTask)
                    context.startActivity(toEdit)
                }
            }
            hapusTask.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val catatan = catatanList[position]
                    // Panggil fungsi hapus item disini
                    hapusCatatan(catatan.idTask, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatatanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return CatatanViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatatanViewHolder, position: Int) {
        val catatan = catatanList[position]
        holder.judulTask.text = catatan.task
        holder.deskripsiTask.text = catatan.deskripsi
        holder.statusTask.text = catatan.status
        holder.tanggalTask.text = catatan.tanggal

        if (catatan.status == "Tuntas") {
            holder.statusTask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_green, 0, 0, 0)
        } else {
            holder.statusTask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_red, 0, 0, 0)
        }
    }

    override fun getItemCount() = catatanList.size

    // fungsi untuk menghapus catatan
    private fun hapusCatatan(idTask: Int, position: Int) {
        ApiConfig.getApiService().deleteCatatan(idTask).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Catatan berhasil dihapus" , Toast.LENGTH_SHORT).show()
                    // Refresh data setelah berhasil dihapus jika diperlukan
                    catatanList.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    Toast.makeText(context, "Gagal menghapus catatan" , Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}" , Toast.LENGTH_SHORT).show()
            }
        })
    }
}