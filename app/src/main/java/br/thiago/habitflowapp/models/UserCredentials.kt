package br.thiago.habitflowapp.models

/**
 * Modelo de Dados para Autenticação.
 * Representa as credenciais (Email e Senha) para as telas de Login e Cadastro.
 * Adicionados 'name' e 'confirmPassword' para a tela de registro.
 */
data class UserCredentials(
    val email: String,
    val password: String,
    val confirmPassword: String? = null,
    val name: String? = null
)
