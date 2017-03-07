package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Element {

    // Start: attributes from JSON file report
    private final String name = null;
    private final String type = null;
    private final String description = null;
    private final String keyword = null;
    private final Step[] steps = new Step[0];
    private final Hook[] before = new Hook[0];
    private final Hook[] after = new Hook[0];
    private final Tag[] tags = new Tag[0];
    // End: attributes from JSON file report

    private static final String SCENARIO_TYPE = "scenario";

    private String specialKey = "";
    private boolean isSpecialKeyChecked = false;

    private Status elementStatus;
    private Status beforeStatus;
    private Status afterStatus;
    private Status stepsStatus;

    private Feature feature;

    public Step[] getSteps() {
        return steps;
    }

    public Hook[] getBefore() {
        return before;
    }

    public Hook[] getAfter() {
        return after;
    }

    public Tag[] getTags() {
        return tags;
    }

    public Status getStatus() {
        return elementStatus;
    }

    public Status getBeforeStatus() {
        return beforeStatus;
    }

    public Status getAfterStatus() {
        return afterStatus;
    }

    public Status getStepsStatus() {
        return stepsStatus;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    public boolean isScenario() {
        return SCENARIO_TYPE.equals(type);
    }

    public Feature getFeature() {
        return feature;
    }

    public String getValidateName() {
        return getName().replaceAll("[^A-Za-z0-9]", "_").toLowerCase();
    }

    public String getSpecialKey() {
        if(!isSpecialKeyChecked){
            initSpecialKey();
            isSpecialKeyChecked = true;
        }
        return specialKey;
    }

    public void setMetaData(Feature feature) {
        this.feature = feature;

        beforeStatus = calculateHookStatus(before);
        afterStatus = calculateHookStatus(after);
        stepsStatus = calculateStepsStatus();
        elementStatus = calculateElementStatus();
    }

    private void initSpecialKey() {
        Pattern pattern = Pattern.compile("^i set scenario key \"(.*)\"$");
        for (int i = 0; i < steps.length; i++) {
            Matcher matcher = pattern.matcher(steps[i].getName().toLowerCase());
            if (matcher.matches()) {
                specialKey = "###" + matcher.group(1);
                break;
            }
        }
    }

    private Status calculateHookStatus(Hook[] hooks) {
        StatusCounter statusCounter = new StatusCounter();
        for (Hook hook : hooks) {
            statusCounter.incrementFor(hook.getResult().getStatus());
        }

        return statusCounter.getFinalStatus();
    }

    private Status calculateElementStatus() {
        StatusCounter statusCounter = new StatusCounter();
        statusCounter.incrementFor(stepsStatus);
        statusCounter.incrementFor(beforeStatus);
        statusCounter.incrementFor(afterStatus);
        return statusCounter.getFinalStatus();
    }

    private Status calculateStepsStatus() {
        StatusCounter statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getResult().getStatus());
        }
        return statusCounter.getFinalStatus();
    }
}
