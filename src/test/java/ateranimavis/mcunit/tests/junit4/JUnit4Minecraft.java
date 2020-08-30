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
package ateranimavis.mcunit.tests.junit4;

import net.minecraft.world.World;

import org.junit.Test;
import org.junit.runner.RunWith;
import ateranimavis.mcunit.driver.Worlds;
import ateranimavis.mcunit.framework.junit4.runner.MinecraftJUnit4Runner;
import ateranimavis.mcunit.tests.cucumber.glue.minecraft.InventorySteps;

import static org.junit.Assert.assertNotNull;

@RunWith(MinecraftJUnit4Runner.class)
public class JUnit4Minecraft {

    private static final InventorySteps inventoryHelper = new InventorySteps();

    @Test
    public void player_can_join_world() {
        World world = Worlds.openWorld("Test World 1");
        assertNotNull(world);

        inventoryHelper.switchSlot(1);
        inventoryHelper.holding("minecraft:diamond_sword");
        inventoryHelper.switchSlot(2);
        inventoryHelper.holdingNothing();
    }

    @Test
    public void player_can_join_another_world() {
        World world = Worlds.openWorld("Test World 2");
        assertNotNull(world);

        inventoryHelper.switchSlot(1);
        inventoryHelper.holdingNothing();
    }

}
