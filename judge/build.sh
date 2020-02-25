cp ../src/Compiler/* Compiler/ -r
javac -cp *.jar Compiler/Main.java Compiler/AST/*.java Compiler/Parser/*.java Compiler/SemanticAnalysis/*.java Compiler/Utils/*.java