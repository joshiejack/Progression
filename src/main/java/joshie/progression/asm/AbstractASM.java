package joshie.progression.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.tileentity.TileEntityChest;

public abstract class AbstractASM {
    public abstract boolean isClass(String name);

    public ClassVisitor newInstance(ClassWriter writer) {
        return null;
    }

    public Object getTarget() {
        return new TileEntityChest();
    }

    public boolean isVisitor() {
        return true;
    }

    public byte[] transform(byte[] modified) {
        return modified;
    }
}