package continued.hideaway.mod.mixins;

import continued.hideaway.mod.feat.ext.EntityAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Rotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccessor {

    @Shadow public abstract float rotate(Rotation transformRotation);

    @Shadow private float xRot;
    @Shadow private float yRot;

    @Shadow public abstract float getXRot();

    @Shadow protected abstract void setRot(float yRot, float xRot);

    @Shadow public abstract void setYBodyRot(float yBodyRot);

    @Shadow public abstract void setYHeadRot(float yHeadRot);

    @Shadow public abstract float getYHeadRot();

    @Shadow public abstract void setYRot(float yRot);

    @Shadow public abstract float getYRot();

    @Unique private Entity entity = (Entity) (Object) this;
}
