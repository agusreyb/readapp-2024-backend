package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.domain.Autor
import ar.edu.unsam.algo3.domain.Lenguaje
import ar.edu.unsam.algo3.domain.Libro
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LibrosTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val borges = Autor("Jorge Luis", "Borges","j.Borges", Lenguaje.Español, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh", "M.Walsh", Lenguaje.Español, 49, 4)
    val shakespeare = Autor("William", "Shakespeare","W.Shakespeare", Lenguaje.Inglés , 52 , 0)

    val libroDesafianteNoComplejo = Libro(
        autor = borges,
        paginas = 700,
        palabras = 2000,
        cant_ediciones = 2,
        ventasSemanales = 7000,
        complejo = false,
        imagen = ""
    )
    val libroComplejo = Libro(
        autor = walsh,
        paginas = 300,
        palabras = 1400,
        cant_ediciones = 1,
        ventasSemanales = 9000,
        complejo = true,
        imagen = ""
    )
    val libroNoDesafiante = Libro(
        autor = borges,
        paginas = 200,
        palabras = 120000,
        cant_ediciones = 2,
        ventasSemanales = 12000,
        complejo = false,
        imagen = ""
    )
    val libroIdiomas = Libro(
        autor = shakespeare,
        paginas = 100,
        palabras = 4000,
        cant_ediciones = 1,
        ventasSemanales = 15000,
        complejo = false,
        imagen = ""
    )

    describe("Libros desafiantes"){
        it("libroDesafianteNoComplejo deberia ser desafiante por tener mas de 600 paginas"){
            libroDesafianteNoComplejo.esDesafiante() shouldBe true
        }
        it("libroComplejo deberia ser desafiante por ser complejo"){
            libroComplejo.esDesafiante() shouldBe true
        }
        it("libroNoDesafiante no deberia ser desafiante"){
            libroNoDesafiante.esDesafiante() shouldBe false
        }
    }

    describe("Best seller"){
        it("si hay mas de 10000 ventas semanales y más de 5 traducciones, aunque menos de 2 ediciones"){
            libroIdiomas.agregarIdioma(Lenguaje.Japonés)
            libroIdiomas.agregarIdioma(Lenguaje.Inglés)
            libroIdiomas.agregarIdioma(Lenguaje.Español)
            libroIdiomas.agregarIdioma(Lenguaje.Alemán)
            libroIdiomas.agregarIdioma(Lenguaje.Italiano)

            libroIdiomas.esBestSeller() shouldBe true
        }
        it("tiene mas de 10000 ventas semanales y mas de 2 ediciones, aunque menos de 5 traducciones"){
            libroNoDesafiante.agregarIdioma(Lenguaje.Hindi)
            libroNoDesafiante.agregarIdioma(Lenguaje.Portugués)

            libroNoDesafiante.esBestSeller() shouldBe true
        }
        it("tiene menos de 10000 ventas, menos de 2 ediciones y menos de 5 traducciones"){
            libroComplejo.agregarIdioma(Lenguaje.Bengalí)

            libroComplejo.esBestSeller() shouldBe false
        }
        it("tiene mas de 10000 ventas pero menos de 2 ediciones y menos de 5 traducciones"){
            libroIdiomas.agregarIdioma(Lenguaje.Mandarín)
            libroIdiomas.agregarIdioma(Lenguaje.Árabe)
            libroIdiomas.agregarIdioma(Lenguaje.Francés)
            libroIdiomas.agregarIdioma(Lenguaje.Ruso)
            libroIdiomas.esBestSeller() shouldBe false
        }
    }

    describe("Idioma"){
        it("Idioma original es lengua nativa del autor"){
            libroIdiomas.idiomaOriginal shouldBe Lenguaje.Inglés
        }
        it("Suma de idiomas: 4 traducciones + idioma original"){
            libroIdiomas.agregarIdioma(Lenguaje.Mandarín)
            libroIdiomas.agregarIdioma(Lenguaje.Árabe)
            libroIdiomas.agregarIdioma(Lenguaje.Francés)
            libroIdiomas.agregarIdioma(Lenguaje.Ruso)
            libroIdiomas.calculoIdiomasTotales() shouldBe 5
        }
    }

    describe("Autor"){
        it("Autor es consagrado"){
            libroNoDesafiante.autor.esConsagrado() shouldBe true
        }
        it("Autor no es consagrado por tener menos de 50 años"){
            libroComplejo.autor.esConsagrado() shouldBe false
        }
        it("Autor no es consagrado por tener menos de 1 premio"){
            libroIdiomas.autor.esConsagrado() shouldBe false
        }
    }
})