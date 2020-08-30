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
package ateranimavis.mcunit.framework.junit4.base;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import ateranimavis.mcunit.framework.launch.MinecraftLauncher;

public class AbstractMinecraftRunner<T extends Runner> extends SingleParentRunner<T> {

    public AbstractMinecraftRunner(Class<?> clazz, RunnerInitializer<T> initializer) throws InitializationError {
        /* Initialize Cucumber using the class provided by ModLauncher's ClassLoader */
        super(loadInMinecraft(clazz), initializer);
    }

    public static Class<?> loadInMinecraft(Class<?> clazz) throws InitializationError {
        try {
            /* This is where Minecraft actually gets loaded */
            /* Note: After first call it skips loading Minecraft again */
            ClassLoader loader = MinecraftLauncher.startup();

            /* Here we look up the clazz in the provided ClassLoader, initializing it if necessary */
            return Class.forName(clazz.getName(), true, loader);
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }

}
