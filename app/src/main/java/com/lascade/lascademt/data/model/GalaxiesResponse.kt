package com.lascade.lascademt.data.model

import com.google.gson.annotations.SerializedName

data class GalaxiesResponse(

	@field:SerializedName("result")
	val result: List<GalaxyItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Any? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class GalaxyItem(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("image-url")
	val imageUrl: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
