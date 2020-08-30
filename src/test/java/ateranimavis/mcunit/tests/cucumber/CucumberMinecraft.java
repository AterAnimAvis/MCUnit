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
package ateranimavis.mcunit.tests.cucumber;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import ateranimavis.mcunit.framework.cucumber.rules.CucumberLogger;
import ateranimavis.mcunit.framework.cucumber.runner.MinecraftCucumberRunner;
import io.cucumber.junit.CucumberOptions;

@RunWith(MinecraftCucumberRunner.class)
@CucumberOptions(
    features = "../src/test/resources/mcunit/cucumber",
    glue = "classpath:ateranimavis/mcunit/tests/cucumber/glue/minecraft",
    plugin = {
        /* pretty print in the terminal */
        "pretty",
        /* json so it can be post processed into reports */
        "json:target/cucumber-json-reports/minecraft.json"
    },
    /* We only want to run tests tagged with Minecraft */
    tags = "@minecraft and not @skip"
)
public class CucumberMinecraft {

    public static @ClassRule CucumberLogger Logger = CucumberLogger.INSTANCE;
    
}
