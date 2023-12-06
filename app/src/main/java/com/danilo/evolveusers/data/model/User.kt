package com.danilo.evolveusers.data.model

import java.sql.Timestamp

data class User (
    var nome: String,
    var username: String,
    var password: String,
    var foto: String,
    var endereco: String,
    var email: String,
    var dataNascimento: Timestamp,
    var sexo: String,
    var cpfCnpj: String,
)