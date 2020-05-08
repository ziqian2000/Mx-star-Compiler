	.section	.data

	.globl	c
c:
	.zero	4

	.globl	x
x:
	.zero	4

	.text

	.globl	main
main:
.unnamed_1_0:
	addi	sp, sp, -16
	sw	s0, 8(sp)
	sw	s1, 12(sp)
	mv	s0, ra
	li	a0, 4
	call	malloc
	li	t0, 123
	sw	t0, c, ra
	lw	s1, c
	li	a0, 4
	call	malloc
	sw	s1, c, ra
	mv	a0, s1
	call	lol
	lw	t0, c
	sw	a0, x, ra
	sw	t0, c, ra
	call	_main
	lw	t0, c
	sw	t0, c, ra
	mv	ra, s0
	lw	s0, 8(sp)
	lw	s1, 12(sp)
	addi	sp, sp, 16
	ret

	.globl	lol
lol:
.entry_1:
	addi	a0, a0, 1
	ret

	.globl	_main
_main:
.entry_2:
	lw	a0, x
	sw	a0, x, t0
	ret

