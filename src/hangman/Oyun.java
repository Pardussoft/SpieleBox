
package hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Oyun {

	private String[] kelimeler;
	public Kelime aktuell;
	private char[] bulunan;
	public int hak;
	public String tip;
	public LinkedList<String> tipListesi;
	public LinkedList<String> kelimeListesi;

	public Oyun(String dosya) throws IOException {
		int j = tipiOku(dosya);
		tip = tipListesi.get(j);
		//System.out.println(tip);
		this.kelimeleriOku(dosya, j);
		aktuell = new Kelime(kelimeSec());
		this.bulunan = new char[aktuell.len()];
		// System.out.println(aktuell.getKelime());
		for (int i = 0; i < bulunan.length; i++) {
			bulunan[i] = '_';
		}
		hak = aktuell.len() / 2 + 1;
	}

	public void kelimeleriOku(String dosya, int satir) throws IOException {

		FileReader fr = new FileReader(dosya);
		BufferedReader br = new BufferedReader(fr);

		String sat;

		kelimeListesi = new LinkedList<>();

		int i = 0;
		int limit = satir * 2;
		while ((sat = br.readLine()) != null) {
			
			if (i == limit + 1) {
				String[] arr = sat.split(" ");
				for (int j = 0; j < arr.length; j++) {
					kelimeListesi.add(arr[j]);
					// System.out.println(kelimeListesi.get(j) +" ");
				}
			}
			i++;
		}

		// System.out.println(kelimeListesi.size());
		kelimeler = new String[kelimeListesi.size()];
		for (int j = 0; j < kelimeListesi.size(); j++) {
			kelimeler[j] = kelimeListesi.get(j);
			System.out.print(kelimeler[j]+ " ");
		}
	}

	public int tipiOku(String dosya) throws IOException {

		FileReader fr = new FileReader(dosya);
		BufferedReader br = new BufferedReader(fr);

		String sat;

		tipListesi = new LinkedList<>();
		while ((sat = br.readLine()) != null) {
			if (sat.contains("(")) {
				tipListesi.add(sat);
			}
		}
		
		Random rnd = new Random();
		return rnd.nextInt(tipListesi.size());

	}

	public void kelimeYazdir() {
		for (int i = 0; i < kelimeler.length; i++) {
			System.out.println(kelimeler[i]);
		}
	}

	private String kelimeSec() {
		Random rnd = new Random();
		int k = rnd.nextInt(kelimeler.length - 1);
		return kelimeler[k];
	}

	public boolean hamle(char ch) {
		char v[] = aktuell.varmi(ch);
		boolean buldummu = false;
		for (int i = 0; i < v.length; i++) {
			if (v[i] == 1) {
				bulunan[i] = ch;
				buldummu = true;
			}
		}

		if (!buldummu) {
			hak--;
			if (hak == 0)
				return true;
		}

		for (int i = 0; i < bulunan.length; i++) {
			if (bulunan[i] == '_') {
				return false;
			}
		}

		return true;
	}

	public void kelimeYaz() {
		System.out.println("Kalan caniniz:" + hak);
		for (int i = 0; i < bulunan.length; i++) {
			System.out.print(this.bulunan[i] + " ");
		}
		System.out.println();

	}

	public int sonuc() {
		if (hak <= 0)
			return 1;

		for (int i = 0; i < bulunan.length; i++) {
			if (bulunan[i] == '_') {
				return 1;
			}
		}

		return 0;
	}

	public void gercekKelimeYaz() {
		System.out.println(aktuell.getKelime());
	}



}
