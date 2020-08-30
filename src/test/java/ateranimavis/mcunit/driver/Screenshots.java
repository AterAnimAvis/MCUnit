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
package ateranimavis.mcunit.driver;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ScreenShotHelper;

import ateranimavis.base.varia.Threads;
import ateranimavis.base.vendor.sneakythrow.Sneaky;

public class Screenshots {

    private static final int TIMEOUT = 30;

    public static byte[] createScreenshot() {
        final CompletableFuture<NativeImage> future = new CompletableFuture<>();
        Threads.renderCall(() -> future.complete(createNativeImage()));
        return Threads.await(future, TIMEOUT, TimeUnit.MINUTES).mapLeft(left -> Sneaky.sneak(left::getBytes)).orThrow();
    }

    private static NativeImage createNativeImage() {
        final Minecraft minecraft = Minecraft.getInstance();
        return ScreenShotHelper.createScreenshot(
            minecraft.getMainWindow().getFramebufferWidth(),
            minecraft.getMainWindow().getFramebufferHeight(),
            minecraft.getFramebuffer()
        );
    }

}
