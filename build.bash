set -e
cd "$(dirname "$0")"
cd judge/
find Compiler/ -name *.java | javac -cp "commons-text-1.6.jar:antlr-4.8-complete.jar:commons-lang3-3.8.1.jar" Compiler/Main.java @/dev/stdin