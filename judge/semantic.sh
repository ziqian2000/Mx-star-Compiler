set -e
cd "$(dirname "$0")"
cat > code.txt
java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar:commons-lang3-3.8.1.jar Compiler.Main