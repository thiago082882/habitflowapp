package br.thiago.habitflowapp.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.ui.theme.onPrimaryContainerDark
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import kotlinx.coroutines.delay

@Composable
fun WelcomeDialog(
    message: String,
    onDismiss: () -> Unit
) {
    if (message.isEmpty()) return

    var displayText by remember { mutableStateOf("") }
    LaunchedEffect(message) {
        displayText = ""
        for (c in message) {
            displayText += c
            delay(30)
        }

        delay(5000)

        onDismiss()
    }


    val infiniteTransition = rememberInfiniteTransition()
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animação de entrada/saída
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -it / 2 },
            animationSpec = tween(700, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(700)),
        exit = slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(700, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(700))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(onPrimaryContainerDark, primaryContainerLight)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onDismiss() }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier
                            .size(32.dp)
                            .graphicsLayer(
                                scaleX = iconScale,
                                scaleY = iconScale
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = displayText,
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
