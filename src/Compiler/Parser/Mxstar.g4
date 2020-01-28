grammar Mxstar;

program
	:   declaration*
	;

declaration
	:   classDecl
	|   functionDecl
	|   variableDecl
	;

classDecl
	:   Class Identifier '{' (functionDecl | variableDecl)* '}'
	;

functionDecl
	:   functionType? Identifier '(' paraDeclList? ')' compoundStmt
	;

variableDecl
	:   type variableList ';'
	;

variableList
	:   singleVariable (',' singleVariable)*
	;

singleVariable
	:   Identifier ('=' expression)?
	;

paraDeclList
	:   singleParaDecl (',' singleParaDecl)*
	;

singleParaDecl
	:   type Identifier
	;

paraList
	:   expression (',' expression)*
	;

functionType
	:   type
	|   Void
	;

type
	:   type '[' ']'                                                						#ArrayType
	|   nonArrayType                                                						#NArrayType
	;

nonArrayType
	:   Int                                                         						#NArrayTypeInt
	|   Bool                                                        						#NArrayTypeBool
	|   String                                                      						#NArrayTypeString
	|   Identifier                                                  						#NArrayTypeId
	;

statement
	:   compoundStmt                                                                        #BlockStmt
	|   variableDecl                                                                        #VarDeclStmt
	|   expression ';'                                                                      #ExprStmt
	|   controlStatement                                                                    #ControlStmt
	;

compoundStmt
	:   '{' statement* '}'
	;

controlStatement
	:   If '(' expression ')' thenStmt=statement (Else elseStmt=statement)?                 #IfStmt
	|   For '(' init=expression? ';' cond=expression? ';' step=expression? ')' statement    #ForStmt
	|   While   '(' expression ')'  statement                       						#WhileStmt
	|   Return expression? ';'                                      						#ReturnStmt
	|   Continue ';'                                                						#ContinueStmt
	|   Break ';'                                                   						#BreakStmt
	;

expression
//  --------------- Other Expressions
	:   Identifier                                                  						#IdExpr
	|   This                                                        						#ThisExpr
	|   constant                                                    						#ConstExpr
	|   '(' expression')'                                           						#SubExpr
	|   expression '.' Identifier                                   						#MemberAccessExpr
	|   array=expression '[' index=expression ']'                   						#SubscriptExpr
	|   expression '(' paraList ')'                            						#FunctionCallExpr
	|   'new' creator                                               						#NewExpr
//  --------------- Unary Operation Expressions
	|   expression op=('++' | '--')                                 						#PostfixOpExpr
	|   <assoc=right>
		op=('++' | '--') expression                                 						#UnaryOpExpr
	|   <assoc=right>
		op=('+' | '-') expression                                   						#UnaryOpExpr
	|   <assoc=right>
		op=('!' | '~') expression                     						                #UnaryOpExpr
//  --------------- Binary Operation Expressions
	|	lhs=expression op=('*' | '/' | '%')    rhs=expression     		    				#BinaryExpr
	|	lhs=expression op=('+' | '-')          rhs=expression								#BinaryExpr
	|	lhs=expression op=('<<' | '>>')        rhs=expression	    						#BinaryExpr
	|	lhs=expression op=('<' | '>')          rhs=expression								#BinaryExpr
	|	lhs=expression op=('<=' | '>=')        rhs=expression								#BinaryExpr
	|	lhs=expression op=('==' | '!=')        rhs=expression								#BinaryExpr
	|	lhs=expression op='&'                  rhs=expression								#BinaryExpr
	|	lhs=expression op='^'                  rhs=expression								#BinaryExpr
	|	lhs=expression op='|'                  rhs=expression								#BinaryExpr
	|	lhs=expression op='&&'                 rhs=expression								#BinaryExpr
	|   lhs=expression op='||'                 rhs=expression								#BinaryExpr
    |   <assoc=right>
        lhs=expression op='='                  rhs=expression     						    #BinaryExpr
	;

creator
	:   nonArrayType ('[' expression ']')+ ('[' ']')*               						#ArrayCreator
	|   nonArrayType                                                						#NArrayCreator
	|   nonArrayType '('  ')'                                       						#ClassCreator
	;

constant
	:   IntegerConstant                                             						#IntegerConstant
	|   StringConstant                                              						#IntegerConstant
	|   NullConstant                                                						#IntegerConstant
	|   BoolConstant                                                						#IntegerConstant
	;

//  --------------------------------------------------- Reserved Words
Int     : 'int';
Bool    : 'bool';
String  : 'string';
Void    : 'void';
If      : 'if';
Else    : 'else';
For     : 'for';
While   : 'while';
Break   : 'break';
Continue: 'continue';
Return  : 'return';
New     : 'new';
Class   : 'class';
This    : 'this';
fragment Null    : 'null';
fragment True    : 'true';
fragment False   : 'false';

//  --------------------------------------------------- Constant
IntegerConstant
	:   [0-9]+
	;

StringConstant
	:   '"' ('\\"' | .)*? '"'
	;

NullConstant
	:   Null
	;

BoolConstant
	:   True | False
	;

//  --------------------------------------------------- Identifier
Identifier  : [a-zA-Z] + [a-zA-Z0-9_]*;

//  --------------------------------------------------- Skip
WhiteSpace
	:   [ \t]+
		-> skip
	;

NewLine
	:   ('\r' | '\n' | '\r' '\n')
		-> skip
	;

BlockComment
	:   '/*' .*? '*/'
		-> skip
	;

LineComment
	:   '//' ~[\r\n]*
		-> skip
	;