package joshie.progression.asm;

import joshie.progression.lib.ProgressionInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/** Moves the firing of the oncrafting event in slotcrafting
 *  to firing when the onCrafting is called, instead of onPickupFrom Slot. */
public class ASMTransferCrafting extends AbstractASM {
    public static HashMap<String, String> acceptedMap = new HashMap();
    public static List accepted = new ArrayList();
    static {
        accepted.add("slimeknights.mantle.inventory.ContainerMultiModule");
        acceptedMap.put("slimeknights.mantle.inventory.ContainerMultiModule", "notifySlotAfterTransfer");
        accepted.add("thaumcraft.common.container.ContainerArcaneWorkbench");
        accepted.add("net.minecraft.inventory.ContainerPlayer");
        accepted.add("xy");
        accepted.add("net.minecraft.inventory.ContainerWorkbench");
        accepted.add("xq");
    }

    @Override
    public boolean isClass(String name) {
        return accepted.contains(name);
    }

    @Override
    public ClassVisitor newInstance(String name, ClassWriter cw) {
        if (acceptedMap.containsKey(name)) {
            final String value = acceptedMap.get(name);
            return new ASMVisitor(cw) {
                @Override
                public boolean isNameValid(String name) {
                    return name.equals(value);
                }
            };
        }

        return new ASMVisitor(cw);
    }

    public class ASMVisitor extends ClassVisitor {
        public ASMVisitor(ClassWriter writer) {
            super(Opcodes.ASM4, writer);
        }

        public boolean isNameValid(String name) {
            return name.equals("transferStackInSlot") || name.equals("func_82846_b") || name.equals("b");
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (isNameValid(name)) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    boolean isDone = false;

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                        if ((((name.equals("onSlotChange") || name.equals("func_75220_a")) && desc.equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V")) || (name.equals("a") && desc.equals("(Lzx;Lzx;)V"))) && !isDone) {
                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                            isDone = true;
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitVarInsn(ALOAD, 4);
                            mv.visitVarInsn(ALOAD, 1);
                            mv.visitVarInsn(ALOAD, 3);
                            mv.visitMethodInsn(INVOKESTATIC, ProgressionInfo.ASMPATH + "asm/helpers/TransferHelper", "onPickup", "(Ljava/lang/Object;Lnet/minecraft/inventory/Slot;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V", false);
                        } else super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }
                };
            }

            return visitor;
        }
    }
}
