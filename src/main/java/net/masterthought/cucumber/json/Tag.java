package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tag {

    private static final String testRailCaseRoot = "https://hotelstonight.testrail.com/index.php?/cases/view/";
    // Start: attributes from JSON file report
    private final String name = null;
    // End: attributes from JSON file report

    public String getName() {
        return name;
    }

    public String getFileName() {
        return generateFileName(name);
    }

    public static String generateFileName(String tagName) {
        // TODO: the file name should be unique
        return String.format("report-tag_%s.html", Util.toValidFileName(tagName.replace("@", "")).trim());
    }

    public boolean isTestRailTag() {
        Pattern pattern = Pattern.compile("^@C\\d+$");
        Matcher matcher = pattern.matcher(getName());
        return matcher.matches();
    }

    public String getTestRailUrl() {
        return testRailCaseRoot + getTestRailCaseId();
    }

    private String getTestRailCaseId() {
        if(getName().length() > 2) {
            return getName().substring(2);
        } else {
            return "";
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object tag) {
        // not fully implemented but I don't expect to have different objects here
        return ((Tag) tag).name.equals(name);
    }
}
