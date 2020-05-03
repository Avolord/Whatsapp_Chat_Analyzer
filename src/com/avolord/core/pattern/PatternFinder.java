package com.avolord.core.pattern;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PatternFinder {
	private ArrayList<PatternPair> datePatterns;
	private ArrayList<PatternPair> timePatterns;
	
	public PatternFinder() {
		this.datePatterns = new ArrayList<>();
		this.timePatterns = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public void initializePatternTable(String filepath) {
		JSONParser jsonParser = new JSONParser();
		
		try (FileReader reader = new FileReader(filepath)) 
		{
			Object obj = jsonParser.parse(reader);
			JSONObject patternTable = (JSONObject) obj;
			
			JSONArray datePatternList = (JSONArray) patternTable.get("datePatterns");
			datePatternList.forEach(dp -> parsePattern((JSONObject) dp, this.datePatterns));
			
			JSONArray timePatternList = (JSONArray) patternTable.get("timePatterns");
			timePatternList.forEach(dp -> parsePattern((JSONObject) dp, this.timePatterns));
			
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
	
	public void parsePattern(JSONObject pattern, List<PatternPair> targetList) {
		
		String regex = (String) pattern.get("regex");
		String format = (String) pattern.get("format");
		
		targetList.add(new PatternPair(regex, format));
	}
	
	public PatternPair determinePattern(String line) {
		PatternPair result = null;

		for (PatternPair datePattern : this.datePatterns) {
			final Matcher matcher = Pattern.compile(datePattern.getRegex() + ", ").matcher(line);
			if (matcher.find()) {
				result = new PatternPair("(" + datePattern.getRegex() + ", ", datePattern.getFormat() + ", ");

				break;
			}
		}
		// if no datePattern matches the line return the null
		if (result == null) {
			return null;
		}

		for (PatternPair timePattern : this.timePatterns) {
			final Matcher matcher = Pattern.compile(" " + timePattern.getRegex() + " - ").matcher(line);
			if (matcher.find()) {
				result.setRegex(result.getRegex() + timePattern.getRegex() + ") - (.*?): (.*)");
				result.setFormat(result.getFormat() + timePattern.getFormat());

				return result;
			}
		}

		return null;
	}
	
}
