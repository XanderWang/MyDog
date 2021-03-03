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
package com.example.androiddevchallenge.repository

import com.example.androiddevchallenge.App
import com.example.androiddevchallenge.model.MyDog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

open class MyDogRepository {

    suspend fun getMyDogs(dataSource: MyDogDataSource) = withContext(Dispatchers.IO) {
        dataSource.loadData()
    }
}

interface MyDogDataSource {
    suspend fun loadData(): List<MyDog>
}

open class LocalMyDogSource : MyDogDataSource {
    override suspend fun loadData(): List<MyDog> = coroutineScope {
        var dataList = ArrayList<MyDog>()
        // 从网络获取或者本地获取
        val job = launch(Dispatchers.IO) {
            dataList = readData()
        }
        job.join()
        readData()
    }

    open fun readData(): ArrayList<MyDog> {
        var dataList = ArrayList<MyDog>()
        try {
            val assetsManager = App.appContext.assets
            val inputReader = InputStreamReader(assetsManager.open("dogs.json"))
            val jsonString = BufferedReader(inputReader).readText()
            val typeOf = object : TypeToken<List<MyDog>>() {}.type
            dataList = Gson().fromJson(jsonString, typeOf)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return dataList
    }
}
