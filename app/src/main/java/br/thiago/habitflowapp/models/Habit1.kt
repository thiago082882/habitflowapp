package br.thiago.habitflowapp.models

import br.thiago.habitflowapp.domain.model.FrequencyType
import java.util.UUID

/**
 * Modelo de Dados principal para representar um Hábito no HabitFlow.
 * * @property id Identificador único do hábito (UUID para evitar colisões).
 * @property name Nome do hábito (Ex: "Beber Água").
 * @property description Descrição detalhada do hábito (Opcional).
 * @property goal Meta diária associada (Ex: "8 copos", "30 minutos").
 * @property streak Sequência atual de dias concluídos.
 * @property completed Status de conclusão para o dia atual.
 * @property frequency Tipo de frequência do hábito (DAILY, WEEKLY, SPECIFIC).
 * @property reminderTime Horário do lembrete (Ex: "08:00").
 * @property isReminderActive Indica se o lembrete está ativado.
 * @property selectedDays Array booleano de 7 posições (Domingo a Sábado) para frequência SPECIFIC.
 */
data class Habit1(

    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val goal: String? = null,
    val streak: Int = 0,
    val completed: Boolean = false,
    val frequency: FrequencyType,
    val reminderTime: String = "",
    val isReminderActive: Boolean = true,
    // [Dom, Seg, Ter, Qua, Qui, Sex, Sab]. True se o dia estiver selecionado.
    val selectedDays: List<Boolean> = listOf(true, true, true, true, true, true, true)
)
