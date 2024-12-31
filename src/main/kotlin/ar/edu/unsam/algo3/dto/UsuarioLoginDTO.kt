package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Usuario


data class UsuarioLoginDTO(
    val mail: String,
    val contrasenia: String
)


data class UsuarioIDDTO(
    val id: Int
)

data class UsuarioNombreDTO(val nombre: String, val apellido: String)

fun Usuario.toIdDTO() = UsuarioIDDTO(id)
