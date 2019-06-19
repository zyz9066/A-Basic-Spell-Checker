package ca.ubishops.yunxiuzhang.BasicSpellChecker;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SpellChecker {
	
	private static final String CORPUS = "corpus-challenge5.txt";
	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		Map<String, Integer> dict = buildDict(CORPUS);
		List<String> ret = spellCheck(dict);
		for(String str:ret) {
			System.out.println(str);
		}		
	}

	private static List<String> spellCheck(Map<String, Integer> dict) {
		List<String> ret = new ArrayList<>();
		
		int num = Integer.parseInt(scan.nextLine());
		for(int i=0;i<num;i++) {
			String word = scan.nextLine();
			StringBuilder sb = null;
			List<String> candidates = new ArrayList<>();
			
			if(dict.containsKey(word)) {
				ret.add(word);
				continue;
			}

			for(int j=0;j<word.length();j++) {
				sb = new StringBuilder(word);
				String w=sb.deleteCharAt(j).toString();
				if(dict.containsKey(w)) {
					candidates.add(w);
				}
			}
			for(int j=0;j<word.length();j++) {
				if(word.charAt(j)<'a'||word.charAt(j)>'z')
					continue;				
				for(char c='a';c<='z';c++) {					
					if(c==word.charAt(j))
						continue;
					char[] ch = word.toCharArray();
					ch[j]=c;
					String t= new String(ch);
					if(dict.containsKey(t)) {
						candidates.add(t);
					}
				}				
			}			
			for(int j=0;j<word.length()-1;j++) {
				char[] ch = word.toCharArray();
				char tmp = ch[j];
				ch[j]=ch[j+1];
				ch[j+1]=tmp;
				String t= new String(ch);
				if(dict.containsKey(t)&&!candidates.contains(t)) {
					candidates.add(t);
				}
			}
			for(int j=0;j<=word.length();j++) {
				for(char c='a';c<='z';c++) {
					sb = new StringBuilder(word);
					sb.insert(j, c);
					String s = sb.toString();
					if(dict.containsKey(s)) {
						candidates.add(s);
					}
				}				
			}
			if(candidates.size()==0) {
				ret.add(word);
			}else if(candidates.size()==1) {
				ret.add(candidates.get(0));
			}else {
				Collections.sort(candidates, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {						
						int res= dict.get(o2)-dict.get(o1);
						if(res==0) {
							return o1.compareTo(o2);
						}else {
							return res;
						}
					}
				});
				ret.add(candidates.get(0));
			}
		}
		return ret;
	}

	private static Map<String, Integer> buildDict(String file) throws Exception {
		Map<String, Integer> dict = new HashMap<>();
		try {
			List<String> lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());

			for (String l : lines) {
				l = l.replaceAll("[^A-Za-z']", " ").trim();
				for (String w : l.split(" ")) {
					if (w.startsWith("'")) {
						w = w.substring(1);
					}
					if (w.endsWith("'")) {
						w = w.substring(0, w.length() - 1);
					}
					w = w.trim();
					if (w.length() == 0)
						continue;

					if (!dict.containsKey(w)) {
						dict.put(w, 1);
					} else {
						dict.put(w, dict.get(w) + 1);
					}
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return dict;
	}

}
