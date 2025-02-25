import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LargeChip(
    title: String,
    isSelected: Boolean,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentSize()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}
