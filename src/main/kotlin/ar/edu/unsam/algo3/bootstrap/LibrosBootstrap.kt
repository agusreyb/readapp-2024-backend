package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.Lenguaje
import ar.edu.unsam.algo3.domain.Libro
import ar.edu.unsam.algo3.repositorio.AutoresRepositorio
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.DependsOn
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(0)
@DependsOn("autoresBootstrap")
class LibrosBootstrap(
    val librosRepositorio: LibrosRepositorio,
    val autoresRepositorio: AutoresRepositorio
): InitializingBean {

    private val libros = mapOf(

        "Bill" to Libro("The Book of Bill", autoresRepositorio.getByID(1), paginas = 45, palabras = 1805, cant_ediciones = 1, ventasSemanales = 1055584, complejo = false, "https://http2.mlstatic.com/D_NQ_NP_700983-MLA75679149893_042024-O.webp"),
        "Quijote" to Libro("Don Quijote de la Mancha", autoresRepositorio.getByID(3), paginas = 920,palabras = 120298, cant_ediciones = 10, ventasSemanales = 130290, complejo = false, "https://www.planetalector.com/usuaris/thumbnails/libros/fotos/374/360/portada_don-quijote-de-la-mancha-comic_miguel-de-cervantes_202310231106.jpg"),
        "Janette" to Libro("I'm Glad My Mom Died ", autoresRepositorio.getByID(2), paginas = 160, palabras = 25005, cant_ediciones = 1, ventasSemanales = 12500, complejo = false, "https://upload.wikimedia.org/wikipedia/en/2/2a/I%27m_Glad_My_Mom_Died_Cover.png"),
        "Cuidado" to Libro("Cuidado con el perro", autoresRepositorio.getByID(4), paginas = 120,palabras = 20000, cant_ediciones = 2, ventasSemanales = 3000, complejo = false, "https://www.loqueleo.com/ar/uploads/2015/11/9789504633440.jpg"),
        "4grandes" to Libro("Los cuatro grandes", autoresRepositorio.getByID(5), paginas = 256, palabras = 45245, cant_ediciones = 4, ventasSemanales = 7800, complejo = false, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlA2blkm3WtOYXx59QXWI0e_VBV5rjJIQ5-Q&s"),
        "Orient" to Libro("Asesinato en el Orient Express", autoresRepositorio.getByID(5), paginas = 248,palabras = 40789, cant_ediciones = 5, ventasSemanales = 15790, complejo = false, "https://images.cdn1.buscalibre.com/fit-in/360x360/2f/e4/2fe487d9fe7072bfc1a4f419964b0c0a.jpg"),
        "Nilo" to Libro("Muerte en el Nilo", autoresRepositorio.getByID(5), paginas = 344, palabras = 50935, cant_ediciones = 6, ventasSemanales = 6200, complejo = false, "https://proassetspdlcom.cdnstatics2.com/usuaris/libros/fotos/201/original/portada_muerte-en-el-nilo_agatha-christie_201505291004.jpg"),
        "Eyre" to Libro("Jane Eyre", autoresRepositorio.getByID(6), paginas = 632,palabras = 93050, cant_ediciones = 12, ventasSemanales = 14300, complejo = false, "https://m.media-amazon.com/images/I/81Zs-tJ6N0L._SY522_.jpg"),
        "Principito" to Libro("El Principito", autoresRepositorio.getByID(7), paginas = 144, palabras = 27800, cant_ediciones = 15, ventasSemanales = 25400, complejo = false, "https://m.media-amazon.com/images/I/71Ime1IrO3L._SY522_.jpg"),
        "OyP" to Libro("Orgullo y Prejuicio", autoresRepositorio.getByID(8), paginas = 448,palabras = 69803, cant_ediciones = 9, ventasSemanales = 32600, complejo = false, "https://m.media-amazon.com/images/I/71snWNh97JL._SY522_.jpg"),
        "Emma" to Libro("Emma", autoresRepositorio.getByID(8), paginas = 512, palabras = 83022, cant_ediciones = 7, ventasSemanales = 26794, complejo = false, "https://m.media-amazon.com/images/I/61LNPggwz0L._SY522_.jpg"),
        "Persuación" to Libro("Persuación", autoresRepositorio.getByID(8), paginas = 150,palabras = 35207, cant_ediciones = 8, ventasSemanales = 12480, complejo = false, "https://m.media-amazon.com/images/I/61G4+n0K+0L._SY522_.jpg"),
        "Eragon" to Libro("Eragon", autoresRepositorio.getByID(9), paginas = 656, palabras = 50281, cant_ediciones = 3, ventasSemanales = 13029, complejo = false, "https://images.cdn1.buscalibre.com/fit-in/360x360/33/6e/336ee8f85de8a52fb119ac103f13fe03.jpg"),
        "Sentido" to Libro("Sentido y sensibilidad", autoresRepositorio.getByID(8), paginas = 376,palabras = 50328, cant_ediciones = 7, ventasSemanales = 9103, complejo = false, "https://m.media-amazon.com/images/I/61r-OTBq-mL._SL1216_.jpg"),
        "Aquiles" to Libro("La canción de Aquiles", autoresRepositorio.getByID(10), paginas = 392, palabras = 54278, cant_ediciones = 2, ventasSemanales = 14782, complejo = false, "https://images.cdn3.buscalibre.com/fit-in/360x360/a2/4e/a24e1d213892418e4ba12d4edceb8a96.jpg"),
        "Circe" to Libro("Circe", autoresRepositorio.getByID(10), paginas = 440,palabras = 56291, cant_ediciones = 2, ventasSemanales = 18299, complejo = false, "https://haverhillpl.org/wp-content/uploads/2021/06/51dzuwLmm-L.jpg"),
        "Medusa" to Libro("Medusa", autoresRepositorio.getByID(11), paginas = 224,palabras = 18920, cant_ediciones = 1, ventasSemanales = 7290, complejo = false, "https://http2.mlstatic.com/D_NQ_NP_608521-MLA69549461173_052023-O.webp"),
        "Troya" to Libro("Las mujeres de Troya", autoresRepositorio.getByID(12), paginas = 340, palabras = 35208, cant_ediciones = 2, ventasSemanales = 9200, complejo = false, "https://http2.mlstatic.com/D_NQ_NP_884346-MLA53624916554_022023-O.webp"),
        "Percy" to Libro("Percy Jackson y el ladrón del rayo", autoresRepositorio.getByID(13), paginas = 288,palabras = 29182, cant_ediciones = 4, ventasSemanales = 20340, complejo = false, "https://d22fxaf9t8d39k.cloudfront.net/e76e5a7e8259c2ee382859dcb1b31b4c503aee32146f1eedb7fd952b28495b44167807.png"),
        "Harry" to Libro("Harry Potter y la piedra filosofal", autoresRepositorio.getByID(14), paginas = 255, palabras = 23281, cant_ediciones = 5, ventasSemanales = 40300, complejo = false, "https://images.cdn3.buscalibre.com/fit-in/360x360/ce/e6/cee6ef96dad70d3f599b953f0e50afc7.jpg"),
        )

    fun crearLibros() {
        libros.values.forEach { libro -> librosRepositorio.apply { create(libro) } }
    }

    fun agregarIdiomas(){
        libros["Bill"]?.agregarIdioma(Lenguaje.Español)
        libros["Cuidado"]?.agregarIdioma(Lenguaje.Portugués)
        libros["4grandes"]?.agregarIdioma(Lenguaje.Español)
        libros["4grandes"]?.agregarIdioma(Lenguaje.Francés)
        libros["4grandes"]?.agregarIdioma(Lenguaje.Portugués)
        libros["4grandes"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Orient"]?.agregarIdioma(Lenguaje.Español)
        libros["Orient"]?.agregarIdioma(Lenguaje.Francés)
        libros["Orient"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Orient"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Nilo"]?.agregarIdioma(Lenguaje.Español)
        libros["Nilo"]?.agregarIdioma(Lenguaje.Francés)
        libros["Nilo"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Nilo"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Eyre"]?.agregarIdioma(Lenguaje.Español)
        libros["Eyre"]?.agregarIdioma(Lenguaje.Francés)
        libros["Eyre"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Eyre"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Eyre"]?.agregarIdioma(Lenguaje.Italiano)
        libros["Principito"]?.agregarIdioma(Lenguaje.Español)
        libros["Principito"]?.agregarIdioma(Lenguaje.Portugués)
        libros["OyP"]?.agregarIdioma(Lenguaje.Español)
        libros["OyP"]?.agregarIdioma(Lenguaje.Francés)
        libros["OyP"]?.agregarIdioma(Lenguaje.Portugués)
        libros["OyP"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Emma"]?.agregarIdioma(Lenguaje.Español)
        libros["Emma"]?.agregarIdioma(Lenguaje.Francés)
        libros["Emma"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Persuación"]?.agregarIdioma(Lenguaje.Español)
        libros["Persuación"]?.agregarIdioma(Lenguaje.Francés)
        libros["Persuación"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Persuación"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Eragon"]?.agregarIdioma(Lenguaje.Español)
        libros["Eragon"]?.agregarIdioma(Lenguaje.Francés)
        libros["Eragon"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Eragon"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Sentido"]?.agregarIdioma(Lenguaje.Español)
        libros["Sentido"]?.agregarIdioma(Lenguaje.Francés)
        libros["Sentido"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Sentido"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Aquiles"]?.agregarIdioma(Lenguaje.Español)
        libros["Aquiles"]?.agregarIdioma(Lenguaje.Francés)
        libros["Aquiles"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Aquiles"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Circe"]?.agregarIdioma(Lenguaje.Español)
        libros["Circe"]?.agregarIdioma(Lenguaje.Francés)
        libros["Circe"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Circe"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Medusa"]?.agregarIdioma(Lenguaje.Español)
        libros["Medusa"]?.agregarIdioma(Lenguaje.Francés)
        libros["Medusa"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Medusa"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Troya"]?.agregarIdioma(Lenguaje.Español)
        libros["Troya"]?.agregarIdioma(Lenguaje.Francés)
        libros["Troya"]?.agregarIdioma(Lenguaje.Portugués)
        libros["Troya"]?.agregarIdioma(Lenguaje.Alemán)
        libros["Percy"]?.agregarIdioma(Lenguaje.Español)
        libros["Percy"]?.agregarIdioma(Lenguaje.Francés)
        libros["Harry"]?.agregarIdioma(Lenguaje.Español)
        libros["Harry"]?.agregarIdioma(Lenguaje.Francés)
    }

    override fun afterPropertiesSet() {
        this.agregarIdiomas()
        this.crearLibros()
    }
}