package com.example.habits.view.habits.screencomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.data.localdatasource.quotes.QuoteEntity
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun MotivationalQuoteComponent(
    quote: QuoteEntity?,
    modifier: Modifier = Modifier
) {
    quote?.let {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = Color.White,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "\"${it.text}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "- ${it.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F7FB)
@Composable
private fun MotivationalQuoteComponentPreview() {
    HabitsTheme {
        val previewQuote = QuoteEntity(
            id = 1,
            text = "We are what we repeatedly do. Excellence, then, is not an act, but a habit.",
            author = "Aristotle"
        )
        MotivationalQuoteComponent(quote = previewQuote)
    }
}