**************************************
*     HISTORIAL DE MODIFICACIONES    *
**************************************

SRec v1.4
=========

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