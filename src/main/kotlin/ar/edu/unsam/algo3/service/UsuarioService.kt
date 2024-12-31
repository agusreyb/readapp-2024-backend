package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo2.readapp.NotFoundException
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import ar.edu.unsam.algo3.repositorio.RecomendacionesRepositorio
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    val usuariosRepositorio: UsuariosRepositorio,
    private val librosRepositorio: LibrosRepositorio,
    private val recomendacionService: RecomendacionService,
    private val recomendacionesRepositorio: RecomendacionesRepositorio) {


    fun search(busqueda: String?): List<Usuario> = usuariosRepositorio.search(busqueda)

    fun getByID(id: Int): Usuario {
           val userCrudoPerfil = usuariosRepositorio.getByID(id)  ?: throw NotFoundException("No se encontró un usuario con el ID: $id")

           if (userCrudoPerfil.perfil is Multiple){
               val perfilesDelMultiple = (userCrudoPerfil.perfil as Multiple).perfiles
               userCrudoPerfil.perfilLista = perfilesDelMultiple
               return userCrudoPerfil
           }else{
               userCrudoPerfil.perfilLista.clear()
               userCrudoPerfil.addPerfilList()
           }
           return userCrudoPerfil

    }

    fun getRecomendacionByID(idRecomendacion: Int): Recomendacion = recomendacionService.getByID(idRecomendacion)

    fun getRecomendacionesAValorar(idUsuario: Int): List<ResumenRecomendacionCompletaDTO> {
        val usuario = getByID(idUsuario)
        val recomendaciones = usuario.recomendacionesAValorar
        return recomendaciones.map { recomendacion -> recomendacionService.modificarRepoEditarValorar(recomendacion, usuario)}.toList()
    }

    fun agregarRecomendacionAValorar(idUsuario: Int, idRecomendacion: Int){
        val usuario = getByID(idUsuario)
        val recomendacionAAgregar = getRecomendacionByID(idRecomendacion)
        usuario.agregarRecomendacionAValorar(recomendacionAAgregar)
    }

    fun eliminarRecomendacionAValorar(idUsuario: Int, idRecomendacion: Int){
        val usuario = getByID(idUsuario)
        val recomendacionAEliminar = getRecomendacionByID(idRecomendacion)
        usuario.eliminarRecomendacionAValorar(recomendacionAEliminar)
    }

    fun estaEnRecomendacionesAValorar(idUsuario: Int, idRecomendacion: Int): Boolean {
        val recomendacion = getRecomendacionByID(idRecomendacion)
        return getByID(idUsuario).estaEnRecomendacionesAValorar(recomendacion)
    }

    fun agregarAmigos(amigosDTO: List<DatosAmigosDTO>, userId: Int) {

        val usuarioLogueado = this.getByID(userId)
        amigosDTO.forEach { amigo ->
            val nuevoAmigo = usuariosRepositorio.getByID(amigo.id)
            usuarioLogueado.agregarAmigo(nuevoAmigo)
        }

    }

    fun eliminarAmigos(amigoDTO: Int, userId:Int) {
        val usuarioLogueado = this.getByID(userId)
        val amigoParaEliminar = this.getByID(amigoDTO)
        usuarioLogueado.quitarAmigo(amigoParaEliminar)
    }

    fun validarUsuarioLogin(usuarioLogin: UsuarioLoginDTO): UsuarioIDDTO {

        // Busca al usuario por email y contraseña
        val usuarioEncontrado = usuariosRepositorio.getAllElements().find { usuario ->
            usuario.mail == usuarioLogin.mail && usuario.password == usuarioLogin.contrasenia
        } ?: throw NotFoundException("Usuario o contraseña incorrectos.")

        // Retorna un DTO con el ID del usuario
        return usuarioEncontrado.toIdDTO()
    }

    fun actualizarDatosUsuario(usuarioActualizadoDTO: UsuarioActualizarDTO) {
        val usuarioAModificar = usuariosRepositorio.getByID(usuarioActualizadoDTO.id)

        usuarioAModificar.apply {
            nombre = usuarioActualizadoDTO.nombre
            apellido = usuarioActualizadoDTO.apellido
            username = usuarioActualizadoDTO.username
            mail = usuarioActualizadoDTO.mail
            fechaNacimiento = usuarioActualizadoDTO.fechaNacimiento //si la fecha es hoy puede tirar error
            vPromedio = usuarioActualizadoDTO.vPromedio.toDouble()
            formaDeLeer = usuarioActualizadoDTO.formaDeLeer
            imgperfil = usuarioActualizadoDTO.imgperfil
        }

        usuarioAModificar.perfil = convertirPerfilLista(usuarioActualizadoDTO.perfilLista)
        usuarioActualizadoDTO.librosLeidos.forEach { libro -> usuarioAModificar.leerLibro(libro) }



    }

    /* START PERFIL LIBROS LEIDOS Y A LEER */
    fun getLibrosALeer(userId: Int): List<resumenCardLibroDTO>{
        val usuarioLogueado = usuariosRepositorio.getByID(userId)
        val todosLosLibros = librosRepositorio.getAllElements().toSet()
        val librosALeer = todosLosLibros
            .minus(usuarioLogueado.librosPorLeer)
            .minus(usuarioLogueado.librosLeidos)

        return librosALeer.map { it -> it.toResumenParaCardLibro() }
    }

    fun getLibrosLeidosMenosTodos(userId:Int):List<resumenCardLibroDTO>{
        val usuarioLogueado = usuariosRepositorio.getByID(userId)
        val librosQuePuedeLeer = librosRepositorio.getAllElements() - usuarioLogueado.librosLeidos

        return librosQuePuedeLeer.map { it -> it.toResumenParaCardLibro() }
    }

    fun agregarLibrosLeidos(userId: Int, librosIds: List<Int>):List<resumenCardLibroDTO>{
        val usuarioLogueado = usuariosRepositorio.getByID(userId)
        librosIds.forEach { libroId ->
            val libro = librosRepositorio.getByID(libroId)
            usuarioLogueado.librosLeidos.add(libro)
        }

        usuarioLogueado.librosPorLeer.removeAll{libros -> librosIds.contains(libros.id)}
        return usuarioLogueado.librosLeidos.map { it -> it.toResumenParaCardLibro() }
    }

    fun eliminarLibrosLeidos(userId: Int, librosIds: List<Int>):List<resumenCardLibroDTO>{
        val usuarioLogueado = usuariosRepositorio.getByID(userId)
        usuarioLogueado.librosLeidos.removeAll{libro -> librosIds.contains(libro.id)}

        return usuarioLogueado.librosLeidos.map { it -> it.toResumenParaCardLibro() }
    }

    fun agregarLibrosALeer(userId: Int, librosIds: List<Int>):List<resumenCardLibroDTO>{
        val usuarioLogueado = usuariosRepositorio.getByID(userId)
        librosIds.forEach { libroId ->
            val libro = librosRepositorio.getByID(libroId)
            usuarioLogueado.librosPorLeer.add(libro)
        }
        return usuarioLogueado.librosLeidos.map { it -> it.toResumenParaCardLibro() }
    }

    fun eliminarLibrosALeer(userId: Int, librosIds: List<Int>):List<resumenCardLibroDTO>{
        val usuarioLogueado = usuariosRepositorio.getByID(userId)
        usuarioLogueado.librosPorLeer.removeAll{libro -> librosIds.contains(libro.id)}

        return usuarioLogueado.librosLeidos.map { it -> it.toResumenParaCardLibro() }
    }
    /* END PERFIL LIBROS LEIDOS Y A LEER */


    //PODRIA IMPLEMENTARLO EN USUARIO.KT Y GUARDAR EL PERFIL DIRECTAMENTE PERO NO SE SI SERIA SU RESPONSABILIDAD
    fun convertirPerfilLista(listaPerfil: List<String>): Perfil {
        if (listaPerfil.size > 1) {
            val multiple = Multiple(mutableListOf())

            listaPerfil.forEach { perfil ->
                when (perfil) {
                    "poliglota" -> multiple.agregarPerfil(poliglota)
                    "precavido" -> multiple.agregarPerfil(precavido)
                    "leedor" -> multiple.agregarPerfil(leedor)
                    "nativista" -> multiple.agregarPerfil(nativista)
                    "calculador" -> multiple.agregarPerfil(calculador)
                    "demandante" -> multiple.agregarPerfil(demandante)
                    "experimentado" -> multiple.agregarPerfil(experimentado)
                    "cambiante" -> multiple.agregarPerfil(cambiante)
                    else -> throw IllegalArgumentException("Perfil desconocido: $perfil")
                }
            }
            return multiple
        } else {
            return when (listaPerfil.first()) {
                "poliglota" -> poliglota
                "precavido" -> precavido
                "leedor" -> leedor
                "nativista" -> nativista
                "calculador" -> calculador
                "demandante" -> demandante
                "experimentado" -> experimentado
                "cambiante" -> cambiante
                else -> throw IllegalArgumentException("Perfil desconocido: ${listaPerfil.first()}")
            }
        }

    }

    fun obtenerTotalUsuarios(): Int {
        return usuariosRepositorio.contar()
    }
}