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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.loading.moddiscovery.ExplodedDirectoryLocator;

import org.apache.commons.lang3.tuple.Pair;
import ateranimavis.mcunit.framework.R;

public class TestModLocator extends ExplodedDirectoryLocator {

    private static final Path       RESOURCES = Paths.get(R.BUILD, "/resources/test");
    private static final Path       MODS_TOML = Paths.get("META-INF", "mods.toml");
    private static final List<Path> CLASSES   = Collections.singletonList(
        Paths.get(R.BUILD, "/classes/java/test")
        /* Add Additions Sources here i.e. '../build/classes/kotlin/test' */
        /* Make sure to change Collections.singletonList to Lists.newArrayList */
    );

    @Override
    public String name() {
        return R.ID;
    }

    @Override
    public void initArguments(Map<String, ?> arguments) {
        /* Pair<ResourcesPath, List<Sources>> */
        List<Pair<Path, List<Path>>> targets = Collections.singletonList(Pair.of(RESOURCES, CLASSES));

        /* Needs to be passed in via a map with the key explodedTargets */
        Map<String, ?> customArguments = Collections.singletonMap(TARGETS, targets);

        if (!Files.exists(RESOURCES.resolve(MODS_TOML))) {
            throw new IllegalStateException("Did you forget to set the working directory?");
        }

        /* Let ExplodedDirectoryLocator handle the rest */
        super.initArguments(customArguments);
    }

    private static final String TARGETS = "explodedTargets";

}
