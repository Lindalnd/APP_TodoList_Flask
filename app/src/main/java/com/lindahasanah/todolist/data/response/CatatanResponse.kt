package com.lindahasanah.todolist.data.response

import com.google.gson.annotations.SerializedName

data class CatatanResponse(

	@field:SerializedName("CatatanResponse")
	val catatanResponse: List<CatatanResponseItem>
)
