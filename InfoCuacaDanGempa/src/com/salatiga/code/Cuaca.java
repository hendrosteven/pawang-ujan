package com.salatiga.code;

/**
 * Class ini digunakan untuk representasi object Cuaca
 * @author Hendro Steven Tampake
 * @version 1.0
 */

public class Cuaca {
	private String kota;
	private String cuaca;
	private String suhuMin;
	private String suhuMax;
	private String kelembapan;
	
	
	public Cuaca(String kota, String cuaca, String suhuMin, String suhuMax, String kelembapan) {
		super();
		this.kota = kota;
		this.cuaca = cuaca;
		this.suhuMin = suhuMin;
		this.suhuMax = suhuMax;
		this.kelembapan = kelembapan;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public String getCuaca() {
		return cuaca;
	}

	public void setCuaca(String cuaca) {
		this.cuaca = cuaca;
	}

	public String getSuhuMin() {
		return suhuMin;
	}

	public void setSuhuMin(String suhuMin) {
		this.suhuMin = suhuMin;
	}

	public String getSuhuMax() {
		return suhuMax;
	}

	public void setSuhuMax(String suhuMax) {
		this.suhuMax = suhuMax;
	}

	public String getKelembapan() {
		return kelembapan;
	}

	public void setKelembapan(String kelembapan) {
		this.kelembapan = kelembapan;
	}

	
	
	
}
