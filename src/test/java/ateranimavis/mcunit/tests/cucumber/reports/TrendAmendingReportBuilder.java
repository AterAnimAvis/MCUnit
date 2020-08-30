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

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.Trends;

public class TrendAmendingReportBuilder extends ExtensibleReportBuilder {

    public TrendAmendingReportBuilder(List<String> jsonFiles, Configuration configuration) {
        super(jsonFiles, configuration);
    }

    @Override
    protected void appendToTrends(Trends trends, Reportable reportable) {
        String build = configuration.getBuildNumber();
        String[] builds = trends.getBuildNumbers();

        /* If no builds just append */
        if (builds.length <= 0) {
            super.appendToTrends(trends, reportable);
            return;
        }

        /* Find the position of the build */
        int entry = ArrayUtils.lastIndexOf(builds, build);

        /* If build doesn't exist then append */
        if (entry == -1) {
            super.appendToTrends(trends, reportable);
            return;
        }

        /* Otherwise modify in place the build */

        /* Build Numbers */
        trends.getBuildNumbers()[entry] = build; // REDUNDANT

        /* Features */
        trends.getPassedFeatures()[entry] = reportable.getPassedFeatures();
        trends.getFailedFeatures()[entry] = reportable.getFailedFeatures();
        trends.getTotalFeatures()[entry] = reportable.getFeatures();

        /* Scenarios */
        trends.getPassedScenarios()[entry] = reportable.getPassedScenarios();
        trends.getFailedScenarios()[entry] = reportable.getFailedScenarios();
        trends.getTotalScenarios()[entry] = reportable.getScenarios();

        /* Steps */
        trends.getPassedSteps()[entry] = reportable.getPassedSteps();
        trends.getFailedSteps()[entry] = reportable.getFailedSteps();
        trends.getSkippedSteps()[entry] = reportable.getSkippedSteps();
        trends.getPendingSteps()[entry] = reportable.getPendingSteps();
        trends.getUndefinedSteps()[entry] = reportable.getUndefinedSteps();
        trends.getTotalSteps()[entry] = reportable.getSteps();

        /* Duration */
        trends.getDurations()[entry] = reportable.getDuration();
    }

}
