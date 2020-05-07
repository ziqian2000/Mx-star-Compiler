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

	.globl	Array_Node.Array_Node
Array_Node.Array_Node:
.entry_2:
	addi	sp, sp, -16
	sw	s0, 8(sp)
	sw	s1, 12(sp)
	mv	s0, ra
	addi	t0, a0, 4
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	addi	s1, a0, 0
	li	ra, 800000
	addi	a0, ra, 4
	call	malloc
	li	ra, 100000
	sw	ra, 0(a0)
	lw	ra, 0(s1)
	sw	a0, 0(s1)
	mv	ra, s0
	lw	s0, 8(sp)
	lw	s1, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Array_Node.push_back
Array_Node.push_back:
.entry_3:
	addi	a2, a0, 0
	addi	t2, a0, 4
	lw	t1, 0(t2)
	addi	t0, t1, 1
	sw	t0, 0(t2)
	lw	t2, 0(a2)
	addi	t1, t1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t1, t2, t0
	lw	t0, 0(t1)
	sw	a1, 0(t1)
	ret

	.globl	Array_Node.pop_back
Array_Node.pop_back:
.entry_4:
	addi	t2, a0, 4
	lw	t1, 0(t2)
	li	t0, 1
	sub	t1, t1, t0
	sw	t1, 0(t2)
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

	.globl	Array_Node.back
Array_Node.back:
.entry_5:
	addi	t2, a0, 0
	addi	t0, a0, 4
	lw	t1, 0(t0)
	li	t0, 1
	sub	t0, t1, t0
	lw	t2, 0(t2)
	addi	t1, t0, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	a0, 0(t0)
	ret

	.globl	Array_Node.front
Array_Node.front:
.entry_6:
	addi	t0, a0, 0
	lw	t2, 0(t0)
	li	t1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	a0, 0(t0)
	ret

	.globl	Array_Node.size
Array_Node.size:
.entry_7:
	addi	t0, a0, 4
	lw	a0, 0(t0)
	ret

	.globl	Array_Node.resize
Array_Node.resize:
.entry_8:
	addi	sp, sp, -16
	sw	s0, 4(sp)
	sw	s1, 8(sp)
	sw	s2, 12(sp)
	mv	s0, ra
	mv	s1, a0
	mv	s2, a1
	j	.unnamed_2_9
.unnamed_2_9:
	addi	ra, s1, 0
	lw	a0, 0(ra)
	call	array.size
	slt	ra, a0, s2
	bne	ra, zero, .unnamed_3_10
	j	.unnamed_4_11
.unnamed_4_11:
	addi	t0, s1, 4
	lw	ra, 0(t0)
	sw	s2, 0(t0)
	mv	ra, s0
	lw	s0, 4(sp)
	lw	s1, 8(sp)
	lw	s2, 12(sp)
	addi	sp, sp, 16
	ret
.unnamed_3_10:
	mv	a0, s1
	call	Array_Node.doubleStorage
	j	.unnamed_2_9

	.globl	Array_Node.get
Array_Node.get:
.entry_12:
	addi	t0, a0, 0
	lw	t2, 0(t0)
	addi	t1, a1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	a0, 0(t0)
	ret

	.globl	Array_Node.set
Array_Node.set:
.entry_13:
	addi	t0, a0, 0
	lw	t2, 0(t0)
	addi	t1, a1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t1, t2, t0
	lw	t0, 0(t1)
	sw	a2, 0(t1)
	ret

	.globl	Array_Node.swap
Array_Node.swap:
.entry_14:
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

	.globl	Array_Node.doubleStorage
Array_Node.doubleStorage:
.entry_15:
	addi	sp, sp, -32
	sw	s0, 8(sp)
	sw	s1, 12(sp)
	sw	s2, 16(sp)
	sw	s3, 20(sp)
	sw	s4, 24(sp)
	sw	s5, 28(sp)
	mv	s0, ra
	mv	s1, a0
	addi	ra, s1, 0
	lw	ra, 0(ra)
	mv	s2, ra
	addi	ra, s1, 4
	lw	ra, 0(ra)
	mv	s3, ra
	addi	s4, s1, 0
	mv	a0, s2
	call	array.size
	li	ra, 2
	mul	s5, a0, ra
	li	ra, 8
	mul	ra, s5, ra
	addi	a0, ra, 4
	call	malloc
	sw	s5, 0(a0)
	lw	ra, 0(s4)
	sw	a0, 0(s4)
	addi	t0, s1, 4
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	j	.unnamed_5_16
.unnamed_5_16:
	addi	ra, s1, 4
	lw	ra, 0(ra)
	sub	ra, ra, s3
	sltu	ra, zero, ra
	bne	ra, zero, .unnamed_6_17
	j	.unnamed_7_18
