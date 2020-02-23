grammar test;

s : expr EOF;

expr    :   expr '*' expr
		|   expr '+' expr
		|   Int
		;
Int : [0-9]+;