package joshie.crafting.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public abstract class AbstractASM {
	public abstract boolean isClass(String name);

	public abstract ClassVisitor newInstance(ClassWriter writer);
}
