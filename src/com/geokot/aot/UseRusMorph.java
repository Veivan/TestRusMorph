package com.geokot.aot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

public class UseRusMorph {

	public static void main(String[] args) {
		// Test 4
		AnalizeText();
		try {
			String tokens = "я не верю своим глазам локомотив ростов хороший матч";
			StringAnalizier stran = new StringAnalizier();
			stran.Analyze(tokens);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}
	
	public static void AnalizeText() {
		try {
			// RussianAnalyzer russian = new RussianAnalyzer();
			// russian.tokenStream(fieldName, reader);
			// String file = "C:\\temp\\qq";
			// LetterDecoderEncoder decoderEncoder = new LetterDecoderEncoder();

			LuceneMorphology luceneMorph = new RussianLuceneMorphology();
	/*		String word = "сколько";
			List<String> morphin = luceneMorph.getMorphInfo(word);
			System.out.println(morphin.get(0));
			//  for (String el : wordBaseForms) System.out.println(el);
	*/		
			
			String fnamein = "C:\\temp\\Tokens.csv";
			String fnameout = "C:\\temp\\TokensOut.csv";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fnamein), "UTF-8"));

			OutputStreamWriter writer = new OutputStreamWriter(	new FileOutputStream(fnameout));
			//OutputStreamWriter writer = new OutputStreamWriter(	new FileOutputStream(fnameout), "UTF-8");

			try {
				String line;
				while ((line = br.readLine()) != null) {
					String[] words = line.split(";");
					String out = "";
					try {
						List<String> wordBaseForms = luceneMorph
								.getMorphInfo(words[1]);
						// List<String> wordBaseForms =
						// luceneMorph.getNormalForms(words[1]);
						out = wordBaseForms.get(0);
					} catch (Exception e) {
						out = "error";
					}
					writer.write(line + ";"+ DivideOut(out) + "\n");
				}
			} finally {
				br.close();
				writer.close();
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}

    private static String DivideOut(String out) 
    {
    	String result = "";
    	if (out == "error")	result = out;
    	else
    	{
       		//String[] parts = out.replaceAll("\\u007C", ";").split(";");
       		String[] parts = out.split("\\u007C");
    		result += parts[0] + ";"; // словоформа
    		String[] gramma = parts[1].split(" ");
    		for (String el : gramma) result += el + ";";   	
    		result = result.substring(0, result.length()-1); // убрать последний ";"
    	}
		return result;
    }

}
