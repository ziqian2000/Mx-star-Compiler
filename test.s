	.section	.data

	.globl	n
n:
	.zero	4

	.globl	m
m:
	.zero	4

	.globl	g
g:
	.zero	4

	.globl	INF
INF:
	.zero	4

	.globl	unnamed_391
unnamed_391:
	.string	"-1"

	.globl	unnamed_397
unnamed_397:
	.string	" "

	.globl	unnamed_398
unnamed_398:
	.string	""

	.text

	.globl	main
main:
.unnamed_1_0:
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	li	a0, 4
	call	malloc
	li	a0, 4
	call	malloc
	li	a0, 4
	call	malloc
	li	a0, 4
	call	malloc
	li	t0, 10000000
	sw	t0, INF, ra
	call	_main
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.push
Heap_Node.push:
.entry_1:
	addi	sp, sp, -48
	sw	s0, 12(sp)
	sw	s1, 16(sp)
	sw	s2, 20(sp)
	sw	s3, 24(sp)
	sw	s4, 28(sp)
	sw	s5, 32(sp)
	sw	s6, 36(sp)
	sw	s7, 40(sp)
	sw	s8, 44(sp)
	sw	s10, 8(sp)
	mv	s10, s11
	mv	s1, ra
	mv	s6, a0
	addi	ra, s6, 0
	lw	ra, 0(ra)
	mv	s5, a1
	mv	s0, ra
	mv	ra, s0
	addi	ra, ra, 4
	lw	ra, 0(ra)
	addi	t0, s0, 0
	lw	t0, 0(t0)
	lw	t0, 0(t0)
	sub	ra, ra, t0
	sltiu	ra, ra, 1
	bne	ra, zero, .splitting_2
	j	.splitting_3
.splitting_2:
	addi	ra, s0, 0
	lw	ra, 0(ra)
	mv	s2, ra
	addi	ra, s0, 4
	lw	ra, 0(ra)
	mv	s3, ra
	addi	s4, s0, 0
	lw	ra, 0(s2)
	mv	s7, ra
	li	ra, 2
	mul	s8, s7, ra
	li	ra, 8
	mul	ra, s8, ra
	addi	a0, ra, 4
	call	malloc
	sw	s8, 0(a0)
	lw	ra, 0(s4)
	sw	a0, 0(s4)
	addi	t0, s0, 4
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	j	.unnamed_13_4
.unnamed_13_4:
	addi	ra, s0, 4
	lw	ra, 0(ra)
	sub	ra, ra, s3
	sltu	ra, zero, ra
	bne	ra, zero, .splitting_5
	j	.splitting_6
.splitting_6:
	mv	a1, s3
	mv	a0, s0
	mv	t2, s2
	mv	t1, s7
	mv	t0, s2
	mv	ra, s4
	mv	a1, a0
	mv	a1, t2
	mv	a1, t1
	mv	a1, t0
	mv	a1, ra
	j	.if_exit_7
.if_exit_7:
	addi	t0, s0, 0
	addi	ra, s0, 4
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, 0(t0)
	sw	s5, 0(t0)
	addi	t0, s0, 4
	lw	ra, 0(t0)
	addi	ra, ra, 1
	sw	ra, 0(t0)
	mv	ra, s6
	addi	ra, ra, 0
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	li	t0, 1
	sub	ra, ra, t0
	mv	s0, ra
	mv	ra, s0
	li	ra, 0
	mv	a7, ra
	li	ra, 0
	mv	t0, ra
	li	ra, 0
	mv	t5, ra
	li	ra, 0
	mv	t6, ra
	li	ra, 0
	mv	t3, ra
	li	ra, 0
	mv	t1, ra
	li	ra, 0
	mv	t4, ra
	li	ra, 0
	mv	a0, ra
	li	ra, 0
	li	ra, 0
	mv	t2, ra
	li	ra, 0
	mv	a1, ra
	li	ra, 0
	mv	a2, ra
	li	ra, 0
	mv	a6, ra
	li	ra, 0
	mv	a3, ra
	li	ra, 0
	mv	a5, ra
	li	ra, 0
	mv	a4, ra
	j	.unnamed_17_8
.unnamed_17_8:
	li	ra, 0
	slt	ra, ra, s0
	bne	ra, zero, .splitting_9
	j	.splitting_10
