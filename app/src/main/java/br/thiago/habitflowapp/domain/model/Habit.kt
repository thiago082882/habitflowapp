package br.thiago.habitflowapp.models

import br.thiago.habitflowapp.domain.model.FrequencyType
import com.google.gson.Gson
import java.util.UUID

/**
 * Modelo de Dados principal para representar um Hábito no HabitFlow.
 * @property id Identificador único do hábito (UUID para evitar colisões).
 * @property name Nome do hábito (Ex: "Beber Água").
 * @property description Descrição detalhada do hábito (Opcional).
 * @property goal Meta diária associada (Ex: "8 copos", "30 minutos").
 * @property streak Sequência atual de dias concluídos.
 * @property completed Status de conclusão para o dia atual.
 * @property frequency Tipo de frequência do hábito (DAILY, WEEKLY, SPECIFIC).
 * @property reminderTime Horário do lembrete (Ex: "08:00").
 * @property isReminderActive Indica se o lembrete está ativado.
 * @property selectedDays Array booleano de 7 posições (Domingo a Sábado) para frequência SPECIFIC.
 * @property userId ID do usuário proprietário do hábito.
 * @property createdAt Timestamp de criação do hábito.
 */
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val goal: String = "",
    val streak: Int = 0,
    val completed: Boolean = false,
    val frequency: FrequencyType = FrequencyType.DAILY,
    val reminderTime: String = "",
    val isReminderActive: Boolean = false,
    val selectedDays: List<Boolean> = listOf(true, true, true, true, true, true, true),
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toJson(): String = Gson().toJson(
        Habit(
            id = id,
            name = name,
            description = description,
            goal = goal,
            streak = streak,
            completed = completed,
            frequency = frequency,
            reminderTime = reminderTime,
            isReminderActive = isReminderActive,
            selectedDays = selectedDays,
            userId = userId,
            createdAt = createdAt
        )
    )

    companion object {
        fun fromJson(data: String): Habit1 = Gson().fromJson(data, Habit1::class.java)
    }
}