package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repositorio.AutoresRepositorio
import ar.edu.unsam.algo3.repositorio.CentrosDeLecturaRepositorio
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class CentrosLecturaBootstrap(
    private val centrosRepositorio: CentrosDeLecturaRepositorio,
    private val autoresRepositorio: AutoresRepositorio
) : InitializingBean {

    private val centrosLectura = mapOf(
        "centroParticular" to CentroDeLectura(
            direccion = "Chubut 1000",
            duracion = 1000.00,
            //NO CREAR UN NUEVO LIBRO, USAR UNO EXISTENTE
            libro = Libro("The Book of Bill", autoresRepositorio.getByID(1), paginas = 45, palabras = 1805, cant_ediciones = 1, ventasSemanales = 1055584, complejo = false, "https://http2.mlstatic.com/D_NQ_NP_700983-MLA75679149893_042024-O.webp"),
            tipodeCentro = Particular()
        ).apply {
            //VER OTRA FORMA MEJOR DE VER QUE SUPERE EL MÁXIMO DE PARTICIPANTES DISPONIBLES
            //Y USAR USUARIOS EXISTENTES NO CREAR UNO NUEVO
            participantes.addAll(List(cupoParticipantes() + 5) { Usuario("Pepa",
                "Gonzales",
                "pepita",
                "pepita@gmail.com",
                "1234",
                2006,
                10,
                7,
                Lenguaje.Inglés,
                250.0,
                Demandante(),
                Promedio(),
                "https://estudiantes.ucontinental.edu.pe/wp-content/uploads/2020/09/Madurez-emocional-7.jpg") })
        },
        "centroEditorial" to  CentroDeLectura(
            direccion = "Calle Falsa 456",
            duracion = 1200.00,
            //NO CREAR UN NUEVO LIBRO, USAR UNO EXISTENTE
            libro = Libro("Asesinato en el Orient Express", autoresRepositorio.getByID(5), paginas = 248,palabras = 40789, cant_ediciones = 5, ventasSemanales = 15790, complejo = false, "https://images.cdn1.buscalibre.com/fit-in/360x360/2f/e4/2fe487d9fe7072bfc1a4f419964b0c0a.jpg"),
            tipodeCentro = Editorial()
        ),
        "centroBiblioteca" to CentroDeLectura(
            direccion = "Camino Rural 789",
            duracion = 800.00,
            //NO CREAR UN NUEVO LIBRO, USAR UNO EXISTENTE
            libro = Libro("Las mujeres de Troya", autoresRepositorio.getByID(12), paginas = 340, palabras = 35208, cant_ediciones = 2, ventasSemanales = 9200, complejo = false, "https://http2.mlstatic.com/D_NQ_NP_884346-MLA53624916554_022023-O.webp"),
            tipodeCentro = Biblioteca()
        )
    )

    private fun guardarCentrosLectura() {
        centrosLectura.values.forEach { centro -> centrosRepositorio.crear(centro) }
    }

    override fun afterPropertiesSet() {
        guardarCentrosLectura()
    }
}
