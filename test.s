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
	li	a1, 5
	li	a0, 0
	li	t1, 1
	mv	t0, a0
	mv	t0, t1
	li	t0, 0
	j	.unnamed_2_2
.unnamed_2_2:
	slt	t0, a1, t1
	xori	t0, t0, 1
	bne	t0, zero, .splitting_3
	j	.splitting_4
.splitting_4:
	ret
.splitting_3:
	li	t0, 1
	mv	t2, a0
	mv	t2, t0
	j	.unnamed_6_5
.unnamed_6_5:
	slt	t2, a1, t0
	xori	t2, t2, 1
	bne	t2, zero, .splitting_6
	j	.splitting_7
.splitting_6:
	add	a0, a0, t1
	addi	t0, t0, 1
	mv	t2, a0
	mv	t2, t0
	j	.unnamed_6_5
.splitting_7:
	addi	a0, a0, 1
	addi	t1, t1, 1
	mv	t2, a0
	mv	t2, t0
	mv	t2, t1
	j	.unnamed_2_2

