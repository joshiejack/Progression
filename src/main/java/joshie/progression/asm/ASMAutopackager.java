package joshie.progression.asm;

import joshie.progression.lib.ProgressionInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMAutopackager extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("org.mcupdater.autopackager.TilePackager");
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
            if (name.equals("tryCraft")) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    boolean thisOne = false;

                    @Override
                    public void visitVarInsn(int opcode, int var) {
                        if (opcode == Opcodes.ASTORE && var == 11 && !thisOne) {
                            thisOne = true;
                            super.visitVarInsn(opcode, var);
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitVarInsn(Opcodes.ALOAD, 10);
                            mv.visitVarInsn(Opcodes.ALOAD, 11);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, ProgressionInfo.ASMPATH + "asm/helpers/AutopackagerHelper", "getResult", "(Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false);
                            mv.visitVarInsn(Opcodes.ASTORE, 11);
                        } else {
                            thisOne = false;
                            super.visitVarInsn(opcode, var);
                        }
                    }
                };
            }

            return visitor;
        }
    }
}