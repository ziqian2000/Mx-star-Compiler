set -e
cd "$(dirname "$0")"
cd judge/
cat > code.txt
java -cp .:antlr-4.8-complete.jar:commons-lang3-3.8.1.jar:commons-text-1.6.jar Compiler.Main -codegen
cp test.s ../output.s
cd ..

