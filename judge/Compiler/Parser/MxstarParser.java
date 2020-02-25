// Generated from C:/Users/runzhe/IdeaProjects/Compiler/src/Compiler/Parser\Mxstar.g4 by ANTLR 4.8
package Compiler.Parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxstarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, Int=33, Bool=34, String=35, Void=36, If=37, Else=38, For=39, 
		While=40, Break=41, Continue=42, Return=43, New=44, Class=45, This=46, 
		IntegerConstant=47, StringConstant=48, NullConstant=49, BoolConstant=50, 
		Identifier=51, WhiteSpace=52, NewLine=53, BlockComment=54, LineComment=55;
	public static final int
		RULE_program = 0, RULE_declaration = 1, RULE_classDecl = 2, RULE_functionDecl = 3, 
		RULE_variableDecl = 4, RULE_variableList = 5, RULE_singleVariable = 6, 
		RULE_paraDeclList = 7, RULE_singleParaDecl = 8, RULE_paraList = 9, RULE_functionType = 10, 
		RULE_type = 11, RULE_nonArrayType = 12, RULE_statement = 13, RULE_compoundStmt = 14, 
		RULE_controlStatement = 15, RULE_expression = 16, RULE_creator = 17, RULE_constant = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "declaration", "classDecl", "functionDecl", "variableDecl", 
			"variableList", "singleVariable", "paraDeclList", "singleParaDecl", "paraList", 
			"functionType", "type", "nonArrayType", "statement", "compoundStmt", 
			"controlStatement", "expression", "creator", "constant"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'('", "')'", "';'", "','", "'='", "'['", "']'", 
			"'.'", "'++'", "'--'", "'+'", "'-'", "'!'", "'~'", "'*'", "'/'", "'%'", 
			"'<<'", "'>>'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&'", 
			"'^'", "'|'", "'&&'", "'||'", "'int'", "'bool'", "'string'", "'void'", 
			"'if'", "'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", 
			"'new'", "'class'", "'this'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "Int", "Bool", 
			"String", "Void", "If", "Else", "For", "While", "Break", "Continue", 
			"Return", "New", "Class", "This", "IntegerConstant", "StringConstant", 
			"NullConstant", "BoolConstant", "Identifier", "WhiteSpace", "NewLine", 
			"BlockComment", "LineComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Mxstar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxstarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << Class) | (1L << Identifier))) != 0)) {
				{
				{
				setState(38);
				declaration();
				}
				}
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public ClassDeclContext classDecl() {
			return getRuleContext(ClassDeclContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public VariableDeclContext variableDecl() {
			return getRuleContext(VariableDeclContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			setState(47);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				classDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(45);
				functionDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(46);
				variableDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(MxstarParser.Class, 0); }
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public List<FunctionDeclContext> functionDecl() {
			return getRuleContexts(FunctionDeclContext.class);
		}
		public FunctionDeclContext functionDecl(int i) {
			return getRuleContext(FunctionDeclContext.class,i);
		}
		public List<VariableDeclContext> variableDecl() {
			return getRuleContexts(VariableDeclContext.class);
		}
		public VariableDeclContext variableDecl(int i) {
			return getRuleContext(VariableDeclContext.class,i);
		}
		public ClassDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitClassDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclContext classDecl() throws RecognitionException {
		ClassDeclContext _localctx = new ClassDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(Class);
			setState(50);
			match(Identifier);
			setState(51);
			match(T__0);
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(54);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(52);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(53);
					variableDecl();
					}
					break;
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(59);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionDeclContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public CompoundStmtContext compoundStmt() {
			return getRuleContext(CompoundStmtContext.class,0);
		}
		public FunctionTypeContext functionType() {
			return getRuleContext(FunctionTypeContext.class,0);
		}
		public ParaDeclListContext paraDeclList() {
			return getRuleContext(ParaDeclListContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(61);
				functionType();
				}
				break;
			}
			setState(64);
			match(Identifier);
			setState(65);
			match(T__2);
			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Identifier))) != 0)) {
				{
				setState(66);
				paraDeclList();
				}
			}

			setState(69);
			match(T__3);
			setState(70);
			compoundStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableListContext variableList() {
			return getRuleContext(VariableListContext.class,0);
		}
		public VariableDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitVariableDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclContext variableDecl() throws RecognitionException {
		VariableDeclContext _localctx = new VariableDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_variableDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			type(0);
			setState(73);
			variableList();
			setState(74);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableListContext extends ParserRuleContext {
		public List<SingleVariableContext> singleVariable() {
			return getRuleContexts(SingleVariableContext.class);
		}
		public SingleVariableContext singleVariable(int i) {
			return getRuleContext(SingleVariableContext.class,i);
		}
		public VariableListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitVariableList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableListContext variableList() throws RecognitionException {
		VariableListContext _localctx = new VariableListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_variableList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			singleVariable();
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(77);
				match(T__5);
				setState(78);
				singleVariable();
				}
				}
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingleVariableContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SingleVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleVariable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitSingleVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleVariableContext singleVariable() throws RecognitionException {
		SingleVariableContext _localctx = new SingleVariableContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_singleVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(Identifier);
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(85);
				match(T__6);
				setState(86);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParaDeclListContext extends ParserRuleContext {
		public List<SingleParaDeclContext> singleParaDecl() {
			return getRuleContexts(SingleParaDeclContext.class);
		}
		public SingleParaDeclContext singleParaDecl(int i) {
			return getRuleContext(SingleParaDeclContext.class,i);
		}
		public ParaDeclListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paraDeclList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitParaDeclList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParaDeclListContext paraDeclList() throws RecognitionException {
		ParaDeclListContext _localctx = new ParaDeclListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_paraDeclList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			singleParaDecl();
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(90);
				match(T__5);
				setState(91);
				singleParaDecl();
				}
				}
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingleParaDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public SingleParaDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleParaDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitSingleParaDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleParaDeclContext singleParaDecl() throws RecognitionException {
		SingleParaDeclContext _localctx = new SingleParaDeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_singleParaDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			type(0);
			setState(98);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParaListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ParaListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paraList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitParaList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParaListContext paraList() throws RecognitionException {
		ParaListContext _localctx = new ParaListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_paraList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			expression(0);
			setState(105);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(101);
				match(T__5);
				setState(102);
				expression(0);
				}
				}
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionTypeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Void() { return getToken(MxstarParser.Void, 0); }
		public FunctionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitFunctionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTypeContext functionType() throws RecognitionException {
		FunctionTypeContext _localctx = new FunctionTypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_functionType);
		try {
			setState(110);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Int:
			case Bool:
			case String:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				type(0);
				}
				break;
			case Void:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				match(Void);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayTypeContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ArrayTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NArrayTypeContext extends TypeContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public NArrayTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new NArrayTypeContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(113);
			nonArrayType();
			}
			_ctx.stop = _input.LT(-1);
			setState(120);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ArrayTypeContext(new TypeContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(115);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(116);
					match(T__7);
					setState(117);
					match(T__8);
					}
					} 
				}
				setState(122);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class NonArrayTypeContext extends ParserRuleContext {
		public NonArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonArrayType; }
	 
		public NonArrayTypeContext() { }
		public void copyFrom(NonArrayTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NArrayTypeIntContext extends NonArrayTypeContext {
		public TerminalNode Int() { return getToken(MxstarParser.Int, 0); }
		public NArrayTypeIntContext(NonArrayTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNArrayTypeInt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NArrayTypeBoolContext extends NonArrayTypeContext {
		public TerminalNode Bool() { return getToken(MxstarParser.Bool, 0); }
		public NArrayTypeBoolContext(NonArrayTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNArrayTypeBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NArrayTypeStringContext extends NonArrayTypeContext {
		public TerminalNode String() { return getToken(MxstarParser.String, 0); }
		public NArrayTypeStringContext(NonArrayTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNArrayTypeString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NArrayTypeIdContext extends NonArrayTypeContext {
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public NArrayTypeIdContext(NonArrayTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNArrayTypeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonArrayTypeContext nonArrayType() throws RecognitionException {
		NonArrayTypeContext _localctx = new NonArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_nonArrayType);
		try {
			setState(127);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Int:
				_localctx = new NArrayTypeIntContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(123);
				match(Int);
				}
				break;
			case Bool:
				_localctx = new NArrayTypeBoolContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(124);
				match(Bool);
				}
				break;
			case String:
				_localctx = new NArrayTypeStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(125);
				match(String);
				}
				break;
			case Identifier:
				_localctx = new NArrayTypeIdContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(126);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprStmtContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExprStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitExprStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarDeclStmtContext extends StatementContext {
		public VariableDeclContext variableDecl() {
			return getRuleContext(VariableDeclContext.class,0);
		}
		public VarDeclStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitVarDeclStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BlockStmtContext extends StatementContext {
		public CompoundStmtContext compoundStmt() {
			return getRuleContext(CompoundStmtContext.class,0);
		}
		public BlockStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ControlStmtContext extends StatementContext {
		public ControlStatementContext controlStatement() {
			return getRuleContext(ControlStatementContext.class,0);
		}
		public ControlStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitControlStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_statement);
		try {
			setState(135);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				compoundStmt();
				}
				break;
			case 2:
				_localctx = new VarDeclStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				variableDecl();
				}
				break;
			case 3:
				_localctx = new ExprStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(131);
				expression(0);
				setState(132);
				match(T__4);
				}
				break;
			case 4:
				_localctx = new ControlStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(134);
				controlStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompoundStmtContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public CompoundStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitCompoundStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundStmtContext compoundStmt() throws RecognitionException {
		CompoundStmtContext _localctx = new CompoundStmtContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_compoundStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			match(T__0);
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << Int) | (1L << Bool) | (1L << String) | (1L << If) | (1L << For) | (1L << While) | (1L << Break) | (1L << Continue) | (1L << Return) | (1L << New) | (1L << This) | (1L << IntegerConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Identifier))) != 0)) {
				{
				{
				setState(138);
				statement();
				}
				}
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(144);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ControlStatementContext extends ParserRuleContext {
		public ControlStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlStatement; }
	 
		public ControlStatementContext() { }
		public void copyFrom(ControlStatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ContinueStmtContext extends ControlStatementContext {
		public TerminalNode Continue() { return getToken(MxstarParser.Continue, 0); }
		public ContinueStmtContext(ControlStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfStmtContext extends ControlStatementContext {
		public StatementContext thenStmt;
		public StatementContext elseStmt;
		public TerminalNode If() { return getToken(MxstarParser.If, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode Else() { return getToken(MxstarParser.Else, 0); }
		public IfStmtContext(ControlStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileStmtContext extends ControlStatementContext {
		public TerminalNode While() { return getToken(MxstarParser.While, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStmtContext(ControlStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BreakStmtContext extends ControlStatementContext {
		public TerminalNode Break() { return getToken(MxstarParser.Break, 0); }
		public BreakStmtContext(ControlStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForStmtContext extends ControlStatementContext {
		public ExpressionContext init;
		public ExpressionContext cond;
		public ExpressionContext step;
		public TerminalNode For() { return getToken(MxstarParser.For, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForStmtContext(ControlStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnStmtContext extends ControlStatementContext {
		public TerminalNode Return() { return getToken(MxstarParser.Return, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStmtContext(ControlStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlStatementContext controlStatement() throws RecognitionException {
		ControlStatementContext _localctx = new ControlStatementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_controlStatement);
		int _la;
		try {
			setState(185);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case If:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				match(If);
				setState(147);
				match(T__2);
				setState(148);
				expression(0);
				setState(149);
				match(T__3);
				setState(150);
				((IfStmtContext)_localctx).thenStmt = statement();
				setState(153);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(151);
					match(Else);
					setState(152);
					((IfStmtContext)_localctx).elseStmt = statement();
					}
					break;
				}
				}
				break;
			case For:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				match(For);
				setState(156);
				match(T__2);
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << New) | (1L << This) | (1L << IntegerConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Identifier))) != 0)) {
					{
					setState(157);
					((ForStmtContext)_localctx).init = expression(0);
					}
				}

				setState(160);
				match(T__4);
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << New) | (1L << This) | (1L << IntegerConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Identifier))) != 0)) {
					{
					setState(161);
					((ForStmtContext)_localctx).cond = expression(0);
					}
				}

				setState(164);
				match(T__4);
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << New) | (1L << This) | (1L << IntegerConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Identifier))) != 0)) {
					{
					setState(165);
					((ForStmtContext)_localctx).step = expression(0);
					}
				}

				setState(168);
				match(T__3);
				setState(169);
				statement();
				}
				break;
			case While:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(170);
				match(While);
				setState(171);
				match(T__2);
				setState(172);
				expression(0);
				setState(173);
				match(T__3);
				setState(174);
				statement();
				}
				break;
			case Return:
				_localctx = new ReturnStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(176);
				match(Return);
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << New) | (1L << This) | (1L << IntegerConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Identifier))) != 0)) {
					{
					setState(177);
					expression(0);
					}
				}

				setState(180);
				match(T__4);
				}
				break;
			case Continue:
				_localctx = new ContinueStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(181);
				match(Continue);
				setState(182);
				match(T__4);
				}
				break;
			case Break:
				_localctx = new BreakStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(183);
				match(Break);
				setState(184);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ConstExprContext extends ExpressionContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ConstExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitConstExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubscriptExprContext extends ExpressionContext {
		public ExpressionContext array;
		public ExpressionContext index;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SubscriptExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitSubscriptExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParaListContext paraList() {
			return getRuleContext(ParaListContext.class,0);
		}
		public FunctionCallExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitFunctionCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdExprContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public IdExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitIdExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SubExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitSubExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryExprContext extends ExpressionContext {
		public ExpressionContext lhs;
		public Token op;
		public ExpressionContext rhs;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BinaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NewExprContext extends ExpressionContext {
		public TerminalNode New() { return getToken(MxstarParser.New, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public NewExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNewExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PostfixOpExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PostfixOpExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitPostfixOpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryOpExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryOpExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitUnaryOpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MemberAccessExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public MemberAccessExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitMemberAccessExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ThisExprContext extends ExpressionContext {
		public TerminalNode This() { return getToken(MxstarParser.This, 0); }
		public ThisExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitThisExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				{
				_localctx = new IdExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(188);
				match(Identifier);
				}
				break;
			case This:
				{
				_localctx = new ThisExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189);
				match(This);
				}
				break;
			case IntegerConstant:
			case StringConstant:
			case NullConstant:
			case BoolConstant:
				{
				_localctx = new ConstExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(190);
				constant();
				}
				break;
			case T__2:
				{
				_localctx = new SubExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(191);
				match(T__2);
				setState(192);
				expression(0);
				setState(193);
				match(T__3);
				}
				break;
			case New:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(195);
				match(New);
				setState(196);
				creator();
				}
				break;
			case T__10:
			case T__11:
				{
				_localctx = new UnaryOpExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(197);
				((UnaryOpExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__10 || _la==T__11) ) {
					((UnaryOpExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(198);
				expression(15);
				}
				break;
			case T__12:
			case T__13:
				{
				_localctx = new UnaryOpExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(199);
				((UnaryOpExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
					((UnaryOpExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(200);
				expression(14);
				}
				break;
			case T__14:
			case T__15:
				{
				_localctx = new UnaryOpExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(201);
				((UnaryOpExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__14 || _la==T__15) ) {
					((UnaryOpExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(202);
				expression(13);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(259);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(257);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(205);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(206);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << T__18))) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(207);
						((BinaryExprContext)_localctx).rhs = expression(13);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(208);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(209);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(210);
						((BinaryExprContext)_localctx).rhs = expression(12);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(211);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(212);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__19 || _la==T__20) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(213);
						((BinaryExprContext)_localctx).rhs = expression(11);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(214);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(215);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__21 || _la==T__22) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(216);
						((BinaryExprContext)_localctx).rhs = expression(10);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(217);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(218);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__23 || _la==T__24) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(219);
						((BinaryExprContext)_localctx).rhs = expression(9);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(220);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(221);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__25 || _la==T__26) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(222);
						((BinaryExprContext)_localctx).rhs = expression(8);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(223);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(224);
						((BinaryExprContext)_localctx).op = match(T__27);
						setState(225);
						((BinaryExprContext)_localctx).rhs = expression(7);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(226);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(227);
						((BinaryExprContext)_localctx).op = match(T__28);
						setState(228);
						((BinaryExprContext)_localctx).rhs = expression(6);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(229);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(230);
						((BinaryExprContext)_localctx).op = match(T__29);
						setState(231);
						((BinaryExprContext)_localctx).rhs = expression(5);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(232);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(233);
						((BinaryExprContext)_localctx).op = match(T__30);
						setState(234);
						((BinaryExprContext)_localctx).rhs = expression(4);
						}
						break;
					case 11:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(235);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(236);
						((BinaryExprContext)_localctx).op = match(T__31);
						setState(237);
						((BinaryExprContext)_localctx).rhs = expression(3);
						}
						break;
					case 12:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(238);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(239);
						((BinaryExprContext)_localctx).op = match(T__6);
						setState(240);
						((BinaryExprContext)_localctx).rhs = expression(1);
						}
						break;
					case 13:
						{
						_localctx = new MemberAccessExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(241);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(242);
						match(T__9);
						setState(243);
						match(Identifier);
						}
						break;
					case 14:
						{
						_localctx = new SubscriptExprContext(new ExpressionContext(_parentctx, _parentState));
						((SubscriptExprContext)_localctx).array = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(244);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(245);
						match(T__7);
						setState(246);
						((SubscriptExprContext)_localctx).index = expression(0);
						setState(247);
						match(T__8);
						}
						break;
					case 15:
						{
						_localctx = new FunctionCallExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(249);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(250);
						match(T__2);
						setState(252);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << New) | (1L << This) | (1L << IntegerConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Identifier))) != 0)) {
							{
							setState(251);
							paraList();
							}
						}

						setState(254);
						match(T__3);
						}
						break;
					case 16:
						{
						_localctx = new PostfixOpExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(255);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(256);
						((PostfixOpExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__10 || _la==T__11) ) {
							((PostfixOpExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(261);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
	 
		public CreatorContext() { }
		public void copyFrom(CreatorContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NArrayCreatorContext extends CreatorContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public NArrayCreatorContext(CreatorContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNArrayCreator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayCreatorContext extends CreatorContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayCreatorContext(CreatorContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitArrayCreator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ClassCreatorContext extends CreatorContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public ClassCreatorContext(CreatorContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitClassCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_creator);
		try {
			int _alt;
			setState(283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				_localctx = new ArrayCreatorContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(262);
				nonArrayType();
				setState(267); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(263);
						match(T__7);
						setState(264);
						expression(0);
						setState(265);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(269); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				} while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER );
				setState(275);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(271);
						match(T__7);
						setState(272);
						match(T__8);
						}
						} 
					}
					setState(277);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				}
				}
				break;
			case 2:
				_localctx = new NArrayCreatorContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(278);
				nonArrayType();
				}
				break;
			case 3:
				_localctx = new ClassCreatorContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(279);
				nonArrayType();
				setState(280);
				match(T__2);
				setState(281);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
	 
		public ConstantContext() { }
		public void copyFrom(ConstantContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BoolConstantContext extends ConstantContext {
		public TerminalNode BoolConstant() { return getToken(MxstarParser.BoolConstant, 0); }
		public BoolConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitBoolConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullConstantContext extends ConstantContext {
		public TerminalNode NullConstant() { return getToken(MxstarParser.NullConstant, 0); }
		public NullConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNullConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringConstantContext extends ConstantContext {
		public TerminalNode StringConstant() { return getToken(MxstarParser.StringConstant, 0); }
		public StringConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitStringConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntegerConstantContext extends ConstantContext {
		public TerminalNode IntegerConstant() { return getToken(MxstarParser.IntegerConstant, 0); }
		public IntegerConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitIntegerConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_constant);
		try {
			setState(289);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerConstant:
				_localctx = new IntegerConstantContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(285);
				match(IntegerConstant);
				}
				break;
			case StringConstant:
				_localctx = new StringConstantContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(286);
				match(StringConstant);
				}
				break;
			case NullConstant:
				_localctx = new NullConstantContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(287);
				match(NullConstant);
				}
				break;
			case BoolConstant:
				_localctx = new BoolConstantContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(288);
				match(BoolConstant);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11:
			return type_sempred((TypeContext)_localctx, predIndex);
		case 16:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 11);
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 9);
		case 5:
			return precpred(_ctx, 8);
		case 6:
			return precpred(_ctx, 7);
		case 7:
			return precpred(_ctx, 6);
		case 8:
			return precpred(_ctx, 5);
		case 9:
			return precpred(_ctx, 4);
		case 10:
			return precpred(_ctx, 3);
		case 11:
			return precpred(_ctx, 2);
		case 12:
			return precpred(_ctx, 1);
		case 13:
			return precpred(_ctx, 20);
		case 14:
			return precpred(_ctx, 19);
		case 15:
			return precpred(_ctx, 18);
		case 16:
			return precpred(_ctx, 16);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\39\u0126\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\7\2*\n\2\f\2\16\2-\13\2\3\3\3\3\3\3\5\3\62\n"+
		"\3\3\4\3\4\3\4\3\4\3\4\7\49\n\4\f\4\16\4<\13\4\3\4\3\4\3\5\5\5A\n\5\3"+
		"\5\3\5\3\5\5\5F\n\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\7\7R\n\7\f"+
		"\7\16\7U\13\7\3\b\3\b\3\b\5\bZ\n\b\3\t\3\t\3\t\7\t_\n\t\f\t\16\tb\13\t"+
		"\3\n\3\n\3\n\3\13\3\13\3\13\7\13j\n\13\f\13\16\13m\13\13\3\f\3\f\5\fq"+
		"\n\f\3\r\3\r\3\r\3\r\3\r\3\r\7\ry\n\r\f\r\16\r|\13\r\3\16\3\16\3\16\3"+
		"\16\5\16\u0082\n\16\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u008a\n\17\3\20"+
		"\3\20\7\20\u008e\n\20\f\20\16\20\u0091\13\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\5\21\u009c\n\21\3\21\3\21\3\21\5\21\u00a1\n\21\3"+
		"\21\3\21\5\21\u00a5\n\21\3\21\3\21\5\21\u00a9\n\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00b5\n\21\3\21\3\21\3\21\3\21\3\21"+
		"\5\21\u00bc\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\5\22\u00ce\n\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22"+
		"\u00ff\n\22\3\22\3\22\3\22\7\22\u0104\n\22\f\22\16\22\u0107\13\22\3\23"+
		"\3\23\3\23\3\23\3\23\6\23\u010e\n\23\r\23\16\23\u010f\3\23\3\23\7\23\u0114"+
		"\n\23\f\23\16\23\u0117\13\23\3\23\3\23\3\23\3\23\3\23\5\23\u011e\n\23"+
		"\3\24\3\24\3\24\3\24\5\24\u0124\n\24\3\24\2\4\30\"\25\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&\2\n\3\2\r\16\3\2\17\20\3\2\21\22\3\2\23\25"+
		"\3\2\26\27\3\2\30\31\3\2\32\33\3\2\34\35\2\u014f\2+\3\2\2\2\4\61\3\2\2"+
		"\2\6\63\3\2\2\2\b@\3\2\2\2\nJ\3\2\2\2\fN\3\2\2\2\16V\3\2\2\2\20[\3\2\2"+
		"\2\22c\3\2\2\2\24f\3\2\2\2\26p\3\2\2\2\30r\3\2\2\2\32\u0081\3\2\2\2\34"+
		"\u0089\3\2\2\2\36\u008b\3\2\2\2 \u00bb\3\2\2\2\"\u00cd\3\2\2\2$\u011d"+
		"\3\2\2\2&\u0123\3\2\2\2(*\5\4\3\2)(\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2"+
		"\2\2,\3\3\2\2\2-+\3\2\2\2.\62\5\6\4\2/\62\5\b\5\2\60\62\5\n\6\2\61.\3"+
		"\2\2\2\61/\3\2\2\2\61\60\3\2\2\2\62\5\3\2\2\2\63\64\7/\2\2\64\65\7\65"+
		"\2\2\65:\7\3\2\2\669\5\b\5\2\679\5\n\6\28\66\3\2\2\28\67\3\2\2\29<\3\2"+
		"\2\2:8\3\2\2\2:;\3\2\2\2;=\3\2\2\2<:\3\2\2\2=>\7\4\2\2>\7\3\2\2\2?A\5"+
		"\26\f\2@?\3\2\2\2@A\3\2\2\2AB\3\2\2\2BC\7\65\2\2CE\7\5\2\2DF\5\20\t\2"+
		"ED\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\7\6\2\2HI\5\36\20\2I\t\3\2\2\2JK\5\30"+
		"\r\2KL\5\f\7\2LM\7\7\2\2M\13\3\2\2\2NS\5\16\b\2OP\7\b\2\2PR\5\16\b\2Q"+
		"O\3\2\2\2RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\r\3\2\2\2US\3\2\2\2VY\7\65\2"+
		"\2WX\7\t\2\2XZ\5\"\22\2YW\3\2\2\2YZ\3\2\2\2Z\17\3\2\2\2[`\5\22\n\2\\]"+
		"\7\b\2\2]_\5\22\n\2^\\\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2a\21\3\2\2"+
		"\2b`\3\2\2\2cd\5\30\r\2de\7\65\2\2e\23\3\2\2\2fk\5\"\22\2gh\7\b\2\2hj"+
		"\5\"\22\2ig\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2l\25\3\2\2\2mk\3\2\2"+
		"\2nq\5\30\r\2oq\7&\2\2pn\3\2\2\2po\3\2\2\2q\27\3\2\2\2rs\b\r\1\2st\5\32"+
		"\16\2tz\3\2\2\2uv\f\4\2\2vw\7\n\2\2wy\7\13\2\2xu\3\2\2\2y|\3\2\2\2zx\3"+
		"\2\2\2z{\3\2\2\2{\31\3\2\2\2|z\3\2\2\2}\u0082\7#\2\2~\u0082\7$\2\2\177"+
		"\u0082\7%\2\2\u0080\u0082\7\65\2\2\u0081}\3\2\2\2\u0081~\3\2\2\2\u0081"+
		"\177\3\2\2\2\u0081\u0080\3\2\2\2\u0082\33\3\2\2\2\u0083\u008a\5\36\20"+
		"\2\u0084\u008a\5\n\6\2\u0085\u0086\5\"\22\2\u0086\u0087\7\7\2\2\u0087"+
		"\u008a\3\2\2\2\u0088\u008a\5 \21\2\u0089\u0083\3\2\2\2\u0089\u0084\3\2"+
		"\2\2\u0089\u0085\3\2\2\2\u0089\u0088\3\2\2\2\u008a\35\3\2\2\2\u008b\u008f"+
		"\7\3\2\2\u008c\u008e\5\34\17\2\u008d\u008c\3\2\2\2\u008e\u0091\3\2\2\2"+
		"\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092\3\2\2\2\u0091\u008f"+
		"\3\2\2\2\u0092\u0093\7\4\2\2\u0093\37\3\2\2\2\u0094\u0095\7\'\2\2\u0095"+
		"\u0096\7\5\2\2\u0096\u0097\5\"\22\2\u0097\u0098\7\6\2\2\u0098\u009b\5"+
		"\34\17\2\u0099\u009a\7(\2\2\u009a\u009c\5\34\17\2\u009b\u0099\3\2\2\2"+
		"\u009b\u009c\3\2\2\2\u009c\u00bc\3\2\2\2\u009d\u009e\7)\2\2\u009e\u00a0"+
		"\7\5\2\2\u009f\u00a1\5\"\22\2\u00a0\u009f\3\2\2\2\u00a0\u00a1\3\2\2\2"+
		"\u00a1\u00a2\3\2\2\2\u00a2\u00a4\7\7\2\2\u00a3\u00a5\5\"\22\2\u00a4\u00a3"+
		"\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\7\7\2\2\u00a7"+
		"\u00a9\5\"\22\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\3"+
		"\2\2\2\u00aa\u00ab\7\6\2\2\u00ab\u00bc\5\34\17\2\u00ac\u00ad\7*\2\2\u00ad"+
		"\u00ae\7\5\2\2\u00ae\u00af\5\"\22\2\u00af\u00b0\7\6\2\2\u00b0\u00b1\5"+
		"\34\17\2\u00b1\u00bc\3\2\2\2\u00b2\u00b4\7-\2\2\u00b3\u00b5\5\"\22\2\u00b4"+
		"\u00b3\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00bc\7\7"+
		"\2\2\u00b7\u00b8\7,\2\2\u00b8\u00bc\7\7\2\2\u00b9\u00ba\7+\2\2\u00ba\u00bc"+
		"\7\7\2\2\u00bb\u0094\3\2\2\2\u00bb\u009d\3\2\2\2\u00bb\u00ac\3\2\2\2\u00bb"+
		"\u00b2\3\2\2\2\u00bb\u00b7\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc!\3\2\2\2"+
		"\u00bd\u00be\b\22\1\2\u00be\u00ce\7\65\2\2\u00bf\u00ce\7\60\2\2\u00c0"+
		"\u00ce\5&\24\2\u00c1\u00c2\7\5\2\2\u00c2\u00c3\5\"\22\2\u00c3\u00c4\7"+
		"\6\2\2\u00c4\u00ce\3\2\2\2\u00c5\u00c6\7.\2\2\u00c6\u00ce\5$\23\2\u00c7"+
		"\u00c8\t\2\2\2\u00c8\u00ce\5\"\22\21\u00c9\u00ca\t\3\2\2\u00ca\u00ce\5"+
		"\"\22\20\u00cb\u00cc\t\4\2\2\u00cc\u00ce\5\"\22\17\u00cd\u00bd\3\2\2\2"+
		"\u00cd\u00bf\3\2\2\2\u00cd\u00c0\3\2\2\2\u00cd\u00c1\3\2\2\2\u00cd\u00c5"+
		"\3\2\2\2\u00cd\u00c7\3\2\2\2\u00cd\u00c9\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce"+
		"\u0105\3\2\2\2\u00cf\u00d0\f\16\2\2\u00d0\u00d1\t\5\2\2\u00d1\u0104\5"+
		"\"\22\17\u00d2\u00d3\f\r\2\2\u00d3\u00d4\t\3\2\2\u00d4\u0104\5\"\22\16"+
		"\u00d5\u00d6\f\f\2\2\u00d6\u00d7\t\6\2\2\u00d7\u0104\5\"\22\r\u00d8\u00d9"+
		"\f\13\2\2\u00d9\u00da\t\7\2\2\u00da\u0104\5\"\22\f\u00db\u00dc\f\n\2\2"+
		"\u00dc\u00dd\t\b\2\2\u00dd\u0104\5\"\22\13\u00de\u00df\f\t\2\2\u00df\u00e0"+
		"\t\t\2\2\u00e0\u0104\5\"\22\n\u00e1\u00e2\f\b\2\2\u00e2\u00e3\7\36\2\2"+
		"\u00e3\u0104\5\"\22\t\u00e4\u00e5\f\7\2\2\u00e5\u00e6\7\37\2\2\u00e6\u0104"+
		"\5\"\22\b\u00e7\u00e8\f\6\2\2\u00e8\u00e9\7 \2\2\u00e9\u0104\5\"\22\7"+
		"\u00ea\u00eb\f\5\2\2\u00eb\u00ec\7!\2\2\u00ec\u0104\5\"\22\6\u00ed\u00ee"+
		"\f\4\2\2\u00ee\u00ef\7\"\2\2\u00ef\u0104\5\"\22\5\u00f0\u00f1\f\3\2\2"+
		"\u00f1\u00f2\7\t\2\2\u00f2\u0104\5\"\22\3\u00f3\u00f4\f\26\2\2\u00f4\u00f5"+
		"\7\f\2\2\u00f5\u0104\7\65\2\2\u00f6\u00f7\f\25\2\2\u00f7\u00f8\7\n\2\2"+
		"\u00f8\u00f9\5\"\22\2\u00f9\u00fa\7\13\2\2\u00fa\u0104\3\2\2\2\u00fb\u00fc"+
		"\f\24\2\2\u00fc\u00fe\7\5\2\2\u00fd\u00ff\5\24\13\2\u00fe\u00fd\3\2\2"+
		"\2\u00fe\u00ff\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\u0104\7\6\2\2\u0101\u0102"+
		"\f\22\2\2\u0102\u0104\t\2\2\2\u0103\u00cf\3\2\2\2\u0103\u00d2\3\2\2\2"+
		"\u0103\u00d5\3\2\2\2\u0103\u00d8\3\2\2\2\u0103\u00db\3\2\2\2\u0103\u00de"+
		"\3\2\2\2\u0103\u00e1\3\2\2\2\u0103\u00e4\3\2\2\2\u0103\u00e7\3\2\2\2\u0103"+
		"\u00ea\3\2\2\2\u0103\u00ed\3\2\2\2\u0103\u00f0\3\2\2\2\u0103\u00f3\3\2"+
		"\2\2\u0103\u00f6\3\2\2\2\u0103\u00fb\3\2\2\2\u0103\u0101\3\2\2\2\u0104"+
		"\u0107\3\2\2\2\u0105\u0103\3\2\2\2\u0105\u0106\3\2\2\2\u0106#\3\2\2\2"+
		"\u0107\u0105\3\2\2\2\u0108\u010d\5\32\16\2\u0109\u010a\7\n\2\2\u010a\u010b"+
		"\5\"\22\2\u010b\u010c\7\13\2\2\u010c\u010e\3\2\2\2\u010d\u0109\3\2\2\2"+
		"\u010e\u010f\3\2\2\2\u010f\u010d\3\2\2\2\u010f\u0110\3\2\2\2\u0110\u0115"+
		"\3\2\2\2\u0111\u0112\7\n\2\2\u0112\u0114\7\13\2\2\u0113\u0111\3\2\2\2"+
		"\u0114\u0117\3\2\2\2\u0115\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u011e"+
		"\3\2\2\2\u0117\u0115\3\2\2\2\u0118\u011e\5\32\16\2\u0119\u011a\5\32\16"+
		"\2\u011a\u011b\7\5\2\2\u011b\u011c\7\6\2\2\u011c\u011e\3\2\2\2\u011d\u0108"+
		"\3\2\2\2\u011d\u0118\3\2\2\2\u011d\u0119\3\2\2\2\u011e%\3\2\2\2\u011f"+
		"\u0124\7\61\2\2\u0120\u0124\7\62\2\2\u0121\u0124\7\63\2\2\u0122\u0124"+
		"\7\64\2\2\u0123\u011f\3\2\2\2\u0123\u0120\3\2\2\2\u0123\u0121\3\2\2\2"+
		"\u0123\u0122\3\2\2\2\u0124\'\3\2\2\2\37+\618:@ESY`kpz\u0081\u0089\u008f"+
		"\u009b\u00a0\u00a4\u00a8\u00b4\u00bb\u00cd\u00fe\u0103\u0105\u010f\u0115"+
		"\u011d\u0123";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}