.splitting_9:
	mv	a3, s0
	mv	ra, s6
	li	ra, 1
	sub	t0, a3, ra
	li	ra, 2
	div	ra, t0, ra
	mv	a4, ra
	mv	t4, a4
	addi	ra, s6, 0
	lw	ra, 0(ra)
	mv	a2, t4
	mv	a1, ra
	addi	ra, a1, 0
	lw	t1, 0(ra)
	addi	t0, a2, 1
	li	ra, 8
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	t0, 0(ra)
	mv	a5, t0
	addi	ra, a5, 4
	lw	ra, 0(ra)
	sub	t1, zero, ra
	addi	ra, s6, 0
	lw	ra, 0(ra)
	mv	t3, s0
	mv	a0, ra
	addi	ra, a0, 0
	lw	a6, 0(ra)
	addi	t2, t3, 1
	li	ra, 8
	mul	ra, t2, ra
	add	ra, a6, ra
	lw	ra, 0(ra)
	mv	t2, ra
	addi	ra, t2, 4
	lw	ra, 0(ra)
	sub	ra, zero, ra
	mv	a6, ra
	slt	ra, t1, a6
	xori	ra, ra, 1
	bne	ra, zero, .splitting_11
	j	.splitting_12
.splitting_11:
	mv	a7, t0
	mv	t0, t4
	mv	ra, t2
	mv	t5, a2
	mv	t4, a6
	mv	a6, a3
	mv	a3, a4
	mv	a2, a7
	mv	a2, t0
	mv	a2, t3
	mv	a2, t1
	mv	a2, ra
	mv	a2, a0
	mv	a2, t2
	mv	a2, a1
	mv	a2, t5
	mv	a2, t4
	mv	a2, a6
	mv	a2, a5
	mv	a2, a3
	j	.unnamed_19_13
.unnamed_19_13:
	lw	s3, 24(sp)
	lw	s4, 28(sp)
	lw	s5, 32(sp)
	lw	s6, 36(sp)
	lw	s7, 40(sp)
	lw	s8, 44(sp)
	mv	ra, s1
	mv	s11, s10
	lw	s10, 8(sp)
	lw	s0, 12(sp)
	lw	s1, 16(sp)
	lw	s2, 20(sp)
	addi	sp, sp, 48
	ret
.splitting_12:
	addi	ra, s6, 0
	lw	t5, 0(ra)
	mv	t6, t4
	mv	ra, s0
	addi	a7, t5, 0
	lw	s2, 0(a7)
	addi	s0, t6, 1
	li	a7, 8
	mul	a7, s0, a7
	add	a7, s2, a7
	lw	a7, 0(a7)
	addi	s0, t5, 0
	lw	s3, 0(s0)
	addi	s2, t6, 1
	li	s0, 8
	mul	s0, s2, s0
	add	s0, s3, s0
	addi	s2, t5, 0
	lw	s4, 0(s2)
	addi	s3, ra, 1
	li	s2, 8
	mul	s2, s3, s2
	add	s3, s4, s2
	lw	s2, 0(s0)
	lw	s2, 0(s3)
	lw	s2, 0(s3)
	sw	s2, 0(s0)
	addi	s0, t5, 0
	lw	s3, 0(s0)
	addi	s2, ra, 1
	li	s0, 8
	mul	s0, s2, s0
	add	s2, s3, s0
	lw	s0, 0(s2)
	sw	a7, 0(s2)
	mv	a7, t0
	mv	t0, t4
	mv	s0, t4
	mv	t4, t2
	mv	s2, a7
	mv	s2, t0
	mv	s2, t5
	mv	s2, t6
	mv	s2, t3
	mv	s2, s0
	mv	s2, t1
	mv	s2, t4
	mv	s2, a0
	mv	s2, ra
	mv	s2, t2
	mv	s2, a1
	mv	s2, a2
	mv	s2, a6
	mv	s2, a3
	mv	s2, a5
	mv	s2, a4
	j	.unnamed_17_8
.splitting_10:
	mv	ra, t4
	mv	t5, a2
	mv	t4, a6
	mv	a6, a3
	mv	a3, a4
	mv	a2, a7
	mv	a2, t0
	mv	a2, t3
	mv	a2, t1
	mv	a2, ra
	mv	a2, a0
	mv	a2, t2
	mv	a2, a1
	mv	a2, t5
	mv	a2, t4
	mv	a2, a6
	mv	a2, a5
	mv	a2, a3
	j	.unnamed_19_13
.splitting_5:
	addi	t0, s0, 0
	addi	ra, s0, 4
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t1, t1, ra
	addi	ra, s0, 4
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t0, s2, ra
	lw	ra, 0(t1)
	lw	ra, 0(t0)
	lw	ra, 0(t0)
	sw	ra, 0(t1)
	addi	t0, s0, 4
	lw	ra, 0(t0)
	addi	ra, ra, 1
	sw	ra, 0(t0)
	j	.unnamed_13_4
