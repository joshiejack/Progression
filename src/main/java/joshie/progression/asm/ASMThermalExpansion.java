package joshie.progression.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import joshie.progression.lib.ProgressionInfo;

public class ASMThermalExpansion extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("cofh.thermalexpansion.gui.container.device.ContainerWorkbench");
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
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "cofh/thermalexpansion/gui/container/device/ContainerWorkbench", "craftMatrix", "Lcofh/lib/inventory/InventoryCraftingCustom;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "cofh/thermalexpansion/gui/container/device/ContainerWorkbench", "craftResult", "Lnet/minecraft/inventory/IInventory;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "cofh/thermalexpansion/gui/container/device/ContainerWorkbench", "myTile", "Lcofh/thermalexpansion/block/device/TileWorkbench;");
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "cofh/thermalexpansion/block/device/TileWorkbench", "getWorldObj", "()Lnet/minecraft/world/World;", false);
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