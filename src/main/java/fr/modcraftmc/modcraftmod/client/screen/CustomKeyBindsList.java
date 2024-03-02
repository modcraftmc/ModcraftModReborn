//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package fr.modcraftmc.modcraftmod.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CustomKeyBindsList extends ContainerObjectSelectionList<CustomKeyBindsList.Entry> {
    final KeyBindsScreen keyBindsScreen;
    int maxNameWidth;

    public CustomKeyBindsList(KeyBindsScreen p_193861_, Minecraft p_193862_) {
        super(p_193862_, p_193861_.width + 45, p_193861_.height, 20, p_193861_.height - 32, 20);
        this.keyBindsScreen = p_193861_;
        KeyMapping[] akeymapping = (KeyMapping[])ArrayUtils.clone(p_193862_.options.keyMappings);
        Arrays.sort((Object[])akeymapping);
        String s = null;
        KeyMapping[] var5 = akeymapping;
        int var6 = akeymapping.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            KeyMapping keymapping = var5[var7];
            String s1 = keymapping.getCategory();
            if (!s1.equals(s)) {
                s = s1;
                this.addEntry(new CategoryEntry(Component.translatable(s1)));
            }

            Component component = Component.translatable(keymapping.getName());
            int i = p_193862_.font.width(component);
            if (i > this.maxNameWidth) {
                this.maxNameWidth = i;
            }

            this.addEntry(new KeyEntry(keymapping, component));
        }

    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 15 + 20;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }

    @OnlyIn(Dist.CLIENT)
    public class CategoryEntry extends Entry {
        final Component name;
        private final int width;

        public CategoryEntry(Component p_193886_) {
            this.name = p_193886_;
            this.width = CustomKeyBindsList.this.minecraft.font.width(this.name);
        }

        public void render(PoseStack p_193888_, int p_193889_, int p_193890_, int p_193891_, int p_193892_, int p_193893_, int p_193894_, int p_193895_, boolean p_193896_, float p_193897_) {
            CustomKeyBindsList.this.minecraft.font.draw(p_193888_, this.name, (float)(CustomKeyBindsList.this.minecraft.screen.width / 2 - this.width / 2), (float)(p_193890_ + p_193893_ - 9 - 1), 16777215);
        }

        public boolean changeFocus(boolean p_193900_) {
            return false;
        }

        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }

                public void updateNarration(NarrationElementOutput p_193906_) {
                    p_193906_.add(NarratedElementType.TITLE, CategoryEntry.this.name);
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class KeyEntry extends Entry {
        private final KeyMapping key;
        private final Component name;
        private final Button changeButton;
        private final Button resetButton;

        KeyEntry(final KeyMapping p_193916_, final Component p_193917_) {
            this.key = p_193916_;
            this.name = p_193917_;
            this.changeButton = new Button(0, 0, 95, 20, p_193917_, (p_193939_) -> {
                CustomKeyBindsList.this.keyBindsScreen.selectedKey = p_193916_;
            }) {
                protected MutableComponent createNarrationMessage() {
                    return p_193916_.isUnbound() ? Component.translatable("narrator.controls.unbound", new Object[]{p_193917_}) : Component.translatable("narrator.controls.bound", new Object[]{p_193917_, super.createNarrationMessage()});
                }
            };
            this.resetButton = new Button(0, 0, 50, 20, Component.translatable("controls.reset"), (p_193935_) -> {
                this.key.setToDefault();
                CustomKeyBindsList.this.minecraft.options.setKey(p_193916_, p_193916_.getDefaultKey());
                KeyMapping.resetMapping();
            }) {
                protected MutableComponent createNarrationMessage() {
                    return Component.translatable("narrator.controls.reset", new Object[]{p_193917_});
                }
            };
        }

        public void render(PoseStack p_193923_, int p_193924_, int p_193925_, int p_193926_, int p_193927_, int p_193928_, int p_193929_, int p_193930_, boolean p_193931_, float p_193932_) {
            boolean flag = CustomKeyBindsList.this.keyBindsScreen.selectedKey == this.key;
            float f = (float)(p_193926_ + 90 - CustomKeyBindsList.this.maxNameWidth);
            CustomKeyBindsList.this.minecraft.font.draw(p_193923_, this.name, f, (float)(p_193925_ + p_193928_ / 2 - 4), 16777215);
            this.resetButton.x = p_193926_ + 190 + 20;
            this.resetButton.y = p_193925_;
            this.resetButton.active = !this.key.isDefault();
            this.resetButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
            this.changeButton.x = p_193926_ + 105;
            this.changeButton.y = p_193925_;
            this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
            boolean flag1 = false;
            boolean keyCodeModifierConflict = true;
            if (!this.key.isUnbound()) {
                KeyMapping[] var15 = CustomKeyBindsList.this.minecraft.options.keyMappings;
                int var16 = var15.length;

                for(int var17 = 0; var17 < var16; ++var17) {
                    KeyMapping keymapping = var15[var17];
                    if (keymapping != this.key && this.key.same(keymapping)) {
                        flag1 = true;
                        keyCodeModifierConflict &= keymapping.hasKeyModifierConflict(this.key);
                    }
                }
            }

            if (flag) {
                this.changeButton.setMessage(Component.literal("> ").append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW)).append(" <").withStyle(ChatFormatting.YELLOW));
            } else if (flag1) {
                this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(keyCodeModifierConflict ? ChatFormatting.GOLD : ChatFormatting.RED));
            }

            this.changeButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        public boolean mouseClicked(double p_193919_, double p_193920_, int p_193921_) {
            return this.changeButton.mouseClicked(p_193919_, p_193920_, p_193921_) ? true : this.resetButton.mouseClicked(p_193919_, p_193920_, p_193921_);
        }

        public boolean mouseReleased(double p_193941_, double p_193942_, int p_193943_) {
            return this.changeButton.mouseReleased(p_193941_, p_193942_, p_193943_) || this.resetButton.mouseReleased(p_193941_, p_193942_, p_193943_);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        public Entry() {
        }
    }
}
