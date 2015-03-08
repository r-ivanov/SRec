package utilidades;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Genera el código java resultante de incorporar líneas de traza, basado en el
 * Document manipulado por el Transformador. Clase de servicios.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class GeneradorJava {

	// true = se muestra código java generado por terminal
	private static boolean ver_codigo_terminal = false;

	/**
	 * Escribe un fichero java, dado un Document
	 * 
	 * @param d
	 *            Document que se escribirá en fichero
	 * @param fichero
	 *            nombre del fichero que se va a escribir
	 */
	public static void writeJavaFile(Document d, String fichero) {
		Element e = d.getDocumentElement();

		FileWriter fw = null;
		try {
			fw = new FileWriter(fichero);
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 1");
		}

		writeJavaFile(e, fw, 0, null);

		try {
			fw.close();
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 2");
		}
	}

	/**
	 * Escribe de manera recursiva un fichero java, dado un Document
	 * 
	 * @param d
	 *            Document que se escribirá en fichero
	 * @param fw
	 *            FileWriter donde se irá volcando el contenido del fichero
	 * @param tab
	 *            número de tabulaciones que se deben escribr antes de cada
	 *            línea
	 * @param n2
	 *            nodo siguiente. En algunas ocasiones necesitamos conocer
	 *            atributos del siguiente nodo (al declarar varias variables a
	 *            la vez)
	 */
	private static void writeJavaFile(Node n, FileWriter fw, int tab, Node n2) {
		String s = reinicializarCadena(tab);

		if (n instanceof Element) {
			Element e = (Element) n;
			Element el[] = ManipulacionElement.getChildElements(e);

			// ***************************************** NODO java-class-file

			if (e.getNodeName().equals("java-class-file")) // Nodo inicial
			{
				for (int i = 0; i < el.length; i++) {
					writeJavaFile(el[i], fw, tab, null);
				}
			}

			// ***************************************** NODO import

			else if (e.getNodeName().equals("import")) {
				esc(fw, conv("import " + e.getAttribute("module") + ";\r\n"));
			}

			// ***************************************** NODO package-decl

			else if (e.getNodeName().equals("package-decl")) {
				// La comentamos porque es mejor que no escriba nada de
				// paquetes, la clase SRec_ no compilará
			}

			// ***************************************** NODO class

			else if (e.getNodeName().equals("class")) // Nodo de la clase
			{
				boolean mas_implements = false;

				s = "\r\n\r\n";
				if (e.getAttribute("visibility").equals("public")) {
					s = s + e.getAttribute("visibility") + " ";
				}
				if (e.getAttribute("abstract").equals("true")) {
					s = s + "abstract ";
				}
				s = s + "class " + e.getAttribute("name") + " ";

				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("superclass")) {
						s = s + "extends " + el[i].getAttribute("name") + " ";
					}
				}
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("implement")) {
						if (!mas_implements) {
							s = s + "implements "
									+ el[i].getAttribute("interface");
							mas_implements = true;
						} else {
							s = s + ", " + el[i].getAttribute("interface");
						}
					}
				}
				s = s + "\r\n{\r\n\r\n";
				esc(fw, conv(s));
				s = reinicializarCadena(tab);
				for (int i = 0; i < el.length; i++) {
					if (!(el[i].getNodeName().equals("superclass") || el[i]
							.getNodeName().equals("implement"))) {
						if (el[i].getNodeName().equals("method")) {
							esc(fw, "\r\n\r\n");
						}
						writeJavaFile(el[i], fw, tab + 1, null);
					}
				}
				s = "\r\n}\r\n";
				esc(fw, conv(s));
			}

			// ***************************************** NODO field

			else if ((e.getNodeName().equals("field"))
					|| (e.getNodeName().equals("local-variable"))) // Nodo
				// atributo
				// de la
				// clase
			{
				if (n2 instanceof Element) {
					Element siguiente = (Element) n2;
					if (siguiente.hasAttribute("continued")) {
						s = s + stringFieldLocalVariable(e, el) + ", ";
					} else {
						s = s + stringFieldLocalVariable(e, el) + ";\r\n";
					}
				} else {
					s = s + stringFieldLocalVariable(e, el) + ";\r\n";
				}
				esc(fw, s);
			}

			// ***************************************** NODO method

			else if (e.getNodeName().equals("method")) {
				boolean mas_throws = false;

				if (e.hasAttribute("visibility")) {
					s = s + e.getAttribute("visibility") + " ";
				}
				if (e.hasAttribute("synchronized")) {
					s = s + "synchronized ";
				}
				if (e.hasAttribute("static")) {
					s = s + "static ";
				}

				s = s + el[0].getAttribute("name");

				if (el[0].hasAttribute("dimensions")) {
					s = s
							+ ServiciosString
							.cadenaDimensiones(Integer.parseInt(el[0]
									.getAttribute("dimensions")));
				}

				s = s + " " + e.getAttribute("name") + " ( ";

				// Parámetros
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("formal-arguments")) {
						s = s
								+ stringParametros(el[i],
										ManipulacionElement
										.getChildElements(el[i]));
					}
				}

				s = s + " )";
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("throws")
							&& mas_throws == false) {
						s = s + " throws " + el[i].getAttribute("exception");
						mas_throws = true;
					} else if (el[i].getNodeName().equals("throws")
							&& mas_throws == true) {
						s = s + ", " + el[i].getAttribute("exception");
					}
				}
				s = s + "\r\n";
				esc(fw, conv(s));
				s = reinicializarCadena(tab);

				// Cuerpo de método
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("block")) {
						writeJavaFile(el[i], fw, tab, null);
					}
				}

				s = reinicializarCadena(tab);
			}

			// ***************************************** NODO constructor

			else if (e.getNodeName().equals("constructor")) {
				boolean mas_throws = false;

				if (e.hasAttribute("visibility")) {
					s = s + e.getAttribute("visibility") + " ";
				}
				if (e.hasAttribute("synchronized")) {
					s = s + "synchronized ";
				}
				if (e.hasAttribute("static")) {
					s = s + "static ";
				}

				s = s + " " + e.getAttribute("name") + " ( ";

				// Parámetros
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("formal-arguments")) {
						s = s
								+ stringParametros(el[i],
										ManipulacionElement
										.getChildElements(el[i]));
					}
				}

				s = s + " )";
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("throws")
							&& mas_throws == false) {
						s = s + " throws " + el[i].getAttribute("exception");
						mas_throws = true;
					} else if (el[i].getNodeName().equals("throws")
							&& mas_throws == true) {
						s = s + ", " + el[i].getAttribute("exception");
					}
				}
				s = s + "\r\n";
				esc(fw, conv(s));
				s = reinicializarCadena(tab);
				s = s + "{\r\n";
				esc(fw, s);
				s = reinicializarCadena(tab);

				// Cuerpo de constructor
				for (int i = 1; i < el.length; i++) {
					writeJavaFile(el[i], fw, tab + 1, null);
				}

				s = s + "}\r\n\r\n\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO this-call o
			// super-call

			else if (e.getNodeName().equals("this-call")
					|| e.getNodeName().equals("super-call")) {
				s = s + stringThisSuper(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO block (metodos)

			else if (e.getNodeName().equals("block")) {
				s = s + "{\r\n";
				esc(fw, s);

				for (int i = 0; i < el.length; i++) {
					if (i < el.length - 1) {
						writeJavaFile(el[i], fw, tab + 1, el[i + 1]);
					} else {
						writeJavaFile(el[i], fw, tab + 1, null);
					}
				}

				s = reinicializarCadena(tab);
				s = s + "}\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO loop (bucles for,
			// while y do)

			else if (e.getNodeName().equals("loop")) {
				if (e.getAttribute("kind").equals("for")) {
					s = s + "for (";
					Element init = null, test = null, update = null;
					Element initHijos[] = null, testHijos[] = null, updateHijos[] = null;

					for (int i = 0; i < el.length; i++) {
						if (el[i].getNodeName().equals("init")) {
							init = el[i];
						} else if (el[i].getNodeName().equals("test")) {
							test = el[i];
						} else if (el[i].getNodeName().equals("update")) {
							update = el[i];
						}
					}
					if (init != null) {
						initHijos = ManipulacionElement.getChildElements(init);

						// Espacio de inicialización del bucle for
						for (int i = 0; i < initHijos.length; i++) {
							if (initHijos[i].getNodeName().equals(
									"local-variable")) {
								s = s
										+ stringFieldLocalVariable(
												initHijos[i],
												ManipulacionElement
												.getChildElements(initHijos[i]));
							} else if (initHijos[i].getNodeName().equals(
									"assignment-expr")) {
								s = s
										+ stringAssignment(
												initHijos[i],
												ManipulacionElement
												.getChildElements(initHijos[i]));
							} else if (initHijos[i].getNodeName().equals(
									"conditional-expr")) {
								s = s
										+ stringConditional(
												initHijos[i],
												ManipulacionElement
												.getChildElements(initHijos[i]));
							}
							if (i < initHijos.length - 1) {
								s = s + ", ";
							} else {
								s = s + "; ";
							}
						}
					} else {
						s = s + "; ";
					}

					if (test != null) {
						testHijos = ManipulacionElement.getChildElements(test);

						// Espacio de condición del bucle for
						for (int i = 0; i < testHijos.length; i++) {
							s = s + stringCondition(test, testHijos);
							if (i < testHijos.length - 1) {
								s = s + ", ";
							} else {
								s = s + "; ";
							}
						}

					} else {
						s = s + "; ";
					}

					if (update != null) {
						updateHijos = ManipulacionElement
								.getChildElements(update);

						// Espacio de actualización del bucle for
						for (int i = 0; i < updateHijos.length; i++) {
							if (updateHijos[i].getNodeName().equals(
									"binary-expr")) {
								s = s
										+ stringExpresion(
												updateHijos[i],
												ManipulacionElement
												.getChildElements(updateHijos[i]));
							} else if (updateHijos[i].getNodeName().equals(
									"unary-expr")) {
								s = s
										+ stringExpresionU(
												updateHijos[i],
												ManipulacionElement
												.getChildElements(updateHijos[i]));
							} else if (updateHijos[i].getNodeName().equals(
									"send")) {
								s = s
										+ stringExpresion(
												updateHijos[i],
												ManipulacionElement
												.getChildElements(updateHijos[i]));
							} else if (updateHijos[i].getNodeName().equals(
									"field-access")) {
								s = s
										+ stringSendTarget(
												updateHijos[i],
												ManipulacionElement
												.getChildElements(updateHijos[i]))
												+ "."
												+ updateHijos[i].getAttribute("field");
							} else if (updateHijos[i].getNodeName().equals(
									"assignment-expr")) {
								s = s
										+ stringAssignment(
												updateHijos[i],
												ManipulacionElement
												.getChildElements(updateHijos[i]));
							}

							if (i < updateHijos.length - 1) {
								s = s + ", ";
							} else {
								s = s + ")\r\n";
							}
						}
					} else {
						s = s + ")\r\n";
					}

					esc(fw, conv(s));
					for (int i = 0; i < el.length; i++) {
						if (!(el[i].getNodeName().equals("init")
								|| el[i].getNodeName().equals("test")
								|| el[i].getNodeName().equals("update") || el[i]
										.getNodeName().equals("block"))) {
							writeJavaFile(el[i], fw, tab + 1, null);
						} else if (el[i].getNodeName().equals("block")) {
							writeJavaFile(el[i], fw, tab, null);
						}
					}
				} else if (e.getAttribute("kind").equals("while")) {
					s = s + "while (";
					Element test = null;
					for (int i = 0; i < el.length; i++) {
						if (el[i].getNodeName().equals("test")) {
							test = el[i];
						}
					}

					Element testHijos[] = ManipulacionElement
							.getChildElements(test);

					s = s + stringCondition(test, testHijos);
					s = s + ")\r\n";
					esc(fw, conv(s));
					s = reinicializarCadena(tab);
					for (int i = 0; i < el.length; i++) {
						if (el[i].getNodeName().equals("block")) {
							writeJavaFile(el[i], fw, tab, null);
						} else if (!el[i].getNodeName().equals("test")) {
							writeJavaFile(el[i], fw, tab + 1, null);
						}
					}

				}

				else if (e.getAttribute("kind").equals("do")) {
					s = s + "do\r\n";
					esc(fw, s);

					Element test = null;

					for (int i = 0; i < el.length; i++) {
						if (el[i].getNodeName().equals("block")) {
							writeJavaFile(el[i], fw, tab, null);
						}
						if (el[i].getNodeName().equals("test")) {
							test = el[i];
						}
					}

					s = reinicializarCadena(tab);
					s = s + "while ( ";

					Element testHijos[] = ManipulacionElement
							.getChildElements(test);

					s = s + stringCondition(test, testHijos);

					s = s + " );\r\n";
					esc(fw, conv(s));
				}
			}

			// ***************************************** NODO empty

			else if (e.getNodeName().equals("empty")) {
				s = s + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO try

			else if (e.getNodeName().equals("try")) {
				s = s + "try\r\n";
				esc(fw, s);

				// Nodo block
				writeJavaFile(el[0], fw, tab, null);

				// Nodo catch
				s = reinicializarCadena(tab);
				s = s + "catch (";

				Element elemCatch[] = ManipulacionElement
						.getChildElements(el[1]);
				s = s
						+ (ManipulacionElement.getChildElements(elemCatch[0])[0])
						.getAttribute("name") + " "
						+ elemCatch[0].getAttribute("name");
				s = s + ")\r\n";
				esc(fw, conv(s));

				writeJavaFile(elemCatch[1], fw, tab, null);
			}

			// ***************************************** NODO throw

			else if (e.getNodeName().equals("throw")) {
				s = s + stringThrow(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO assert

			else if (e.getNodeName().equals("assert")) {
				s = s + stringAssert(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO unary-expr

			else if (e.getNodeName().equals("unary-expr")) {
				s = s + stringExpresionU(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO assignment-expr

			else if (e.getNodeName().equals("assignment-expr")) {
				s = s + stringAssignment(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO conditional-expr

			else if (e.getNodeName().equals("conditional-expr")) {
				s = s + stringConditional(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO if

			else if (e.getNodeName().equals("if")) {
				Element test = null, trueCase = null, falseCase = null;
				Element testHijos[] = null, trueHijos[] = null, falseHijos[] = null;

				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("test")) {
						test = el[i];
					} else if (el[i].getNodeName().equals("true-case")) {
						trueCase = el[i];
					} else if (el[i].getNodeName().equals("false-case")) {
						falseCase = el[i];
					}
				}

				testHijos = ManipulacionElement.getChildElements(test);
				if (trueCase != null) {
					trueHijos = ManipulacionElement.getChildElements(trueCase);
				}
				if (falseCase != null) {
					falseHijos = ManipulacionElement
							.getChildElements(falseCase);
				}

				s = s + "if ( ";
				s = s + stringCondition(test, testHijos);
				s = s + " )\r\n";
				esc(fw, s);

				s = reinicializarCadena(tab);

				if (trueHijos != null) {
					for (int i = 0; i < trueHijos.length; i++) {
						if (trueHijos[i].getNodeName().equals("block")) {
							writeJavaFile(trueHijos[i], fw, tab, null);
						} else {
							writeJavaFile(trueHijos[i], fw, tab + 1, null);
						}
					}
				}

				if (falseHijos != null) {
					s = reinicializarCadena(tab);
					s = s + "else\r\n";
					esc(fw, s);
					for (int i = 0; i < falseHijos.length; i++) {
						if (falseHijos[i].getNodeName().equals("block")) {
							writeJavaFile(falseHijos[i], fw, tab, null);
						} else {
							writeJavaFile(falseHijos[i], fw, tab + 1, null);
						}
					}
				}
			}

			// ***************************************** NODO switch

			else if (e.getNodeName().equals("switch")) {
				s = s + "switch(";
				if (el[0].getNodeName().equals("var-ref")) {
					s = s + el[0].getAttribute("name");
				} else if (el[0].getNodeName().equals("literal-boolean")
						|| el[0].getNodeName().equals("literal-number")
						|| el[0].getNodeName().equals("literal-char")) {
					s = s + el[0].getAttribute("value");
				} else if (el[0].getNodeName().equals("literal-string")) {
					s = s + stringValorString(el[0]);
				} else if (el[0].getNodeName().equals("binary-expr")) {
					s = s
							+ stringExpresion(el[0],
									ManipulacionElement.getChildElements(el[0]));
				} else if (el[0].getNodeName().equals("literal-null")) {
					s = s + "null";
				} else if (el[0].getNodeName().equals("this")) {
					s = s + "this";
				} else if (el[0].getNodeName().equals("send")) {
					s = s
							+ stringSend(el[0],
									ManipulacionElement.getChildElements(el[0]));
				} else if (el[0].getNodeName().equals("field-access")) {
					s = s
							+ stringSendTarget(el[0],
									ManipulacionElement.getChildElements(el[0]))
									+ "." + el[0].getAttribute("field");
				}

				s = s + ")\r\n";
				esc(fw, conv(s));
				s = reinicializarCadena(tab);
				s = s + "{\r\n";
				esc(fw, s);
				s = reinicializarCadena(tab);
				for (int i = 0; i < el.length; i++) {
					if (el[i].getNodeName().equals("switch-block")) {
						writeJavaFile(el[i], fw, tab + 1, null);
					}
				}
				s = reinicializarCadena(tab);
				s = s + "}\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO switch-block
			// (casos de switch, incluido default)

			else if (e.getNodeName().equals("switch-block")) {
				if (el[0].getNodeName().equals("case")) {
					s = s + "case ";
					Element valorTest[] = ManipulacionElement
							.getChildElements(el[0]);
					if (valorTest[0].getNodeName().equals("literal-number")
							|| valorTest[0].getNodeName()
							.equals("literal-char")
							|| valorTest[0].getNodeName().equals(
									"literal-boolean")) {
						s = s + valorTest[0].getAttribute("value");
					} else if (valorTest[0].getNodeName().equals(
							"literal-string")) {
						s = s + stringValorString(valorTest[0]);
					} else if (valorTest[0].getNodeName()
							.equals("literal-null")) {
						s = s + "null";
					} else if (valorTest[0].getNodeName().equals("binary-expr")) {
						s = s
								+ stringExpresion(valorTest[0],
										ManipulacionElement
										.getChildElements(valorTest[0]));
					} else if (valorTest[0].getNodeName().equals("var-ref")) {
						s = s + valorTest[0].getAttribute("name");
					} else if (valorTest[0].getNodeName().equals("paren")) {
						s = s
								+ stringExpresionParen(valorTest[0],
										ManipulacionElement
										.getChildElements(valorTest[0]));
					} else if (valorTest[0].getNodeName().equals("this")) {
						s = s + "this";
					} else if (valorTest[0].getNodeName().equals("send")) {
						s = s
								+ stringSend(valorTest[0],
										ManipulacionElement
										.getChildElements(valorTest[0]));
					} else if (valorTest[0].getNodeName()
							.equals("field-access")) {
						s = s
								+ stringSendTarget(valorTest[0],
										ManipulacionElement
										.getChildElements(valorTest[0]))
										+ "." + valorTest[0].getAttribute("field");
					}
				} else {
					s = s + "default";
				}
				s = s + ":\r\n";
				esc(fw, conv(s));

				for (int i = 1; i < el.length; i++) {
					writeJavaFile(el[i], fw, tab + 1, null);
				}
			}

			// ***************************************** NODO break

			else if (e.getNodeName().equals("break")) {
				s = s + "break;\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO continue

			else if (e.getNodeName().equals("continue")) {
				s = s + "continue;\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO send

			else if (e.getNodeName().equals("send")) {
				s = s + stringSend(e, el) + ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO field-access

			else if (e.getNodeName().equals("field-access")) {
				s = s + stringSendTarget(e, el) + "." + e.getAttribute("field")
						+ ";\r\n";
				esc(fw, s);
			}

			// ***************************************** NODO return

			else if (e.getNodeName().equals("return")) {
				s = s + "return ";

				if (el.length > 0) {
					if (el[0].getNodeName().equals("paren")) {
						s = s
								+ stringExpresionParen(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("cast-expr")) {
						s = s
								+ stringExpresionCasting(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("var-ref")) {
						s = s + el[0].getAttribute("name");
					} else if (el[0].getNodeName().equals("array-ref")) {
						s = s
								+ stringArrayRef(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("new-array")) {
						s = s
								+ stringNewArray(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("new")) {
						s = s
								+ stringNew(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("literal-number")
							|| el[0].getNodeName().equals("literal-char")) {
						s = s + el[0].getAttribute("value");
					} else if (el[0].getNodeName().equals("literal-string")) {
						s = s + stringValorString(el[0]);
					} else if (el[0].getNodeName().equals("literal-boolean")) {
						s = s + el[0].getAttribute("value");
					} else if (el[0].getNodeName().equals("literal-null")) {
						s = s + "null";
					} else if (el[0].getNodeName().equals("this")) {
						s = s + "this";
					} else if (el[0].getNodeName().equals("instanceof-test")) {
						s = s
								+ stringInstanceOf(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("unary-expr")) {
						s = s
								+ stringExpresionU(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("binary-expr")) {
						s = s
								+ stringExpresion(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("conditional-expr")) {
						s = s
								+ stringConditional(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("send")) {
						s = s
								+ stringSend(el[0],
										ManipulacionElement
										.getChildElements(el[0]));
					} else if (el[0].getNodeName().equals("access-field")) {
						s = s
								+ stringSendTarget(el[0],
										ManipulacionElement
										.getChildElements(el[0])) + "."
										+ el[0].getAttribute("field") + "\r\n";
					} else if (el[0].getNodeName().equals("field-access")) {
						s = s
								+ stringSendTarget(el[0],
										ManipulacionElement
										.getChildElements(el[0])) + "."
										+ el[0].getAttribute("field") + "\r\n";
					}
				}
				s = s + ";\r\n";
				esc(fw, conv(s));
			}

			// ***************************************** NODO new
			else if (e.getNodeName().equals("new")) {
				s = s + stringNew(e, el) + ";\r\n";
				esc(fw, s);
			}

			else {
				s = s + "// " + e.getNodeName()
						+ "(sentencia no reconocida)\r\n";
				esc(fw, conv(s));
			}
		}
	}

	/**
	 * Representación String de un nodo "field" o "local-variable"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringFieldLocalVariable(Element e, Element el[]) {
		String s = "";

		// Visibilidad sólo aparece en atributos de clase
		if (e.getNodeName().equals("field")) {
			s = s + e.getAttribute("visibility") + " ";
		}

		if (e.getAttribute("static").equals("true")) {
			s = s + "static ";
		}
		if (e.getAttribute("final").equals("true")) {
			s = s + "final ";
		}

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")
					&& !(el[i].hasAttribute("dimensions"))) {
				if (e.hasAttribute("continued")) {
					s = s + e.getAttribute("name");
				} else {
					s = s + el[i].getAttribute("name") + " "
							+ e.getAttribute("name");
				}
			} else if (el[i].getNodeName().equals("type")
					&& el[i].hasAttribute("dimensions")) {
				s = s + el[i].getAttribute("name") + " "
						+ e.getAttribute("name");
				s = s
						+ ServiciosString.cadenaDimensiones(Integer
								.parseInt(el[i].getAttribute("dimensions")));
			}
		}

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("array-initializer")) {
				s = s
						+ " = "
						+ stringArrayInitializer(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ " = "
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ " = "
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + " = " + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ " = "
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().contains("literal-")
					&& !(el[i].getNodeName().contains("string"))
					&& !(el[i].getNodeName().contains("null"))) {
				s = s + " = " + el[i].getAttribute("value");
			} else if (el[i].getNodeName().contains("literal-string")) {
				s = s + " = " + stringValorString(el[i]);
			} else if (el[i].getNodeName().contains("literal-null")) {
				s = s + " = null";
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + " = " + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("this")) {
				s = s + " = this";
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ " = "
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ " = "
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ " = "
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ " = "
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ " = "
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("paren")) {
				s = s
						+ " = "
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ " = "
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "throw"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringThrow(Element e, Element el[]) {
		String s = "";

		s = s + "throw ";

		if (el[0].getNodeName().equals("var-ref")) {
			s = s + el[0].getAttribute("name");
		} else if (el[0].getNodeName().equals("new")) {
			s = s
					+ stringNew(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("send")) {
			s = s
					+ stringSend(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[0],
							ManipulacionElement.getChildElements(el[0])) + "."
							+ el[0].getAttribute("field");
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "assert"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringAssert(Element e, Element el[]) {
		String s = "";

		s = s + "assert ";
		s = s
				+ stringCondition(el[0],
						ManipulacionElement.getChildElements(el[0]));
		s = s + " : ";

		s = s
				+ stringAssertErrorMessage(el[1],
						ManipulacionElement.getChildElements(el[1]));

		return s;
	}

	/**
	 * Representación String de un nodo de error de una sentencia "assert"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringAssertErrorMessage(Element e, Element el[]) {
		String s = "";

		if (el[0].getNodeName().equals("binary-expr")) {
			s = s
					+ stringExpresion(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("unary-expr")) {
			s = s
					+ stringExpresionU(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("literal-boolean")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("instanceof-test")) {
			s = s
					+ stringInstanceOf(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("conditional-expr")) {
			s = s
					+ stringConditional(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("send")) {
			s = s
					+ stringSend(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("var-ref")) {
			s = s + el[0].getAttribute("name");
		} else if (el[0].getNodeName().equals("array-ref")) {
			s = s
					+ stringArrayRef(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("paren")) {
			s = s
					+ stringExpresionParen(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("cast-expr")) {
			s = s
					+ stringExpresionCasting(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[0],
							ManipulacionElement.getChildElements(el[0])) + "."
							+ el[0].getAttribute("field");
		} else if (el[0].getNodeName().contains("literal-")
				&& !(el[0].getNodeName().contains("string"))
				&& !(el[0].getNodeName().contains("null"))) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().contains("literal-string")) {
			s = s + stringValorString(el[0]);
		} else if (el[0].getNodeName().contains("literal-null")) {
			s = s + "null";
		} else if (el[0].getNodeName().equals("literal-boolean")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("array-initializer")) {
			s = s
					+ stringArrayInitializer(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("new-array")) {
			s = s
					+ stringNewArray(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("new")) {
			s = s
					+ stringNew(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else {
			s = s + "/*no reconocido*/";
		}

		return conv(s);

	}

	/**
	 * Representación String de un nodo "condition"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringCondition(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("instanceof-test")) {
				s = s
						+ stringInstanceOf(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "dimension"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringDimExpr(Element e, Element el[]) {
		String s = "";

		if (el.length > 0) {
			if (el[0].getNodeName().contains("literal-")
					&& !el[0].getNodeName().contains("string")
					&& !el[0].getNodeName().contains("null")) {
				s = s + el[0].getAttribute("value");
			} else if (el[0].getNodeName().contains("literal-string")) {
				s = s + stringValorString(el[0]);
			} else if (el[0].getNodeName().contains("literal-null")) {
				s = s + "null";
			} else if (el[0].getNodeName().equals("literal-boolean")) {
				s = s + el[0].getAttribute("value");
			} else if (el[0].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[0],
								ManipulacionElement.getChildElements(el[0]));
			} else if (el[0].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[0],
								ManipulacionElement.getChildElements(el[0]))
								+ "." + el[0].getAttribute("field");
			} else if (el[0].getNodeName().equals("var-ref")) {
				s = s + el[0].getAttribute("name");
			} else if (el[0].getNodeName().equals("this")) {
				s = s + "this";
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "assignment"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringAssignment(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("lvalue")) {
				Element hijosLValue[] = ManipulacionElement
						.getChildElements(el[i]);
				if (hijosLValue[0].getNodeName().equals("var-ref")) {
					s = s + hijosLValue[0].getAttribute("name")
							+ e.getAttribute("op");
				} else if (hijosLValue[0].getNodeName().equals("array-ref")) {
					s = s
							+ stringArrayRef(hijosLValue[0],
									ManipulacionElement
									.getChildElements(hijosLValue[0]))
									+ e.getAttribute("op");
				} else if (hijosLValue[0].getNodeName().equals("field-access")) {
					s = s
							+ stringSendTarget(hijosLValue[0],
									ManipulacionElement
									.getChildElements(hijosLValue[0]))
									+ "." + hijosLValue[0].getAttribute("field")
									+ e.getAttribute("op");
				}
			}
			if (el[i].getNodeName().equals("literal-number")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-char")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-null")) {
				s = s + "null";
			} else if (el[i].getNodeName().equals("this")) {
				s = s + "this";
			} else if (el[i].getNodeName().equals("literal-string")) {
				s = s + stringValorString(el[i]);
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("assignment-expr")) {
				s = s
						+ stringAssignment(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}
		}
		return conv(s);
	}

	/**
	 * Representación String de un nodo "conditional"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringConditional(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("literal-number")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-char")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-null")) {
				s = s + "null";
			} else if (el[i].getNodeName().equals("this")) {
				s = s + "this";
			} else if (el[i].getNodeName().equals("literal-string")) {
				s = s + stringValorString(el[i]);
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}
			if (i == 0 && el.length > 1) {
				s = s + " ? ";
			}
			if (i == 1 && el.length > 2) {
				s = s + " : ";
			}
		}
		return conv(s);
	}

	/**
	 * Representación String de un nodo "array-ref"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringArrayRef(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			Element hijos[] = ManipulacionElement.getChildElements(el[i]);
			if (el[i].getNodeName().equals("base")) {
				s = s
						+ stringArrayBase(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("offset")) {
				s = s + "[";
				if (hijos[0].getNodeName().equals("paren")) {
					s = s
							+ stringExpresionParen(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				} else if (hijos[0].getNodeName().equals("var-ref")) {
					s = s + hijos[0].getAttribute("name");
				} else if (hijos[0].getNodeName().equals("literal-number")
						|| hijos[0].getNodeName().equals("literal-char")) {
					s = s + hijos[0].getAttribute("value");
				} else if (hijos[0].getNodeName().equals("literal-boolean")) {
					s = s + hijos[0].getAttribute("value");
				} else if (hijos[0].getNodeName().equals("literal-null")) {
					s = s + "null";
				} else if (hijos[0].getNodeName().equals("this")) {
					s = s + "this";
				} else if (hijos[0].getNodeName().equals("literal-string")) {
					s = s + stringValorString(hijos[0]);
				} else if (hijos[0].getNodeName().equals("unary-expr")) {
					s = s
							+ stringExpresionU(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				} else if (hijos[0].getNodeName().equals("binary-expr")) {
					s = s
							+ stringExpresion(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				} else if (hijos[0].getNodeName().equals("conditional-expr")) {
					s = s
							+ stringConditional(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				} else if (hijos[0].getNodeName().equals("cast-expr")) {
					s = s
							+ stringExpresionCasting(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				} else if (hijos[0].getNodeName().equals("field-access")) {
					s = s
							+ stringSendTarget(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0])) + "."
									+ hijos[0].getAttribute("field");
				} else if (hijos[0].getNodeName().equals("array-ref")) {
					s = s
							+ stringArrayRef(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				} else if (hijos[0].getNodeName().equals("send")) {
					s = s
							+ stringSend(hijos[0],
									ManipulacionElement
									.getChildElements(hijos[0]));
				}
				s = s + "]";
			}
		}
		return conv(s);
	}

	/**
	 * Representación String de un nodo "base"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringArrayBase(Element e, Element el[]) {
		String s = "";

		if (el[0].getNodeName().equals("var-ref")) {
			s = s + el[0].getAttribute("name");
		} else if (el[0].getNodeName().equals("array-ref")) {
			s = s
					+ stringArrayRef(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[0],
							ManipulacionElement.getChildElements(el[0])) + "."
							+ el[0].getAttribute("field");
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "initializer"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringArrayInitializer(Element e, Element el[]) {
		String s = " { ";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().contains("literal-")
					&& !el[i].getNodeName().contains("string")
					&& !el[i].getNodeName().contains("null")) {
				s = s + " " + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-string")) {
				s = s + stringValorString(el[i]);
			} else if (el[i].getNodeName().equals("literal-null")) {
				s = s + " null";
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + " " + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + " " + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("this")) {
				s = s + "this";
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("array-initializer")) {
				s = s
						+ stringArrayInitializer(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}

			if (i < (el.length - 1)) {
				s = s + ",";
			} else {
				s = s + " }";
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "new-array"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringNewArray(Element e, Element el[]) {
		String s = "new ";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")) {
				s = s + el[i].getAttribute("name");
			}
		}

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("dim-expr")) {
				s = s
						+ "["
						+ stringDimExpr(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "]";
			}
		}
		
		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("array-initializer")) {
				s = s + stringArrayInitializer(el[i],
						ManipulacionElement.getChildElements(el[i]));
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "send"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringSend(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("target")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ ".";
			}
		}
		s = s + e.getAttribute("message") + "(";
		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("arguments")) {
				s = s
						+ stringSendArguments(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}
		}
		s = s + ")";

		return s;
	}

	/**
	 * Representación String de un nodo "super"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringThisSuper(Element e, Element el[]) {
		String s = "";

		if (e.getNodeName().equals("this-call")) {
			s = s + "this";
		} else if (e.getNodeName().equals("super-call")) {
			s = s + "super";
		}
		s = s + "(";
		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("arguments")) {
				s = s
						+ stringSendArguments(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}
		}
		s = s + ")";

		return s;
	}

	/**
	 * Representación String de un nodo "target"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringSendTarget(Element e, Element el[]) {
		String s = "";

		if (el[0].getNodeName().equals("paren")) {
			s = s
					+ stringExpresionParen(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("var-ref")) {
			s = s + el[0].getAttribute("name");
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[0],
							ManipulacionElement.getChildElements(el[0])) + "."
							+ el[0].getAttribute("field");
		} else if (el[0].getNodeName().equals("send")) {
			s = s
					+ stringSend(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("array-ref")) {
			s = s
					+ stringArrayRef(el[0],
							ManipulacionElement.getChildElements(el[0]))
							+ e.getAttribute("op");
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("super")) {
			s = s + "super";
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo que representa los argumetnos del método
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringSendArguments(Element e, Element el[]) {
		String s = "";

		if (el.length > 0) {
			for (int i = 0; i < el.length; i++) {
				if (el[i].getNodeName().equals("paren")) {
					s = s
							+ stringExpresionParen(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("var-ref")) {
					s = s + el[i].getAttribute("name");
				} else if (el[i].getNodeName().equals("send")) {
					s = s
							+ stringSend(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("literal-number")
						|| el[i].getNodeName().equals("literal-char")) {
					s = s + el[i].getAttribute("value");
				} else if (el[i].getNodeName().equals("literal-boolean")) {
					s = s + el[i].getAttribute("value");
				} else if (el[i].getNodeName().equals("literal-null")) {
					s = s + "null";
				} else if (el[i].getNodeName().equals("this")) {
					s = s + "this";
				} else if (el[i].getNodeName().equals("literal-string")) {
					s = s + stringValorString(el[i]);
				} else if (el[i].getNodeName().equals("unary-expr")) {
					s = s
							+ stringExpresionU(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("binary-expr")) {
					s = s
							+ stringExpresion(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("conditional-expr")) {
					s = s
							+ stringConditional(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("cast-expr")) {
					s = s
							+ stringExpresionCasting(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("new")) {
					s = s
							+ stringNew(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("new-array")) {
					s = s
							+ stringNewArray(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else if (el[i].getNodeName().equals("field-access")) {
					s = s
							+ stringSendTarget(el[i],
									ManipulacionElement.getChildElements(el[i]))
									+ "." + el[i].getAttribute("field");
				} else if (el[i].getNodeName().equals("array-ref")) {
					s = s
							+ stringArrayRef(el[i],
									ManipulacionElement.getChildElements(el[i]));
				} else {
					s = s + "error";
				}
				if (i < el.length - 1) {
					s = s + ",";
				}
			}
		}
		return conv(s);
	}

	/**
	 * Representación String de un nodo "new"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringNew(Element e, Element el[]) {
		String s = "new ";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")) {
				s = s + el[i].getAttribute("name") + "(";
			}
			if (el[i].getNodeName().equals("arguments")) {
				s = s
						+ stringSendArguments(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ ")";
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo que representa expresiones binarias
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringExpresion(Element e, Element el[]) {
		String s = "";

		if (el[0].getNodeName().equals("paren")) {
			s = s
					+ stringExpresionParen(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("var-ref")) {
			s = s + el[0].getAttribute("name");
		} else if (el[0].getNodeName().equals("literal-number")
				|| el[0].getNodeName().equals("literal-char")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("literal-boolean")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("literal-null")) {
			s = s + "null";
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("literal-string")) {
			s = s + stringValorString(el[0]);
		} else if (el[0].getNodeName().equals("unary-expr")) {
			s = s
					+ stringExpresionU(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("conditional-expr")) {
			s = s
					+ stringConditional(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("binary-expr")) {
			s = s
					+ stringExpresion(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("cast-expr")) {
			s = s
					+ stringExpresionCasting(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[0],
							ManipulacionElement.getChildElements(el[0])) + "."
							+ el[0].getAttribute("field");
		} else if (el[0].getNodeName().equals("array-ref")) {
			s = s
					+ stringArrayRef(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("send")) {
			s = s
					+ stringSend(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("new")) {
			s = s
					+ stringNew(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("new-array")) {
			s = s
					+ stringNewArray(el[0],
							ManipulacionElement.getChildElements(el[0]));
		}

		// Adaptación para operadores modificados en JavaParser con el fin de
		// que no se produzcan errores
		// ( '<' y '&' están "prohibidos" )
		if (e.getAttribute("op").equals("_and_")) {
			s = s + " && ";
		} else if (e.getAttribute("op").equals("_and_b_")) {
			s = s + " & ";
		} else if (e.getAttribute("op").equals("menor=")) {
			s = s + " <= ";
		} else if (e.getAttribute("op").equals("menormenor=")) {
			s = s + " <<= ";
		} else if (e.getAttribute("op").equals("menormenormenor")) {
			s = s + " <<< ";
		} else if (e.getAttribute("op").equals("menormenor")) {
			s = s + " << ";
		} else if (e.getAttribute("op").equals("menor")) {
			s = s + " < ";
		} else if (e.getAttribute("op").equals("asp=")) {
			s = s + " &= ";
		} else {
			s = s + " " + e.getAttribute("op") + " ";
		}

		if (el[1].getNodeName().equals("paren")) {
			s = s
					+ stringExpresionParen(el[0],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("var-ref")) {
			s = s + el[1].getAttribute("name");
		} else if (el[1].getNodeName().equals("literal-number")
				|| el[1].getNodeName().equals("literal-char")) {
			s = s + el[1].getAttribute("value");
		} else if (el[1].getNodeName().equals("literal-boolean")) {
			s = s + el[1].getAttribute("value");
		} else if (el[1].getNodeName().equals("literal-null")) {
			s = s + "null";
		} else if (el[1].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[1].getNodeName().equals("literal-string")) {
			s = s + stringValorString(el[1]);
		} else if (el[1].getNodeName().equals("unary-expr")) {
			s = s
					+ stringExpresionU(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("cast-expr")) {
			s = s
					+ stringExpresionCasting(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[1],
							ManipulacionElement.getChildElements(el[1])) + "."
							+ el[1].getAttribute("field");
		} else if (el[1].getNodeName().equals("array-ref")) {
			s = s
					+ stringArrayRef(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("conditional-expr")) {
			s = s
					+ stringConditional(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("binary-expr")) {
			s = s
					+ stringExpresion(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("send")) {
			s = s
					+ stringSend(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("new")) {
			s = s
					+ stringNew(el[1],
							ManipulacionElement.getChildElements(el[1]));
		} else if (el[1].getNodeName().equals("new-array")) {
			s = s
					+ stringNewArray(el[1],
							ManipulacionElement.getChildElements(el[1]));
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo que representa expresiones encerradas en
	 * paréntesis
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringExpresionParen(Element e, Element el[]) {
		String s = "(";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("literal-number")
					|| el[i].getNodeName().equals("literal-char")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-null")) {
				s = s + "null";
			} else if (el[i].getNodeName().equals("this")) {
				s = s + "this";
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-string")) {
				s = s + stringValorString(el[i]);
			} else if (el[i].getNodeName().equals("instanceof-test")) {
				s = s
						+ stringInstanceOf(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[i],
								ManipulacionElement.getChildElements(el[i]));
			}
		}
		s = s + ")";
		return conv(s);
	}

	/**
	 * Representación String de un nodo que representa expresiones de casting
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringExpresionCasting(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")) {
				s = s + "(" + el[i].getAttribute("name") + ")";
			}
		}

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("var-ref")) {
				s = s + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("literal-number")
					|| el[i].getNodeName().equals("literal-char")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-null")) {
				s = s + "null";
			} else if (el[i].getNodeName().equals("this")) {
				s = s + "this";
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-string")) {
				s = s + stringValorString(el[i]);
			} else if (el[i].getNodeName().equals("instanceof-test")) {
				s = s
						+ stringInstanceOf(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[1].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[1],
								ManipulacionElement.getChildElements(el[1]));
			} else if (el[1].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[1],
								ManipulacionElement.getChildElements(el[1]));
			}
		}
		return conv(s);
	}

	/**
	 * Representación String de un nodo que representa expresiones unarias (
	 * ++x, --y )
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringExpresionU(Element e, Element el[]) {
		String s = "";

		if (!e.hasAttribute("post") || e.getAttribute("post").equals("false")) {
			s = s + e.getAttribute("op");
		}

		if (el[0].getNodeName().equals("var-ref")) {
			s = s + el[0].getAttribute("name");
		} else if (el[0].getNodeName().equals("array-ref")) {
			s = s
					+ stringArrayRef(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("literal-number")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("literal-char")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("literal-boolean")) {
			s = s + el[0].getAttribute("value");
		} else if (el[0].getNodeName().equals("literal-string")) {
			s = s + stringValorString(el[0]);
		} else if (el[0].getNodeName().equals("this")) {
			s = s + "this";
		} else if (el[0].getNodeName().equals("cast-expr")) {
			s = s
					+ stringExpresionCasting(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("field-access")) {
			s = s
					+ stringSendTarget(el[0],
							ManipulacionElement.getChildElements(el[0])) + "."
							+ el[0].getAttribute("field");
		} else if (el[0].getNodeName().equals("send")) {
			s = s
					+ stringSend(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("conditional-expr")) {
			s = s
					+ stringConditional(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("new")) {
			s = s
					+ stringNew(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("new-array")) {
			s = s
					+ stringNewArray(el[0],
							ManipulacionElement.getChildElements(el[0]));
		} else if (el[0].getNodeName().equals("paren")) {
			s = s
					+ stringExpresionParen(el[0],
							ManipulacionElement.getChildElements(el[0]));
		}

		if (e.getAttribute("post").equals("true")) {
			s = s + e.getAttribute("op");
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "instance-of"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringInstanceOf(Element e, Element el[]) {
		String s = "";

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("var-ref")) {
				s = s + el[i].getAttribute("name");
			} else if (el[i].getNodeName().equals("this")) {
				s = s + "this";
			} else if (el[i].getNodeName().equals("paren")) {
				s = s
						+ stringExpresionParen(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("array-ref")) {
				s = s
						+ stringArrayRef(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("literal-number")
					|| el[i].getNodeName().equals("literal-char")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-null")) {
				s = s + "null";
			} else if (el[i].getNodeName().equals("literal-boolean")) {
				s = s + el[i].getAttribute("value");
			} else if (el[i].getNodeName().equals("literal-string")) {
				s = s + stringValorString(el[i]);
			} else if (el[i].getNodeName().equals("instanceof-test")) {
				s = s
						+ stringInstanceOf(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("cast-expr")) {
				s = s
						+ stringExpresionCasting(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("unary-expr")) {
				s = s
						+ stringExpresionU(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("field-access")) {
				s = s
						+ stringSendTarget(el[i],
								ManipulacionElement.getChildElements(el[i]))
								+ "." + el[i].getAttribute("field");
			} else if (el[i].getNodeName().equals("binary-expr")) {
				s = s
						+ stringExpresion(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("conditional-expr")) {
				s = s
						+ stringConditional(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[i].getNodeName().equals("send")) {
				s = s
						+ stringSend(el[i],
								ManipulacionElement.getChildElements(el[i]));
			} else if (el[1].getNodeName().equals("new")) {
				s = s
						+ stringNew(el[1],
								ManipulacionElement.getChildElements(el[1]));
			} else if (el[1].getNodeName().equals("new-array")) {
				s = s
						+ stringNewArray(el[1],
								ManipulacionElement.getChildElements(el[1]));
			}
		}

		s = s + " instanceof ";
		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")) {
				s = s + el[i].getAttribute("name");
			}
		}

		return conv(s);
	}

	/**
	 * Representación String de un nodo "formal-arguments"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @param el
	 *            Lista de hijos de e
	 * @return representación String del Element
	 */
	private static String stringParametros(Element e, Element argumentos[]) {
		String s = "";

		for (int i = 0; i < argumentos.length; i++) {
			Element arg_tipo[] = ManipulacionElement
					.getChildElements(argumentos[i]);
			s = s + arg_tipo[0].getAttribute("name") + " "
					+ argumentos[i].getAttribute("name");
			if (arg_tipo[0].hasAttribute("dimensions")) {
				s = s
						+ ServiciosString.cadenaDimensiones(Integer
								.parseInt(arg_tipo[0]
										.getAttribute("dimensions")));
			}

			if (i < argumentos.length - 1) {
				s = s + ", ";
			}
		}

		return conv(s);
	}

	/**
	 * Método que realiza conversiones para lograr restaurar el estado original
	 * de cadenas de caracteres, nombres de variables, etc.
	 * 
	 * @param s
	 *            cadena con caracteres convertidos a secuencias
	 * @return cadena restaurada
	 */
	private static String conv(String s) {
		s = s.replace("[[signomenos]]", "<");
		s = s.replace("[[signomas]]", ">");

		s = s.replace("[[a_acentuado]]", "á");
		s = s.replace("[[e_acentuado]]", "é");
		s = s.replace("[[i_acentuado]]", "í");
		s = s.replace("[[o_acentuado]]", "ó");
		s = s.replace("[[u_acentuado]]", "ú");
		s = s.replace("[[A_acentuado]]", "Á");
		s = s.replace("[[E_acentuado]]", "É");
		s = s.replace("[[I_acentuado]]", "Í");
		s = s.replace("[[O_acentuado]]", "Ó");
		s = s.replace("[[U_acentuado]]", "Ú");
		s = s.replace("[[n_sombrero]]", "ñ");
		s = s.replace("[[N_sombrero]]", "Ñ");
		s = s.replace("[[comillas]]", "\"");

		return s;
	}

	/**
	 * Representación String de un nodo "string"
	 * 
	 * @param e
	 *            Element del que se va a sacar la representación
	 * @return representación String del Element
	 */
	private static String stringValorString(Element e) {
		String s = "\"" + e.getAttribute("value") + "\"";
		return conv(s);
	}

	/**
	 * Reinicializa una cadena para poder esciribr en ella una sentencia, una
	 * vez ha dejado la tabulación correcta
	 * 
	 * @param tab
	 *            número de espacios que se dejarán de tabulación
	 * @return cadena reinicializada, lista para ser escrita
	 */
	private static String reinicializarCadena(int tab) {
		String s = "";
		for (int i = 0; i < tab; i++) {
			s = s + "    ";
		}
		return s;
	}
	
	/**
	 * Crea una nueva clase con el path especificado.
	 * 
	 * @param path Path de la nueva clase.
	 */
	public static void crearClase(String path) {

		String nombreClase = path.substring(0, path.lastIndexOf(".java"));
		nombreClase = nombreClase.substring(nombreClase
				.lastIndexOf(File.separator) + 1);

		FileWriter fw = null;
		try {
			fw = new FileWriter(path);
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 1");
		}

		esc(fw, "public class " + nombreClase
				+ "\r\n{\r\n\r\n\r\n\r\n\r\n\r\n\r\n}\r\n");

		try {
			fw.close();
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 2");
		}

	}

	/**
	 * Escribe una cadena en un fichero
	 * 
	 * @param fw
	 *            descriptor de fichero en el que se escribirá
	 * @param s
	 *            cadena que se escribirá en el fichero
	 */
	private static void esc(FileWriter fw, String s) {
		try {
			fw.write(s);
			if (ver_codigo_terminal) {
				System.out.print(s);
			}
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 2");
		}
	}
}