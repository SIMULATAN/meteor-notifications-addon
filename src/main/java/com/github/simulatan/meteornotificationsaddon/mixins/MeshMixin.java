package com.github.simulatan.meteornotificationsaddon.mixins;

import com.github.simulatan.meteornotificationsaddon.utils.MeshAccessor;
import meteordevelopment.meteorclient.renderer.Mesh;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mesh.class)
public interface MeshMixin extends MeshAccessor {
    @Accessor
    int getIndicesCount();

    @Accessor
    long getIndicesPointer();

    @Accessor
    void setIndicesCount(int count);
}
