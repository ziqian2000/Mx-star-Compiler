	.section	.data

	.globl	unnamed_49
unnamed_49:
	.string	" "

	.globl	unnamed_51
unnamed_51:
	.string	""

	.text

	.globl	main
main:
.unnamed_1_0:
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	call	_main
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	_main
_main:
.entry_1:
	addi	sp, sp, -32
	sw	s0, 12(sp)
	sw	s1, 16(sp)
	sw	s2, 20(sp)
	sw	s3, 24(sp)
	sw	s4, 28(sp)
	mv	s0, ra
	call	getInt
	mv	s2, a0
	li	ra, 4
	mul	ra, s2, ra
	addi	a0, ra, 4
	call	malloc
	sw	s2, 0(a0)
	mv	s3, a0
	li	ra, 0
	mv	s4, ra
	mv	ra, s4
	j	.unnamed_10_2
.unnamed_10_2:
	slt	ra, s4, s2
	bne	ra, zero, .splitting_3
	j	.splitting_4
.splitting_4:
	mv	a1, s3
	lw	a0, 0(a1)
	li	t1, 0
	li	t0, 0
	li	t2, 0
	j	.unnamed_2_5
.unnamed_2_5:
	li	ra, 1
	sub	ra, a0, ra
	slt	ra, t1, ra
	bne	ra, zero, .splitting_6
	j	.splitting_7
.splitting_6:
	mv	t2, t1
	addi	t0, t1, 1
	mv	ra, t0
	mv	ra, t2
	j	.unnamed_6_8
.unnamed_6_8:
	slt	ra, t0, a0
	bne	ra, zero, .splitting_9
	j	.splitting_10
.splitting_9:
	addi	a2, t0, 1
	li	ra, 4
	mul	ra, a2, ra
	add	a3, a1, ra
	addi	a2, t2, 1
	li	ra, 4
	mul	ra, a2, ra
	add	ra, a1, ra
	lw	a2, 0(a3)
	lw	ra, 0(ra)
	slt	ra, a2, ra
	bne	ra, zero, .splitting_11
	j	.splitting_12
.splitting_12:
	j	.if_exit_13
.if_exit_13:
	addi	t0, t0, 1
	mv	ra, t0
	mv	ra, t2
	j	.unnamed_6_8
.splitting_11:
	mv	t2, t0
	j	.if_exit_13
.splitting_10:
	addi	a2, t1, 1
	li	ra, 4
	mul	ra, a2, ra
	add	ra, a1, ra
	lw	ra, 0(ra)
	addi	a3, t1, 1
	li	a2, 4
	mul	a2, a3, a2
	add	a4, a1, a2
	addi	a3, t2, 1
	li	a2, 4
	mul	a2, a3, a2
	add	a3, a1, a2
	lw	a2, 0(a4)
	lw	a2, 0(a3)
	lw	a2, 0(a3)
	sw	a2, 0(a4)
	addi	a3, t2, 1
	li	a2, 4
	mul	a2, a3, a2
	add	a3, a1, a2
	lw	a2, 0(a3)
	sw	ra, 0(a3)
	addi	t1, t1, 1
	mv	t0, t1
	mv	t0, t2
	j	.unnamed_2_5
.splitting_7:
	li	ra, 0
	mv	s1, ra
	mv	ra, s1
	j	.unnamed_14_14
.unnamed_14_14:
	slt	ra, s1, s2
	bne	ra, zero, .splitting_15
	j	.splitting_16
.splitting_15:
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, s3, ra
	lw	a0, 0(ra)
	call	toString
	la	a1, unnamed_49
	call	string_add
	call	print
	addi	ra, s1, 1
	mv	s1, ra
	mv	ra, s1
	j	.unnamed_14_14
.splitting_16:
	la	a0, unnamed_51
	call	println
	li	a0, 0
	lw	s3, 24(sp)
	lw	s4, 28(sp)
	mv	ra, s0
	lw	s0, 12(sp)
	lw	s1, 16(sp)
	lw	s2, 20(sp)
	addi	sp, sp, 32
	ret
.splitting_3:
	addi	t0, s4, 1
	li	ra, 4
	mul	ra, t0, ra
	add	s1, s3, ra
	call	getInt
	lw	ra, 0(s1)
	sw	a0, 0(s1)
	addi	ra, s4, 1
	mv	s4, ra
	mv	ra, s4
	j	.unnamed_10_2