.splitting_3:
	li	a1, 0
	li	a0, 0
	li	t2, 0
	li	t1, 0
	li	t0, 0
	li	ra, 0
	j	.if_exit_7

	.globl	Heap_Node.pop
Heap_Node.pop:
.entry_14:
	addi	sp, sp, -16
	sw	s0, 8(sp)
	sw	s1, 12(sp)
	mv	s0, ra
	addi	ra, a0, 0
	lw	ra, 0(ra)
	addi	ra, ra, 0
	lw	t1, 0(ra)
	li	t0, 1
	li	ra, 8
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	mv	s1, ra
	addi	t1, a0, 0
	addi	ra, a0, 0
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	li	t0, 1
	sub	t0, ra, t0
	lw	t1, 0(t1)
	li	ra, 0
	addi	t2, t1, 0
	lw	a2, 0(t2)
	addi	a1, ra, 1
	li	t2, 8
	mul	t2, a1, t2
	add	t2, a2, t2
	lw	t2, 0(t2)
	addi	a1, t1, 0
	lw	a2, 0(a1)
	addi	a1, ra, 1
	li	ra, 8
	mul	ra, a1, ra
	add	a3, a2, ra
	addi	ra, t1, 0
	lw	a2, 0(ra)
	addi	a1, t0, 1
	li	ra, 8
	mul	ra, a1, ra
	add	a1, a2, ra
	lw	ra, 0(a3)
	lw	ra, 0(a1)
	lw	ra, 0(a1)
	sw	ra, 0(a3)
	addi	ra, t1, 0
	lw	t1, 0(ra)
	addi	t0, t0, 1
	li	ra, 8
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, 0(t0)
	sw	t2, 0(t0)
	addi	ra, a0, 0
	lw	ra, 0(ra)
	addi	t2, ra, 4
	lw	t1, 0(t2)
	li	t0, 1
	sub	t0, t1, t0
	sw	t0, 0(t2)
	addi	t0, ra, 0
	addi	ra, ra, 4
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 8
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	li	a1, 0
	call	Heap_Node.maxHeapify
	mv	a0, s1
	mv	ra, s0
	lw	s0, 8(sp)
	lw	s1, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	Heap_Node.maxHeapify
Heap_Node.maxHeapify:
.entry_15:
	addi	sp, sp, -16
	sw	s0, 0(sp)
	sw	s1, 4(sp)
	sw	s2, 8(sp)
	sw	s3, 12(sp)
	mv	s0, ra
	mv	t0, a1
	mv	ra, a0
	li	ra, 2
	mul	ra, t0, ra
	addi	ra, ra, 1
	mv	a7, ra
	mv	t0, a1
	mv	ra, a0
	li	ra, 2
	mul	ra, t0, ra
	addi	ra, ra, 2
	mv	t3, ra
	mv	ra, a0
	addi	ra, ra, 0
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	slt	ra, a7, ra
	bne	ra, zero, .splitting_16
	j	.splitting_17
.splitting_16:
	addi	ra, a0, 0
	lw	t0, 0(ra)
	addi	ra, t0, 0
	lw	t2, 0(ra)
	addi	t1, a7, 1
	li	ra, 8
	mul	ra, t1, ra
	add	ra, t2, ra
	lw	ra, 0(ra)
	mv	a3, ra
	addi	ra, a3, 4
	lw	ra, 0(ra)
	sub	ra, zero, ra
	mv	a2, ra
	addi	ra, a0, 0
	lw	ra, 0(ra)
	mv	t2, a1
	mv	t1, ra
	addi	ra, t1, 0
	lw	a5, 0(ra)
	addi	a4, t2, 1
	li	ra, 8
	mul	ra, a4, ra
	add	ra, a5, ra
	lw	a4, 0(ra)
	addi	ra, a4, 4
	lw	ra, 0(ra)
	sub	ra, zero, ra
	slt	a6, ra, a2
	mv	a5, a4
	mv	t4, a7
	mv	t5, a3
	mv	a5, a6
	mv	a5, t0
	mv	a5, t4
	mv	a5, t1
	mv	a5, t2
	mv	a5, a2
	mv	a5, t5
	mv	a5, ra
	mv	a5, a3
	mv	a5, a4
	j	.unnamed_22_18
.unnamed_22_18:
	bne	a6, zero, .splitting_19
	j	.splitting_20
.splitting_20:
	mv	t0, a1
	mv	ra, t0
	j	.if_exit_21
.if_exit_21:
	mv	ra, a0
	addi	ra, ra, 0
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	slt	ra, t3, ra
	bne	ra, zero, .splitting_22
	j	.splitting_23
