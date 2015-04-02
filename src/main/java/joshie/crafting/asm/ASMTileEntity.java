package joshie.crafting.asm;

import joshie.crafting.lib.CraftingInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMTileEntity extends AbstractASM {
	@Override
	public boolean isClass(String name) {
		return name.equals("net.minecraft.tileentity.TileEntity") || name.equals("aor");
	}

	@Override
	public ClassVisitor newInstance(ClassWriter writer) {
		return new ASMVisitor(writer);
	}
	
	public class ASMVisitor extends ClassVisitor {
		public ASMVisitor(ClassWriter writer) {
			super(Opcodes.ASM4, writer);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
			if (desc.equals("(Lnet/minecraft/nbt/NBTTagCompound;)V")) {
				if (name.equals("readFromNBT") || name.equals("func_145839_a")) {
					return new MethodVisitor(Opcodes.ASM4, visitor) {
						@Override
						public void visitCode() {
							super.visitCode();
							mv.visitVarInsn(Opcodes.ALOAD, 0);
							mv.visitVarInsn(Opcodes.ALOAD, 1);
					        mv.visitMethodInsn(Opcodes.INVOKESTATIC, CraftingInfo.ASMPATH + "player/PlayerTracker", "readFromNBT", "(Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/nbt/NBTTagCompound;)V", false);
						}
					};
				} else if (name.equals("writeToNBT") || name.equals("func_145841_b")) {
					return new MethodVisitor(Opcodes.ASM4, visitor) {
						@Override
						public void visitCode() {
							super.visitCode();
							mv.visitVarInsn(Opcodes.ALOAD, 0);
							mv.visitVarInsn(Opcodes.ALOAD, 1);
					        mv.visitMethodInsn(Opcodes.INVOKESTATIC, CraftingInfo.ASMPATH + "player/PlayerTracker", "writeToNBT", "(Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/nbt/NBTTagCompound;)V", false);
						}
					};
				}
			}
			
			return visitor;
		}
	}
}
