package com.example.habits.view.habits.screencomponents

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    userName: String = "Ilias",
    @DrawableRes profileIcon: Int = R.drawable.ic_profile,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        title = {
            Row {
                Text(text = "Hello, ")
                Text(text = "$userName!", color = colorResource(R.color.medium_priority))
            }
        },
        actions = {
            Image(
                painter = painterResource(profileIcon),
                contentDescription = "Profile",
                modifier = Modifier.size(34.dp)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}
