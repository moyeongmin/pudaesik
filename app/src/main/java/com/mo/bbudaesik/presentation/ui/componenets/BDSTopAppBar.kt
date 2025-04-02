package com.mo.bbudaesik.presentation.ui.componenets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mo.bbudaesik.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BDSTopAppBar(showDialog: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "뿌대식",
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.logo),
                contentDescription = "logo",
                tint = Color.Unspecified,
                modifier =
                    Modifier
                        .size(56.dp)
                        .padding(start = 10.dp),
            )
        },
        actions = {
            IconButton(
                onClick = { showDialog() },
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "search",
                    modifier =
                        Modifier
                            .size(36.dp)
                            .padding(end = 10.dp),
                    tint = Color(0xFF00AAFF),
                )
            }
        },
    )
}

@Preview
@Composable
private fun BDSTopAppBarPreview() {
    BDSTopAppBar(
        showDialog = {},
    )
}
