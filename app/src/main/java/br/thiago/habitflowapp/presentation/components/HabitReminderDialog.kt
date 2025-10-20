package br.thiago.habitflowapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.ui.theme.onSurfaceVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceContainerLowestLight

@Composable
fun HabitReminderDialog(
    appName: String,
    timeLabel: String,
    title: String,
    message: AnnotatedString,
    onDismiss: () -> Unit,
    onPrimaryAction: (() -> Unit)? = null,
    onSecondaryAction: (() -> Unit)? = null,
    primaryActionLabel: String = "CONCLUIR",
    secondaryActionLabel: String = "ADIAR"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceContainerLowestLight, RoundedCornerShape(16.dp))
                    .border(1.dp, outlineVariantLight, RoundedCornerShape(16.dp))
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "$appName • Agendado para $timeLabel",
                    color = outlineLight,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )


                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 6.dp)
                )


                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = onSurfaceVariantLight,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                HorizontalDivider(
                    color = outlineVariantLight.copy(alpha = 0.5f)
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (onSecondaryAction != null) {
                        TextButton(onClick = onSecondaryAction) {
                            Text(
                                text = secondaryActionLabel.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (onPrimaryAction != null) {
                        TextButton(onClick = onPrimaryAction) {
                            Text(
                                text = primaryActionLabel.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color.Transparent
    )
}
/*
@Composable
fun HabitReminderDialog(
    habitName: String,
    reminderTime: String,
    onDismiss: () -> Unit,
    onSnooze: () -> Unit,
    onComplete: () -> Unit
) {
    AppNotificationDialog(
        appName = "HabitFlow",
        timeLabel = reminderTime,
        title = "⏰ Hora do Hábito!",
        message = buildAnnotatedString {
            append("Sua rotina está te chamando. Está na hora de ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("o $habitName")
            }
            append(".")
        },
        onDismiss = onDismiss,
        onPrimaryAction = onComplete,
        onSecondaryAction = onSnooze,
        primaryActionLabel = "CONCLUIR",
        secondaryActionLabel = "ADIAR"
    )
}

 */
@Preview(showBackground = true)
@Composable
fun HabitReminderDialogPreview() {
    HabitReminderDialog(
        appName = "HabitFlow",
        timeLabel = "10:00",
        title = "⏰ Hora do Hábito!",

        message = buildAnnotatedString {
            append("Sua rotina está te chamando. Está na hora de ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("o Hábito de Estudar")
            }
            append(".")
        },
        onDismiss = {},
        onPrimaryAction = {},
        onSecondaryAction = {},
        primaryActionLabel = "CONCLUIR",
        secondaryActionLabel = "ADIAR"


    )
}
