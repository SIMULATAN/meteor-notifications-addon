package com.github.simulatan.meteornotificationsaddon.mixins;

import com.github.simulatan.meteornotificationsaddon.utils.MeshAccessor;
import meteordevelopment.meteorclient.renderer.Mesh;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mesh.class)
public interface MeshMixin extends MeshAccessor {
	@Accessor(remap = false)
	int getIndicesCount();

	@Accessor(remap = false)
	long getIndicesPointer();

	@Accessor(remap = false)
	void setIndicesCount(int count);
}
