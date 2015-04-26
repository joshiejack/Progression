package joshie.progression.asm;

import joshie.progression.lib.ProgressionInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMAE2 extends AbstractASM {	
	@Override
	public boolean isClass(String name) {
		return name.equals("appeng.container.implementations.ContainerCraftingTerm");
	}

	@Override
	public ClassVisitor newInstance(ClassWriter writer) {
		return new TerminalVisitor(writer);
	}	
	
	public class TerminalVisitor extends ClassVisitor {
		public TerminalVisitor(ClassWriter writer) {
			super(Opcodes.ASM4, writer);
		}
		
		@Override
		public FieldVisitor visitField (int access, String name, String desc, String signature, Object value) {
			FieldVisitor visitor = super.visitField(access, name, desc, signature, value);
			if (name.equals("output")) {
				visitor = super.visitField(Opcodes.ACC_PRIVATE, "thePlayer", "Lnet/minecraft/entity/player/EntityPlayer;", null, null);
			}
			
			return visitor;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		    
			MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
			if (desc.equals("(Lnet/minecraft/inventory/IInventory;)V") && (name.equals("onCraftMatrixChanged") || name.equals("func_75130_a"))) {
				return new MethodVisitor(Opcodes.ASM4, visitor) {
					@Override
					public void visitTypeInsn(int opcode, String name) {
						if (name.equals("appeng/container/ContainerNull")) {
							super.visitTypeInsn(Opcodes.NEW, ProgressionInfo.ASMPATH + "asm/ContainerPlayer");
						} else super.visitTypeInsn(opcode, name);
					}
					
					@Override
					   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
						if (name.equals("<init>") && owner.contains("ContainerNull")) {
							super.visitVarInsn(Opcodes.ALOAD, 0);
							super.visitFieldInsn(Opcodes.GETFIELD, "appeng/container/implementations/ContainerCraftingTerm", "thePlayer", "Lnet/minecraft/entity/player/EntityPlayer;");
							super.visitMethodInsn(opcode, ProgressionInfo.ASMPATH + "asm/ContainerPlayer", name, "(Lnet/minecraft/entity/player/EntityPlayer;)V", itf);
						} else super.visitMethodInsn(opcode, owner, name, desc, itf);
					}
					
					@Override
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
						if (name.equals("cn") && desc.equals("Lappeng/container/ContainerNull;")) {
							super.visitLocalVariable(name, "L" + ProgressionInfo.ASMPATH + "asm/ContainerPlayer;", signature, start, end, index);
						} else super.visitLocalVariable(name, desc, signature, start, end, index);
					}
				};
			} else if (name.equals("<init>")) {
				return new MethodVisitor(Opcodes.ASM4, visitor) {
					@Override
					public void visitCode() {
					    String player = CraftingTransformer.isObfuscated? "field_70458_d" : "player";
						super.visitVarInsn(Opcodes.ALOAD, 0);
						super.visitVarInsn(Opcodes.ALOAD, 1);
						super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/player/InventoryPlayer", player, "Lnet/minecraft/entity/player/EntityPlayer;");
						super.visitFieldInsn(Opcodes.PUTFIELD, "appeng/container/implementations/ContainerCraftingTerm", "thePlayer", "Lnet/minecraft/entity/player/EntityPlayer;");
						super.visitCode();
					}
				};
			}
			
			return visitor;
		}
	}
}
