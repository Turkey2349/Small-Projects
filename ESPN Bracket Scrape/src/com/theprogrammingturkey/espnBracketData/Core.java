package com.theprogrammingturkey.espnBracketData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Core {
	private static final JsonParser PARSER = new JsonParser();

	public static void main(String[] args) {
		int[] gameOrder = { 7, 6, 27, 12, 8, 5, 28, 11, 15, 32, 21, 9, 16, 31, 22, 10 };
		int[] correct = { 14, 11, 53, 23, 15, 9, 55, 22, 30, 63, 41, 17, 31, 61, 43, 20 };
		int[] numBracketsCorrect = new int[gameOrder.length];
		int totalNumBrackets = 0;
		List<String> readIDs = new ArrayList<>();
		// 3479558
		int id = 1;
		while (id < 3598999) {
			int total = 1;
			int start = 0;
			int end = 1000;
			while (start < total) {
				JsonObject groupJson = getJson(
						"http://fantasy.espncdn.com/tournament-challenge-bracket/2019/en/api/group?groupID=" + id
								+ "&sort=-1&start=" + start + "&length=" + end).getAsJsonObject();
				if (groupJson.has("g") && groupJson.has("s") && groupJson.has("e")) {
					JsonObject groupMembers = groupJson.getAsJsonObject("g");
					total = groupMembers.get("s").getAsInt();
					for (JsonElement memberElem : groupMembers.getAsJsonArray("e")) {
						JsonObject member = memberElem.getAsJsonObject();
						String swid = member.get("swid").getAsString();
						if (!readIDs.contains(swid) && member.has("ps")) {
							String ps = member.get("ps").getAsString();
							if (!ps.isEmpty()) {
								totalNumBrackets++;
								readIDs.add(swid);

								String[] picks = ps.split("\\|");
								for (int i = 0; i < gameOrder.length; i++) {
									if (Integer.parseInt(picks[gameOrder[i] - 1]) == correct[i]) {
										numBracketsCorrect[i]++;
									} else {
										break;
									}
								}
							} else {
								total = 1;
								break;
							}
						}
					}
					start = end + 1;
					end += 1000;
					System.out.println(
							"-------------" + id + "(" + (start / 1000) + "/" + (total / 1000) + ")----------");
					for (int i = 0; i < numBracketsCorrect.length; i++) {
						System.out.println(
								numBracketsCorrect[i] + "/" + (i == 0 ? totalNumBrackets : numBracketsCorrect[i - 1]));
					}
				} else {
					total = 0;
				}
			}
			id++;
		}
	}

	public static JsonElement getJson(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			return PARSER.parse(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonObject();
	}
}
