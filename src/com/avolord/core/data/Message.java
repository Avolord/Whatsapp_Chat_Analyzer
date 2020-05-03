package com.avolord.core.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Message {
	private String content;
	private Timestamp timestamp;
	
	public Message(String content, String timestamp, String format) {
		this.content = content;
		this.timestamp = generateTimestamp(timestamp, format);
	}
	
	public Message(String content, Timestamp timestamp) {
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public void addLineToContent(String line) {
		this.content += "\n" + line;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
					return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
					return false;
		return true;
	}
	
	private static Timestamp generateTimestamp(String timestamp, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(timestamp));
		
		return Timestamp.valueOf(localDateTime);
	}
	
}
