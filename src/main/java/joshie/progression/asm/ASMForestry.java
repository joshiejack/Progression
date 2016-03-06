package joshie.progression.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import joshie.progression.lib.ProgressionInfo;

public class ASMForestry extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("forestry.factory.gadgets.TileWorktable");
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
            if (desc.equals("(I)Z") && name.equals("canTakeStack")) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, "forestry/factory/gadgets/TileWorktable", "craftingInventory", "Lforestry/core/inventory/TileInventoryAdapter;");
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "forestry/factory/gadgets/TileWorktable", "getInternalInventory", "()Lforestry/core/inventory/IInventoryAdapter;", false);
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, "forestry/factory/gadgets/TileWorktable", "currentRecipe", "Lforestry/factory/recipes/RecipeMemory$Recipe;");
                        mv.visitVarInsn(Opcodes.ILOAD, 1);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ProgressionInfo.ASMPATH + "asm/helpers/ForestryHelper", "canCraftRecipe", "(Lnet/minecraft/tileentity/TileEntity;Lforestry/core/inventory/TileInventoryAdapter;Lnet/minecraft/inventory/IInventory;Lforestry/factory/recipes/RecipeMemory$Recipe;I)Z", false);
                        mv.visitInsn(Opcodes.IRETURN);
                    }
                };
            }

            return visitor;
        }
    }
}