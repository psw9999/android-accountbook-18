package com.psw9999.android_accountbook_18.ui.configuration.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psw9999.android_accountbook_18.R

@Composable
fun ConfigurationAppbar(
    title: String
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                text = title,
                color = colorResource(R.color.purple)
            )
        }

        Divider(thickness = 1.dp, color = colorResource(R.color.purple))
    }
}
