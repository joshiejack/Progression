package joshie.crafting.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMFurnace extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.tileentity.TileEntityFurnace") || name.equals("apg");
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
            if (desc.equals("()Z") && (name.equals("canSmelt") || name.equals("k"))) {
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
                        if (name.equals("getSmeltingResult") || (name.equals("a") && desc.equals("(Ladd;)Ladd;"))) {
                            String stacks = name.equals("getSmeltingResult") ? "furnaceItemStacks" : "field_145957_n";
                            mv.visitMethodInsn(opcode, owner, name, desc, itf);
                            mv.visitVarInsn(Opcodes.ASTORE, 1);
                            Label l3 = new Label();
                            mv.visitLabel(l3);
                            mv.visitFieldInsn(Opcodes.GETSTATIC, "joshie/crafting/api/crafting/CraftingEvent$CraftingType", "FURNACE", "Ljoshie/crafting/api/crafting/CraftingEvent$CraftingType;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityFurnace", stacks, "[Lnet/minecraft/item/ItemStack;");
                            mv.visitInsn(Opcodes.ICONST_0);
                            mv.visitInsn(Opcodes.AALOAD);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "joshie/crafting/helpers/CraftingHelper", "canUseItemForCrafting", "(Ljoshie/crafting/api/crafting/CraftingEvent$CraftingType;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/item/ItemStack;)Z", false);
                            Label l4 = new Label();
                            mv.visitJumpInsn(Opcodes.IFNE, l4);
                            mv.visitInsn(Opcodes.ICONST_0);
                            mv.visitInsn(Opcodes.IRETURN);
                            mv.visitLabel(l4);
                            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "net/minecraft/item/ItemStack" }, 0, null);
                            mv.visitFieldInsn(Opcodes.GETSTATIC, "joshie/crafting/api/crafting/CraftingEvent$CraftingType", "FURNACE", "Ljoshie/crafting/api/crafting/CraftingEvent$CraftingType;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitVarInsn(Opcodes.ALOAD, 1);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "joshie/crafting/helpers/CraftingHelper", "canCraftItem", "(Ljoshie/crafting/api/crafting/CraftingEvent$CraftingType;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/item/ItemStack;)Z", false);
                            Label l5 = new Label();
                            mv.visitJumpInsn(Opcodes.IFNE, l5);
                            mv.visitInsn(Opcodes.ICONST_0);
                            mv.visitInsn(Opcodes.IRETURN);
                            mv.visitLabel(l5);
                            canVisit = false;
                        } else super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }
                };
            }

            return visitor;
        }
    }
}
