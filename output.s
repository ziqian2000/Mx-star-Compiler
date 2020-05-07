	.section	.data

	.globl	unnamed_2
unnamed_2:
	.string	"FUCK"

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
	la	a0, unnamed_2
	call	print
	li	a0, 0
	mv	ra, s0
	lw	s0, 12(sp)
	addi	sp, sp, 16
	ret

