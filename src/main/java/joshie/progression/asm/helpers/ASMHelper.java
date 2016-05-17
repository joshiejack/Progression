package joshie.progression.asm.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.progression.asm.ASMConfig;
import joshie.progression.asm.AbstractASM;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static joshie.progression.lib.PInfo.MODID;

public class ASMHelper {
    public enum ObfType {
        NOTCH, FUNC, NAMED;
    }

    public static ASMConfig getASMConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        File file = new File("config/" + MODID + "/asm.json");
        if (!file.exists()) {
            try {
                ASMConfig config = new ASMConfig();
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(config));
                writer.close(); //Write the default json to file
                return config;
            } catch (Exception ignored) {}
        } else {
            try {
                return gson.fromJson(FileUtils.readFileToString(file), ASMConfig.class);
            } catch (Exception ignored) {}
        }

        return new ASMConfig();
    }

    public static Pair<MethodNode, ObfType> getMethodAndObfType(ClassNode node, String... methodAndDescription)  {
        for (int i = 0; i < methodAndDescription.length; i+= 2) {
            String name = methodAndDescription[i];
            String desc = methodAndDescription[i + 1];
            for (MethodNode method : node.methods) {
                if (method.name.equals(name) && method.desc.equals(desc)) {
                    if (i == 0) return Pair.of(method, ObfType.NAMED);
                    else if (i == 2) return Pair.of(method, ObfType.FUNC);
                    else if (i == 4) return Pair.of(method, ObfType.NOTCH);
                }
            }
        }

        return null;
    }

    public static AbstractInsnNode getFirstInstruction(AbstractInsnNode insn)  {
        for (AbstractInsnNode instruction = insn; instruction != null; instruction = instruction.getNext())  {
            if (instruction.getType() != AbstractInsnNode.LABEL && instruction.getType() != AbstractInsnNode.LINE) {
                return instruction;
            }
        }

        return null;
    }

    public static String getClassFor(String name) {
        return FMLDeobfuscatingRemapper.INSTANCE.unmap(name.replace("/", ".")).replace('.', '/');
    }

    public static byte[] override(AbstractASM asm, byte[] data) {
        ClassNode node = new ClassNode();
        ClassReader cr = new ClassReader(data);
        cr.accept(node, 0);

        //Get the Result
        Pair<MethodNode, ObfType> result = getMethodAndObfType(node, asm.getMethodNameAndDescription());
        MethodNode m = result.getKey();
        InsnList list = new InsnList();
        asm.addInstructions(result.getValue(), list);
        m.instructions.insertBefore(getFirstInstruction(m.instructions.getFirst()), list);

        //Write everything
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES) {
            @Override
            protected String getCommonSuperClass(final String original1, final String original2) {
                return super.getCommonSuperClass(ASMHelper.getClassFor(original1), ASMHelper.getClassFor(original2));
            }
        };

        node.accept(cw);
        return cw.toByteArray();
    }

    public static byte[] visit(String name, AbstractASM a, byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = a.newInstance(name, cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}
