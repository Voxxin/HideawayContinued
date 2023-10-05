package continued.hideaway.mod.feat.wardrobe;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.feat.rendering.screen.WardrobeScreen;
import continued.hideaway.mod.util.GuiUtils;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class Wardrobe {

    private static boolean initiated = false;
    public static AbstractClientPlayer wardrobePlayer = null;

    public static ArrayList<ItemStack> headWear = new ArrayList<>();
    public static ArrayList<ItemStack> chestWear = new ArrayList<>();
    public static ArrayList<ItemStack> holdable = new ArrayList<>();

    private static boolean headWearB = false;
    private static boolean chestWearB = false;
    private static boolean holdableB = false;

    private static boolean setHeadS = false;
    private static boolean setChestS = false;
    private static boolean setHoldableS = false;

    public static WardrobeOutfit outfit = null;

    static int tickCounter = 0;

    public static void tick() {
        if (HideawayPlus.location() != Location.WARDROBE_WHEEL) {
            initiated = false;
            StaticValues.wardrobeEntity.clear();
            if (wardrobePlayer != null) wardrobePlayer.remove(Entity.RemovalReason.DISCARDED);
            wardrobePlayer = null;
            StaticValues.playerRotationSet = false;
            resetWardrobe();
            outfit = null;
            timerR();
            return;
        }

        if (HideawayPlus.player() == null) return;

        if (!initiated) {
            if (!headWearB || !chestWearB || !holdableB) {
                setupWardrobe();
                return;
            }

            if (GuiUtils.getChestMenu() != null) {
                ChestMenu menu = GuiUtils.getChestMenu();
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("Close")).findFirst().orElse(null);
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
                return;
            }

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

    public static void setupWardrobe() {
        if (HideawayPlus.client().screen == null) {
            HideawayPlus.player().swing(InteractionHand.MAIN_HAND);
        }
        if (GuiUtils.getChestMenu() == null) return;
        ChestMenu menu = GuiUtils.getChestMenu();

        String screenName = HideawayPlus.client().screen.getTitle().getString();

        if (!headWearB) {
            setHeadS = (screenName.contains("\uE249") || screenName.contains("\uE243"));
            if (!timer()) {timer(); return;}

            if (!setHeadS) {
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("Hats & Hair")).findFirst().orElse(null);
                if (slotPaper == null) return;

                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
                return;
            }

            for (ItemStack itemStack : menu.getItems()) {
                if (itemStack.getItem() == Items.PAPER) continue;
                if (headWear.contains(itemStack)) continue;
                headWear.add(itemStack);
            }

            if (GuiUtils.containsNextButton(menu)) {
                GuiUtils.nextAndPress(menu);
            } else {headWearB = true; timerR();}
        } else if (!chestWearB) {
            setChestS = (screenName.contains("\uE244") || screenName.contains("\uE24A"));
            if (!timer()) {timer(); return;}

            if (!setChestS) {
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("Trinkets")).findFirst().orElse(null);
                if (slotPaper == null) return;
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
                return;
            }

            for (ItemStack itemStack : menu.getItems()) {
                if (itemStack.getItem() == Items.PAPER) continue;
                if (chestWear.contains(itemStack)) continue;
                chestWear.add(itemStack);
            }

            if (GuiUtils.containsNextButton(menu)) {
                GuiUtils.nextAndPress(menu);
            } else {chestWearB = true; timerR();}

        } else if (!holdableB) {
            setHoldableS = (screenName.contains("\uE245") || screenName.contains("\uE24B"));
            if (!timer()) {timer(); return;}

            if (!setHoldableS) {
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("Items")).findFirst().orElse(null);
                if (slotPaper == null) return;
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
                return;
            }

            for (ItemStack itemStack : menu.getItems()) {
                if (itemStack.getItem() == Items.PAPER) continue;
                if (holdable.contains(itemStack)) continue;
                holdable.add(itemStack);
            }

            if (GuiUtils.containsNextButton(menu)) {
                GuiUtils.nextAndPress(menu);
            } else {holdableB = true; timerR();}
        }
    }

    private static void resetWardrobe() {
        headWear.clear();
        chestWear.clear();
        holdable.clear();

        headWearB = false;
        chestWearB = false;
        holdableB = false;

        setHeadS = false;
        setChestS = false;
        setHoldableS = false;
    }

    public static void applyOutfit() {
        List<ItemStack> allOutfitItems = new ArrayList<>();
        if (outfit.head != null) allOutfitItems.add(outfit.head);
        if (outfit.chest != null) allOutfitItems.add(outfit.chest);
        if (outfit.holdable != null) allOutfitItems.add(outfit.holdable);

        if (HideawayPlus.client().screen == null || GuiUtils.getChestMenu() == null) return;
        ChestMenu menu = GuiUtils.getChestMenu();

        boolean setSlot = false;
        String[] categories = {"Hats & Hair", "Trinkets", "Items"};

        for (String category : categories) {
            if (!setSlot) {
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains(category)).findFirst().orElse(null);
                if (slotPaper != null) {
                    GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
                    setSlot = true;
                }
            } else {
                for (Slot itemSlot : menu.slots) {
                    ItemStack itemStack = itemSlot.getItem();
                    if (allOutfitItems.contains(itemStack)) {
                        GuiUtils.pressSlot(GuiUtils.getContainerScreen(), itemSlot);
                    }
                }
                if (GuiUtils.containsNextButton(menu)) {
                    GuiUtils.nextAndPress(menu);
                } else {
                    if (category.equals("Hats & Hair")) {
                        headWearB = true;
                    } else if (category.equals("Trinkets")) {
                        chestWearB = true;
                    } else if (category.equals("Items")) {
                        holdableB = true;
                    }
                }
            }
        }
    }

    private static boolean timer() {
        if (tickCounter >= 10) {
            tickCounter = 0;
            return true;
        } else {
            tickCounter++;
            return false;
        }
    }

    private static void timerR() {
        tickCounter = 0;
    }
}
