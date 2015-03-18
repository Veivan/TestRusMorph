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
		String tag; // ��� TreeTagger
	}
	
	// taglist ����� ��������� ������ ����� �� TreeTagger � ������� ���� � �����
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
       		result.baseform = parts[0]; // ����������
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
 			case "����" : el.tag = "CC";
 				break;
 			case "�����" : el.tag = "IN";
				break;
 			case "�" : 
 				el.tag = "JJ";
 				if (el.gramma.contains("�����")) el.tag = "JJR";
 				if (el.gramma.contains("����")) el.tag = "JJS";
 				if (el.gramma.contains("��")) el.tag = "POS";
				break;
 			case "�" : 
 				el.tag = "NN";
 				if (el.gramma.contains("��")) el.tag = "NNS";
 				if (el.gramma.contains("��") && el.gramma.contains("���")) el.tag = "NP";
 				if (el.gramma.contains("��") && el.gramma.contains("���")) el.tag = "NPS";
				break;
 			case "��" : el.tag = "PP";
				break;
 			case "��-�" : el.tag = "PPS";
				break;
 			case "�����" : 
 				el.tag = "RBZ";
 				if (el.gramma.contains("�����")) el.tag = "RBR";
				break;
 			case "�" : 
 				el.tag = "RB";
 				if (el.gramma.contains("����")) el.tag = "WRB";
 				if (el.gramma.contains("����")) el.tag = "RBS";
			break;
 			case "����" : el.tag = "RP";
				break;
 			case "����" : el.tag = "UH";
				break;
 			case "���������" : 
 				el.tag = "MVZ";
 				if (el.gramma.contains("���") && el.gramma.contains("���")) el.tag = "VVG";
 				if (el.gramma.contains("���") && el.gramma.contains("���")) el.tag = "VVN";
				break;
 			case "���������" : el.tag = "VV";
				break;
 			case "�" : 
				if (el.gramma.contains("���")) el.tag = "VVD";
				if (el.gramma.contains("���")) el.tag = "MVP";
				if (el.gramma.contains("���")) el.tag = "MVF";
				if (el.gramma.contains("���") && el.gramma.contains("3�")) el.tag = "VBZ";
				if (el.gramma.contains("���")) el.tag = "MVO";
				break;
 			case "��_����" : el.tag = "MPK";
				break;
 			case "��_���������" : el.tag = "MZK";
				break;
 			case "������������" : el.tag = "MDE";
				break;
 			case "����-�" : el.tag = "CD";
				break;
 			case "����" : 
 				el.tag = "CD";
				if (el.gramma.contains("��")) el.tag = "CDQ";
				if (el.gramma.contains("�����")) el.tag = "CDB";
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
