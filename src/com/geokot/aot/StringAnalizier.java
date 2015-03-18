package com.geokot.aot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

public class StringAnalizier {
	private class MyToken {
		String token;
		String baseform;
		String letter;
		String pos;
		String gramma;
		String tag; // Тэг TreeTagger
	}
	
	// taglist будет содержать список Тэгов по TreeTagger и Частоту тэга в твите
	private Map<String, Integer> taglist = new HashMap<String, Integer>();
	
	private List<MyToken> tokenslist = new ArrayList<MyToken>();
	private LuceneMorphology luceneMorph;

	public StringAnalizier() throws IOException {
		luceneMorph = new RussianLuceneMorphology();
	}

	public void Analize(String tokens) {
		tokenslist.clear();
		String[] words = tokens.split(" ");
		String out = "";

		for (String token : words) {
			try {
				List<String> wordBaseForms = luceneMorph.getMorphInfo(token);
				out = wordBaseForms.get(0);
			} catch (Exception e) {
				out = "error";
			}
			tokenslist.add(DivideOut(token, out));
		}
		IdentTags();
		String print = "";
		for (MyToken el : tokenslist) {
			print = el.token + ";" + el.baseform + ";" + el.letter + ";" + el.pos + 
					";" + el.gramma + ";" + el.tag; 
			System.out.println(print);
		}

		CountTags();
		for( Entry<String, Integer> entry : taglist.entrySet() ) {
			System.out.println(entry.getKey() + " " + entry.getValue().toString());
		}	
	}
	
    private MyToken DivideOut(String token, String out) 
    {
    	MyToken result = new MyToken();
    	result.token = token;
    	if (out == "error")	result.baseform = out;
    	else
    	{
       		String[] parts = out.split("\\u007C");
       		result.baseform = parts[0]; // словоформа
    		String[] gramma = parts[1].split(" ");
    		result.letter = gramma[0];
    		result.pos = gramma[1];
    		if (gramma.length > 2)
    			result.gramma = gramma[2];
    	}
		return result;
    }

    private void IdentTags() 
    {
 		for (MyToken el : tokenslist) {
 			switch (el.pos) {
 			case "СОЮЗ" : el.tag = "CC";
 				break;
 			case "ПРЕДЛ" : el.tag = "IN";
				break;
 			case "П" : 
 				el.tag = "JJ";
 				if (el.gramma.contains("сравн")) el.tag = "JJR";
 				if (el.gramma.contains("прев")) el.tag = "JJS";
 				if (el.gramma.contains("вн")) el.tag = "POS";
				break;
 			case "С" : 
 				el.tag = "NN";
 				if (el.gramma.contains("мн")) el.tag = "NNS";
 				if (el.gramma.contains("ед") && el.gramma.contains("имя")) el.tag = "NP";
 				if (el.gramma.contains("мн") && el.gramma.contains("имя")) el.tag = "NPS";
				break;
 			case "МС" : el.tag = "PP";
				break;
 			case "МС-П" : el.tag = "PPS";
				break;
 			case "ПРЕДК" : 
 				el.tag = "RBZ";
 				if (el.gramma.contains("сравн")) el.tag = "RBR";
				break;
 			case "Н" : 
 				el.tag = "RB";
 				if (el.gramma.contains("вопр")) el.tag = "WRB";
 				if (el.gramma.contains("прев")) el.tag = "RBS";
			break;
 			case "ЧАСТ" : el.tag = "RP";
				break;
 			case "МЕЖД" : el.tag = "UH";
				break;
 			case "ПРИЧАСТИЕ" : 
 				el.tag = "MVZ";
 				if (el.gramma.contains("нст") && el.gramma.contains("дст")) el.tag = "VVG";
 				if (el.gramma.contains("прш") && el.gramma.contains("дст")) el.tag = "VVN";
				break;
 			case "ИНФИНИТИВ" : el.tag = "VV";
				break;
 			case "Г" : 
				if (el.gramma.contains("прш")) el.tag = "VVD";
				if (el.gramma.contains("нст")) el.tag = "MVP";
				if (el.gramma.contains("буд")) el.tag = "MVF";
				if (el.gramma.contains("нст") && el.gramma.contains("3л")) el.tag = "VBZ";
				if (el.gramma.contains("пвл")) el.tag = "MVO";
				break;
 			case "КР_ПРИЛ" : el.tag = "MPK";
				break;
 			case "КР_ПРИЧАСТИЕ" : el.tag = "MZK";
				break;
 			case "ДЕЕПРИЧАСТИЕ" : el.tag = "MDE";
				break;
 			case "ЧИСЛ-П" : el.tag = "CD";
				break;
 			case "ЧИСЛ" : 
 				el.tag = "CD";
				if (el.gramma.contains("вн")) el.tag = "CDQ";
				if (el.gramma.contains("сравн")) el.tag = "CDB";
				break;
 			} 			
		}
    }

    private void CountTags() 
    {
    	taglist.clear();
		for (MyToken el : tokenslist) {
			if (taglist.containsKey(el.tag)) { 
				for( Entry<String, Integer> entry : taglist.entrySet() )
					if( el.tag.equals( entry.getKey() ) ) {
						Integer v = entry.getValue() + 1;
						entry.setValue(v);
					}
			}
			else
				taglist.put(el.tag, 1);
		}
    }
	
}
