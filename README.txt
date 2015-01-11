**************************************
*     HISTORIAL DE MODIFICACIONES    *
**************************************

SRec v1.6 (Programaci�n Din�mica)
==================================================

- Se ha a�adido funcionalidad para generar grafos de dependencia en SRec.

- Seg�n se visualizan ejecuciones, el usuario ahora dispone de un nuevo bot�n que permite generar con una organizaci�n
autom�tica el conjunto de nodos que forman el grafo de dependencia de la ejecuci�n actual.

- Si la ejecuci�n actual se compone de distintos m�todos, el usuario puede elegir previamente el m�todo para el que generar
el grafo de dependencia.

- Se ha a�adido soporte para aristas no lineales en SRec, ahora se dispone de un nuevo enrutador de aristas que dibuja aristas
parab�licas que rodean los nodos si la trayectoria de la arista atraviesa alguno de los nodos.

- Se ha a�adido funcionalidad para que el usuario pueda reorganizar manualmente los nodos del grafo.

- Se ha a�adido funcionalidad para que los nodos del grafo respeten los valores de configuraci�n para visibilidad de valores de entrada,
de salida, o ambos. As� como los valores de configuraci�n para visibilidad de par�metros.

- Se ha a�adido funcionalidad para que el usuario pueda especificar un valor de filas y columnas para dibujar detr�s del grafo de dependencia,
una matriz donde el usuario pueda reorganizar los nodos y tabularlos manualmente. 

- Se ha a�adido funcionalidad para que el usuario pueda tabular autom�ticamente los nodos del grafo de dependencia en una matriz, dadas
cualquier expresi�n para filas y una expresi�n para columnas, pudiendo relacionar los par�metros de entrada con los �ndices de la matriz.

- Se ha a�adido funcionalidad para que el usuario pueda exportar una captura del grafo de dependencia.

- Se ha a�adido funcionalidad para que el usuario pueda aumentar, reducir, y ajusta el tama�o de zoom de la ventana del grafo de dependencia.

- Se ha a�adido un cuadro de proceso para cuando el algoritmo se est� ejecutando, con un bot�n cancelar que permite detener la ejecuci�n
en cualquier momento. Tambi�n se ha limitado el n�mero de nodos que puede generar un algoritmo a 350, de modo que puedan evitarse errores
debidos a falta de memoria cuando las ejecuciones de algoritmos son exageradamente grandes.

- Se han modificado algunos de los iconos de la aplicaci�n para que resulten m�s intuitivos.

SRec v1.5 (M�ltiples Visualizaciones)
==================================================

- Se ha a�adido soporte para m�ltiples visualizaciones de algoritmos en SRec.

- SRec ahora puede tomar valores m�ltiples para la introducci�n de par�metros, basta con especificar los valores
separados por comas, o mediante la notaci�n x..y que incluye todos los valores desde x a y, en orden ascendiente o descendiente.

- El cuadro de introducci�n de par�metros ahora permite visualizar los valores para el conjunto de ejecuciones que ser�n lanzadas
mediante el bot�n "Ver Valores...".

- Al igual que anteriormente, si el formato del conjunto de valores especificado es err�neo o contiene tipos incorrectos para el par�metro,
SRec no permitir� lanzar la ejecuci�n, notificando acerca de los par�metros con valores incorrectos, esto tambi�n se aplica cuando se
desea ver el conjunto de valores para las distintas ejecuciones.

- Al pulsar sobre "Ver valores..." se muestra un nuevo cuadro de di�logo que permite visualizar estos valores, la longitud de esta tabla
se ajusta autom�ticamente a los valores de las ejecuciones, permitiendo ver claramente los conjuntos de valores sin necesidad de ajustar
su tama�o manualmente. Por otro lado, el viewport de la tabla mostrar� un m�ximo de 10 valores, siendo desplazable cuando se supere dicho
n�mero.

