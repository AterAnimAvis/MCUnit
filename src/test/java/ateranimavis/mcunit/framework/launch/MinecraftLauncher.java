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
package ateranimavis.mcunit.framework.launch;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.client.Minecraft;
import net.minecraftforge.userdev.LaunchTesting;

import com.google.common.base.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ateranimavis.base.varia.Threads;
import ateranimavis.mcunit.framework.R;

public class MinecraftLauncher {

    public static final Logger        logger      = LogManager.getLogger(R.NAME + " Launcher");
    public static final AtomicBoolean initialized = new AtomicBoolean();

    /**
     * Main Entry Point
     */
    public static ClassLoader startup() throws InterruptedException, ExecutionException {
        if (!initialized.get()) launchFramework();

        /* Update Thread Context */
        ClassLoader loader = Callbacks.LOADER.get();
        logger.warn("Updating Thread Context");
        Thread.currentThread().setContextClassLoader(loader);

        return loader;
    }

    private static void launchFramework() throws InterruptedException, ExecutionException {
        /* Simple Initialization Check */
        if (!initialized.compareAndSet(false, true)) return;

        /* Check for no Environment Variables */
        if (Strings.isNullOrEmpty(System.getenv("nativesDirectory"))) {
            throw new IllegalStateException("Did you forget to copy the environment variables across???");
        }

        /* Launch Minecraft in another thread */
        logger.warn("Starting");
        launchModLauncher();

        /* Await until we get the ModLauncher ClassLoader */
        logger.warn("Awaiting ModLauncher");
        Callbacks.LOADER.get();

        /* Await until Minecraft is loaded */
        logger.warn("Awaiting Minecraft");
        Callbacks.INITIALIZED.get();

        /* Wait so Splash Screen goes */
        Threads.sleep(2000);

        /* Install Shutdown Hooks */
        logger.warn("Install Shutdown Hooks");
        Runtime.getRuntime().addShutdownHook(new Thread(MinecraftLauncher::shutdown));

        logger.warn("Loading Complete");
    }

    private static void launchModLauncher() {
        Runnable launcher = () -> {
            try {
                /* Launch Mod Loader */
                LaunchTesting.main();
            } catch (Exception e) {
                /* On error make sure the loader gets notified */
                logger.error(e);
                Callbacks.LOADER.completeExceptionally(e);
                throw new RuntimeException(e);
            }
        };

        /* Start ModLauncher in a new Thread */
        Thread thread = new Thread(launcher);
        thread.setName("ModLauncher");
        thread.start();
    }

    protected static void provideTransformingClassLoader(ClassLoader classLoader) {
        Callbacks.LOADER.complete(classLoader);
    }

    public static void shutdown() {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.shutdownMinecraftApplet();

            /* TODO: v Check if necessary - was added to ensure that native can release all resources */
            Thread.sleep(5000);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static final class Callbacks {
        public static final CompletableFuture<ClassLoader> LOADER      = new CompletableFuture<>();
        public static final CompletableFuture<Void>        INITIALIZED = new CompletableFuture<>();

    }

}
