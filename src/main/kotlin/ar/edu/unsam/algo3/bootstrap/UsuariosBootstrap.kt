package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repositorio.AutoresRepositorio
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.DependsOn
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
@DependsOn("librosBootstrap")
class UsuariosBootstrap(
    val usuariosRepositorio: UsuariosRepositorio,
    val librosRepositorio: LibrosRepositorio,
    val autoresRepositorio: AutoresRepositorio,
): InitializingBean {
    val poliglota = Poliglota()
    val precavido = Precavido()
    val leedor = Leedor()
    val nativista = Nativista()
    val calculador = Calculador()
    val demandante = Demandante()
    val experimentado = Experimentado()
    val cambiante = Cambiante()


     val usuarios = mapOf(
        "Juan" to Usuario(
            "Juan",
            "Perez",
            "juancito",
            "juanperez@gmail.com",
            "Password1.",
            2002,
            9,
            12,
            Lenguaje.Español,
            200.0,
            Multiple(mutableListOf(poliglota, demandante)),
            Promedio(),
            "https://img.a.transfermarkt.technology/portrait/big/298613-1694431029.jpg?lm=1"
        ),
        "Maria" to Usuario(
            "Maria",
            "Gomez",
            "mariagomez",
            "mariagomez@gmail.com",
            "Password1.",
            2006,
            10,
            7,
            Lenguaje.Inglés,
            250.0,
            Precavido(),
            Ansioso(),
            "https://imagessl.casadellibro.com/img/autores/20083030.jpg"
        ),
        "Admin" to Usuario(
            "User",
            "Admin",
            "admin",
            "admin@gmail.com",
            "Password1.",
            2006,
            10,
            7,
            Lenguaje.Francés,
            300.0,
            Leedor(),
            Fanatico(),
            "https://img.freepik.com/foto-gratis/cierre-dientes-hombre-moreno-sonriendo_1187-5800.jpg"
        ),
        "Mariano" to Usuario(
            "Mariano",
            "Apellido Falso",
            "marianox34",
            "mariano999@gmail.com",
            "mamatequiero123.",
            2002,
            9,
            12,
            Lenguaje.Español,
            150.0,
            Multiple(mutableListOf(experimentado, calculador)),
            Recurrente(),
            "https://media.istockphoto.com/id/533837393/es/foto/payaso.jpg?s=612x612&w=0&k=20&c=x90RAkaZXoE5lqccTYwFLtyVtepTf8xVXY6AdXDPFZs="
        ),
        "MC" to Usuario(
            "MC",
            "A",
            "mc@gmail.com",
            "mca.lo@gmail.com",
            "12345678A.",
            2006,
            10,
            7,
            Lenguaje.Francés,
            250.0,
            Nativista(),
            Recurrente(),
            "https://www.clarin.com/img/2024/04/25/HKCtyMwSF_2000x1500__1.jpg"
        ),
         "Pepa" to Usuario(
             "Pepa",
             "Gonzales",
             "pepita",
             "pepita@gmail.com",
             "Pepita123.",
             2006,
             10,
             7,
             Lenguaje.Inglés,
             250.0,
             Demandante(),
             Promedio(),
             "https://estudiantes.ucontinental.edu.pe/wp-content/uploads/2020/09/Madurez-emocional-7.jpg"
         )
    )

    fun crearUsuario() {
        usuarios.values.forEach { usuario -> usuariosRepositorio.apply { create(usuario) } }
    }

    fun agregarAmigo(){
        usuarios["Juan"]!!.agregarAmigo(usuariosRepositorio.getByID(2))
    }

    fun agregarAutorPreferido(){
        usuarios["Juan"]!!.agregarAutorPreferido(autoresRepositorio.getByID(1))
        usuarios["Juan"]!!.agregarAutorPreferido(autoresRepositorio.getByID(2))
        usuarios["Maria"]!!.agregarAutorPreferido(autoresRepositorio.getByID(3))
        usuarios["Maria"]!!.agregarAutorPreferido(autoresRepositorio.getByID(4))
        usuarios["Admin"]!!.agregarAutorPreferido(autoresRepositorio.getByID(5))
        usuarios["Admin"]!!.agregarAutorPreferido(autoresRepositorio.getByID(6))
        usuarios["Admin"]!!.agregarAutorPreferido(autoresRepositorio.getByID(7))
    }

    fun leerlibros(){
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(1))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(2))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(3))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(4))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(5))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(6))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(7))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(8))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(9))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(10))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(11))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(12))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(13))
        usuarios["Juan"]!!.leerLibro(librosRepositorio.getByID(14))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(2))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(8))
        usuarios["Maria"]!!.agregarLibroPorLeer(librosRepositorio.getByID(11))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(9))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(1))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(2))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(3))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(4))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(15))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(16))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(17))
        usuarios["Maria"]!!.leerLibro(librosRepositorio.getByID(18))
        usuarios["Admin"]!!.leerLibro(librosRepositorio.getByID(5))
        usuarios["Admin"]!!.leerLibro(librosRepositorio.getByID(6))
        usuarios["Admin"]!!.leerLibro(librosRepositorio.getByID(7))
        usuarios["Admin"]!!.leerLibro(librosRepositorio.getByID(19))
        usuarios["Admin"]!!.leerLibro(librosRepositorio.getByID(20))
        usuarios["Admin"]!!.leerLibro(librosRepositorio.getByID(13))
        usuarios["Mariano"]!!.leerLibro(librosRepositorio.getByID(10))
        usuarios["Mariano"]!!.leerLibro(librosRepositorio.getByID(11))
        usuarios["Mariano"]!!.leerLibro(librosRepositorio.getByID(12))
        usuarios["Mariano"]!!.leerLibro(librosRepositorio.getByID(14))
        usuarios["Pepa"]!!.leerLibro(librosRepositorio.getByID(18))
        usuarios["Pepa"]!!.leerLibro(librosRepositorio.getByID(19))
        usuarios["Pepa"]!!.leerLibro(librosRepositorio.getByID(20))
    }

    fun agregarLibroPorLeer(){
        usuarios["Admin"]!!.agregarLibroPorLeer(librosRepositorio.getByID(14))
    }

    override fun afterPropertiesSet() {
        this.leerlibros()
        this.agregarAutorPreferido()
        this.crearUsuario()
        this.agregarAmigo()
        this.agregarLibroPorLeer()
    }
}