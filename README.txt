**************************************
*     HISTORIAL DE MODIFICACIONES    *
**************************************

SRec v1.4
=========

- Se ha añadido un sistema de control de versiones para SRec (git).

- Se ha añadido documentación (en formato java doc) con explicación y especificación para cada clase y método de la aplicación (Sobre el 5% de la aplicación estaba previamente documentado, pero desactualizado):
    - Todos los métodos públicos y privados de la aplicación: 1625 métodos.
    - Todos los constructores públicos y privados de la aplicación: 135 constructores.
    - Todas las clases de la aplicación: 106 clases.

- Se ha generado la documentación de toda la aplicación en formato javadoc (html).

- Se ha corregido y estandarizado todo el código fuente de la aplicación.

- Se ha eliminado el código comentado, obsoleto o inalcanzable de toda la aplicación, algunos números:
    - 251 métodos eliminados.
    - 30 constructores eliminados.
    - 312 variables eliminadas.
    - 4 clases eliminadas.

- Se han eliminado todos los ficheros comprimidos con código o recursos obsoletos.

- Se han refactorizado los botones de la aplicación, ofreciendo una interfaz de métodos mucho más semántica y sencilla.

- Se han corregido leaks de memoria que hacían que con sucesivas ejecuciones de algoritmos la aplicación fuese más lenta.

- Se ha añadido funcionalidad para mantener la visibilidad de métodos y parámetros en sucesivas ejecuciones de un mismo método.

- Se han corregido diversos problemas de concurrencia en el panel de código de la aplicación:
    - En muchas ocasiones la barra de numeración de líneas desaparecía o aparecía duplicada cuando la vista se actualizaba.
    - También se ha corregido un problema por el que la vista hacía scroll hasta el final en muchas ocasiones.

- Se han eliminado del código fuente los ficheros de opciones que definen la configuración para un usuario/máquina específicos, modificando algunas opciones por defecto en SRec, esto permite que cada nueva distribución de SRec parta de un estado limpio y configurable por el usuario, y evita los problemas por configuraciones específicas para una determinada máquina.

- Se ha añadido un sistema de build basado en ant que permite la automatizar la compilación y generación de versiones para distribución de SRec. Esto también permite poner SRec en cualquier entorno de integración continua. Tras añadir este punto y con todas las tareas de limpieza aplicadas, se ha revisado la generación del jar distribuible, que ha pasado de ocupar 2241 Kb a 569 Kb (Únicamente tamaño de jar ejecutable, sin contar ficheros auxiliares como imágenes que deben incluirse fuera del jar en el paquete de distribución).

SRec v1.3
=========

** PROBLEMAS CON JAVA 7
 * No se queda bloqueado al compilar clases con JDK 7.
 * Solucionados porblemas al seleccionar colores en JDK 7.
 * Visualización correcta del panel de personalización de colores en JDK 7.
 * No se queda bloqueado al seleccionar NO en activar la vista DYV con JDK 7.

** PROBLEMAS PUNTUALES
 * Solucionado problema de codificación de archivos XML. Admite ficheros y directorios con tildes o ñ.
 * Solucionado problema de "CLASE VACIA" con la clase "RecursivosPD.java". [Problema de atributos entre métodos].
 * Solucionado problema con la clase "AlineacionSecuencias.java". Ya se pueden declarar variables del tipo "int a, b, c;"
 * Solucionado problema al generar parámetros aleatorios. Se han activado por defecto.
 * Solucionado problema que impedia seleccionar algunas opciones después de cambiar el idioma.

** ENTRADA DE DATOS
 * Modificada la asignación de parámetros para admitir unicamente char de la forma 'a' y String de la forma "abc" (comillas simples y dobles).
 * Modificada la generación de parámetros aleatorios para crearlos de la forma 'a' y String de la forma "abc" (comillas simples y dobles).
 * Vista de árbol, de pila y de traza muestran las comillas en los caracteres y strings tal y como se introducen.

** NUEVA ANIMACIÓN
 * Se ha separado en dos funciones distintas la selección del método a ejecutar y la asignación de parámetros para lanzar animaciones.
 * Antes de seleccionar método, comprueba si la clase ha sido modificada, y en caso afirmativo, ofrece guardar y procesar. 
 * Solucionado el error que desactiva la opción para procesar clases cuando no debe. 

** BOTONES DEL RATÓN
 * Se ha añadido la opción "Resaltar nodo" haciendo click derecho sobre un nodo en la vista de árbol.
 * Ahora se puede cambiar el color para la opción "Resaltar nodo".
 * Se han añadido las mismas opciones del botón derecho del ratón para la vista de pila. 

** ESTRUCTURA DE MENÚS
 * Nueva estructuración de los menús "Filtrado y Selección", "Árbol" y "Traza".
 * Nueva opción de "Sangrado" en el menú de "Traza".
 * Nueva opción de "Identificador de método" en el menú de "Traza".
 * Nueva opción de "Solo estructura de datos en DYV" en el menú de "Traza".
 * Nueva opción "Arrancar animación en estado inicial".
 * La opción "Identificardor de método" ahora es común para las vistas de árbol, pila y traza, mostrando los mismos prefijos.
 * Las opciones específicas de DYV aparecen deshabilitadas cuando las vistas de DYV no están activadas.
 * Atenuación de las opciones de menú desactivadas.
++++++++[NUEVO] * Se ha modificado la barra de herramientas para adaptarla a la nueva estructura de menus.

** FUSIÓN ANIMACIONES DE TRAZA Y CRONOLÓGICA
 * Se han fusionado las vistas de traza y cronológica, de modo que la primera aparece si DYV no está activado, y la segunda en el caso contrario.
 * Ahora la opción de "Orden Cronólogico" se aplica a ambas modalidades de vista.

** VISUALIZACIÓN
 * La vista global del árbol se ajusta de manerá dinámica al espacio disponible.
 * El ajuste de la vista global se controla desde una nueva opción del menú "Árbol".
 * Nueva opción del menú árbol para desplazar dinamicamente la visualización del mismo hacia el último nodo creado.
++++++++[NUEVO] * Solucionado el problema que provocaba que el visor "saltara" hacia la posición inicial cuando se movia fuera del árbol.
++++++++[NUEVO] * Solucionado problema que provocaba comportamientos extraños durante el desplazamiento automático del visor cuando estaba activado el ancho dinámico del árbol.

** DIÁLOGO ACTIVACIÓN DIVIDE Y VENCERAS
 * Cuando el número de parámetros del método a activar sea menor que diez (y se introduzca un numero -no letras-), el cursor irá saltando automaticamente de una caja de texto a la siguiente, haciendo este proceso más dinámico.
 * Se puede saltar de un campo de texto a otro utilizando las teclas arriba y abajo. 

** OTROS DIÁLOGOS
++++++++[NUEVO] * Se han añadido botones de cancelar en todos los diálogos para dar homogeneidad al programa.
++++++++[NUEVO] * Se ha añadido un diálogo de información cuando la Máquina virtual de Java se configura correctamente.
++++++++[NUEVO] * Se ha suprimido el diálogo de error para configurar la JVM en la primera ejecución de SRec.

** EXPORTAR CAPTURAS
++++++++[NUEVO] * Ahora se pueden hacer exportaciones sin problemas para las vistas de Árbol, Pila y Traza (solo en DYV). Estructura solo se permiten capturas del estado actual.