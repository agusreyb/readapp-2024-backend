
package ar.edu.unsam.algo2.readapp

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repositorio.CriterioLibros
import ar.edu.unsam.algo3.repositorio.Repositorio
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.every
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ServiceLibrosTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val mockedServiceLibros = mockk<ServiceLibros>(relaxUnitFun = true)
    val pruebaLibro1 = SerializableLibro(0, 3, 5)
    val pruebaLibro2 = SerializableLibro(1, 45, 99)
    var listaLibros = mutableListOf(pruebaLibro1, pruebaLibro2)

    every { mockedServiceLibros.getLibros() } answers { Json.encodeToString(listaLibros) }

    describe("Deserializacion de objetos que provienen de un JSON de el ServiceLibros"){
        val autorPrueba = Autor("Ricardo", "Darin","rdarin", Lenguaje.Español, 63, 4)
        val libro1 = Libro(
            autor = autorPrueba,
            paginas = 1,
            palabras = 1,
            cant_ediciones = 106,
            ventasSemanales = 121,
            complejo = false,
            imagen = ""
        )
        val libro2 = Libro(
            autor = autorPrueba,
            paginas = 4,
            palabras = 2,
            cant_ediciones = 9,
            ventasSemanales = 0,
            complejo = false,
            imagen = ""
        )
        val repositorioLibros = Repositorio<Libro>(CriterioLibros)
        val actualizacionLibros = ActualizacionLibros(repositorioLibros, mockedServiceLibros)
        it("deberia cambiar los valores del libro con id 1" +
                " de ediciones(9 a 45) y de ventasSemanales(0 a 99)") {
            repositorioLibros.create(libro1)
            repositorioLibros.create(libro2)
            repositorioLibros.getByID(1)?.cant_ediciones shouldBe 9
            repositorioLibros.getByID(1)?.ventasSemanales shouldBe 0
            actualizacionLibros.actualizarRepositorio()
            repositorioLibros.getByID(1)?.cant_ediciones shouldBe 45
            repositorioLibros.getByID(1)?.ventasSemanales shouldBe 99
        }
        it("deberia dar IllegalArgumentException"){
            repositorioLibros.create(libro1)
            shouldThrow<IllegalArgumentException>{actualizacionLibros.actualizarRepositorio()}
        }
    }


})