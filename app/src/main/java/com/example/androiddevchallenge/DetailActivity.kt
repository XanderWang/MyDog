/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.model.MyDog
import com.example.androiddevchallenge.ui.theme.MyTheme

class DetailActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DetailActivityxxxx"
    }

    private lateinit var selectedDog: MyDog
    private var selectedPosition = 0
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.e(TAG, "onCreate:$intent")
        val dog = intent.getParcelableExtra<MyDog>(SELECTED_DOG)
        selectedPosition = intent.getIntExtra(SELECTED_POSITION, 0)
        if (dog == null) {
            Toast.makeText(this, "There must be something wrong.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        selectedDog = dog
        Toast.makeText(this, "Dog name:${dog.name}", Toast.LENGTH_SHORT).show()
        setContent {
            MyTheme {
                MyDetail(dog) {
                    navigateBack()
                }
            }
        }
    }

    private fun navigateBack() {
        val intent = Intent()
        intent.putExtra(SELECTED_POSITION, selectedPosition)
        intent.putExtra(ADOPTED, selectedDog.adopted)
        setResult(RESULT_OK, intent)
        finish()
    }
}

@Composable
fun MyDetail(myDog: MyDog, onBack: () -> Unit) {
//    Surface(color = MaterialTheme.colors.background) {
//        Text(text = "Detail Activity")
//    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = myDog.name
                    )
                },
                backgroundColor = Color.Transparent, elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        val backIcon: Painter = painterResource(android.R.drawable.ic_delete)
                        Icon(painter = backIcon, contentDescription = "back")
                    }
                }
            )
        }
    ) {
//        MyDetail(myDog = myDog)
    }
}

@Composable
fun MyDetail(myDog: MyDog) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Avatar(
            avatar = myDog.avatarFilename,
            name = myDog.name
        )
        Spacer(
            modifier = Modifier.requiredHeight(26.dp)
        )
        Spacer(
            modifier = Modifier.requiredHeight(26.dp)
        )
        Introduction(
            introduction = myDog.introduction
        )
    }
}

@Composable
fun Avatar(avatar: String, name: String) {
    val imageIdentity = App.appContext.resources.getIdentifier(
        avatar, "drawable",
        App.appContext.packageName
    )
    val image: Painter = painterResource(imageIdentity)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = name,
            modifier = Modifier
                .requiredSize(150.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun Introduction(introduction: String) {
    Text(
        text = introduction,
        fontSize = 18.sp,
        style = MaterialTheme.typography.body1
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun DetailLightPreview() {
    MyTheme {
        MyDetail(MyDog("Eurasier", "eurasier", "eurasier", true)) {
        }
    }
}
