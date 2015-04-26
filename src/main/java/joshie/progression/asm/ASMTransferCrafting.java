package joshie.progression.asm;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.lib.ProgressionInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/** Moves the firing of the oncrafting event in slotcrafting
 *  to firing when the onCrafting is called, instead of onPickupFrom Slot. */
public class ASMTransferCrafting extends AbstractASM {
    public static List accepted = new ArrayList();
    static {
        accepted.add("tconstruct.tools.inventory.CraftingStationContainer");
        accepted.add("net.minecraft.inventory.ContainerPlayer$1");
        accepted.add("net.minecraft.inventory.ContainerPlayer");
        accepted.add("net.minecraft.inventory.ContainerWorkbench");
        accepted.add("aaf");
        accepted.add("aaq");
    }

    @Override
    public boolean isClass(String name) {
        return accepted.contains(name);
    }

    @Override
    public ClassVisitor newInstance(ClassWriter cw) {
        return new ASMVisitor(cw);
    }

    public class ASMVisitor extends ClassVisitor {
        public ASMVisitor(ClassWriter writer) {
            super(Opcodes.ASM4, writer);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if ((desc.equals("(Lnet/minecraft/entity/player/EntityPlayer;I)Lnet/minecraft/item/ItemStack;") && name.equals("transferStackInSlot")) || (desc.equals("(Lyz;I)Ladd;") && name.equals("b"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    boolean isDone = false;

                    @Override
                    //Ignore code between instance and firePlayerCraftingEvent
                    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                        if ((name.equals("onSlotChange") || name.equals("func_75220_a") || name.equals("a")) && !isDone) {
                            String copy = name.equals("onSlotChange") ? "copy" : "func_77946_l";
                            isDone = true;
                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                            mv.visitFieldInsn(Opcodes.GETSTATIC, ProgressionInfo.ASMPATH + "api/ProgressionAPI", "registry", "L" + ProgressionInfo.ASMPATH + "api/IRegistry;");
                            mv.visitVarInsn(Opcodes.ALOAD, 1);
                            mv.visitLdcInsn("crafting");
                            mv.visitInsn(Opcodes.ICONST_1);
                            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitInsn(Opcodes.ICONST_0);
                            mv.visitVarInsn(Opcodes.ALOAD, 3);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", copy, "()Lnet/minecraft/item/ItemStack;", false);
                            mv.visitInsn(Opcodes.AASTORE);
                            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, ProgressionInfo.ASMPATH + "api/IRegistry", "fireTrigger", "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;[Ljava/lang/Object;)Z", true);
                            mv.visitInsn(Opcodes.POP);
                        } else super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }
                };
            }

            return visitor;
        }
    }
}
