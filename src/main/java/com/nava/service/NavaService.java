package com.nava.service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Component
public class NavaService {

	private final static String POST_URL = "https://2swdepm0wa.execute-api.us-east-1.amazonaws.com/prod/NavaInterview/measures";
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private RestTemplate restTemplate;
	private CSVParser csvParser;
    private Gson gson = new Gson();

	private List<SchemaRow> loadSchema(String schema) {
		List<SchemaRow> rows = new ArrayList<>();
		try {
			File resource = resourceLoader.getResource("classpath:schemas/" + schema + ".csv").getFile();
			Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
			csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("column", "width", "type"));
			for (CSVRecord record : csvParser) {
				SchemaRow row = new SchemaRow(
						record.get("column"), 
						Integer.parseInt(record.get("width")), 
						DataType.valueOf(record.get("type")));
				rows.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	private List<String> loadData(String data) {
		try {
			File resource = resourceLoader.getResource("classpath:data/" + data + ".txt").getFile();
			String value = new String(Files.readAllBytes(resource.toPath()));
			return Arrays.asList(value.split("\\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	private List<Map<String, Object>> createJsonMap(String value) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<SchemaRow> schema = loadSchema(value);
		List<String> data = loadData(value);
		int beginIndex = 0;
		Map<String, Object> map = new LinkedHashMap<>();
		for (String dataRow : data) {
			for (SchemaRow schemaRow : schema) {
				String section = dataRow.substring(beginIndex, beginIndex + schemaRow.getLength());
				beginIndex += schemaRow.getLength();
				if (schemaRow.getType() == DataType.TEXT) {
					map.put(schemaRow.getColumn(), section.trim());
				} else if (schemaRow.getType() == DataType.BOOLEAN) {
					map.put(schemaRow.getColumn(), Integer.parseInt(section.trim()) == 1);
				} else if (schemaRow.getType() == DataType.INTEGER) {
					map.put(schemaRow.getColumn(), Integer.parseInt(section.trim()));
				}
			}
			beginIndex = 0;
			mapList.add(new LinkedHashMap<>(map));
			map.clear();
		}
		return mapList;
	}
	
	private List<String> createJsonValues(String value) {
		List<String> values = new ArrayList<>();
		List<Map<String, Object>> mapList = createJsonMap(value);
		for (Map<String, Object> map : mapList) {
			values.add(gson.toJson(map));
		}
		return values;
	}
	
	public List<ResponseEntity<String>> start(String value) {
		List<ResponseEntity<String>> responses = new ArrayList<>();
		List<String> jsonValues = createJsonValues(value);
		for (String jsonValue : jsonValues) {
			ResponseEntity<String> response = restTemplate.postForEntity(POST_URL, jsonValue, String.class);
			responses.add(response);
		}
		return responses;
	}

}
