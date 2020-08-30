/*
 * MIT License
 *
 * Copyright (c) 2020 ateranimavis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ateranimavis.mcunit.driver.impl;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import ateranimavis.base.varia.Threads;
import ateranimavis.mcunit.driver.screens.CreateWorld;
import ateranimavis.mcunit.framework.runtime.callbacks.WrappedCallbacks;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class WorldImpl {

    @Nullable
    public static WorldSummary findWorldSummary(String name) {
        return Threads.supply(minecraft -> {
            SaveFormat saveformat = minecraft.getSaveLoader();

            try {
                return saveformat
                    .getSaveList()
                    .stream()
                    .filter(it -> Objects.equals(name, it.getDisplayName()))
                    .findFirst()
                    .orElse(null);
            } catch (AnvilConverterException ex) {
                throw new AssertionError(ex);
            }
        }).orThrow();
    }

    public static World loadWorld(WorldSummary summary) {
        return awaitLoaded((minecraft) -> {
            /* Check if the world could be loaded */
            if (!minecraft.getSaveLoader().canLoadWorld(summary.getFileName())) {
                fail("Could not load world " + summary.getDisplayName());
                return;
            }

            /* Load the world */
            minecraft.launchIntegratedServer(summary.getFileName(), summary.getDisplayName(), null);
        });
    }

    /**
     * TODO: This needs a rework with Screens
     */
    public static World createWorld(String name) {
        assertNull(Threads.supply(minecraft -> minecraft.world).orThrow());

        Screen screen = Threads.supply(minecraft -> minecraft.currentScreen).orThrow();

        boolean isCreate = screen instanceof CreateWorldScreen;
        if (!isCreate)
            ScreensImpl.awaitScreen(new CreateWorldScreen(new MainMenuScreen()));

        /* Make sure to re-grab the current screen or you we won't be able to find the necessary widgets */
        screen = Threads.supply(minecraft -> minecraft.currentScreen).orThrow();

        TextFieldWidget nameField = CreateWorld.enterName(screen);
        Button gameMode = CreateWorld.gameMode(screen);
        Button cheats = CreateWorld.cheats(screen);
        Button create = CreateWorld.create(screen);

        return awaitLoaded(minecraft -> {
            nameField.setText(name);
            gameMode.onPress(); // -> Hardcore
            gameMode.onPress(); // -> Creative
            cheats.onPress();   // -> Cheats On
            create.onPress();   // -> Trigger World Creation
        });
    }

    public static void closeWorld() {
        Threads.run((minecraft) -> {
            if (minecraft.world == null) return;

            /* Send Disconnect */
            minecraft.world.sendQuittingDisconnectingPacket();

            /* Handle Unloading */
            if (minecraft.isIntegratedServerRunning()) {
                minecraft.unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
            } else {
                minecraft.unloadWorld();
            }
        }).orThrow();

        /* This is what we should actually use if we are not on an integrated server */
        //boolean isRealms = minecraft.isConnectedToRealms();
        //if (isIntegrated) {
        //    minecraft.displayGuiScreen(new MainMenuScreen());
        //} else if (isRealms) {
        //    new RealmsBridge().switchToRealms(new MainMenuScreen());
        //} else {
        //    minecraft.displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
        //}

        /* Display Main Menu */
        ScreensImpl.awaitScreen(new MainMenuScreen());
    }

    public static World awaitLoaded(Consumer<Minecraft> consumer) {
        CompletableFuture<World> callback = new CompletableFuture<>();

        Consumer<EntityJoinWorldEvent> listener = (event) -> {
            boolean isClientWorld = event.getWorld().isRemote;
            if (!isClientWorld) return;

            boolean isPlayer = event.getEntity() instanceof PlayerEntity;
            if (!isPlayer) return;

            callback.complete(Minecraft.getInstance().world);
        };

        return WrappedCallbacks.await(callback, consumer, listener);
    }

}