.unnamed_6_17:
	addi	t0, s1, 0
	addi	ra, s1, 4
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t1, t1, ra
	addi	ra, s1, 4
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t0, s2, ra
	lw	ra, 0(t1)
	lw	ra, 0(t0)
	lw	ra, 0(t0)
	sw	ra, 0(t1)
	j	.unnamed_8_19
.unnamed_8_19:
	addi	t0, s1, 4
	lw	ra, 0(t0)
	addi	ra, ra, 1
	sw	ra, 0(t0)
	j	.unnamed_5_16
.unnamed_7_18:
	lw	s3, 20(sp)
	lw	s4, 24(sp)
	lw	s5, 28(sp)
	mv	ra, s0
	lw	s0, 8(sp)
	lw	s1, 12(sp)
	lw	s2, 16(sp)
	addi	sp, sp, 32
	ret

	.globl	Heap_Node.Heap_Node
Heap_Node.Heap_Node:
.entry_20:
	addi	sp, sp, -16
	sw	s0, 4(sp)
	sw	s1, 8(sp)
	sw	s2, 12(sp)
	mv	s0, ra
	addi	s1, a0, 0
	li	a0, 8
	call	malloc
	mv	s2, a0
	mv	a0, s2
	call	Array_Node.Array_Node
	lw	ra, 0(s1)
	sw	s2, 0(s1)
	mv	ra, s0
	lw	s0, 4(sp)
	lw	s1, 8(sp)
	lw	s2, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.push
