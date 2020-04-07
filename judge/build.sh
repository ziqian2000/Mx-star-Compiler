set -e
cd "$(dirname "$0")"
cp ../src/Compiler/* Compiler/ -r
find Compiler/ -name *.java | javac -cp "./commons-text-1.6.jar:./commons-lang3-3.8.1.jar:./antlr-4.8-complete.jar" Compiler/Main.java @/dev/stdin