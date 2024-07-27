package net.aoba.gui.components.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.aoba.gui.GuiManager;
import net.aoba.utils.render.Render2D;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class AobaImageButtonWidget extends PressableWidget {
    private Consumer<AobaImageButtonWidget> pressAction;
    private final Identifier image;
    private int u = 0;
    private int v = 0;
    private boolean background = true;

    public AobaImageButtonWidget(int x, int y, int width, int height, Identifier image) {
        super(x, y, width, height, Text.empty());

        this.image = image;
    }

    public AobaImageButtonWidget(int x, int y, int u, int v, int width, int height, Identifier image) {
        super(x, y, width, height, Text.empty());

        this.image = image;
        this.u = u;
        this.v = v;
    }

    public AobaImageButtonWidget(int x, int y, int u, int v, int width, int height, Identifier image, boolean background) {
        super(x, y, width, height, Text.empty());

        this.image = image;
        this.u = u;
        this.v = v;
        this.background = background;
    }

    public void setPressAction(Consumer<AobaImageButtonWidget> pressAction) {
        this.pressAction = pressAction;
    }

    @Override
    public void onPress() {
        if (pressAction != null) {
            pressAction.accept(this);
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();

        if (background) {
            RenderSystem.disableCull();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Render2D.drawRoundedBox(matrix, getX(), getY(), width, height, GuiManager.roundingRadius.getValue(), GuiManager.backgroundColor.getValue());
            Render2D.drawRoundedOutline(matrix, getX(), getY(), width, height, GuiManager.roundingRadius.getValue(), GuiManager.borderColor.getValue());
            RenderSystem.enableCull();
        }

        context.drawTexture(image, getX(), getY(), u, v, width, height, width, height);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        // For brevity, we'll just skip this for now - if you want to add narration to your widget, you can do so here.
        return;
    }
}