Heap_Node.push:
.entry_21:
	addi	sp, sp, -16
	sw	s0, 0(sp)
	sw	s1, 4(sp)
	sw	s2, 8(sp)
	sw	s3, 12(sp)
	mv	s0, ra
	mv	s1, a0
	mv	s2, a1
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a1, s2
	call	Array_Node.push_back
	mv	a0, s1
	call	Heap_Node.size
	li	ra, 1
	sub	ra, a0, ra
	mv	s3, ra
	addi	ra, s2, 4
	lw	a0, 0(ra)
	call	printlnInt
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a1, s3
	call	Array_Node.get
	call	Node.key_
	call	printlnInt
	lw	s3, 12(sp)
	mv	ra, s0
	lw	s0, 0(sp)
	lw	s1, 4(sp)
	lw	s2, 8(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.pop
Heap_Node.pop:
.entry_22:
	addi	sp, sp, -16
	sw	s0, 0(sp)
	sw	s1, 4(sp)
	sw	s2, 8(sp)
	sw	s3, 12(sp)
	mv	s0, ra
	mv	s1, a0
	addi	ra, s1, 0
	lw	a0, 0(ra)
	call	Array_Node.front
	mv	s2, a0
	addi	s3, s1, 0
	mv	a0, s1
	call	Heap_Node.size
	li	ra, 1
	sub	a2, a0, ra
	lw	a0, 0(s3)
	li	a1, 0
	call	Array_Node.swap
	addi	ra, s1, 0
	lw	a0, 0(ra)
	call	Array_Node.pop_back
	mv	a0, s1
	li	a1, 0
	call	Heap_Node.maxHeapify
	mv	a0, s2
	lw	s3, 12(sp)
	mv	ra, s0
	lw	s0, 0(sp)
	lw	s1, 4(sp)
	lw	s2, 8(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.top
Heap_Node.top:
.entry_23:
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	addi	ra, a0, 0
	lw	a0, 0(ra)
	call	Array_Node.front
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.size
Heap_Node.size:
.entry_24:
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	addi	ra, a0, 0
	lw	a0, 0(ra)
	call	Array_Node.size
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.lchild
Heap_Node.lchild:
.entry_25:
	li	t0, 2
	mul	t0, a1, t0
	addi	a0, t0, 1
	ret

	.globl	Heap_Node.rchild
Heap_Node.rchild:
.entry_26:
	li	t0, 2
	mul	t0, a1, t0
	addi	a0, t0, 2
	ret

	.globl	Heap_Node.pnt
Heap_Node.pnt:
.entry_27:
	li	t0, 1
	sub	t1, a1, t0
	li	t0, 2
	div	a0, t1, t0
	ret

	.globl	Heap_Node.maxHeapify
Heap_Node.maxHeapify:
.entry_28:
	addi	sp, sp, -32
	sw	s0, 4(sp)
	sw	s1, 8(sp)
	sw	s2, 12(sp)
	sw	s3, 16(sp)
	sw	s4, 20(sp)
	sw	s5, 24(sp)
	sw	s6, 28(sp)
	mv	s0, ra
	mv	s2, a0
	mv	s3, a1
	mv	a0, s2
	mv	a1, s3
	call	Heap_Node.lchild
	mv	s4, a0
	mv	a0, s2
	mv	a1, s3
	call	Heap_Node.rchild
	mv	s5, a0
	mv	s6, s3
	mv	a0, s2
	call	Heap_Node.size
	slt	ra, s4, a0
	bne	ra, zero, .if_then_29
	j	.if_exit_30
.if_exit_30:
	mv	a0, s2
	call	Heap_Node.size
	slt	ra, s5, a0
	bne	ra, zero, .if_then_31
	j	.if_exit_32
.if_exit_32:
	sub	ra, s6, s3
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_33
	j	.if_exit_34
.if_then_33:
	j	.func_exit_35
.func_exit_35:
	lw	s3, 16(sp)
	lw	s4, 20(sp)
	lw	s5, 24(sp)
	lw	s6, 28(sp)
	mv	ra, s0
	lw	s0, 4(sp)
	lw	s1, 8(sp)
	lw	s2, 12(sp)
	addi	sp, sp, 32
	ret
.if_exit_34:
	addi	ra, s2, 0
	lw	a0, 0(ra)
	mv	a1, s3
	mv	a2, s6
	call	Array_Node.swap
	mv	a0, s2
	mv	a1, s6
	call	Heap_Node.maxHeapify
	j	.func_exit_35
.if_then_31:
	addi	ra, s2, 0
	lw	a0, 0(ra)
	mv	a1, s5
	call	Array_Node.get
	call	Node.key_
	mv	s1, a0
	addi	ra, s2, 0
	lw	a0, 0(ra)
	mv	a1, s6
	call	Array_Node.get
	call	Node.key_
	slt	ra, a0, s1
	bne	ra, zero, .if_then_36
	j	.if_exit_37
.if_exit_37:
	j	.if_exit_32
.if_then_36:
	mv	s6, s5
	j	.if_exit_37
.if_then_29:
	addi	ra, s2, 0
	lw	a0, 0(ra)
	mv	a1, s4
	call	Array_Node.get
	call	Node.key_
	mv	s1, a0
	addi	ra, s2, 0
	lw	a0, 0(ra)
	mv	a1, s6
	call	Array_Node.get
	call	Node.key_
	slt	ra, a0, s1
	bne	ra, zero, .if_then_38
	j	.if_exit_39
.if_then_38:
	mv	s6, s4
	j	.if_exit_39
.if_exit_39:
	j	.if_exit_30

	.globl	Node.key_
Node.key_:
.entry_40:
	addi	t0, a0, 4
	lw	a0, 0(t0)
	ret

	.globl	_main
_main:
.entry_41:
	addi	sp, sp, -16
	sw	s0, 8(sp)
	sw	s1, 12(sp)
	mv	s1, ra
	li	a0, 4
	call	malloc
	mv	s0, a0
	mv	a0, s0
	call	Heap_Node.Heap_Node
	li	a0, 8
	call	malloc
	mv	a1, a0
	addi	t0, a1, 0
	lw	ra, 0(t0)
	li	ra, 1
	sw	ra, 0(t0)
	addi	t0, a1, 4
	lw	ra, 0(t0)
	li	ra, 3
	sw	ra, 0(t0)
	mv	a0, s0
	call	Heap_Node.push
	li	a0, 8
	call	malloc
	mv	a1, a0
	addi	t0, a1, 0
	lw	ra, 0(t0)
	li	ra, 2
	sw	ra, 0(t0)
	addi	t0, a1, 4
	lw	ra, 0(t0)
	li	ra, 4
	sw	ra, 0(t0)
	mv	a0, s0
	call	Heap_Node.push
	li	a0, 0
	mv	ra, s1
	lw	s0, 8(sp)
	lw	s1, 12(sp)
	addi	sp, sp, 16
	ret

