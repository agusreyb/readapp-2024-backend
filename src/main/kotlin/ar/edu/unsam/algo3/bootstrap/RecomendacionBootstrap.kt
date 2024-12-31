package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import ar.edu.unsam.algo3.repositorio.RecomendacionesRepositorio
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.DependsOn
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(2)
@DependsOn("librosBootstrap", "usuariosBootstrap")
class RecomendacionBootstrap(
    val recomendacionesRepositorio: RecomendacionesRepositorio,
    final val usuariosRepositorio: UsuariosRepositorio,
    val librosRepositorio: LibrosRepositorio,
): InitializingBean {

    private val recomendaciones = mapOf(
        "RecomendacionLoca" to Recomendacion(titulo = "Recomendacion Loca", usuariosRepositorio.getByID(2), "Es un conjunto de libros muy random. Disfrutalo!", false),
        "RecomendacionMisteriosa" to Recomendacion(titulo = "Recomendacion Misteriosa", usuariosRepositorio.getByID(3), "Espero que te guste este conjunto de libros de misterios de Agatha Christie"),
        "Clasicos" to Recomendacion(titulo = "Clasicos!", usuariosRepositorio.getByID(1), "Te dejo una lista de libros clásicos que TENES que leer" ),
        "JaneAusten" to Recomendacion(titulo = "Jane Austen <3", usuariosRepositorio.getByID(4), "Mejores libros de esta hermosa escritora. Romance"),
        "Mitologia" to Recomendacion(titulo = "Mitologia Griega", usuariosRepositorio.getByID(2), "Estos libros son reversiones de historias clásicas de la mitología griega. Muy entretenidos"),
        "Fantasia" to Recomendacion(titulo = "Fantasia", usuariosRepositorio.getByID(3), "Estos libros son el comienzo de sagas que te van a dejar maravillado y con ganas de estar dentro de ese mundo", false),
        "Consagrados" to Recomendacion(titulo = "Consagrados", usuariosRepositorio.getByID(6), "Libros de autores consagrados")
    )

    fun crearRecomendaciones() {
        recomendaciones.values.forEach { recomendacion -> recomendacionesRepositorio.apply { create(recomendacion) } }
    }

    fun agregarLibros() {
        recomendaciones["RecomendacionLoca"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(1))
        recomendaciones["RecomendacionLoca"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(2))
        recomendaciones["RecomendacionLoca"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(3))
        recomendaciones["RecomendacionLoca"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(4))
        recomendaciones["RecomendacionMisteriosa"]!!.agregarLibro(usuariosRepositorio.getByID(3),librosRepositorio.getByID(5))
        recomendaciones["RecomendacionMisteriosa"]!!.agregarLibro(usuariosRepositorio.getByID(3),librosRepositorio.getByID(6))
        recomendaciones["RecomendacionMisteriosa"]!!.agregarLibro(usuariosRepositorio.getByID(3),librosRepositorio.getByID(7))
        recomendaciones["Clasicos"]!!.agregarLibro(usuariosRepositorio.getByID(1),librosRepositorio.getByID(8))
        recomendaciones["Clasicos"]!!.agregarLibro(usuariosRepositorio.getByID(1),librosRepositorio.getByID(9))
        recomendaciones["Clasicos"]!!.agregarLibro(usuariosRepositorio.getByID(1),librosRepositorio.getByID(2))
        recomendaciones["JaneAusten"]!!.agregarLibro(usuariosRepositorio.getByID(4),librosRepositorio.getByID(10))
        recomendaciones["JaneAusten"]!!.agregarLibro(usuariosRepositorio.getByID(4),librosRepositorio.getByID(11))
        recomendaciones["JaneAusten"]!!.agregarLibro(usuariosRepositorio.getByID(4),librosRepositorio.getByID(12))
        recomendaciones["JaneAusten"]!!.agregarLibro(usuariosRepositorio.getByID(4),librosRepositorio.getByID(14))
        recomendaciones["Mitologia"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(15))
        recomendaciones["Mitologia"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(16))
        recomendaciones["Mitologia"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(17))
        recomendaciones["Mitologia"]!!.agregarLibro(usuariosRepositorio.getByID(2),librosRepositorio.getByID(18))
        recomendaciones["Fantasia"]!!.agregarLibro(usuariosRepositorio.getByID(3),librosRepositorio.getByID(19))
        recomendaciones["Fantasia"]!!.agregarLibro(usuariosRepositorio.getByID(3),librosRepositorio.getByID(20))
        recomendaciones["Fantasia"]!!.agregarLibro(usuariosRepositorio.getByID(3),librosRepositorio.getByID(13))
        recomendaciones["Consagrados"]!!.agregarLibro(usuariosRepositorio.getByID(6),librosRepositorio.getByID(18))
        recomendaciones["Consagrados"]!!.agregarLibro(usuariosRepositorio.getByID(6),librosRepositorio.getByID(19))
        recomendaciones["Consagrados"]!!.agregarLibro(usuariosRepositorio.getByID(6),librosRepositorio.getByID(20))


    }

    fun agregarValoraciones(){
        recomendaciones["RecomendacionLoca"]!!.valorar(usuariosRepositorio.getByID(1),3.2,"Si partimos de la base de donde estábamos parados, y dentro de la confrontación que hubo en determinadas situaciones sociales, donde el marco y el contexto iban avalando las realidades que suceden dentro de determinados organismos, lo principal y fundamental es trabajar en medidas a corto y mediano plazo que puedan satisfacer las necesidades de todo tipo, si esto sucede así.")
        recomendaciones["RecomendacionMisteriosa"]!!.valorar(usuariosRepositorio.getByID(1),5.0,"Me lei todos los libros y desde entonces estoy obsesionado con esta autora. Tanto es la obsesion que mi esposa me dejo, mis hijos no me quieren ver, mis amigos no me quieren escuchar, mi jefe me rajó, pero nada de eso importa cuando tengo a Agatha Christie.")
    }

    override fun afterPropertiesSet() {
        this.agregarLibros()
        this.agregarValoraciones()
        this.crearRecomendaciones()
    }
}