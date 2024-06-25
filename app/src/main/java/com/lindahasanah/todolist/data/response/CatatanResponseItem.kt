package com.lindahasanah.todolist.data.response

import com.google.gson.annotations.SerializedName

data class CatatanResponseItem(

	@field:SerializedName("task")
	val task: String,

	@field:SerializedName("id_task")
	val idTask: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("tanggal")
	val tanggal: String
)