package com.avolord.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.avolord.core.data.User;

public class DataAnalyzer extends DataCleaner {

	public DataAnalyzer() {
		super();
	}

	@Override
	public void start(String path) {
		super.start(path);

		for (User user : this.users) {
			int messageCount = user.getMessages().size();
			System.out.println(user.getName() + " has written " + messageCount + " messages ["
					+ messageCount / (float) totalMessages * 100.0f + "%]");
		}

	}

	@SuppressWarnings("unused")
	private Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean ascending) {
		List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

		// Sorting the list based on values
		list.sort((o1, o2) -> ascending
				? o1.getValue().compareTo(o2.getValue()) == 0 ? o1.getKey().compareTo(o2.getKey())
						: o1.getValue().compareTo(o2.getValue())
				: o2.getValue().compareTo(o1.getValue()) == 0 ? o2.getKey().compareTo(o1.getKey())
						: o2.getValue().compareTo(o1.getValue()));
		return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

	}
}
