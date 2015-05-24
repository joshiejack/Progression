package joshie.progression.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class ProgressionTransformer implements IFMLLoadingPlugin, IClassTransformer {
    public static boolean isObfuscated = false;
    public static List<AbstractASM> asm = new ArrayList();

    static {
        asm.add(new ASMTileEntity());
        asm.add(new ASMFurnace());
        asm.add(new ASMTransferCrafting());
        asm.add(new ASMContainerPlayer());
        asm.add(new ASMContainerWorkbench());
        asm.add(new ASMAE2());
        asm.add(new ASMAutopackager());
        asm.add(new ASMForestry());
        asm.add(new ASMThaumcraft());
        asm.add(new ASMThermalExpansion());
        asm.add(new ASMTinkers());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        byte[] modified = data;
        for (AbstractASM a : asm) {
            if (a.isClass(name)) {
                if (a.isVisitor()) {
                    ClassReader cr = new ClassReader(modified);
                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    ClassVisitor cv = a.newInstance(cw);
                    cr.accept(cv, ClassReader.EXPAND_FRAMES);
                    modified = cw.toByteArray();
                }

                modified = a.transform(modified);
            }
        }

        return modified;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { ProgressionTransformer.class.getName() };
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
