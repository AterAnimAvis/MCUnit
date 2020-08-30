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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;

import ateranimavis.mcunit.framework.runtime.callbacks.WrappedCallbacks;

/**
 * TODO: This needs a rework
 */
public class ScreensImpl {

    public static <T extends Widget> Optional<T> findWidget(Screen screen, Class<T> clazz, Predicate<T> filter) {
        return screen.children()
                     .stream()
                     .filter(clazz::isInstance)
                     .map(clazz::cast)
                     .filter(filter)
                     .findFirst();
    }

    public static Button findButton(Screen screen, Predicate<Button> filter, String message) {
        return findWidget(screen, Button.class, filter).orElseThrow(() -> new AssertionError(message));
    }

    public static TextFieldWidget findTextField(Screen screen, Predicate<TextFieldWidget> filter, String message) {
        return findWidget(screen, TextFieldWidget.class, filter).orElseThrow(() -> new AssertionError(message));
    }

    public static <T extends Widget> Predicate<T> withMessage(String message) {
        return (widget) -> message.equals(widget.getMessage());
    }

    public static <T extends Widget> Predicate<T> withI18nMessage(String message) {
        return withMessage(I18n.format(message));
    }

    public static String i18n(String message) {
        return I18n.format(message);
    }

    public static Screen awaitScreen(Screen screen) {
        CompletableFuture<Screen> callback = new CompletableFuture<>();

        Consumer<GuiScreenEvent.InitGuiEvent.InitGuiEvent.Post> listener =
            (event) -> callback.complete(Minecraft.getInstance().currentScreen);

        return WrappedCallbacks.await(callback, minecraft -> minecraft.displayGuiScreen(screen), listener);
    }

}
