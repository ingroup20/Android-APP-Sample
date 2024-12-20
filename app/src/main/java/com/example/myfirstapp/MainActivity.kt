package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfirstapp.ui.theme.MyFirstAppTheme
//------------------------
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// 常量定義，方便日後國際化或維護
private const val GREETING_MESSAGE = "Hello"
private const val BUTTON_TEXT = "Greet Me"
private const val PLACEHOLDER_TEXT = "Enter your name"


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstAppTheme { //定整個應用程式的外觀風格
                // Scaffold 是Jetpack Compose 提供的一個基礎佈局，常用來建立一個標準的應用程式頁面結構，支援頂部欄（TopBar）、底部欄（BottomBar）、浮動按鈕（FAB）等元素。
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->//填滿整個螢幕大小
                    MyApp(Modifier.padding(innerPadding))

                }
                PhotoUploaderScreen()

            }
        }
    }
}

@Composable //描述 UI 的外觀和行為
fun MyApp(modifier: Modifier = Modifier) {  //可傳入參數，此案例沒使用
    // 使用 `remember` 來保持狀態
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf(GREETING_MESSAGE) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),// 外部傳入的 `modifier` 保持彈性
        verticalArrangement = Arrangement.spacedBy(16.dp) // 設定垂直間距
    ) {
        //輸入框
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (name.isEmpty()) {
                    Text(PLACEHOLDER_TEXT) // 使用常量
                }
                innerTextField() // 顯示輸入框內容
            }
        )

        //按鈕
        Button(
            onClick = { message = "$GREETING_MESSAGE, $name!" }, // 更新訊息
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(BUTTON_TEXT) // 使用常量
        }

        //顯示訊息
        Text(
            text = message,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MyFirstAppTheme {
        MyApp() //此案例沒傳入參數
    }
}

//------------------v1.0-------------------------
@Composable
fun PhotoUploaderScreen(viewModel: PhotoUploaderViewModel = viewModel()) {
    val imageUri by viewModel.imageUri.collectAsState()
    val serverResponse by viewModel.serverResponse.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.pickImage() }) {
            Text("從相簿選擇照片")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let {
            Text("已選擇圖片路徑：$it")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.uploadImageToServer() }) {
                Text("上傳圖片")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (serverResponse.isNotEmpty()) {
            Text("伺服器回應結果：")
            LazyColumn {
                items(serverResponse.size) { index ->
                    Text("項目 ${index + 1}: ${serverResponse[index]}")
                }
            }
        }
    }
}
