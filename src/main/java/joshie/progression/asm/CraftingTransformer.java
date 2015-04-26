package joshie.progression.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class CraftingTransformer implements IFMLLoadingPlugin, IClassTransformer {
    public static boolean isObfuscated = false;
    public static List<AbstractASM> asm = new ArrayList();

    static {
        asm.add(new ASMTransferCrafting());
        asm.add(new ASMCrafting());
        asm.add(new ASMTileEntity());
        asm.add(new ASMFurnace());
        asm.add(new ASMAE2());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        byte[] modified = data;
        for (AbstractASM a : asm) {
            if (a.isClass(name)) {
                ClassReader cr = new ClassReader(modified);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = a.newInstance(cw);
                cr.accept(cv, ClassReader.EXPAND_FRAMES);
                modified = cw.toByteArray();
            }
        }

        return modified;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { CraftingTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscated = ((Boolean) data.get("runtimeDeobfuscationEnabled"));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
