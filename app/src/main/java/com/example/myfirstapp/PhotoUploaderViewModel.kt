package com.example.myfirstapp

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

class PhotoUploaderViewModel(application: Application) : AndroidViewModel(application) {

    // MutableStateFlow 用於存儲選擇的圖片 URI
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    // MutableStateFlow 用於存儲伺服器回應結果
    private val _serverResponse = MutableStateFlow<List<String>>(emptyList())
    val serverResponse: StateFlow<List<String>> = _serverResponse

    // Retrofit API 物件，這裡的 baseUrl 是伺服器端點的地址
    private val api = Retrofit.Builder()
        .baseUrl("https://yourserver.com/api/") // 替換為你的伺服器地址
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // 使用 ImagePicker 來從相冊中選擇圖片，並將選擇結果存儲到 _imageUri 中
    fun pickImage() {
        ImagePicker.with(getApplication())
            .galleryOnly() // 指定僅限於相冊選擇圖片
            .createIntent { intent ->
                (getApplication<Application>()).startActivity(intent)
            }
    }

    // 將選擇的圖片上傳至伺服器
    fun uploadImageToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            imageUri.value?.let { uri ->
                val file = File(uri.path!!)
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestBody)

                try {
                    // 呼叫伺服器上的 uploadImage 方法
                    val response = api.uploadImage(body)
                    withContext(Dispatchers.Main) {
                        // 將伺服器回應的資料更新到 _serverResponse 中
                        _serverResponse.value = response.data
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

// 定義 ApiService 接口，用於與伺服器進行圖片上傳通訊
interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ApiResponse
}

// 定義 ApiResponse 資訊類型，用於存儲伺服器回應的資料
data class ApiResponse(
    val data: List<String>
)