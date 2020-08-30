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

import java.util.function.BiPredicate;
import java.util.function.Supplier;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

import org.apache.commons.lang3.tuple.Pair;

public class BaseMod {

    protected void registerEventListener(Object target) {
        MinecraftForge.EVENT_BUS.register(target);
    }

    protected void registerModEventListener(Object target) {
        FMLJavaModLoadingContext.get().getModEventBus().register(target);
    }

    /**
     * Registers a DISPLAYTEST extension point so we don't cause Forge to show a incompatibility warning with servers
     * who don't have the mod installed. <br/> Thanks <a href="https://github.com/phit">@phit</a>
     *
     * @see <a href="https://github.com/MinecraftForge/MinecraftForge/pull/7209">MinecraftForge#7209</a>
     */
    protected void registerIgnoreDisplayTest(ModLoadingContext context) {
        /* The existence of this mod doesn't actually matter so return IGNORESERVERONLY */
        Supplier<String> getVersion = () -> FMLNetworkConstants.IGNORESERVERONLY;
        BiPredicate<String, Boolean> testVersion = (remote, isServer) -> true;

        context.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(getVersion, testVersion));
    }

}
