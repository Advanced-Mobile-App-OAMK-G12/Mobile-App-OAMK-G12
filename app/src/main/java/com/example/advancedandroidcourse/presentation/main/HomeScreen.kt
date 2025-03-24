package com.example.advancedandroidcourse.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.advancedandroidcourse.presentation.auth.AuthState
import com.example.advancedandroidcourse.presentation.auth.AuthViewModel
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.presentation.composables.SearchBar
import com.example.advancedandroidcourse.presentation.composables.BottomBar
import com.example.advancedandroidcourse.ui.theme.LogoColor

@Composable
fun HomeScreen(modifier : Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel){
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

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
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "easy Finn Logo",
                modifier = Modifier.size(80.dp)
            )
            SearchBar(
                value = searchValue,
                onValueChange = { searchValue = it },
                iconRes = R.drawable.search
            )
        }
        TextButton(onClick = {
            authViewModel.signout()
        }) {
            Text(text = "Sign out")
        }
        BottomBar(navController = navController)
    }
}