- El conjunto de m�ltiples ejecuciones se calcula con el producto cartesiano de los valores especificados para cada par�metro, lo que significa
que se lanzar� una ejecuci�n por cada combinaci�n de valores posible, los valores para las ejecuciones, son visualizables desde el cuadro
"Ver valores..."

- Una vez que se especifican los valores mediante el cuadro de introducci�n de par�metros, SRec comprueba si se trata de una o varias ejecuciones.
En el caso de ser varias, se activa el modo de m�ltiples visualizaciones de SRec.

- En el modo de m�ltiples visualizaciones de SRec, todos los paneles de visualizaci�n del estado de la ejecuci�n activa se colocan en el primer
panel, dejando el segundo panel para las previsualizaciones del conjunto de �rboles, en este caso el primer panel se establece para que ocupe
un 70% del espacio disponible, dejando el 30% restante para las previsualizaciones.

- Cuando el modo de m�ltiples visualizaciones de SRec est� activo, mediante la opci�n "Ubicaci�n de Vistas y Paneles", se permite cambiar
la orientaci�n del panel de previsualizaciones, de este modo el usuario puede decidir si usar una orientaci�n u otra, siendo m�s �til la
visualizaci�n horizontal para �rboles que crecen en anchura, y la visualizaci�n vertical para �rboles que crecen en profundidad.

- Por otro lado, cuando el modo de m�ltiples visualizaciones est� activo, se deshabilitan tanto las opciones de recolocaci�n de paneles como
las opciones de zoom para el panel de previsualizaciones.

- El usuario puede visualizar las distintas ejecuciones simplemente con hacer click sobre ellas, est�s ejecuciones se lanzar�n en su estado final,
permitiendo al usuario utilizar las herramientas de animaci�n para ir atr�s y adelante en la ejecuci�n. La ejecuci�n actualmente activa se
identifica en el panel de previsualizaciones mediante un marco de selecci�n.

- En todo momento el usuario puede cerrar la visualizaci�n de la misma manera que se hac�a anteriormente, pulsando sobre la "X" del panel de
animaciones, esto restaura los paneles de visualizaci�n para ejecutar cualquier otro algoritmo.

- Se ha modificado la opci�n "Identificador de m�todo" para que al activarse o desactivarse, tenga efecto inmediato sobre todas las
previsualizaciones que se est�n mostrando, actualizando todas ellas cuando la opci�n se activa o se desactiva.

- Se han modificado las opciones aplicables desde "Formato tipogr�fico" para que al modificarse dichas opciones, tengan efecto sobre todas las
previsualizaciones que se est�n mostrando, actualizando todas ellas cuando cambia alg�n elemento de configuraci�n de fuentes o colores.

- Se ha modificado la opci�n "Datos de entrada y salida..." para que al seleccionar la visibilidad de las celdas de entrada y salida, tenga
efecto inmediato sobre todas las previsualizaciones que se est�n mostrando.

- Se ha modificado la opci�n "M�todos y par�metros..." para que al seleccionar filtros de visibilidad para m�todos internos y par�metros, tenga
efecto inmediato sobre todas las previsualizaciones que se est�n mostrando.

- Se ha modificado la opci�n "Llamadas terminadas..." para que al seleccionar distintos valores de configuraci�n, tengan efecto inmediato sobre
todas las previsualizaciones que se est�n mostrando.

- Se ha modificado la opci�n "Estructura de datos en DYV..." para que al activarla o desactivarla, tenga efecto inmediato sobre todas las
previsualizaciones que se est�n mostrando.

- Se ha a�adido tests de JUnit a la aplicaci�n (se encuentran en el paquete tests) para validar autom�ticamente los escenarios de introducci�n
de par�metros.

- Se han corregido diversos problemas de concurrencia en la visualizaci�n de �rboles que provocaban que la aplicaci�n presentara inconsistencias
y hac�an que en ciertas ocasiones algunas aristas entre nodos no se pintaran correctamente.

SRec v1.4 (Limpieza, Documentaci�n, Sistema de build y Bugfixing)
=================================================================

