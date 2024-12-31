package ar.edu.unsam.algo3.domain


import ar.edu.unsam.algo2.readapp.BusinessException
import ar.edu.unsam.algo2.readapp.NotFoundException
import ar.edu.unsam.algo3.repositorio.Entidad
import java.time.LocalDate
import java.time.Period

class Usuario(
    var nombre: String,
    var apellido: String,
    var username: String,
    var mail: String,
    var password: String,
    var anioNacimiento: Int,
    val mesNacimiento: Int,
    val diaNacimiento: Int,
    var lenguaNativa: Lenguaje,
    var vPromedio: Double,
    var perfil: Perfil,//CORRESPONDE A CRITERIO DE BUSQUEDA
    var formaDeLeer: FormaDeLeer, //CORRESPONDE A TIPO DE LECTURA
    var imgperfil: String,


    ) : Entidad {

    override var id: Int = -1
    var fechaNacimiento: LocalDate = LocalDate.of(anioNacimiento, mesNacimiento, diaNacimiento)
    var amigos = mutableSetOf<Usuario>()
    var librosLeidos = mutableSetOf<Libro>()
    var librosPorLeer = mutableSetOf<Libro>()
    var misRecomendaciones = mutableListOf<Recomendacion>() //sacar esta variable
    var recomendacionesAValorar = mutableListOf<Recomendacion>()
    var autoresPreferidos = mutableSetOf<Autor>()
    var cantVecesLeido = mutableMapOf<Int, Int>()
    var perfilLista = mutableListOf<Perfil>()

    fun addPerfilList(){
        perfilLista.add(perfil)
    }

    fun cambiarPerfil(nuevoPerfil: Perfil){
        perfil = nuevoPerfil
    }

    fun edad(): Int = Period.between(fechaNacimiento, LocalDate.now()).years

    fun calculaVelocidadPromedio(libro : Libro) = if (libro.esDesafiante()) vPromedio*2 else vPromedio

    fun agregarAutorPreferido(autor: Autor){
        autoresPreferidos.add(autor)
    }

    fun agregarAmigo(amigo : Usuario){
        if (amigos.contains(amigo))
            throw BusinessException("Este usuario ya forma parte de su lista de amigos")
        else amigos.add(amigo)
    }

    fun quitarAmigo(amigo : Usuario){
        if (amigos.contains(amigo))
            amigos.remove(amigo)
        else {
            throw BusinessException("Este usuario no pertenece a su lista de amigos")
        }
    }

    fun leerLibro(libro : Libro){  //SE PUEDE MEJORAR CAMBIANDO LIBROS LEIDOS POR UN MAPA COMPLETO
        if (!librosLeidos.contains(libro)){
            librosLeidos.add(libro)
            cantVecesLeido[libro.id]=1}
        else { cantVecesLeido[libro.id]=(cantVecesLeido.getValue(libro.id) + 1) }
    }

    fun agregarLibroPorLeer(libro : Libro){
        librosPorLeer.add(libro)
    }

    //Tiempo de Lectura
    fun tiempoDeLecturaPromedio(libro: Libro):Double = libro.palabras / vPromedio

    fun tiempoDeLectura(libro : Libro): Double {
        return formaDeLeer.tiempoDeLectura(libro, this)
    }

    fun tiempoLecturaRecomendacionTotal(recomendacion: Recomendacion) = recomendacion.tiempoLecturaRecomendacion(this)

    fun tiempoLecturaRecomendacionAhorrado(recomendacion: Recomendacion) = recomendacion.tiempoLecturaRecomendacionAhorrado(this)

    fun tiempoLecturaRecomendacionNeto(recomendacion: Recomendacion) = recomendacion.tiempoLecturaNeto(this)

    //Recomendaciones
    private fun crearRecomendacion(esPublica : Boolean ) = Recomendacion("", this, "", esPublica)

    fun recomendacionEsApta(recomendacion: Recomendacion): Boolean =
        perfil.esApta(recomendacion, this) and (recomendacion.usuarioPuedeLeer(this)) || recomendacion.creador == this

    fun agregarRecomendacion(esPublica : Boolean): Recomendacion {
        val recomendacionNueva = crearRecomendacion(esPublica)
        misRecomendaciones.add(recomendacionNueva)
        return recomendacionNueva
    }

    fun agregarLibroARecomendacion(libro: Libro, recomendacion: Recomendacion) {
        recomendacion.agregarLibro(this, libro)
    }

    fun editarReseniaDeRecomendacion(recomendacion: Recomendacion, texto: String){
        recomendacion.editarResenia(this, texto)
    }

    fun valorarRecomendacion(recomendacion: Recomendacion, valor: Double, comentario: String){
        recomendacion.valorar(this, valor, comentario)
    }

    fun editarValoracionRecomendacion(recomendacion: Recomendacion, valor: Double){
        recomendacion.editarValoracion(this, valor)
    }

    fun agregarRecomendacionAValorar(recomendacion: Recomendacion) {
        if (!recomendacionesAValorar.contains(recomendacion)) {
            recomendacionesAValorar.add(recomendacion)
        }
    }

    fun eliminarRecomendacionAValorar(recomendacion: Recomendacion) {
        if (recomendacionesAValorar.contains(recomendacion)) {
            recomendacionesAValorar.remove(recomendacion)
        }
    }

    fun estaEnRecomendacionesAValorar(recomendacion:Recomendacion): Boolean{
        return recomendacionesAValorar.contains(recomendacion)
    }
}




