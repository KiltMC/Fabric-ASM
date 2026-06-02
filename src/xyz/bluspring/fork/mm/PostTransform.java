package xyz.bluspring.fork.mm;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class PostTransform implements IExtension {

	private final Map<String, Set<Consumer<ClassNode>>> postClassModifiers;

	PostTransform(Map<String, Set<Consumer<ClassNode>>> postClassModifiers) {
		this.postClassModifiers = postClassModifiers;
	}

	@Override
	public boolean checkActive(MixinEnvironment environment) {
		return true;
	}

	@Override
	public void preApply(ITargetClassContext context) {

	}

	@Override
	public void postApply(ITargetClassContext context) {
		ClassInfo info = context.getClassInfo();

		if (!info.isMixin()) {//Shouldn't be but checking doesn't hurt

			ClassNode node = context.getClassNode();
			Set<Consumer<ClassNode>> transformations = postClassModifiers.get(node.name);
			if (transformations != null) {
				for (Consumer<ClassNode> transformer : transformations) {
					transformer.accept(node);
				}
			}

		}
	}

	@Override
	public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {

	}
}
