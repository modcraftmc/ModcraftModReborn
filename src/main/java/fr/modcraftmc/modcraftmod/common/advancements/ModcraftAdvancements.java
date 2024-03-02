package fr.modcraftmc.modcraftmod.common.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModcraftAdvancements extends AdvancementProvider {


    public ModcraftAdvancements(DataGenerator generatorIn, ExistingFileHelper fileHelper)
    {
        super(generatorIn, fileHelper);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement.Builder.advancement()
                .display(Items.GOLD_INGOT,
                        Component.literal("GOOOOOLDDDENNFORGE !"),
                        Component.literal("Obtain a golden ingot."),
                        new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false)
                .addCriterion("obtain_golden_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                .save(consumer, new ResourceLocation("modcraftmc", "golden_ingot"), fileHelper);

        Advancement.Builder.advancement()
                .display(Items.COOKED_CHICKEN,
                        Component.literal("Well well well..."),
                        Component.literal("Chicken and melon ? huh ?"),
                        new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false)
                .addCriterion("obtain_chicken_and_melon", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHICKEN, Items.MELON_SLICE))
                .save(consumer, new ResourceLocation("modcraftmc", "wellwellwell"), fileHelper);
    }
}
