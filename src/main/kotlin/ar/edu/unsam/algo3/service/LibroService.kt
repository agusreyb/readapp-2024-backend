package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo2.readapp.BusinessException
import ar.edu.unsam.algo2.readapp.NotFoundException
import ar.edu.unsam.algo3.domain.Autor
import ar.edu.unsam.algo3.domain.Libro
import ar.edu.unsam.algo3.domain.Recomendacion
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.repositorio.AutoresRepositorio
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import org.springframework.stereotype.Service

@Service
class LibroService (val librosRepositorio: LibrosRepositorio, val autoresRepositorio: AutoresRepositorio) {

    fun getByID(id: Int): Libro = librosRepositorio.getByID(id)

    fun search(busqueda: String?): List<Libro> {
        val resultados = librosRepositorio.search(busqueda)
        if (resultados.isEmpty()) {
            throw NotFoundException("Libro no encontrado.")
        }
        return resultados
    }

    fun obtenerTodosLosLibros(): List<resumenCardLibroDTO> {
        return librosRepositorio.getAllElements().map { it -> it.toResumenParaCardLibro() }
    }
    fun obtenerTotalLibros(): Int {
        return librosRepositorio.contar()
    }

    fun nuevoLibro (libroACrear: nuevoLibroDTO){

        if (libroACrear.titulo_libro == "" ){
            throw BusinessException("No fue posible ingresar el nuevo libro")
        }

        librosRepositorio.create(Libro(
            libroACrear.titulo_libro,
            libroACrear.autor,
            libroACrear.cant_pags_libro,
            libroACrear.cant_palabras_libro,
            libroACrear.cant_ediciones,
            libroACrear.ventas_semanales,
            libroACrear.esDesafiante,
            imagen = "",
        ).apply { traducciones.addAll(libroACrear.idiomas_libro) })

    }

    fun actualizarLibro(libroUpdate: LibroConAutorCompletoDTO){

        var libroModificable = librosRepositorio.getByID(libroUpdate.id.toInt())

        libroModificable.apply {
            titulo = libroUpdate.titulo_libro
            autor = obtenerAutor(libroUpdate.autor.id)
            cant_ediciones = libroUpdate.ediciones.toInt()
            paginas= libroUpdate.cant_pags_libro.toInt()
            palabras = libroUpdate.cant_palabras_libro.toInt()
            ventasSemanales = libroUpdate.ventas_semanales.toInt()
            complejo = libroUpdate.esDesafiante


        }

    }

    fun obtenerAutor(idAutor: Int): Autor {
        return  autoresRepositorio.getByID(idAutor)
    }


}