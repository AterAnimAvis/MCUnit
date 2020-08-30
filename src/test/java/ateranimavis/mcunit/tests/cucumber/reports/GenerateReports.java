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
package ateranimavis.mcunit.tests.cucumber.reports;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.util.MinecraftVersion;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.versions.forge.ForgeVersion;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.reducers.ReducingMethod;

public class GenerateReports implements TestRule {

    private final File         outputDir;
    private final List<String> jsonFiles;

    public GenerateReports(String output, String... jsons) {
        outputDir = new File(output);
        jsonFiles = Arrays.stream(jsons).map(File::new).map(File::getAbsolutePath).collect(Collectors.toList());
    }

    public GenerateReports(File output, File... jsons) {
        outputDir = output;
        jsonFiles = Arrays.stream(jsons).map(File::getAbsolutePath).collect(Collectors.toList());
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                /* Execute Suite */
                base.evaluate();

                /* Generate Report */
                generate();
            }
        };
    }

    private void generate() {
        //final Configuration configuration = ConfigurationFactory.getConfiguration(outputDir);
        Configuration configuration = getConfiguration();
        new TrendAmendingReportBuilder(jsonFiles, configuration).generateReports();
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration(outputDir, "MCUnit");

        configuration.setBuildNumber("1.0.0"); // TODO:
        configuration.setTrendsStatsFile(new File(outputDir, "cucumber-html-reports/trends.json"));

        //TODO:
        configuration.addClassifications("Minecraft", MinecraftVersion.load().getReleaseTarget());
        configuration.addClassifications("Forge", ForgeVersion.getVersion());
        configuration.addClassifications("FML", FMLLoader.getMcpVersion());

        //TODO:
        configuration.setQualifier("integration", "Integration Tests");
        configuration.setQualifier("unit", "Unit Tests");

        configuration.addReducingMethod(ReducingMethod.SKIP_EMPTY_JSON_FILES);
        configuration.addReducingMethod(ReducingMethod.HIDE_EMPTY_HOOKS);

        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));

        return configuration;
    }

}
