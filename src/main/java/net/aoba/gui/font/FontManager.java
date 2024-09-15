/*
 * Aoba Hacked Client
 * Copyright (C) 2019-2024 coltonk9043
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.aoba.gui.font;

import com.mojang.logging.LogUtils;
import net.aoba.Aoba;
import net.aoba.event.events.FontChangedEvent;
import net.aoba.settings.SettingManager;
import net.aoba.settings.types.StringSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.*;
import net.minecraft.client.font.FontFilterType.FilterMap;
import net.minecraft.client.font.TrueTypeFontLoader.Shift;
import net.minecraft.util.Identifier;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FT_Face;
import org.lwjgl.util.freetype.FreeType;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FontManager {
    private MinecraftClient MC;
    private TextRenderer currentFontRenderer;

    public ConcurrentHashMap<String, TextRenderer> fontRenderers;
    public StringSetting fontSetting;

    public FontManager() {
        fontRenderers = new ConcurrentHashMap<>();
        MC = MinecraftClient.getInstance();

        fontSetting = new StringSetting("font", "The font that Aoba will use.", "minecraft");
        fontSetting.addOnUpdate((i) -> {
            FontManager font = Aoba.getInstance().fontManager;
            font.SetRenderer(font.fontRenderers.get(i));
        });

        SettingManager.registerSetting(fontSetting, Aoba.getInstance().settingManager.hiddenContainer);
    }

    public void Initialize() {
        fontRenderers.put("minecraft", MC.textRenderer);

        File fontDirectory = new File(MC.runDirectory + File.separator + "aoba" + File.separator + "fonts");
        if (fontDirectory.exists() && fontDirectory.isDirectory()) {
            File[] files = fontDirectory.listFiles((dir, name) -> name.endsWith(".ttf"));

            if (files != null) {
                for (File file : files) {
                    try {
                        Font font = LoadTTFFont(file, 12.5f, 2, new TrueTypeFontLoader.Shift(-1, 0), "");
                        List<Font.FontFilterPair> list = new ArrayList<>();
                        list.add(new Font.FontFilterPair(font, FilterMap.NO_FILTER));

                        try (FontStorage storage = new FontStorage(MC.getTextureManager(), Identifier.of("aoba:" + file.getName()))) {
                            storage.setFonts(list, Set.of());
                            fontRenderers.put(file.getName().replace(".ttf", ""), new TextRenderer(id -> storage, true));
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load font: " + file.getName());
                        LogUtils.getLogger().error(e.getMessage());
                    }
                }
            }
        }

        currentFontRenderer = fontRenderers.values().iterator().next();
    }

    public TextRenderer GetRenderer() {
        return currentFontRenderer;
    }

    public void SetRenderer(TextRenderer renderer) {
        this.currentFontRenderer = renderer;
        Aoba.getInstance().eventManager.Fire(new FontChangedEvent());
    }

    private static Font LoadTTFFont(File location, float size, float oversample, Shift shift, String skip) throws IOException {
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(Files.readAllBytes(location.toPath()).length);
        byteBuffer.put(Files.readAllBytes(location.toPath())).flip();

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer pointerBuffer = memoryStack.mallocPointer(1);
            FreeTypeUtil.checkFatalError(FreeType.FT_New_Memory_Face(FreeTypeUtil.initialize(), byteBuffer, 0L, pointerBuffer), "Initializing font face");
            FT_Face fT_Face = FT_Face.create(pointerBuffer.get());

            String string = FreeType.FT_Get_Font_Format(fT_Face);
            if (!"TrueType".equals(string)) {
                throw new IOException("Font is not in TTF format, was " + string);
            }
            FreeTypeUtil.checkFatalError(FreeType.FT_Select_Charmap(fT_Face, FreeType.FT_ENCODING_UNICODE), "Find unicode charmap");

            return new TrueTypeFont(byteBuffer, fT_Face, size, oversample, shift.x(), shift.y(), skip);
        } finally {
            MemoryUtil.memFree(byteBuffer);
        }
    }
}