.splitting_23:
	li	ra, 0
	mv	a2, ra
	mv	ra, a2
	li	ra, 0
	li	ra, 0
	mv	t4, ra
	li	ra, 0
	mv	a5, ra
	li	ra, 0
	mv	a4, ra
	li	ra, 0
	mv	a3, ra
	li	t1, 0
	li	t5, 0
	li	t2, 0
	li	t6, 0
	li	a6, 0
	j	.unnamed_25_24
.unnamed_25_24:
	bne	a2, zero, .splitting_25
	j	.splitting_26
.splitting_26:
	mv	ra, t0
	j	.if_exit_27
.if_exit_27:
	sub	t0, ra, a1
	sltiu	t0, t0, 1
	bne	t0, zero, .splitting_28
	j	.splitting_29
.splitting_28:
	li	t1, 0
	li	t0, 0
	li	ra, 0
	j	.func_exit_30
.func_exit_30:
	lw	s3, 12(sp)
	mv	ra, s0
	lw	s0, 0(sp)
	lw	s1, 4(sp)
	lw	s2, 8(sp)
	addi	sp, sp, 16
	ret
.splitting_29:
	addi	t0, a0, 0
	lw	t0, 0(t0)
	mv	s1, a1
	mv	s2, ra
	mv	s3, t0
	addi	t0, s3, 0
	lw	t2, 0(t0)
	addi	t1, s1, 1
	li	t0, 8
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	t0, 0(t0)
	addi	t1, s3, 0
	lw	a1, 0(t1)
	addi	t2, s1, 1
	li	t1, 8
	mul	t1, t2, t1
	add	a2, a1, t1
	addi	t1, s3, 0
	lw	a1, 0(t1)
	addi	t2, s2, 1
	li	t1, 8
	mul	t1, t2, t1
	add	t2, a1, t1
	lw	t1, 0(a2)
	lw	t1, 0(t2)
	lw	t1, 0(t2)
	sw	t1, 0(a2)
	addi	t1, s3, 0
	lw	a1, 0(t1)
	addi	t2, s2, 1
	li	t1, 8
	mul	t1, t2, t1
	add	t2, a1, t1
	lw	t1, 0(t2)
	sw	t0, 0(t2)
	mv	a1, ra
	call	Heap_Node.maxHeapify
	mv	t1, s3
	mv	t0, s2
	mv	ra, s1
	mv	t1, t0
	mv	t1, ra
	j	.func_exit_30
.splitting_25:
	mv	ra, t3
	j	.if_exit_27
.splitting_22:
	addi	ra, a0, 0
	lw	t1, 0(ra)
	mv	ra, t3
	mv	t2, t1
	addi	t1, t2, 0
	lw	a3, 0(t1)
	addi	a2, ra, 1
	li	t1, 8
	mul	t1, a2, t1
	add	t1, a3, t1
	lw	a4, 0(t1)
	addi	t1, a4, 4
	lw	t1, 0(t1)
	sub	a3, zero, t1
	addi	t1, a0, 0
	lw	a5, 0(t1)
	addi	t1, a5, 0
	lw	a6, 0(t1)
	addi	a2, t0, 1
	li	t1, 8
	mul	t1, a2, t1
	add	t1, a6, t1
	lw	t1, 0(t1)
	mv	a6, t1
	addi	t1, a6, 4
	lw	t1, 0(t1)
	sub	t1, zero, t1
	mv	a7, t1
	slt	a2, a7, a3
	mv	t4, a4
	mv	t1, t0
	mv	t5, a6
	mv	t6, a6
	mv	a6, a7
	mv	ra, t4
	mv	ra, a5
	mv	ra, a4
	mv	ra, a3
	mv	ra, a2
	mv	ra, t1
	mv	ra, t5
	mv	ra, t2
	mv	ra, t6
	mv	ra, a6
	j	.unnamed_25_24
.splitting_19:
	mv	t0, a7
	j	.if_exit_21
.splitting_17:
	li	ra, 0
	mv	a6, ra
	mv	ra, a6
	li	ra, 0
	mv	a5, ra
	li	ra, 0
	mv	t0, ra
	li	ra, 0
	mv	t4, ra
	li	ra, 0
	mv	t1, ra
	li	ra, 0
	mv	t2, ra
	li	ra, 0
	mv	a2, ra
	li	t5, 0
	li	ra, 0
	li	a3, 0
	li	a4, 0
	j	.unnamed_22_18

	.globl	dijkstra
