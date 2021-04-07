package uk.co.stikman.spiro.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Samples {
	private static Samples instance;

	public static Samples get() {
		if (instance == null)
			create();
		return instance;
	}

	private Map<String, String> entries = new HashMap<>();

	private static void create() {
		Samples res = new Samples();
		String current = null;
		StringBuilder sb = null;
		for (String s : SpiroResources.INSTANCE.samples().getText().split("\r?\n")) {
			if (s.startsWith("[")) {
				if (current != null)
					res.entries.put(current, sb.toString());
				current = s.substring(1, s.length() - 1);
				sb = new StringBuilder();
			} else {
				sb.append(s).append("\n");
			}
		}
		if (current != null)
			res.entries.put(current, sb.toString());
		instance = res;
	}

	public List<String> keys() {
		return entries.keySet().stream().sorted().collect(Collectors.toList());
	}

	public String get(String key) {
		return entries.get(key);
	}
}
