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
package ateranimavis.mcunit.framework.runtime.callbacks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import ateranimavis.base.varia.Threads;

public class WrappedCallbacks {

    public static <T, E extends Event> T await(CompletableFuture<T> callback, Consumer<Minecraft> trigger,
                                               Consumer<E> listener) {

        MinecraftForge.EVENT_BUS.addListener(listener);
        Threads.run(trigger).orThrow();

        T result = Threads.await(callback).orThrow();
        MinecraftForge.EVENT_BUS.unregister(listener);

        return result;
    }

}
