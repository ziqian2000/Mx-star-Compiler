cat > code.txt
res=`java -cp .:antlr-4.8-complete.jar:commons-text-1.6.jar Compiler.Main`
echo $res
