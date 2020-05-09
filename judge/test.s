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

	.globl	qpow
qpow:
.entry_2:
	li	t0, 1
	mv	t1, t0
	mv	t1, a0
	mv	t1, a1
	j	.unnamed_2_3
.unnamed_2_3:
	li	t1, 0
	slt	t1, t1, a1
	bne	t1, zero, .splitting_4
	j	.splitting_5
.splitting_5:
	mv	a0, t0
	ret
.splitting_4:
	andi	t2, a1, 1
	li	t1, 1
	sub	t1, t2, t1
	sltiu	t1, t1, 1
	bne	t1, zero, .splitting_6
	j	.splitting_7
.splitting_6:
	mul	t0, t0, a0
	rem	t0, t0, a2
	j	.if_exit_8
.if_exit_8:
	mul	t1, a0, a0
	rem	a0, t1, a2
	li	t1, 2
	div	a1, a1, t1
	mv	t1, t0
	mv	t1, a0
	mv	t1, a1
	j	.unnamed_2_3
.splitting_7:
	j	.if_exit_8

	.globl	_main
_main:
.entry_9:
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	li	a0, 2
	li	a1, 10
	li	a2, 10000
	call	qpow
	call	toString
	call	println
	li	a0, 0
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

