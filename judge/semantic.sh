cat > code.txt
java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar Compiler.Main

# this script is called when the judge wants our compiler to compile a source file.
# print the compiled source, i.e. asm code, directly to stdout.
# don't print anything other to stdout.
# if you would like to print some debug information, please go to stderr.

# set -e
# export CCHK="java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar Compiler.Main"
# cat > data.in   # save everything in stdin to program.txt
# $CCHK