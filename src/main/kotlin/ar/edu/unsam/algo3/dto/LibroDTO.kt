package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Autor
import ar.edu.unsam.algo3.domain.Lenguaje
import ar.edu.unsam.algo3.domain.Libro

data class LibroResumenDTO (val titulo: String, val autor: AutorDTO)

fun Libro.resumenDTO() = LibroResumenDTO(titulo, autor.toDTO())

data class LibroTituloDTO (val titulo: String)

fun Libro.tituloDTO() = LibroTituloDTO(titulo)

data class LibroResumenCompletoDTO (
    val id: Number,
    val titulo_libro: String,
    val autor_nombre: String,
    val autor_apellido: String,
    val imagen_libro_url : String,
    val cant_pags_libro : Number,
    val cant_palabras_libro: Number,
    val cant_ediciones: Int,
    val idiomas_libro: List<String>,
    val ventas_semanales: Number,
    val esBestSeller: Boolean,
    val esDesafiante: Boolean,
    val esLargo: Boolean,
    val paginas_libro: Number
    )

data class LibroConAutorCompletoDTO (
    val id: Number,
    val titulo_libro: String,
    val autor : AutorResumenDTO,
    val imagen_libro_url : String,
    val cant_pags_libro : Number,
    val cant_palabras_libro: Number,
    val ediciones: Number,
    val idiomas_libro: List<String>,
    val ventas_semanales: Number,
    val esBestSeller: Boolean,
    val esDesafiante: Boolean,
    val esLargo: Boolean,
    val paginas_libro: Number
)


data class resumenCardLibroDTO (
    val id: Number,
    val titulo_libro: String,
    val autor_nombre: String,
    val autor_apellido: String,
    val imagen_libro_url : String,
    val cant_pags_libro : Number,
    val cant_palabras_libro: Number,
    val idiomas_libro: List<String>,
    val ventas_semanales: Number,
)

data class nuevoLibroDTO(
    val titulo_libro: String,
    val autor: Autor,
    val cant_ediciones: Int,
    val cant_pags_libro: Int,
    val cant_palabras_libro: Int,
    val ventas_semanales: Int,
    val esDesafiante: Boolean,
    val idiomas_libro: List<Lenguaje>

)


fun Libro.resumenCompletoDTO() =
    LibroResumenCompletoDTO(id, titulo, autor.nombre, autor.apellido, imagen, paginas, palabras, cant_ediciones,calculoIdiomasTotalesString() , ventasSemanales, esBestSeller(), esDesafiante(), esLargo(),paginasLargo)

fun Libro.toResumenParaCardLibro() = resumenCardLibroDTO(
    id,
    titulo,
    autor.nombre,
    autor.apellido,
    imagen,
    paginas,
    palabras,
    calculoIdiomasTotalesString(),
    ventasSemanales
)


fun Libro.resumenConAutorCompletoDTO(): LibroConAutorCompletoDTO {
    return LibroConAutorCompletoDTO(
        id,
        titulo,
        autor.toResumenDTO(),
        imagen,
        paginas,
        palabras,
        cant_ediciones,
        calculoIdiomasTotalesString(),
        ventasSemanales,
        esBestSeller(),
        esDesafiante(),
        esLargo(),
        paginasLargo
    )
}

