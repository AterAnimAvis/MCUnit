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
package ateranimavis.mcunit.framework.runtime;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ateranimavis.base.varia.BaseMod;
import ateranimavis.mcunit.framework.R;
import ateranimavis.mcunit.framework.runtime.callbacks.LaunchCompleteCallback;

@Mod(R.ID)
public class MCUnitMod extends BaseMod {

    public static final Logger logger = LogManager.getLogger(R.NAME);

    public MCUnitMod() {
        logger.warn("Initializing Minecraft Test Mod");

        /* Register our Initial Launch Callback */
        registerEventListener(LaunchCompleteCallback.class);

        /* We don't care if this mod is not on the server or not on the client */
        registerIgnoreDisplayTest(ModLoadingContext.get());
    }

}
