package br.thiago.habitflowapp.domain.model

/**
 * Enum representando os dias da semana.
 */
enum class WeekDay(val shortName: String, val fullName: String) {
    SUNDAY("D", "Domingo"),
    MONDAY("S", "Segunda"),
    TUESDAY("T", "Terça"),
    WEDNESDAY("Q", "Quarta"),
    THURSDAY("Q", "Quinta"),
    FRIDAY("S", "Sexta"),
    SATURDAY("S", "Sábado");

    companion object {
        val allDays = values().toList()
    }
}