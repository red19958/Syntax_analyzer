cd ..\
SET lab_2_translation_methods=%CD%
SET tmp=%lab_2_translation_methods%\tmp
mkdir %tmp%
javac -cp lib\junit-4.11.jar;lib\hamcrest-core-1.3.jar src\*.java -d %tmp%
cd %tmp%
java -cp ..\lib\junit-4.11.jar;..\lib\hamcrest-core-1.3.jar;%tmp% org.junit.runner.JUnitCore TestParser
cd ..
rmdir /s /q %tmp%
pause