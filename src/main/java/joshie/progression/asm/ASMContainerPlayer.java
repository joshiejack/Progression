package joshie.progression.asm;

import joshie.progression.lib.ProgressionInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMContainerPlayer extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.inventory.ContainerPlayer") || name.equals("net.minecraft.inventory.ContainerPlayer$1") || name.equals("aaq") || name.equals("aap");
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
        public MethodVisitor visitMethod(int access, final String name, String desc, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (desc.equals("(Lnet/minecraft/inventory/IInventory;)V") || desc.equals("(Lrb;)V")) {
                if (name.equals("onCraftMatrixChanged") || name.equals("a") || name.equals("func_75130_a")) {
                    return new MethodVisitor(Opcodes.ASM4, visitor) {
                        @Override
                        public void visitCode() {
                            String matrix = name.equals("onCraftMatrixChanged") ? "craftMatrix" : "field_75181_e";
                            String result = name.equals("onCraftMatrixChanged") ? "craftResult" : "field_75179_f";
                            String player = name.equals("onCraftMatrixChanged") ? "thePlayer" : "field_82862_h";
                            String world = name.equals("onCraftMatrixChanged") ? "worldObj" : "field_70170_p";
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/inventory/ContainerPlayer", matrix, "Lnet/minecraft/inventory/InventoryCrafting;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/inventory/ContainerPlayer", result, "Lnet/minecraft/inventory/IInventory;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/inventory/ContainerPlayer", player, "Lnet/minecraft/entity/player/EntityPlayer;");
                            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", world, "Lnet/minecraft/world/World;");
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, ProgressionInfo.ASMPATH + "asm/helpers/VanillaHelper", "onContainerChanged", "(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/inventory/IInventory;Lnet/minecraft/world/World;)V", false);
                            mv.visitInsn(Opcodes.RETURN);
                        }
                    };
                }
            }

            return visitor;
        }
    }
}