dijkstra:
.entry_31:
	addi	sp, sp, -64
	sw	s0, 12(sp)
	sw	s1, 16(sp)
	sw	s2, 20(sp)
	sw	s3, 24(sp)
	sw	s4, 28(sp)
	sw	s5, 32(sp)
	sw	s6, 36(sp)
	sw	s7, 40(sp)
	sw	s8, 44(sp)
	sw	s9, 48(sp)
	sw	s10, 52(sp)
	sw	s11, 56(sp)
	sw	ra, 60(sp)
	mv	s1, a0
	lw	s2, n
	lw	s3, INF
	lw	s4, g
	li	ra, 4
	mul	ra, s2, ra
	addi	a0, ra, 4
	call	malloc
	sw	s2, 0(a0)
	sw	a0, 8(sp)
	li	ra, 4
	mul	ra, s2, ra
	addi	a0, ra, 4
	call	malloc
	sw	s2, 0(a0)
	mv	s11, a0
	li	ra, 0
	j	.unnamed_30_32
.unnamed_30_32:
	slt	t0, ra, s2
	bne	t0, zero, .splitting_33
	j	.splitting_34
.splitting_34:
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t0, s11, ra
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	li	a0, 4
	call	malloc
	mv	s0, a0
	addi	s6, s0, 0
	li	a0, 8
	call	malloc
	mv	s5, a0
	addi	t0, s5, 4
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	addi	s7, s5, 0
	li	ra, 128
	addi	a0, ra, 4
	call	malloc
	li	ra, 16
	sw	ra, 0(a0)
	lw	ra, 0(s7)
	sw	a0, 0(s7)
	lw	ra, 0(s6)
	sw	s5, 0(s6)
	mv	s8, s0
	li	a0, 8
	call	malloc
	mv	a1, a0
	addi	t0, a1, 4
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	addi	t0, a1, 0
	lw	ra, 0(t0)
	sw	s1, 0(t0)
	sw	s2, n, ra
	sw	s3, INF, ra
	sw	s4, g, ra
	mv	a0, s8
	call	Heap_Node.push
	lw	ra, g
	lw	t1, INF
	lw	t0, n
	mv	t2, ra
	mv	t2, t0
	mv	t2, t1
	li	t2, 0
	mv	s10, t2
	li	t2, 0
	mv	s2, t2
	li	a4, 0
	li	a3, 0
	li	a2, 0
	li	a1, 0
	li	a0, 0
	li	t2, 0
	mv	s1, t2
	j	.unnamed_34_35
.unnamed_34_35:
	addi	t2, s8, 0
	lw	t2, 0(t2)
	mv	s9, t2
	addi	t2, s9, 4
	lw	t2, 0(t2)
	mv	s0, t2
	li	t2, 0
	sub	t2, s0, t2
	sltu	t2, zero, t2
	bne	t2, zero, .splitting_36
	j	.splitting_37
.splitting_36:
	sw	t0, n, t2
	sw	t1, INF, t0
	sw	ra, g, t0
	mv	a0, s8
	call	Heap_Node.pop
	lw	ra, g
	lw	t1, INF
	lw	t0, n
	addi	t2, a0, 0
	lw	t2, 0(t2)
	mv	s7, t2
	addi	a0, s7, 1
	li	t2, 4
	mul	a0, a0, t2
	lw	t2, 8(sp)
	add	t2, t2, a0
	lw	a0, 0(t2)
	li	t2, 1
	sub	t2, a0, t2
	sltiu	t2, t2, 1
	bne	t2, zero, .splitting_38
	j	.splitting_39
.splitting_39:
	addi	a0, s7, 1
	li	t2, 4
	mul	a0, a0, t2
	lw	t2, 8(sp)
	add	a0, t2, a0
	lw	t2, 0(a0)
	li	t2, 1
	sw	t2, 0(a0)
	addi	t2, ra, 8
	lw	a1, 0(t2)
	addi	a0, s7, 1
	li	t2, 4
	mul	t2, a0, t2
	add	a0, a1, t2
	lw	t2, 0(a0)
	lw	t2, 0(a0)
	mv	a0, s10
	mv	s4, t2
	mv	s5, ra
	mv	s6, t0
	mv	t2, s1
	mv	s3, t1
	mv	ra, a0
	mv	ra, s4
	mv	ra, s5
	mv	ra, s6
	mv	ra, t2
	mv	ra, s3
	j	.unnamed_37_40
.unnamed_37_40:
	li	ra, 1
	sub	ra, zero, ra
	sub	ra, s4, ra
	sltu	ra, zero, ra
	bne	ra, zero, .splitting_41
	j	.splitting_42
