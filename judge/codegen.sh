cat > code.txt

java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar Compiler.Main -codegen > out.txt
cat out.txt