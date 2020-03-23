cp ../src/Compiler/* Compiler/ -r
javac -cp commons-text-1.6.jar:antlr-4.8-complete.jar Compiler/Main.java Compiler/*/*.java Compiler/*/*/*.java