.splitting_42:
	mv	s10, a0
	mv	s2, s4
	mv	a4, s0
	mv	ra, s5
	mv	a3, s9
	mv	a2, s7
	mv	a1, s0
	mv	a0, s8
	mv	t0, s6
	mv	s1, t2
	mv	t1, s3
	mv	t2, s10
	mv	t2, s2
	mv	t2, a4
	mv	t2, ra
	mv	t2, a3
	mv	t2, a2
	mv	t2, a1
	mv	t2, a0
	mv	t2, t0
	mv	t2, s1
	mv	t2, t1
	j	.unnamed_34_35
.splitting_41:
	addi	ra, s5, 0
	lw	t1, 0(ra)
	addi	t0, s4, 1
	li	ra, 12
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	addi	ra, ra, 4
	lw	ra, 0(ra)
	mv	s1, ra
	addi	ra, s5, 0
	lw	t1, 0(ra)
	addi	t0, s4, 1
	li	ra, 12
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	addi	ra, ra, 8
	lw	ra, 0(ra)
	addi	t1, s7, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t0, s11, t0
	lw	t0, 0(t0)
	add	ra, t0, ra
	mv	s2, ra
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, s11, ra
	lw	ra, 0(ra)
	slt	ra, s2, ra
	xori	ra, ra, 1
	bne	ra, zero, .splitting_43
	j	.splitting_44
.splitting_43:
	mv	t1, s5
	mv	ra, s6
	mv	t0, s3
	mv	t2, t1
	mv	t2, ra
	mv	t2, t0
	j	.unnamed_40_45
.unnamed_40_45:
	addi	t2, t1, 4
	lw	a1, 0(t2)
	addi	a0, s4, 1
	li	t2, 4
	mul	t2, a0, t2
	add	a0, a1, t2
	lw	t2, 0(a0)
	lw	t2, 0(a0)
	mv	a0, s2
	mv	s4, t2
	mv	s5, t1
	mv	s6, ra
	mv	t2, s1
	mv	s3, t0
	mv	ra, a0
	mv	ra, s4
	mv	ra, s5
	mv	ra, s6
	mv	ra, t2
	mv	ra, s3
	j	.unnamed_37_40
.splitting_44:
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t0, s11, ra
	lw	ra, 0(t0)
	sw	s2, 0(t0)
	li	a0, 8
	call	malloc
	mv	a1, a0
	addi	t0, a1, 0
	lw	ra, 0(t0)
	sw	s1, 0(t0)
	addi	t1, a1, 4
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t0, s11, ra
	lw	ra, 0(t1)
	lw	ra, 0(t0)
	lw	ra, 0(t0)
	sw	ra, 0(t1)
	sw	s6, n, ra
	sw	s3, INF, ra
	sw	s5, g, ra
	mv	a0, s8
	call	Heap_Node.push
	lw	t1, g
	lw	t0, INF
	lw	ra, n
	mv	t2, t1
	mv	t2, ra
	mv	t2, t0
	j	.unnamed_40_45
.splitting_38:
	mv	a4, s0
	mv	a3, s9
	mv	a2, s7
	mv	a1, s0
	mv	a0, s8
	mv	t2, s10
	mv	s10, t2
	mv	t2, s2
	mv	s2, t2
	mv	t2, a4
	mv	t2, ra
	mv	t2, a3
	mv	t2, a2
	mv	t2, a1
	mv	t2, a0
	mv	t2, t0
	mv	t2, s1
	mv	s1, t2
	mv	t2, t1
	j	.unnamed_34_35
.splitting_37:
	sw	t0, n, t2
	sw	t1, INF, t0
	sw	ra, g, t0
	mv	a0, s11
	lw	s3, 24(sp)
	lw	s4, 28(sp)
	lw	s5, 32(sp)
	lw	s6, 36(sp)
	lw	s7, 40(sp)
	lw	s8, 44(sp)
	lw	s9, 48(sp)
	lw	ra, 60(sp)
	lw	s11, 56(sp)
	lw	s10, 52(sp)
	lw	s0, 12(sp)
	lw	s1, 16(sp)
	lw	s2, 20(sp)
	addi	sp, sp, 64
	ret
.splitting_33:
	addi	t1, ra, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t1, s11, t0
	lw	t0, 0(t1)
	sw	s3, 0(t1)
	addi	t1, ra, 1
	li	t0, 4
	mul	t1, t1, t0
	lw	t0, 8(sp)
	add	t1, t0, t1
	lw	t0, 0(t1)
	li	t0, 0
	sw	t0, 0(t1)
	addi	ra, ra, 1
	j	.unnamed_30_32

	.globl	_main
