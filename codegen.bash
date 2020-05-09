set -e
cd "$(dirname "$0")"
cat > code.txt
java -cp "./bin:./lib/commons-text-1.6.jar:./lib/commons-lang3-3.8.1.jar:./lib/antlr-4.8-complete.jar" Compiler.Main -codegen
cp ./test.s output.s