package com.psw9999.android_accountbook_18.ui.configuration.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.dto.CategoryDto

@Composable
fun ConfigurationCategory(
    category: CategoryDto,
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
                text = category.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(R.color.purple)
            )

            Spacer(modifier = Modifier.weight(1f))

            Surface (
                shape = RoundedCornerShape(16.dp),
                color = Color(category.color),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    maxLines = 1,
                    color = colorResource(R.color.white),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Divider(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = colorResource(R.color.purple_40)
        )
    }
}