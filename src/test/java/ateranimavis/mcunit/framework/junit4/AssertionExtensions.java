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
package ateranimavis.mcunit.framework.junit4;

import net.minecraft.client.gui.screen.Screen;

import static org.junit.Assert.fail;

public class AssertionExtensions {

    public static void assertInstanceOf(Class<?> clazz, Object instance) {
        assertInstanceOf(null, clazz, instance);
    }

    public static void assertInstanceOf(String message, Class<?> clazz, Object instance) {
        if (!clazz.isAssignableFrom(instance.getClass())) fail(message);
    }

    public static void assertAtScreen(String name, Class<?> clazz, Screen screen) {
        assertInstanceOf("Expected to be at " + name + " but was at " + screen.getTitle().getString(), clazz, screen);
    }

}
