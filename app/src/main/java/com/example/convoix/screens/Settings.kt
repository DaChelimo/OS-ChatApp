package com.example.convoix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.convoix.R

@Composable
fun Settings(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.blurry_gradient_haikei),
        contentDescription = "",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )
        SettingOption(
            title = "Customization",
            description = "Customize your chat screen",
            onClick = { navController.navigate("cus")},
            painter = painterResource(R.drawable.equalizer)
        )
        SettingOption(
            title = "Blocked Users",
            description = "View and manage blocked users",
            onClick = { navController.navigate("blck") },
            painter = painterResource(id = R.drawable.block_user_6380092)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SettingOption(
    painter: Painter,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .padding(vertical = 6.dp, horizontal = 16.dp)
        .clickable { onClick() }
        .background(
            Color.White.copy(alpha = 0.2f),
            RoundedCornerShape(12.dp)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(modifier = Modifier
            .padding(10.dp)
            .size(35.dp),
            painter = painter, contentDescription = null)
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp, top = 10.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }

}

@Preview
@Composable
private fun PreviewSettings() {
    Settings(navController = rememberNavController())
}
