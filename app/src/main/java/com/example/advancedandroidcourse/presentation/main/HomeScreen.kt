package com.example.advancedandroidcourse.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advancedandroidcourse.presentation.composables.SearchBar
import com.example.advancedandroidcourse.ui.theme.LogoColor

@Composable
fun HomeScreen(modifier : Modifier = Modifier){
    var searchValue by remember { mutableStateOf("")}
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 6.dp, top = 42.dp),

        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ){
            Column (
                modifier = modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "easy",
                    color = LogoColor,
                    fontSize = 18.sp
                )
                Text(
                    text = "Finn",
                    color = LogoColor,
                    fontSize = 18.sp
                )
            }
            SearchBar(
                value = searchValue,
                onValueChange = { searchValue = it },
                icon = Icons.Default.Search
            )
        }
    }
}