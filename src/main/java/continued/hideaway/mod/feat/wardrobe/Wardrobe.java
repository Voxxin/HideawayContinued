package continued.hideaway.mod.feat.wardrobe;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.feat.rendering.screen.WardrobeScreen;
import continued.hideaway.mod.util.GuiUtils;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.gui.screens.OptionsScreen;
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

    public static boolean applyingOutfit = false;

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

            if (!headWear.isEmpty() && headWear == chestWear) { headWear.clear(); chestWear.clear(); headWearB = false;  chestWearB = false; setupWardrobe(); return;}
            if (!headWear.isEmpty() && headWear == holdable) { headWear.clear(); holdable.clear(); headWearB = false; holdableB = false; setupWardrobe(); return;}
            if (!chestWear.isEmpty() && chestWear == holdable) { chestWear.clear(); holdable.clear(); chestWearB = false; holdableB = false; setupWardrobe(); return;}

            if (GuiUtils.getChestMenu() != null) {
                ChestMenu menu = GuiUtils.getChestMenu();
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("Close")).findFirst().orElse(null);
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
                return;
            }

            initiated = true;
            initializeWardrobe();
        }

        if (initiated && outfit == null) {headWearB = false; chestWearB = false; holdableB = false;}
        if (applyingOutfit && outfit != null) applyOutfit();

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
                if (itemStack.getItem() == Items.AIR) continue;
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
                if (itemStack.getItem() == Items.AIR) continue;
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
                if (itemStack.getItem() == Items.AIR) continue;
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
        applyingOutfit = true;

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

            for (Slot slot : menu.slots) {
                ItemStack itemStack = slot.getItem();
                if (!itemStack.hasTag()) continue;
                if (outfit.head.getItem() == Items.AIR) {
                    if (!itemStack.getTag().getAsString().contains("Currently equipped")) continue;
                    GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slot);
                    break;
                }

                String itemName = itemStack.getDisplayName().getString();
                String outfitName = outfit.head.getDisplayName().getString();

                if (!itemName.equals(outfitName)) continue;
                if (itemStack.getTag().getAsString().contains("Currently equipped")) continue;
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slot);
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

            for (Slot slot : menu.slots) {
                ItemStack itemStack = slot.getItem();
                if (!itemStack.hasTag()) continue;
                if (outfit.chest.getItem() == Items.AIR) {
                    if (!itemStack.getTag().getAsString().contains("Currently equipped")) continue;
                    GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slot);
                    break;
                }

                String itemName = itemStack.getDisplayName().getString();
                String outfitName = outfit.chest.getDisplayName().getString();

                if (!itemName.equals(outfitName)) continue;
                if (itemStack.getTag().getAsString().contains("Currently equipped")) continue;
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slot);
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

            for (Slot slot : menu.slots) {
                ItemStack itemStack = slot.getItem();
                if (!itemStack.hasTag()) continue;
                if (outfit.holdable.getItem() == Items.AIR) {
                    if (!itemStack.getTag().getAsString().contains("Currently equipped")) continue;
                    GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slot);
                    break;
                }
                String itemName = itemStack.getDisplayName().getString();
                String outfitName = outfit.holdable.getDisplayName().getString();

                if (!itemName.equals(outfitName)) continue;
                if (itemStack.getTag().getAsString().contains("Currently equipped")) continue;
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slot);
            }

            if (GuiUtils.containsNextButton(menu)) {
                GuiUtils.nextAndPress(menu);
            } else {holdableB = true; timerR();}
        }

        if (headWearB && chestWearB && holdableB) {
            applyingOutfit = false;
            outfit = null;
            if (GuiUtils.getChestMenu() != null) {
                Slot slotPaper = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("Close")).findFirst().orElse(null);
                GuiUtils.pressSlot(GuiUtils.getContainerScreen(), slotPaper);
            }

            HideawayPlus.client().options.keyShift.setDown(true);
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
