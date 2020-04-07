set -e
cd "$(dirname "$0")"
cat > code.txt
java -cp .:commons-text-1.6.jar:commons-lang3-3.8.1.jar:antlr-4.8-complete.jar Compiler.Main -semantic