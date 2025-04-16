package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    iconRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onSearchClick
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "SearchAction"
                )
                //Icon(painter = painterResource(id = iconRes), contentDescription = "Search Icon")
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions =  KeyboardActions(
            onSearch = {
                onSearchClick()
            }
        )
    )
}