_main:
.entry_46:
	addi	sp, sp, -48
	sw	s0, 12(sp)
	sw	s1, 16(sp)
	sw	s2, 20(sp)
	sw	s3, 24(sp)
	sw	s4, 28(sp)
	sw	s5, 32(sp)
	sw	s6, 36(sp)
	sw	s7, 40(sp)
	sw	s8, 44(sp)
	mv	s0, ra
	lw	a0, n
	lw	t2, m
	lw	t1, g
	lw	t0, INF
	sw	a0, n, ra
	sw	t2, m, ra
	sw	t1, g, ra
	sw	t0, INF, ra
	call	getInt
	lw	t2, INF
	lw	t1, g
	lw	t0, m
	lw	ra, n
	sw	a0, n, ra
	sw	t0, m, ra
	sw	t1, g, ra
	sw	t2, INF, ra
	call	getInt
	lw	s4, INF
	lw	ra, g
	lw	ra, m
	lw	s1, n
	mv	s3, a0
	li	a0, 16
	call	malloc
	mv	s2, a0
	addi	s5, s2, 0
	li	ra, 12
	mul	ra, s3, ra
	addi	a0, ra, 4
	call	malloc
	sw	s3, 0(a0)
	lw	ra, 0(s5)
	sw	a0, 0(s5)
	addi	s5, s2, 4
	li	ra, 4
	mul	ra, s3, ra
	addi	a0, ra, 4
	call	malloc
	sw	s3, 0(a0)
	lw	ra, 0(s5)
	sw	a0, 0(s5)
	addi	s5, s2, 8
	li	ra, 4
	mul	ra, s1, ra
	addi	a0, ra, 4
	call	malloc
	sw	s1, 0(a0)
	lw	ra, 0(s5)
	sw	a0, 0(s5)
	li	ra, 0
	j	.unnamed_2_47
.unnamed_2_47:
	slt	t0, ra, s3
	bne	t0, zero, .splitting_48
	j	.splitting_49
.splitting_48:
	addi	t0, s2, 4
	lw	t2, 0(t0)
	addi	t1, ra, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t2, t2, t0
	li	t0, 1
	sub	t1, zero, t0
	lw	t0, 0(t2)
	sw	t1, 0(t2)
	addi	ra, ra, 1
	j	.unnamed_2_47
.splitting_49:
	li	ra, 0
	j	.unnamed_6_50
.unnamed_6_50:
	slt	t0, ra, s1
	bne	t0, zero, .splitting_51
	j	.splitting_52
.splitting_52:
	addi	t0, s2, 12
	lw	ra, 0(t0)
	li	ra, 0
	sw	ra, 0(t0)
	li	t0, 0
	mv	ra, s2
	mv	s8, t0
	mv	t0, s1
	mv	t1, s3
	mv	t2, s4
	mv	a0, ra
	mv	a0, s8
	mv	a0, t0
	mv	a0, t1
	mv	a0, t2
	li	a3, 0
	li	a2, 0
	li	a1, 0
	li	a0, 0
	j	.unnamed_26_53
.unnamed_26_53:
	slt	a0, s8, t1
	bne	a0, zero, .splitting_54
	j	.splitting_55
.splitting_55:
	li	s3, 0
	mv	a0, ra
	mv	a0, t0
	mv	a0, t1
	mv	a0, t2
	mv	a0, s3
	li	s1, 0
	li	s2, 0
	j	.unnamed_41_56
.unnamed_41_56:
	slt	a0, s3, t0
	bne	a0, zero, .splitting_57
	j	.splitting_58
.splitting_58:
	sw	t0, n, a0
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	li	a0, 0
	lw	s3, 24(sp)
	lw	s4, 28(sp)
	lw	s5, 32(sp)
	lw	s6, 36(sp)
	lw	s7, 40(sp)
	lw	s8, 44(sp)
	mv	ra, s0
	lw	s0, 12(sp)
	lw	s1, 16(sp)
	lw	s2, 20(sp)
	addi	sp, sp, 48
	ret
.splitting_57:
	sw	t0, n, a0
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	mv	a0, s3
	call	dijkstra
	mv	s2, a0
	lw	t2, INF
	lw	ra, g
	lw	t1, m
	lw	t0, n
	li	s1, 0
	mv	a0, ra
	mv	a0, s1
	mv	a0, t0
	mv	a0, t1
	mv	a0, t2
	j	.unnamed_45_59
.unnamed_45_59:
	slt	a0, s1, t0
	bne	a0, zero, .splitting_60
	j	.splitting_61
.splitting_61:
	sw	t0, n, a0
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	la	a0, unnamed_398
	call	println
	lw	t2, INF
	lw	ra, g
	lw	t1, m
	lw	t0, n
	addi	s3, s3, 1
	mv	a0, ra
	mv	a0, s1
	mv	a0, t0
	mv	a0, s2
	mv	a0, t1
	mv	a0, t2
	mv	a0, s3
	j	.unnamed_41_56
