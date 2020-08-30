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

import java.util.Objects;
import javax.annotation.Nullable;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSummary;

import ateranimavis.base.varia.Threads;
import ateranimavis.mcunit.driver.impl.WorldImpl;

public class Worlds {

    public static boolean inWorld() {
        return Threads.supply((minecraft -> minecraft.world != null)).orThrow();
    }

    @Nullable
    public static World openWorld(String name) {
        return openWorld(name, true);
    }

    @Nullable
    public static World openWorld(String name, boolean create) {
        World world = Threads.supply(minecraft -> minecraft.world).orThrow();

        /* Check Current World */
        if (world != null) {
            if (Objects.equals(world.getWorldInfo().getWorldName(), name)) return world;

            /* We're in a world so we need to close it. */
            closeWorld();
        }

        /* Check Saved Worlds */
        WorldSummary summary = WorldImpl.findWorldSummary(name);
        if (summary != null) return WorldImpl.loadWorld(summary);

        /* Create World */
        if (create) return createWorld(name);

        /* No World Found! */
        return null;
    }

    public static World createWorld(String name) {
        World world = WorldImpl.createWorld(name);

        Configuration.pause();

        return world;
    }

    public static void closeWorld() {
        WorldImpl.closeWorld();

        Configuration.pause(2);
    }

}
