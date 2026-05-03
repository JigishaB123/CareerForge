package com.careerforge.util;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class SkillUtils {

    private SkillUtils() {}

    public static List<String> normalizeSkills(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return Collections.emptyList();
        }

        return skills.stream()
            .filter(skill -> skill != null && !skill.isBlank())
            .flatMap(skill -> java.util.Arrays.stream(skill.split(",")))
            .map(String::trim)
            .filter(skill -> !skill.isEmpty())
            .distinct()
            .collect(Collectors.toList());
    }

    public static Set<String> normalizeSkillSet(List<String> skills) {
        return normalizeSkills(skills).stream()
            .map(skill -> skill.toLowerCase(Locale.US))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<String> normalizeSkillSet(String skillText) {
        if (skillText == null || skillText.isBlank()) {
            return Collections.emptySet();
        }

        return normalizeSkills(List.of(skillText)).stream()
            .map(skill -> skill.toLowerCase(Locale.US))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