.splitting_60:
	addi	a1, s1, 1
	li	a0, 4
	mul	a0, a1, a0
	add	a0, s2, a0
	lw	a0, 0(a0)
	sub	a0, a0, t2
	sltiu	a0, a0, 1
	bne	a0, zero, .splitting_62
	j	.splitting_63
.splitting_62:
	sw	t0, n, a0
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	la	a0, unnamed_391
	call	print
	lw	t2, INF
	lw	ra, g
	lw	t1, m
	lw	t0, n
	mv	a0, ra
	mv	a0, t0
	mv	a0, t1
	mv	a0, t2
	j	.if_exit_64
.if_exit_64:
	sw	t0, n, a0
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	la	a0, unnamed_397
	call	print
	lw	t2, INF
	lw	ra, g
	lw	t1, m
	lw	t0, n
	addi	s1, s1, 1
	mv	a0, ra
	mv	a0, s1
	mv	a0, t0
	mv	a0, t1
	mv	a0, t2
	j	.unnamed_45_59
.splitting_63:
	addi	a1, s1, 1
	li	a0, 4
	mul	a0, a1, a0
	add	a0, s2, a0
	lw	a0, 0(a0)
	sw	t0, n, a1
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	call	toString
	lw	a1, INF
	lw	t2, g
	lw	t1, m
	lw	t0, n
	sw	t0, n, ra
	sw	t1, m, ra
	sw	t2, g, ra
	sw	a1, INF, ra
	call	print
	lw	t2, INF
	lw	ra, g
	lw	t1, m
	lw	t0, n
	mv	a0, ra
	mv	a0, t0
	mv	a0, t1
	mv	a0, t2
	j	.if_exit_64
.splitting_54:
	sw	t0, n, a0
	sw	t1, m, t0
	sw	ra, g, t0
	sw	t2, INF, ra
	call	getInt
	lw	a1, INF
	lw	t2, g
	lw	t1, m
	lw	t0, n
	mv	s2, a0
	sw	t0, n, ra
	sw	t1, m, ra
	sw	t2, g, ra
	sw	a1, INF, ra
	call	getInt
	lw	a1, INF
	lw	t2, g
	lw	t1, m
	lw	t0, n
	mv	s3, a0
	sw	t0, n, ra
	sw	t1, m, ra
	sw	t2, g, ra
	sw	a1, INF, ra
	call	getInt
	lw	s7, INF
	lw	s1, g
	lw	s5, m
	lw	s4, n
	mv	s6, s3
	mv	s3, a0
	li	a0, 12
	call	malloc
	addi	t0, a0, 0
	lw	ra, 0(t0)
	sw	s2, 0(t0)
	addi	t0, a0, 4
	lw	ra, 0(t0)
	sw	s6, 0(t0)
	addi	t0, a0, 8
	lw	ra, 0(t0)
	sw	s3, 0(t0)
	addi	t0, s1, 0
	addi	ra, s1, 12
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 12
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, 0(t0)
	sw	a0, 0(t0)
	addi	t0, s1, 4
	addi	ra, s1, 12
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t2, t1, ra
	addi	ra, s1, 8
	lw	t1, 0(ra)
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, 0(t2)
	lw	ra, 0(t0)
	lw	ra, 0(t0)
	sw	ra, 0(t2)
	addi	ra, s1, 8
	lw	t1, 0(ra)
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t1, t1, ra
	addi	t0, s1, 12
	lw	ra, 0(t1)
	lw	ra, 0(t0)
	lw	ra, 0(t0)
	sw	ra, 0(t1)
	addi	t0, s1, 12
	lw	ra, 0(t0)
	addi	ra, ra, 1
	sw	ra, 0(t0)
	addi	t0, s8, 1
	mv	ra, s1
	mv	a3, s2
	mv	a2, s3
	mv	s8, t0
	mv	t0, s4
	mv	a1, s1
	mv	t1, s5
	mv	a0, s6
	mv	t2, s7
	mv	a4, ra
	mv	a4, a3
	mv	a4, a2
	mv	a4, s8
	mv	a4, t0
	mv	a4, a1
	mv	a4, t1
	mv	a4, a0
	mv	a4, t2
	j	.unnamed_26_53
.splitting_51:
	addi	t0, s2, 8
	lw	t2, 0(t0)
	addi	t1, ra, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t2, t2, t0
	li	t0, 1
	sub	t1, zero, t0
	lw	t0, 0(t2)
	sw	t1, 0(t2)
	addi	ra, ra, 1
	j	.unnamed_6_50

