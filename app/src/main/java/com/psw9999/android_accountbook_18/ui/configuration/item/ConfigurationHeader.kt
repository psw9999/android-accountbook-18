package com.psw9999.android_accountbook_18.ui.configuration.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psw9999.android_accountbook_18.R

@Composable
fun ConfigurationHeader (
    title : String
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        //Spacer(modifier = Modifier.height(24.dp))
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(R.color.lightPurple)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(thickness = 1.dp, color = colorResource(R.color.purple_40))
    }
}