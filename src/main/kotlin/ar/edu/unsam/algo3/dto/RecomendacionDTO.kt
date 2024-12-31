package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Recomendacion
import ar.edu.unsam.algo3.domain.Valoracion
import java.time.LocalDate

data class CardRecomendacionDTO (val titulo: String, val creador: UsuarioIDDTO, val descripcion: String, val libros: List<LibroTituloDTO>,  val avgValoraciones: Number){}

fun Recomendacion.toCardRecomendacionDTO(): CardRecomendacionDTO {
    val libros = libros.map { libro -> libro.tituloDTO() }.toList()
    return CardRecomendacionDTO(titulo, creador.toIdDTO(),descripcion, libros, avgValoraciones)
}

data class ResumenRecomendacionCompletaDTO (
    val id : Number,
    val creadorId : Number, //NO SE DEBERIA MANDAR, CHEQUEAR EN EL BACK
    val titulo : String,
    val esPublica : Boolean,
    val descripcion : String,
    val lista_libros : List<LibroResumenCompletoDTO>,
    val valoraciones : List<ValoracionDTO>,
    var puedeEditar : Boolean,
    var puedeValorar : Boolean,
    val avgValoraciones: Number
    ){}

data class RecomendacionUpdateDTO (
    val id : Number,
    val creadorId : Number, //NO SE DEBERIA MANDAR, CHEQUEAR EN EL BACK
    val titulo : String,
    val esPublica : Boolean,
    val descripcion : String,
    val lista_libros : List<Number>
){}

data class ValoracionDTO(
    val creador_nombre: String,
    val creador_apellido: String,
    val img_perfil: String,
    val valor : Double,
    val comentario: String,
    val fecha: LocalDate
){}

data class ValoracionNueva(
    val creadorId: Int,
    val valor : Double,
    val comentario: String,
){}

fun Valoracion.valoracionToDTO() = ValoracionDTO(creador.nombre, creador.apellido, creador.imgperfil, valor, comentario, fecha)

fun Recomendacion.resumenRecomendacionCompletaDTO(): ResumenRecomendacionCompletaDTO {

    val libros = libros.map { libro -> libro.resumenCompletoDTO() }.toList()
    val valoraciones = valoraciones.map {valoracion -> valoracion.valoracionToDTO() }.toList()
    return ResumenRecomendacionCompletaDTO(
        id,
        creador.toIdDTO().id,
        titulo,
        esPublica,
        descripcion,
        libros,
        valoraciones,
        false,
        false,
        promedioValoraciones()
        )
}


