package nox.uala.challenge.features.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nox.uala.challenge.R

@Composable
fun LoadingLayout() {
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column {
            CircularProgressIndicator(
                modifier = androidx.compose.ui.Modifier.size(48.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.loading),
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
        }

    }
}