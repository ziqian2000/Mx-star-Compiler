set -e
cd "$(dirname "$0")"
find ./src/Compiler -name *.java | javac -cp "./lib/commons-text-1.6.jar:./lib/commons-lang3-3.8.1.jar:./lib/antlr-4.8-complete.jar" src/Compiler/Main.java @/dev/stdin -d bin
