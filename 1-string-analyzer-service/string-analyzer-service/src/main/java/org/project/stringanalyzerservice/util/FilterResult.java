package org.project.stringanalyzerservice.util;

import java.util.Map;
import java.util.function.Predicate;

public record FilterResult(Predicate<String> predicate, Map<String, String> appliedFilters) {}
