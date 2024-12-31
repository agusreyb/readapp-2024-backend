package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Autor
import ar.edu.unsam.algo3.domain.Lenguaje

data class AutorDTO (val nombre: String, val apellido: String)

fun Autor.toDTO() = AutorDTO(nombre, apellido)

data class AutorResumenDTO(val id: Int, var nombre: String, var apellido: String, var lenguaNativa: Lenguaje)

fun Autor.toResumenDTO() = AutorResumenDTO(id, nombre, apellido, lenguaNativa)