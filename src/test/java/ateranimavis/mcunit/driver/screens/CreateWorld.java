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
package ateranimavis.mcunit.driver.screens;

import java.util.function.Predicate;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;

import static ateranimavis.mcunit.driver.impl.ScreensImpl.findButton;
import static ateranimavis.mcunit.driver.impl.ScreensImpl.findTextField;
import static ateranimavis.mcunit.driver.impl.ScreensImpl.i18n;
import static ateranimavis.mcunit.driver.impl.ScreensImpl.withI18nMessage;

/**
 * TODO: This needs a rework
 */
public class CreateWorld {

    private static String notFound(String name) {
        return "Could not find " + name + " in Create World!";
    }

    public static TextFieldWidget enterName(Screen screen) {
        return findTextField(screen, withI18nMessage("selectWorld.enterName"), notFound("Enter Name field"));
    }

    public static Button gameMode(Screen screen) {
        String prefix = i18n("selectWorld.gameMode") + ": ";
        Predicate<Button> filter = (button) -> (button.getMessage().startsWith(prefix));

        return findButton(screen, filter, notFound("GameMode button"));
    }

    public static Button cheats(Screen screen) {
        String prefix = i18n("selectWorld.allowCommands") + " ";
        Predicate<Button> filter = (button) -> (button.getMessage().startsWith(prefix));

        return findButton(screen, filter, notFound("Cheats button"));
    }

    public static Button create(Screen screen) {
        return findButton(screen, withI18nMessage("selectWorld.create"), notFound("Create World button"));
    }

}
