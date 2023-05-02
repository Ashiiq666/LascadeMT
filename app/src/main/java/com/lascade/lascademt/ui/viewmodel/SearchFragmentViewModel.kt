package com.lascade.lascademt.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lascade.lascademt.data.model.GalaxiesResponse
import com.lascade.lascademt.data.network.NetworkStateManager
import com.lascade.lascademt.repository.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragmentViewModel : ViewModel() {
    private var galaxiesLiveData = MutableLiveData<NetworkStateManager<GalaxiesResponse>>()

    fun getGalaxies() {
        RetrofitInstance.api.getGalaxies().enqueue(object : Callback<GalaxiesResponse> {
            override fun onResponse(
                call: Call<GalaxiesResponse>,
                response: Response<GalaxiesResponse>
            ) {
                galaxiesLiveData.value = if (response.body() != null) {
                    NetworkStateManager.Success(response.body()!!)
                } else {
                    NetworkStateManager.Error("Error body null")
                }
            }

            override fun onFailure(call: Call<GalaxiesResponse>, t: Throwable) {
                Log.d("logThis", t.stackTraceToString())
                galaxiesLiveData.value = NetworkStateManager.Error(t.message.toString(), null, t)
            }
        })
    }

    fun observeGalaxiesLiveData(): LiveData<NetworkStateManager<GalaxiesResponse>> {
        return galaxiesLiveData
    }
}