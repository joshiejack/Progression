package joshie.crafting.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMFurnace extends AbstractASM {	
	@Override
	public boolean isClass(String name) {
		return name.equals("net.minecraft.tileentity.TileEntityFurnace");
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
			if (desc.equals("()Z") && name.equals("canSmelt")) {
				return new MethodVisitor(Opcodes.ASM4, visitor) {
					boolean canVisit = true;
					@Override
					public void visitVarInsn(int opcode, int var) {
						if (!canVisit) {
							canVisit = true;
						} else super.visitVarInsn(opcode, var);
					}
					
					@Override
					   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
						if (name.equals("getSmeltingResult")) {
							super.visitMethodInsn(opcode, owner, name, desc, itf);
							super.visitVarInsn(Opcodes.ASTORE, 1);
							Label l3 = new Label();
							super.visitLabel(l3);
							super.visitVarInsn(Opcodes.ALOAD, 1);
							Label l4 = new Label();
							super.visitJumpInsn(Opcodes.IFNULL, l4);
							super.visitFieldInsn(Opcodes.GETSTATIC, "joshie/crafting/api/CraftingAPI", "players", "Ljoshie/crafting/api/IPlayerTracker;");
							super.visitVarInsn(Opcodes.ALOAD, 0);
							super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "joshie/crafting/api/IPlayerTracker", "getTileOwner", "(Lnet/minecraft/tileentity/TileEntity;)Ljoshie/crafting/api/crafting/ICrafter;", true);
							super.visitFieldInsn(Opcodes.GETSTATIC, "joshie/crafting/api/crafting/CraftingType", "FURNACE", "Ljoshie/crafting/api/crafting/CraftingType;");
							super.visitVarInsn(Opcodes.ALOAD, 1);
							super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "joshie/crafting/api/crafting/ICrafter", "canCraftItem", "(Ljoshie/crafting/api/crafting/CraftingType;Lnet/minecraft/item/ItemStack;)Z", true);
							super.visitJumpInsn(Opcodes.IFNE, l4);
							super.visitInsn(Opcodes.ICONST_0);
							super.visitInsn(Opcodes.IRETURN);
							super.visitLabel(l4);
							canVisit = false;
						} else super.visitMethodInsn(opcode, owner, name, desc, itf);
					}
				};
			}
			
			return visitor;
		}
	}
}
