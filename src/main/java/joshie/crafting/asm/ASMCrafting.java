package joshie.crafting.asm;

import joshie.crafting.lib.CraftingInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMCrafting extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.item.crafting.CraftingManager") || name.equals("aff") || name.equals("afe");
    }

    @Override
    public ClassVisitor newInstance(ClassWriter writer) {
        return new CraftingVisitor(writer);
    }

    public class CraftingVisitor extends ClassVisitor {
        public CraftingVisitor(ClassWriter writer) {
            super(Opcodes.ASM4, writer);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if ((desc.equals("(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;") && name.equals("findMatchingRecipe")) || (desc.equals("(Laae;Lahb;)Ladd;") && name.equals("a"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        mv.visitVarInsn(Opcodes.ALOAD, 1);
                        mv.visitVarInsn(Opcodes.ALOAD, 2);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, CraftingInfo.ASMPATH + "crafting/RecipeHandler", "findMatchingRecipe", "(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;", false);
                        mv.visitInsn(Opcodes.ARETURN);
                        mv.visitMaxs(2, 3);
                    }
                };
            }

            return visitor;
        }
    }
}
