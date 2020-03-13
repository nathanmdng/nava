package com.nava.service;

public class SchemaRow {

	private String column;
	private int length;
	private DataType type;
	
	public SchemaRow(String column, int length, DataType type) {
		this.column = column;
		this.length = length;
		this.type = type;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "SchemaRow [column=" + column + ", length=" + length + ", type=" + type + "]";
	}
	
}
