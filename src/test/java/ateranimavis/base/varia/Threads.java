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
package ateranimavis.base.varia;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.systems.IRenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;

public class Threads {

    public static void renderCall(IRenderCall call) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(call);
        } else {
            call.execute();
        }
    }

    public static <R> Either<R, Exception> supply(Function<Minecraft, R> supplier) {
        final Minecraft minecraft = Minecraft.getInstance();

        return await(minecraft.supplyAsync(() -> supplier.apply(minecraft)));
    }

    public static <R> Either<R, Exception> supply(Supplier<R> supplier) {
        final Minecraft minecraft = Minecraft.getInstance();

        return await(minecraft.supplyAsync(supplier));
    }

    public static Either<?, Exception> run(Consumer<Minecraft> consumer) {
        final Minecraft minecraft = Minecraft.getInstance();

        return await(minecraft.runAsync(() -> consumer.accept(minecraft)));
    }

    public static Either<?, Exception> run(Runnable runnable) {
        final Minecraft minecraft = Minecraft.getInstance();

        return await(minecraft.runAsync(runnable));
    }

    public static <R> Either<R, Exception> await(CompletableFuture<R> future) {
        try {
            return Either.left(future.get());
        } catch (Exception e) {
            return Either.right(e);
        }
    }

    public static <R> Either<R, Exception> await(CompletableFuture<R> future, int timeout, TimeUnit units) {
        try {
            return Either.left(future.get(timeout, units));
        } catch (Exception e) {
            return Either.right(e);
        }
    }

    public static void sleep(int millis) {
        try { Thread.sleep(millis); } catch (Exception e) { throw new RuntimeException(e); }
    }

}
