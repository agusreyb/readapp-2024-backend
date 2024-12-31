package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.*
import java.time.LocalDate

val poliglota = Poliglota()
val precavido = Precavido()
val leedor = Leedor()
val nativista = Nativista()
val calculador = Calculador()
val demandante = Demandante()
val experimentado = Experimentado()
val cambiante = Cambiante()
//val multiple = Multiple()



data class UsuarioActualizarDTO (
    var id: Int,
    var nombre: String,
    var apellido: String,
    var username: String,
    var mail: String,
    var fechaNacimiento: LocalDate,
    var vPromedio: Int,
    var formaDeLeer: FormaDeLeer,
    var perfilLista: List<String>,
    var librosLeidos: List<Libro>,
    var imgperfil: String,
    )



data class DatosUsuarioDTO(
    var id: Int,
    var nombre: String,
    var apellido: String,
    var username: String,
    var mail: String,
    var fechaNacimiento: LocalDate,
    var vPromedio: Int,
    var formaDeLeer: FormaDeLeer,
    var perfilLista: List<Perfil>,
    var librosLeidos: List<Libro>,
    var librosPorLeer: List<Libro>,
    var autoresPreferidos: List<Autor>,
    var amigos : List<Int>,
    var cantVecesLeido: Map<Int, Int>,
    var imgperfil: String,
)

data class DatosAmigosDTO(
    var id: Int,
    var nombre: String,
    var apellido: String,
    var username: String,
    var imgperfil: String
)

data class ActualizarLibrosDTO(
    val librosPorLeer: List<Int>,
    val librosAEliminar: List<Int>
)

fun Usuario.resumenAmigosDTO() = DatosAmigosDTO(id, nombre, apellido, username, imgperfil)



fun Usuario.resumenDatosDTO() =
    DatosUsuarioDTO( id, nombre, apellido, username, mail, fechaNacimiento,
        vPromedio.toInt(), formaDeLeer, perfilLista, librosLeidos.toList(), librosPorLeer.toList(), autoresPreferidos.toList(),
        obtenerIdAmigos(this), cantVecesLeido.toMap(), imgperfil
    )



fun obtenerIdAmigos(usuario: Usuario): List<Int>{
    val idAmigos = usuario.amigos.map{ amigo -> amigo.id}
    return idAmigos
}


