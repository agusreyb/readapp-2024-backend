package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.repositorio.Entidad
import java.time.LocalDate

open class Recomendacion(
    var titulo: String,
    var creador: Usuario,
    var descripcion: String,
    var esPublica: Boolean = true
) : Permisos(), Entidad {
    override var id : Int = -1

    var libros = mutableSetOf<Libro>()
    val valoraciones: MutableList<Valoracion> = mutableListOf()
    val listaObservers = mutableListOf<Observer>()
    var avgValoraciones : Double = this.promedioValoraciones()

    fun usuarioPuedeLeer(usuario : Usuario): Boolean {
        return if (esPublica || usuario == creador) true
        else esAmigo(usuario, this.creador)
    }

    //Tiempos de lectura
    private fun librosYaLeidos (usuario : Usuario) = libros.filter { cadaLibro -> cadaLibro in usuario.librosLeidos}

    fun tiempoLecturaRecomendacion(usuario: Usuario) : Double = this.libros.sumOf { cadaLibro -> usuario.tiempoDeLectura(cadaLibro) }

    fun tiempoLecturaRecomendacionAhorrado(usuario : Usuario) :Double = this.librosYaLeidos(usuario).sumOf { cadaLibro -> usuario.tiempoDeLectura(cadaLibro)}

    fun tiempoLecturaNeto(usuario: Usuario) = tiempoLecturaRecomendacion(usuario) - tiempoLecturaRecomendacionAhorrado(usuario)

    //Valoracion
    fun encuentraValoracionUsuario(usuario: Usuario): Int{
        return valoraciones.indexOfFirst { it.creador == usuario }
    }

    fun valorar(usuario: Usuario, valor: Double, comentario: String){
        if (!this.usuarioPuedeValorar(usuario)) {
            throw IllegalArgumentException("Usted no tiene permiso para valorar esta recomendacion")
        }
        val valoracion = Valoracion(usuario, valor, comentario, LocalDate.now())
        valoracion.validacionValor(valor)
        valoraciones.add(valoracion)
        usuario.eliminarRecomendacionAValorar(this)
    }

    fun usuarioPuedeValorar(usuario: Usuario): Boolean {
        return (creador!= usuario &&
                (libroEstaLeido(usuario, this) || (todosTienenMismoAutor(this) && comprueboAutorPreferido(usuario, this)))) &&
                (!this.valoraciones.any { valoracion -> valoracion.creador == usuario })
    }

    fun promedioValoraciones(): Double {
        avgValoraciones = valoraciones.sumOf { valoracion -> valoracion.valor } / valoraciones.size
        return avgValoraciones
    }

    fun editarValoracion(usuario: Usuario, valor: Double){
        if (this.encuentraValoracionUsuario(usuario) == -1)
            throw IllegalArgumentException("El usuario '$usuario' no dejó ninguna valoración")
        valoraciones[encuentraValoracionUsuario(usuario)].editarValor(valor)
    }

    //Edición
    fun agregarLibro(usuario: Usuario, libro: Libro){
        validacionPermisoEdicion(usuario)
        if (!permisoAgregarLibro(this, usuario, libro)) {
            throw IllegalArgumentException("Usted y el autor de la reseña deben haber leído el libro")
        }
        listaObservers.forEach{ it.libroAgregado(usuario, libro, this) }
        libros.add(libro)
    }

    fun usuarioPuedeEditar (usuario: Usuario) : Boolean {
        return ((esAmigo(usuario, creador) and libroEstaLeido(usuario, this))) or
                (usuario == creador)
    }

    fun validacionPermisoEdicion(usuario: Usuario) {
        if (!usuarioPuedeEditar(usuario)) {
            throw IllegalArgumentException("El usuario '$usuario' no tiene permiso para editar esta recomendación.")
        }
    }
    
    fun editarResenia(usuario: Usuario, texto: String) {
        validacionPermisoEdicion(usuario)
        descripcion = texto
    }

    //Observers
    fun agregarObserver(observer: Observer){
        listaObservers.add(observer)
    }

    fun quitarObserver(observer: Observer){
        listaObservers.remove(observer)
    }
}

abstract class Permisos{
    fun esAmigo(usuario : Usuario, creadorDeResenia: Usuario) : Boolean {
        return creadorDeResenia.amigos.contains(usuario)
    }
    fun libroEstaLeido(usuario: Usuario, recomendacion : Recomendacion): Boolean {
        return usuario.librosLeidos.containsAll(recomendacion.libros)
    }
    fun permisoAgregarLibro (recomendacion: Recomendacion, usuario: Usuario, libro: Libro): Boolean{
        return usuario.librosLeidos.contains(libro) and recomendacion.creador.librosLeidos.contains(libro)
    }
    fun todosTienenMismoAutor (recomendacion: Recomendacion): Boolean{
        val mapAutores = recomendacion.libros.map{it.autor}.toSet()
        return mapAutores.size == 1
    }
    fun comprueboAutorPreferido(usuario: Usuario, recomendacion: Recomendacion): Boolean{
        return usuario.autoresPreferidos.contains(recomendacion.libros.firstOrNull()?.autor)
    }
}

class Valoracion(
    var creador: Usuario,
    var valor: Double,
    var comentario: String,
    val fecha: LocalDate
) {

    fun validacionValor(valor: Double) {
        if ((valor <= 0) or (valor > 5)) {
            throw IllegalArgumentException("El numero no es válido")
        }
    }

    fun editarValor(nuevoValor: Double){
        validacionValor(nuevoValor)
        valor = nuevoValor
    }
}
