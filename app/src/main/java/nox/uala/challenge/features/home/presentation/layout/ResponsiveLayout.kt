package nox.uala.challenge.features.home.presentation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class DisplayType {
    PORTRAIT, LANDSCAPE
}

@Composable
fun detectDisplayType(): DisplayType {
    val configuration = LocalConfiguration.current
    return if (configuration.screenWidthDp > configuration.screenHeightDp) {
        DisplayType.LANDSCAPE
    } else {
        DisplayType.PORTRAIT
    }
}

@Composable
fun SplitScreenLayout(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    leftWeight: Float = 0.4f,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(leftWeight)
                .fillMaxSize()
                .padding(start = 12.dp, end = 2.dp, top = 4.dp, bottom = 4.dp)
        ) {
            leftContent()
        }
        
        Box(
            modifier = Modifier
                .weight(1f - leftWeight)
                .fillMaxSize()
                .padding(4.dp)
        ) {
            rightContent()
        }
    }
}
