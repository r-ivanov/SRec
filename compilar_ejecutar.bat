@echo off
del *.class
cd botones
del *.class
cd ../conf
del *.class
cd ../cuadros
del *.class
cd ../datos
del *.class
cd ../eventos
del *.class
cd ../grafica
del *.class
cd ../opciones
del *.class
cd ../paneles
del *.class
cd ../utilidades
del *.class
cd ../ventanas
del *.class
cd ..
cls
"C:\Archivos de programa\Java\jdk1.6.0_01\bin\javac" -Xlint SRec.java & java -Xmx256M SRec