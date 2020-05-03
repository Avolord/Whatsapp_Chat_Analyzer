package com.avolord.core.data;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String name;
	private ArrayList<Message> messages;

	public User(String name) {
		this.name = name;
		this.messages = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = new ArrayList<>(messages);
	}

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public Message getMessage(int index) {
		return this.messages.get(index);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messages == null) ? 0 : messages.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (messages == null) {
			if (other.messages != null)
				return false;
		} else if (!messages.equals(other.messages))
					return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
					return false;
		return true;
	}

}
