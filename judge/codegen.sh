cat > code.txt

# java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar:commons-lang3-3.8.1.jar Compiler.Main > out.txt
# cat out.txt

res=`java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar:commons-lang3-3.8.1.jar Compiler.Main < in.txt`
echo $res"\c"
# echo $res"\c" | sed 's/ /\n/g ' # since echo will replace '\n' with 'space'
