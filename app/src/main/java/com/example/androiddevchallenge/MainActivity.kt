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
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.model.MyDog
import com.example.androiddevchallenge.repository.LocalMyDogSource
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.purple200
import com.example.androiddevchallenge.ui.theme.purple500

const val SELECTED_DOG = "selected_dog"
const val SELECTED_POSITION = "selected_position"
const val ADOPTED = "adopted"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp { position, myDog ->
                    Intent(this, DetailActivity::class.java).apply {
                        putExtra(SELECTED_POSITION, position)
                        putExtra(SELECTED_DOG, myDog)
                        this@MainActivity.startActivity(this)
                    }
                }
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(onClick: (position: Int, myDog: MyDog) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar({
                Text(text = "My Dog")
            })
        }
    ) {
        DogList(myDogs = LocalMyDogSource().readData(), onClick = onClick)
    }
}

@Composable
fun DogList(myDogs: List<MyDog>, onClick: (position: Int, myDog: MyDog) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(Modifier.fillMaxWidth()) {
            itemsIndexed(myDogs) { position, myDog ->
                MyDogCard(position, myDog, onClick)
            }
        }
    }
}

@Composable
fun MyDogCard(position: Int, myDog: MyDog, onClick: (position: Int, myDog: MyDog) -> Unit) {
    Card(
        elevation = 3.dp,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .requiredHeight(180.dp)
            .clickable { onClick(position, myDog) }
    ) {
        MyDogCardView(avatar = myDog.avatarFilename, name = myDog.name)
    }
}

@Composable
fun MyDogCardView(avatar: String, name: String) {
    val imageIdentity = App.appContext.resources.getIdentifier(
        avatar, "drawable",
        App.appContext.packageName
    )
    val image: Painter = painterResource(imageIdentity)
    Image(
        painter = image,
        contentDescription = name,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Surface(
            color = purple200,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = name,
                color = purple500,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun MainLightPreview() {
    MyTheme {
        MyApp { position, myDog ->
            Log.d("dd", "$position , $myDog")
        }
    }
}
