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

import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;

public class ModLauncherCallback implements ILaunchPluginService {

    public static final String NAME = "mc_unit_launch_callback";

    private static final String LAUNCHER_PACKAGE = MinecraftLauncher.class.getPackage().getName();

    /* Skip all classes related to Launching */
    private static final List<String> SKIP_PACKAGES = Collections.singletonList(LAUNCHER_PACKAGE + ".");

    @Override
    public void initializeLaunch(ITransformerLoader transformerLoader, Path[] specialPaths) {
        /* Get ModLauncher's ClassLoader */
        ITransformingClassLoader loader = (ITransformingClassLoader) Thread.currentThread().getContextClassLoader();

        /* Prevent the reloading of this Class and our Launcher Class in ModLauncher's ClassLoader */
        loader.addTargetPackageFilter(className -> SKIP_PACKAGES.stream().noneMatch(className::startsWith));

        /* Supply the ClassLoader to our Launcher */
        MinecraftLauncher.provideTransformingClassLoader(loader.getInstance());
    }

    @Override
    public <T> T getExtension() {
        return null;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        /* Return an EnumSet of nothing to prevent ModLauncher from calling our processClass method needlessly */
        return EnumSet.noneOf(Phase.class);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        /* Return false as the passed in node doesn't need any frames recalculating as we don't touch it */
        return false;
    }

}
