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
	li	t1, 10000
	j	.entry_2
.entry_2:
	li	a0, 1
	j	.unnamed_2_3
.unnamed_2_3:
	li	t2, 0
	slt	t2, t2, t0
	bne	t2, zero, .unnamed_3_4
	j	.unnamed_4_5
.unnamed_3_4:
	andi	a1, t0, 1
	li	t2, 1
	sub	t2, a1, t2
	sltiu	t2, t2, 1
	bne	t2, zero, .if_then_6
	j	.if_exit_7
.if_then_6:
	mul	t2, a0, ra
	rem	a0, t2, t1
	j	.if_exit_7
.if_exit_7:
	mul	ra, ra, ra
	rem	ra, ra, t1
	li	t2, 2
	div	t0, t0, t2
	j	.unnamed_2_3
.unnamed_4_5:
	j	.second_half_8
.second_half_8:
	call	toString
	call	println
	li	a0, 0
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