- Se ha a�adido un sistema de control de versiones para SRec (git).

- Se ha a�adido documentaci�n (en formato java doc) con explicaci�n y especificaci�n para cada clase y m�todo de la aplicaci�n (Sobre el 5% de la aplicaci�n estaba previamente documentado, pero desactualizado):
    - Todos los m�todos p�blicos y privados de la aplicaci�n: 1625 m�todos.
    - Todos los constructores p�blicos y privados de la aplicaci�n: 135 constructores.
    - Todas las clases de la aplicaci�n: 106 clases.

- Se ha generado la documentaci�n de toda la aplicaci�n en formato javadoc (html).

- Se ha corregido y estandarizado todo el c�digo fuente de la aplicaci�n.

- Se ha eliminado el c�digo comentado, obsoleto o inalcanzable de toda la aplicaci�n, algunos n�meros:
    - 251 m�todos eliminados.
    - 30 constructores eliminados.
    - 312 variables eliminadas.
    - 4 clases eliminadas.

- Se han eliminado todos los ficheros comprimidos con c�digo o recursos obsoletos.

- Se han refactorizado los botones de la aplicaci�n, ofreciendo una interfaz de m�todos mucho m�s sem�ntica y sencilla.

- Se han corregido leaks de memoria que hac�an que con sucesivas ejecuciones de algoritmos la aplicaci�n fuese m�s lenta.

- Se ha a�adido funcionalidad para mantener la visibilidad de m�todos y par�metros en sucesivas ejecuciones de un mismo m�todo.

- Se han corregido diversos problemas de concurrencia en el panel de c�digo de la aplicaci�n:
    - En muchas ocasiones la barra de numeraci�n de l�neas desaparec�a o aparec�a duplicada cuando la vista se actualizaba.
    - Tambi�n se ha corregido un problema por el que la vista hac�a scroll hasta el final en muchas ocasiones.

- Se han eliminado del c�digo fuente los ficheros de opciones que definen la configuraci�n para un usuario/m�quina espec�ficos, modificando algunas opciones por defecto en SRec, esto permite que cada nueva distribuci�n de SRec parta de un estado limpio y configurable por el usuario, y evita los problemas por configuraciones espec�ficas para una determinada m�quina.

- Se ha a�adido un sistema de build basado en ant que permite la automatizar la compilaci�n y generaci�n de versiones para distribuci�n de SRec. Esto tambi�n permite poner SRec en cualquier entorno de integraci�n continua. Tras a�adir este punto y con todas las tareas de limpieza aplicadas, se ha revisado la generaci�n del jar distribuible, que ha pasado de ocupar 2241 Kb a 569 Kb (�nicamente tama�o de jar ejecutable, sin contar ficheros auxiliares como im�genes que deben incluirse fuera del jar en el paquete de distribuci�n).

SRec v1.3
=========

** PROBLEMAS CON JAVA 7
 * No se queda bloqueado al compilar clases con JDK 7.
 * Solucionados porblemas al seleccionar colores en JDK 7.
 * Visualizaci�n correcta del panel de personalizaci�n de colores en JDK 7.
 * No se queda bloqueado al seleccionar NO en activar la vista DYV con JDK 7.

** PROBLEMAS PUNTUALES
 * Solucionado problema de codificaci�n de archivos XML. Admite ficheros y directorios con tildes o �.
 * Solucionado problema de "CLASE VACIA" con la clase "RecursivosPD.java". [Problema de atributos entre m�todos].
 * Solucionado problema con la clase "AlineacionSecuencias.java". Ya se pueden declarar variables del tipo "int a, b, c;"
 * Solucionado problema al generar par�metros aleatorios. Se han activado por defecto.
 * Solucionado problema que impedia seleccionar algunas opciones despu�s de cambiar el idioma.

** ENTRADA DE DATOS
 * Modificada la asignaci�n de par�metros para admitir unicamente char de la forma 'a' y String de la forma "abc" (comillas simples y dobles).
 * Modificada la generaci�n de par�metros aleatorios para crearlos de la forma 'a' y String de la forma "abc" (comillas simples y dobles).
 * Vista de �rbol, de pila y de traza muestran las comillas en los caracteres y strings tal y como se introducen.

