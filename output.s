	.section	.data

	.text

	.globl	array.size
array.size:
.unnamed_0_0:
	lw	a0, 0(a0)
	ret

	.globl	main
main:
.unnamed_1_1:
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	call	_main
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Array_Node.pop_back
Array_Node.pop_back:
.entry_2:
	addi	t2, a0, 4
	lw	t1, 0(t2)
	li	t0, 1
	sub	t0, t1, t0
	sw	t0, 0(t2)
	addi	t1, a0, 0
	addi	t0, a0, 4
	lw	t2, 0(t1)
	lw	t0, 0(t0)
	addi	t1, t0, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	a0, 0(t0)
	ret

	.globl	Array_Node.front
Array_Node.front:
.entry_3:
	addi	t0, a0, 0
	lw	t2, 0(t0)
	li	t1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	a0, 0(t0)
	ret

	.globl	Array_Node.swap
Array_Node.swap:
.entry_4:
	addi	t0, a0, 0
	lw	t2, 0(t0)
	addi	t1, a1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	t0, 0(t0)
	addi	t1, a0, 0
	lw	a3, 0(t1)
	addi	t2, a1, 1
	li	t1, 8
	mul	t1, t2, t1
	add	a3, a3, t1
	addi	t1, a0, 0
	lw	a1, 0(t1)
	addi	t2, a2, 1
	li	t1, 8
	mul	t1, t2, t1
	add	t2, a1, t1
	lw	t1, 0(a3)
	lw	t1, 0(t2)
	lw	t1, 0(t2)
	sw	t1, 0(a3)
	addi	t1, a0, 0
	lw	a0, 0(t1)
	addi	t2, a2, 1
	li	t1, 8
	mul	t1, t2, t1
	add	t2, a0, t1
	lw	t1, 0(t2)
	sw	t0, 0(t2)
	ret

	.globl	Heap_Node.lchild
Heap_Node.lchild:
.entry_5:
	li	t0, 2
	mul	t0, a1, t0
	addi	a0, t0, 1
	ret

	.globl	Heap_Node.rchild
Heap_Node.rchild:
.entry_6:
	li	t0, 2
	mul	t0, a1, t0
	addi	a0, t0, 2
	ret

	.globl	_main
_main:
.entry_7:
	addi	sp, sp, -32
	sw	s0, 12(sp)
	sw	s1, 16(sp)
	sw	s2, 20(sp)
	sw	s3, 24(sp)
	sw	s4, 28(sp)
	mv	s0, ra
	li	a0, 4
	call	malloc
	mv	s1, a0
	mv	ra, s1
	addi	s2, ra, 0
	li	a0, 8
	call	malloc
	mv	s3, a0
	mv	ra, s3
	addi	t1, ra, 4
	lw	t0, 0(t1)
	li	t0, 0
	sw	t0, 0(t1)
	addi	s4, ra, 0
	li	ra, 800000
	addi	a0, ra, 4
	call	malloc
	li	ra, 100000
	sw	ra, 0(a0)
	lw	ra, 0(s4)
	sw	a0, 0(s4)
	lw	ra, 0(s2)
	sw	s3, 0(s2)
	li	a0, 8
	call	malloc
	addi	t0, a0, 0
	lw	ra, 0(t0)
	li	ra, 1
	sw	ra, 0(t0)
	addi	t0, a0, 4
	lw	ra, 0(t0)
	li	ra, 3
	sw	ra, 0(t0)
	mv	s2, s1
	addi	ra, s2, 0
	lw	ra, 0(ra)
	addi	t2, ra, 0
	addi	t1, ra, 4
	lw	t0, 0(t1)
	addi	ra, t0, 1
	sw	ra, 0(t1)
	lw	t1, 0(t2)
	addi	t0, t0, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, 0(t0)
	sw	a0, 0(t0)
	addi	ra, s2, 0
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	li	t0, 1
	sub	s3, ra, t0
	addi	ra, a0, 4
	lw	a0, 0(ra)
	call	printlnInt
	addi	ra, s2, 0
	lw	ra, 0(ra)
	addi	ra, ra, 0
	lw	t1, 0(ra)
	addi	t0, s3, 1
	li	ra, 8
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	a0, 0(ra)
	call	printlnInt
	li	a0, 8
	call	malloc
	addi	t0, a0, 0
	lw	ra, 0(t0)
	li	ra, 2
	sw	ra, 0(t0)
	addi	t0, a0, 4
	lw	ra, 0(t0)
	li	ra, 4
	sw	ra, 0(t0)
	addi	ra, s1, 0
	lw	ra, 0(ra)
	addi	t2, ra, 0
	addi	t1, ra, 4
	lw	t0, 0(t1)
	addi	ra, t0, 1
	sw	ra, 0(t1)
	lw	t1, 0(t2)
	addi	t0, t0, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, 0(t0)
	sw	a0, 0(t0)
	addi	ra, s1, 0
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	li	t0, 1
	sub	s2, ra, t0
	addi	ra, a0, 4
	lw	a0, 0(ra)
	call	printlnInt
	addi	ra, s1, 0
	lw	ra, 0(ra)
	addi	ra, ra, 0
	lw	t1, 0(ra)
	addi	t0, s2, 1
	li	ra, 8
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	a0, 0(ra)
	call	printlnInt
	li	a0, 0
	lw	s3, 24(sp)
	lw	s4, 28(sp)
	mv	ra, s0
	lw	s0, 12(sp)
	lw	s1, 16(sp)
	lw	s2, 20(sp)
	addi	sp, sp, 32
	ret

