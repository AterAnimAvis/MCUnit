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
package ateranimavis.mcunit.tests.cucumber.glue.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import ateranimavis.mcunit.driver.Configuration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * These aren't actually thread safe like World is
 * TODO: Convert to thread-safe / wait until issue arises
 */
public class InventorySteps {

    @When("the player switches to slot {int}")
    public void switchSlot(int slot) {
        /* Make sure the slot is between 1-9 */
        assertTrue("slot should be between 1-9", slot > 0 && slot < 10);

        /* Get the current player */
        PlayerEntity player = Minecraft.getInstance().player;
        assertNotNull(player);

        /* Set the current selected slot */
        player.inventory.currentItem = slot - 1; /* Minecraft actual stores this as slot 0-8 */

        Configuration.pause(2);
    }

    @Then("the player is holding (a ){string}")
    public void holding(String name) {
        /* Get the current player */
        PlayerEntity player = Minecraft.getInstance().player;
        assertNotNull(player);

        /* Get their held item */
        ItemStack heldItem = player.getHeldItemMainhand();

        /* Convert it to the registry name */
        ResourceLocation registryName = heldItem.getItem().getRegistryName();
        assertNotNull(registryName);

        /* Assert Equals */
        assertEquals(name, registryName.toString());

        Configuration.pause(2);
    }

    @Then("the player is holding nothing")
    public void holdingNothing() {
        holding("minecraft:air");
    }

}
