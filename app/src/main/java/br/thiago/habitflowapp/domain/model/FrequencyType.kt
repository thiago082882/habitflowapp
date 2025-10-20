package br.thiago.habitflowapp.domain.model

/**
 * Define os tipos de frequência que um hábito pode ter.
 * Isso garante a segurança de tipos (Type Safety) no Kotlin.
 */
enum class FrequencyType {
    DAILY,
    WEEKLY,
    SPECIFIC
}