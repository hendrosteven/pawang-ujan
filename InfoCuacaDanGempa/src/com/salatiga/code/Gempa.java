package com.salatiga.code;

/**
 * Class ini digunakan untuk representasi object Gempa
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class Gempa {
	private String tanggal;
	private String latitude;
	private String longitude;
	private String magnitude;
	private String kedalaman;
	private String keterangan;
	private String dirasakan;

	public String getTanggal() {
		return tanggal;
	}

	public void setTanggalJam(String tanggal) {
		this.tanggal = tanggal;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(String magnitude) {
		this.magnitude = magnitude;
	}

	public String getKedalaman() {
		return kedalaman;
	}

	public void setKedalaman(String kedalaman) {
		this.kedalaman = kedalaman;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getDirasakan() {
		return dirasakan;
	}

	public void setDirasakan(String dirasakan) {
		this.dirasakan = dirasakan;
	}

	public Gempa(String tanggal, String latitude, String longitude,
			String magnitude, String kedalaman, String keterangan,
			String dirasakan) {
		super();
		this.tanggal = tanggal;
		this.latitude = latitude;
		this.longitude = longitude;
		this.magnitude = magnitude;
		this.kedalaman = kedalaman;
		this.keterangan = keterangan;
		this.dirasakan = dirasakan;
	}

}
