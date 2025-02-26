package com.candra.chillivision.data.response

import android.os.Parcelable
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetDetail
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListHistorySubscriptionResponse(

	@field:SerializedName("data")
	val data: List<ListHistorySubscription?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ListHistorySubscription(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("subscriptions")
	val subscriptions: SubscriptionsGetDetail? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null,

	@field:SerializedName("subscription_id")
	val subscriptionId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("payment_method")
	val paymentMethod: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("image_transaction")
	val imageTransaction: String? = null
) : Parcelable
