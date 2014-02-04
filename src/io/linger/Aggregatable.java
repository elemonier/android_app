package io.linger;

public interface Aggregatable
{
	void add(Aggregatable aggregatable);
	String toString();
	void postToDatabase();
}
