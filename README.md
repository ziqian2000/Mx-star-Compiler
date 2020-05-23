# 1. About

This project is a toy compiler for Mx* language to RISC-V assembly, implemented in Java. 

It is [a course project](https://github.com/peterzheng98/Compiler-2020).

# 2. Design

It reads code from `code.txt`, prints IR in `ir.txt` and prints RISC-V assembly in `test.s`. The input file is `test.in`.

## 2.1 Architecture

| Contents               | Descriptions                                                 |
| ---------------------- | ------------------------------------------------------------ |
| Parser                 | Using ANTLR 4.                                               |
| AST                    | Nothing special.                                             |
| IR                     | The quadruple IR.                                            |
| Optimization           | Function Inlining.                                           |
|                        | Load global  variables in advance and postpone store.        |
|                        | Loop-invariant code  motion (on SSA).                        |
|                        | Sparse conditional  constant propagation (on SSA).           |
|                        | Common  subexpression elimination (on SSA).                  |
|                        | Dead code  elimination (on SSA).                             |
|                        | Control flow graph  simplification (on both SSA and non-SSA). |
| Register Allocation    | Graph coloring.                                              |
| Peephole  Optimization | Countless peephole optimizations.                            |
| Assembly               | RISC-V.                                                      |

# 3. Performance 

B2 baseline (highest baseline in that course project)  passed. 

Slightly better than GCC O1.