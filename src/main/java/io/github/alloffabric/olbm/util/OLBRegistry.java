package io.github.alloffabric.olbm.util;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

/**
 * Exists *purely* so we have access to a `containsId()` method on the server. Thaaaaaanks, ProGuard.
 */
public class OLBRegistry<T> extends SimpleRegistry<T> {
	public OLBRegistry() {
		super(RegistryKey.ofRegistry(new Identifier("olbregistry")), Lifecycle.stable());
	}

	public boolean hasId(Identifier id) {
		return this.entriesById.containsKey(id);
	}
}
