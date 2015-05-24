package joshie.progression.asm;

import joshie.progression.lib.ProgressionInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMThaumcraft extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("thaumcraft.common.container.ContainerArcaneWorkbench");
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
                            mv.visitFieldInsn(Opcodes.GETFIELD, "thaumcraft/common/container/ContainerArcaneWorkbench", "tileEntity", "Lthaumcraft/common/tiles/TileArcaneWorkbench;");
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, "thaumcraft/common/container/ContainerArcaneWorkbench", "ip", "Lnet/minecraft/entity/player/InventoryPlayer;");
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, ProgressionInfo.ASMPATH + "asm/helpers/ThaumcraftHelper", "onContainerChanged", "(Lthaumcraft/common/tiles/TileArcaneWorkbench;Lnet/minecraft/entity/player/InventoryPlayer;)V", false);
                            mv.visitInsn(Opcodes.RETURN);
                        }
                    };
                }
            }

            return visitor;
        }
    }
}