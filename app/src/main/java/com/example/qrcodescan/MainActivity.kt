package com.example.qrcodescan

import android.content.res.Resources.Theme
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrcodescan.ui.theme.QRCodeScanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeScanTheme {

                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainScreen( )
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column {
        TopAppBar()
        Bottom()

    }
}

@Composable
fun TopAppBar(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Messages") },
                actions = {
                    Icon(modifier =Modifier, imageVector = Icons.Default.Person, contentDescription = "Favorite")
                }
            )
        },
        backgroundColor = Color(139, 240, 213),
        bottomBar = {
            Bottom()
        }
    ) {
    }
}

@Composable
fun Bottom() {
    val padding = 86.dp
    val borderWidth = 4.dp
    val color = Color.Black
    val shape1 = RoundedCornerShape(44.dp)
    val height = 48.dp
    Row(
        modifier = Modifier
            .padding(padding)
            .border(borderWidth, color, shape1)
            .width(200.dp)
            .height(height),
    ) {
        var bitmap  by remember{ mutableStateOf<Bitmap?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()){
            bitmap = it
        }
        IconButton(onClick = { launcher.launch() }) {
            Image(painter = painterResource(id = R.drawable.camera), contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .graphicsLayer {
                        shape= RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                            )
                        clip=true
                    }
                    .background(Color.White)
            )
        }
        bitmap?.let {
            Image(bitmap = bitmap?.asImageBitmap()!!, contentDescription = "")
        }
        val context = LocalContext.current
        var imageUri by remember { mutableStateOf<Uri?>(null)}
        var bitmap1 by remember{ mutableStateOf<Bitmap?>(null)}

        val launcher1 = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()){
            imageUri = it
        }
        IconButton(onClick = { launcher1.launch("image/*") }) {
            Image(painter = painterResource(id = R.drawable.gallery), contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .graphicsLayer {
                        shape= RoundedCornerShape(

                            topEnd = 20.dp,
                            bottomEnd = 20.dp,

                        )
                        clip=true
                    }

                    .background(Color.White)
            )
        }
        imageUri?.let {

            bitmap1 = if(Build.VERSION.SDK_INT < 28){
                MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            }else {
                val source = ImageDecoder.createSource(context.contentResolver,it)
                ImageDecoder.decodeBitmap(source)
            }
            Image(bitmap = bitmap1?.asImageBitmap()!!, contentDescription = ""  )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QRCodeScanTheme {
        MainScreen()
    }
}


