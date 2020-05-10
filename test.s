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

	.globl	_main
_main:
.entry_2:
	li	a0, 0
	ret

