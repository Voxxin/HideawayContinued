package continued.hideaway.mod.feat.wardrobe;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.feat.screen.WardrobeScreen;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Wardrobe {

    private static boolean initiated = false;
    public static AbstractClientPlayer wardrobePlayer = null;

    public static void tick() {
        if (HideawayPlus.location() != Location.WARDROBE_WHEEL) {
            initiated = false;
            StaticValues.wardrobeEntity.clear();
            if (wardrobePlayer != null) wardrobePlayer.remove(Entity.RemovalReason.DISCARDED);
            wardrobePlayer = null;
            StaticValues.playerRotationSet = false;
            return;
        }
        if (HideawayPlus.player() == null) return;

        if (!initiated) {
            initiated = true;
            initializeWardrobe();
        }

        int distance = 5;
        AABB boundingBox = new AABB(HideawayPlus.player().position().x() + distance, HideawayPlus.player().position().y() + distance, HideawayPlus.player().position().z() + distance, HideawayPlus.player().position().x() - distance, HideawayPlus.player().position().y() - distance, HideawayPlus.player().position().z() - distance);
        List<Player> playerList = HideawayPlus.player().level().getNearbyPlayers(TargetingConditions.forNonCombat(), HideawayPlus.player(), boundingBox);
        List<ArmorStand> armourStandList = HideawayPlus.player().level().getEntitiesOfClass(ArmorStand.class, boundingBox);

        StaticValues.wardrobeEntity.clear();
        for (Player player : playerList) {
            if (player == HideawayPlus.player()) continue;
            if (StaticValues.wardrobeEntity.contains(player.getUUID().toString())) continue;
            StaticValues.wardrobeEntity.add(player.getUUID().toString());
        }

        StaticValues.wardrobeArmourStand.clear();
        for (ArmorStand armourStand : armourStandList) {
            if (StaticValues.wardrobeArmourStand.contains(armourStand.getUUID().toString())) continue;
            StaticValues.wardrobeArmourStand.add(armourStand.getUUID().toString());
        }
    }

    private static void initializeWardrobe() {
        assert HideawayPlus.client().player != null;
        HideawayPlus.client().setScreen(new WardrobeScreen());
    }
}