** NUEVA ANIMACI�N
 * Se ha separado en dos funciones distintas la selecci�n del m�todo a ejecutar y la asignaci�n de par�metros para lanzar animaciones.
 * Antes de seleccionar m�todo, comprueba si la clase ha sido modificada, y en caso afirmativo, ofrece guardar y procesar. 
 * Solucionado el error que desactiva la opci�n para procesar clases cuando no debe. 

** BOTONES DEL RAT�N
 * Se ha a�adido la opci�n "Resaltar nodo" haciendo click derecho sobre un nodo en la vista de �rbol.
 * Ahora se puede cambiar el color para la opci�n "Resaltar nodo".
 * Se han a�adido las mismas opciones del bot�n derecho del rat�n para la vista de pila. 

** ESTRUCTURA DE MEN�S
 * Nueva estructuraci�n de los men�s "Filtrado y Selecci�n", "�rbol" y "Traza".
 * Nueva opci�n de "Sangrado" en el men� de "Traza".
 * Nueva opci�n de "Identificador de m�todo" en el men� de "Traza".
 * Nueva opci�n de "Solo estructura de datos en DYV" en el men� de "Traza".
 * Nueva opci�n "Arrancar animaci�n en estado inicial".
 * La opci�n "Identificardor de m�todo" ahora es com�n para las vistas de �rbol, pila y traza, mostrando los mismos prefijos.
 * Las opciones espec�ficas de DYV aparecen deshabilitadas cuando las vistas de DYV no est�n activadas.
 * Atenuaci�n de las opciones de men� desactivadas.
++++++++[NUEVO] * Se ha modificado la barra de herramientas para adaptarla a la nueva estructura de menus.

** FUSI�N ANIMACIONES DE TRAZA Y CRONOL�GICA
 * Se han fusionado las vistas de traza y cronol�gica, de modo que la primera aparece si DYV no est� activado, y la segunda en el caso contrario.
 * Ahora la opci�n de "Orden Cron�logico" se aplica a ambas modalidades de vista.

** VISUALIZACI�N
 * La vista global del �rbol se ajusta de maner� din�mica al espacio disponible.
 * El ajuste de la vista global se controla desde una nueva opci�n del men� "�rbol".
 * Nueva opci�n del men� �rbol para desplazar dinamicamente la visualizaci�n del mismo hacia el �ltimo nodo creado.
++++++++[NUEVO] * Solucionado el problema que provocaba que el visor "saltara" hacia la posici�n inicial cuando se movia fuera del �rbol.
++++++++[NUEVO] * Solucionado problema que provocaba comportamientos extra�os durante el desplazamiento autom�tico del visor cuando estaba activado el ancho din�mico del �rbol.

** DI�LOGO ACTIVACI�N DIVIDE Y VENCERAS
 * Cuando el n�mero de par�metros del m�todo a activar sea menor que diez (y se introduzca un numero -no letras-), el cursor ir� saltando automaticamente de una caja de texto a la siguiente, haciendo este proceso m�s din�mico.
 * Se puede saltar de un campo de texto a otro utilizando las teclas arriba y abajo. 

** OTROS DI�LOGOS
++++++++[NUEVO] * Se han a�adido botones de cancelar en todos los di�logos para dar homogeneidad al programa.
++++++++[NUEVO] * Se ha a�adido un di�logo de informaci�n cuando la M�quina virtual de Java se configura correctamente.
++++++++[NUEVO] * Se ha suprimido el di�logo de error para configurar la JVM en la primera ejecuci�n de SRec.

** EXPORTAR CAPTURAS
++++++++[NUEVO] * Ahora se pueden hacer exportaciones sin problemas para las vistas de �rbol, Pila y Traza (solo en DYV). Estructura solo se permiten capturas del estado actual.