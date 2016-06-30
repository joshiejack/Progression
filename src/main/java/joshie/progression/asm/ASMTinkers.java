package joshie.progression.asm;

import joshie.progression.asm.helpers.ASMHelper.ObfType;
import joshie.progression.lib.PInfo;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class ASMTinkers extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("slimeknights.tconstruct.tools.inventory.ContainerCraftingStation");
    }

    @Override
    public boolean isValidASMType(ASMType type) {
        return type == ASMType.OVERRIDE;
    }

    @Override
    public String[] getMethodNameAndDescription() {
        return new String[] { "onCraftMatrixChanged", "(Lnet/minecraft/inventory/IInventory;)V", "func_75130_a", "(Lnet/minecraft/inventory/IInventory;)V",  "a", "(Lql;)V" };
    }

    @Override
    public void addInstructions(ObfType type, InsnList list) {
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "slimeknights/tconstruct/tools/inventory/ContainerCraftingStation", "craftMatrix", "Lslimeknights/tconstruct/shared/inventory/InventoryCraftingPersistent;"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "slimeknights/tconstruct/tools/inventory/ContainerCraftingStation", "craftResult", "Lnet/minecraft/inventory/IInventory;"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "slimeknights/tconstruct/tools/inventory/ContainerCraftingStation", "world", "Lnet/minecraft/world/World;"));
        list.add(new MethodInsnNode(INVOKESTATIC, PInfo.ASMPATH + "asm/helpers/VanillaHelper", "onContainerChanged", "(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/inventory/IInventory;Lnet/minecraft/world/World;)V", false));
        list.add(new InsnNode(RETURN));
    }
}