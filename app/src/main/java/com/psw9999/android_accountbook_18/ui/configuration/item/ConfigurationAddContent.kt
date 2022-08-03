package com.psw9999.android_accountbook_18.ui.configuration.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psw9999.android_accountbook_18.R

@Composable
fun ConfigurationAddContent(
    type: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = "$type 추가하기",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(R.color.purple)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_property_1_plus),
                contentDescription = "Add Content",
                tint = colorResource(R.color.purple)
            )
        }
        Divider(thickness = 1.dp, color = colorResource(R.color.lightPurple))
    }
}