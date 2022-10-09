package com.handysparksoft.valenciabustracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.handysparksoft.valenciabustracker.framework.permission.PermissionRationaleAction
import com.handysparksoft.valenciabustracker.ui.theme.ValenciaBusTrackerTheme

@Composable
fun PostNotificationPermissionRationaleScreen(
    permissionRationaleAction: (PermissionRationaleAction) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.il_bell_notification),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.padding(32.dp)
            )
            Text(text = stringResource(R.string.show_rationale_title), style = MaterialTheme.typography.titleLarge)
            Text(
                text = stringResource(R.string.show_rationale_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        Icon(
            painter = painterResource(R.drawable.il_bus_stop),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(48.dp))
        Row {
            TextButton(onClick = { permissionRationaleAction(PermissionRationaleAction.Skipped) }) {
                Text(text = stringResource(R.string.show_rationale_skip))
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { permissionRationaleAction(PermissionRationaleAction.Granted) }
            ) {
                Text(text = stringResource(R.string.show_rationale_accept))
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "es")
@Composable
internal fun PostNotificationPermissionRationaleScreenPreview() {
    ValenciaBusTrackerTheme {
        PostNotificationPermissionRationaleScreen(permissionRationaleAction = {})
    }
}
