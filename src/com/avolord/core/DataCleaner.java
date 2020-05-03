package com.avolord.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.avolord.core.data.Message;
import com.avolord.core.data.User;
import com.avolord.core.pattern.PatternFinder;
import com.avolord.core.pattern.PatternPair;

public class DataCleaner {

	private static final Logger logger;

	static {
		logger = Logger.getLogger(DataCleaner.class.getName());
	}

	private PatternPair patternPair;
	protected int totalMessages;
	protected ArrayList<User> users;
	protected HashMap<String, Integer> wordDict;

	public DataCleaner() {
		this.totalMessages = 0;
		this.users = new ArrayList<>();
		this.wordDict = new HashMap<>();
		this.patternPair = null;
	}

	public void start(String path) {
		if (!isValidPath(path)) {
			logger.log(Level.WARNING, "Please choose a valid path!");
			return;
		}

		File file = new File(path);
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {

			if (!determinePattern(br)) {
				logger.log(Level.INFO, "Pattern could not be determined!");
				return;
			}

			processData(br);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean determinePattern(BufferedReader br) throws IOException {
		// go through the lines and determine a fitting pattern

		PatternFinder pf = new PatternFinder();
		pf.initializePatternTable("resources/patterns.json");

		String line = "";
		while (patternPair == null) {
			br.mark(255);
			if ((line = br.readLine()) != null) {
				patternPair = pf.determinePattern(line);
			} else {
				break;
			}
		}
		
		if(this.patternPair == null) {
			return false;
		}

		// jump back to the line where the pattern was determined
		if (Pattern.matches(this.patternPair.getRegex(), line)) {
			br.reset();
		}

		return true;
	}

	private void processData(BufferedReader br) throws IOException {
		final Pattern messagePattern = Pattern.compile(this.patternPair.getRegex());

		String line = null;
		Message msg = null;
		while ((line = br.readLine()) != null) {
			msg = processLine(messagePattern, line, msg);
		}
	}

	private Message processLine(final Pattern pattern, String line, Message lastMessage) {
		final Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			String timeStr = matcher.group(1);
			String username = matcher.group(2);
			String message = matcher.group(3);

			User user = null;
			for (User u : this.users) {
				if (u.getName().equals(username)) {
					user = u;
				}
			}

			if (user == null) {
				user = new User(username);
				this.users.add(user);
			}

			lastMessage = new Message(message, timeStr, this.patternPair.getFormat());
			user.addMessage(lastMessage);

			this.totalMessages++;

			addMessageToDict(message);

		} else {
			if (lastMessage != null) {
				lastMessage.addLineToContent(line);
				addMessageToDict(line);
			}
		}
		return lastMessage;
	}

	private void addMessageToDict(String message) {
		String[] words = message.split("\\s+");
		for (String word : words) {
			word = word.toLowerCase();
			if (word.equals("") || word.equals("<media") || word.equals("omitted>")) {
				continue;
			}
			Integer value;
			if ((value = this.wordDict.get(word)) != null) {
				this.wordDict.put(word, value + 1);
			} else {
				this.wordDict.put(word, 1);
			}

		}
	}

	private static boolean isValidPath(String path) {
		try {
			Paths.get(path);
		} catch (InvalidPathException | NullPointerException ex) {
			return false;
		}
		return true;
	}

	public PatternPair getPatternPair() {
		return patternPair;
	}

	public void setPatternPair(PatternPair patternPair) {
		this.patternPair = patternPair;
	}

	public int getTotalMessages() {
		return totalMessages;
	}

	public void setTotalMessages(int totalMessages) {
		this.totalMessages = totalMessages;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = new ArrayList<>(users);
	}

}
