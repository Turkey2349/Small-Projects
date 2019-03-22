package com.theprogrammingturkey.espnBracketData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Core
{
	private static final JsonParser PARSER = new JsonParser();

	public static void main(String[] args)
	{
		int[] gameOrder = { 7 };
		int[] correct = { 14 };
		int[] numBracketsCorrect = new int[gameOrder.length];
		int totalNumBrackets = 0;
		List<String> readIDs = new ArrayList<>();
		// 3479558
		int id = 1;
		while (id < 3598999)
		{
			JsonObject groupJson = getJson("http://fantasy.espncdn.com/tournament-challenge-bracket/2019/en/api/group?groupID=" + id + "&sort=-1&start=0&length=1000").getAsJsonObject();
			JsonObject groupMembers = groupJson.getAsJsonObject("g");
			for (JsonElement memberElem : groupMembers.getAsJsonArray("e"))
			{
				JsonObject member = memberElem.getAsJsonObject();
				String swid = member.get("swid").getAsString();
				if (!readIDs.contains(swid) && member.has("ps"))
				{
					String ps = member.get("ps").getAsString();
					if (!ps.isEmpty())
					{
						totalNumBrackets++;
						readIDs.add(swid);

						String[] picks = ps.split("\\|");
						for (int i = 0; i < gameOrder.length; i++)
						{
							if (Integer.parseInt(picks[gameOrder[i] - 1]) == correct[i])
							{
								numBracketsCorrect[i]++;
							}
						}
					}
				}
			}
			System.out.println(numBracketsCorrect[0] + "/" + totalNumBrackets);
			id++;
		}
	}

	public static JsonElement getJson(String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			return PARSER.parse(reader);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return new JsonObject();
	}
}
