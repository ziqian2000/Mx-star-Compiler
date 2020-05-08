	.section	.data

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
	addi	sp, sp, -16
	sw	s0, 12(sp)
	mv	s0, ra
	li	ra, 2
	li	t0, 10
	li	t2, 10000
	li	a0, 1
	mv	t1, ra
	mv	t1, t0
	mv	t1, a0
	j	.unnamed_2_2
.unnamed_2_2:
	li	t1, 0
	slt	t1, t1, t0
	bne	t1, zero, .splitting_3
	j	.splitting_4
.splitting_3:
	andi	a1, t0, 1
	li	t1, 1
	sub	t1, a1, t1
	sltiu	t1, t1, 1
	bne	t1, zero, .splitting_5
	j	.splitting_6
.splitting_6:
	j	.if_exit_7
.if_exit_7:
	mul	ra, ra, ra
	rem	ra, ra, t2
	li	t1, 2
	div	t0, t0, t1
	mv	t1, ra
	mv	t1, t0
	mv	t1, a0
	j	.unnamed_2_2
.splitting_5:
	mul	t1, a0, ra
	rem	a0, t1, t2
	j	.if_exit_7
.splitting_4:
	call	toString
	call	println
	li	a0, 0
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

