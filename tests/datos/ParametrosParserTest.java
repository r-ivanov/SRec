package tests.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import datos.ParametrosParser;

/**
 * Clase de tests que comprueba que las funciones para determinar la corrección
 * de los valores introducidos para la ejecución de un algorimo, se comportan
 * como es esperado.
 * 
 * @author David Pastor Herranz
 */
public class ParametrosParserTest {

	@Test
	public void testParametrosParserReconoceCadenaVacia() {

		String valor = "";

		List<String> valores = ParametrosParser.reemplazarYPartirValores(valor);

		assertEquals(1, valores.size());
		assertEquals(valor, valores.get(0));
	}

	@Test
	public void testParametrosParserReconoceUnSoloValor() {

		String valor = "2.5";

		List<String> valores = ParametrosParser.reemplazarYPartirValores(valor);

		assertEquals(1, valores.size());
		assertEquals(valor, valores.get(0));
	}

	@Test
	public void testParametrosParserReconoceUnSoloValorArray() {

		String valor = "{1, 2, 3, 4}";

		List<String> valores = ParametrosParser.reemplazarYPartirValores(valor);

		assertEquals(1, valores.size());
		assertEquals(valor, valores.get(0));
	}

	@Test
	public void testParametrosParserReconoceUnSoloValorMatriz() {

		String valor = "{{1, 2},{3, 4},{5, 6}}";

		List<String> valores = ParametrosParser.reemplazarYPartirValores(valor);

		assertEquals(1, valores.size());
		assertEquals(valor, valores.get(0));
	}

	@Test
	public void testParametrosParserReconoceVariosParametros() {

		String primerValor = "1";
		String segundoValor = "2";
		String tercerValor = "3";
		String valorEntrada = primerValor + " ," + segundoValor + " ,"
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 0));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosArray() {

		String primerValor = "{1}";
		String segundoValor = "{2, 3, 4}";
		String tercerValor = "{}";
		String valorEntrada = primerValor + ", " + segundoValor + ", "
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 1));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosMatriz() {

		String primerValor = "{{1}}";
		String segundoValor = "{{2}, {3, 5}}";
		String tercerValor = "{{}}";
		String valorEntrada = primerValor + " , " + segundoValor + " , "
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 2));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosDouble() {

		String primerValor = "1.0";
		String segundoValor = "2.1";
		String tercerValor = "3.2";
		String valorEntrada = primerValor + " ," + segundoValor + " ,"
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser.comprobarValoresParametro(valores,
				"double", 0));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosDoubleEnArray() {

		String primerValor = "{1.1}";
		String segundoValor = "{2.2, 3.4, 4.5}";
		String tercerValor = "{}";
		String valorEntrada = primerValor + ", " + segundoValor + ", "
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser.comprobarValoresParametro(valores,
				"double", 1));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosDoubleEnMatriz() {

		String primerValor = "{{1.2}}";
		String segundoValor = "{{2.7}, {3.6, 5.3}}";
		String tercerValor = "{{}}";
		String valorEntrada = primerValor + " , " + segundoValor + " , "
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser.comprobarValoresParametro(valores,
				"double", 2));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosString() {

		String primerValor = "\"1\"";
		String segundoValor = "\"2\"";
		String tercerValor = "\"3\"";
		String valorEntrada = primerValor + " ," + segundoValor + " ,"
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser.comprobarValoresParametro(valores,
				"String", 0));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosStringEnArray() {

		String primerValor = "{\"1\"}";
		String segundoValor = "{\"2\", \"3\", \"4\"}";
		String tercerValor = "{}";
		String valorEntrada = primerValor + ", " + segundoValor + ", "
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser.comprobarValoresParametro(valores,
				"String", 1));
	}

	@Test
	public void testParametrosParserReconoceVariosParametrosStringEnMatriz() {

		String primerValor = "{{\"1\"}}";
		String segundoValor = "{{\"2\"}, {\"3\", \"5\"}}";
		String tercerValor = "{{}}";
		String valorEntrada = primerValor + " , " + segundoValor + " , "
				+ tercerValor;

		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals(primerValor, valores.get(0));
		assertEquals(segundoValor, valores.get(1));
		assertEquals(tercerValor, valores.get(2));
		assertTrue(ParametrosParser.comprobarValoresParametro(valores,
				"String", 2));
	}

	@Test
	public void testParametrosParserReemplazaRangosCorrectamente() {

		String valorEntrada = "1..4";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("1", valores.get(0));
		assertEquals("2", valores.get(1));
		assertEquals("3", valores.get(2));
		assertEquals("4", valores.get(3));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 0));
	}

	@Test
	public void testParametrosParserReemplazaRangosDecrecientesCorrectamente() {

		String valorEntrada = "3..1";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("3", valores.get(0));
		assertEquals("2", valores.get(1));
		assertEquals("1", valores.get(2));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 0));
	}

	@Test
	public void testParametrosParserReemplazaRangosCorrectamenteConValoresNegativos() {

		String valorEntrada = "-1..0";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("-1", valores.get(0));
		assertEquals("0", valores.get(1));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 0));
	}

	@Test
	public void testParametrosParserReemplazaRangosCorrectamenteConElMismoValor() {

		String valorEntrada = "0..0";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("0", valores.get(0));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 0));
	}

	@Test
	public void testParametrosParserReemplazaRangosCorrectamenteConArrays() {

		String valorEntrada = "{0..2}";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("{0,1,2}", valores.get(0));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 1));
	}

	@Test
	public void testParametrosParserReemplazaVariosRangosCorrectamenteConArrays() {

		String valorEntrada = "{0..2,4..5}";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("{0,1,2,4,5}", valores.get(0));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 1));
	}

	@Test
	public void testParametrosParserReemplazaCorrectamenteConMatrices() {

		String valorEntrada = "{{0..2},{4..5},{3,5}}";
		List<String> valores = ParametrosParser
				.reemplazarYPartirValores(valorEntrada);

		assertEquals("{{0,1,2},{4,5},{3,5}}", valores.get(0));
		assertTrue(ParametrosParser
				.comprobarValoresParametro(valores, "int", 2));
	}
}
