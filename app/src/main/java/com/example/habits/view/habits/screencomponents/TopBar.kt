package com.example.habits.view.habits.screencomponents

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R

@Composable
fun TopBar(
    userName: String = "Ilias",
    @DrawableRes profileIcon: Int = R.drawable.ic_profile,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .padding(24.dp)
                .fillMaxWidth(),
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(text = "Hello, ")
            Text(text = "$userName!", color = colorResource(R.color.medium_priority))
        }
        Image(
            painter = painterResource(profileIcon),
            contentDescription = null,
            modifier = Modifier.size(34.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}
