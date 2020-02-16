package io.github.alloffabric.olbm.util;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

/**
 * Exists *purely* so we have access to a `containsId()` method on the server. Thaaaaaanks, ProGuard.
 */
public class OLBRegistry<T> extends SimpleRegistry<T> {
	public boolean hasId(Identifier id) {
		return this.entries.containsKey(id);
	}
}
