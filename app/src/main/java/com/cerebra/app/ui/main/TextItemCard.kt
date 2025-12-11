package com.cerebra.app.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cerebra.app.data.local.entity.TextEntity
import org.json.JSONObject

@Composable
fun TextItemCard(text: TextEntity, onClick: () -> Unit) {
    val progressPercent = try {
        if (text.progress.startsWith("{")) {
            JSONObject(text.progress).optInt("percent", 0)
        } else {
            text.progress.toIntOrNull() ?: 0
        }
    } catch (e: Exception) {
        0
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progressPercent / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            Text("$progressPercent%", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.End))
        }
    }
}
