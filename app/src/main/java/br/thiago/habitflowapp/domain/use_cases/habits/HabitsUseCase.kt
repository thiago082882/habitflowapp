package br.thiago.habitflowapp.domain.use_cases.habits

data class HabitsUseCases(
    val create: CreateHabit,
    val getHabits: GetHabits,
    val getHabitsByIdUser: GetHabitsByIdUser,
    val deleteHabit: DeleteHabit,
    val updateHabit: UpdateHabit,
    val toggleHabitCompletion: ToggleHabitUseCase,
    val getHabitById: GetHabitById
)



