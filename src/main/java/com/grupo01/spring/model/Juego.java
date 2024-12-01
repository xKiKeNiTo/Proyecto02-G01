package com.grupo01.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;


/**
 * Clase Juego Modelo para los objetos de tipo Juego con los atributos, getters,
 * setters y toString 29/11/2024
 * @version 1
 * @author raul_
 * 29/11/2014
 */

@Entity
@Table(name="juegos")
public class Juego {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long rank;	
	
    @NotEmpty(message = "El nombre del juego no puede estar vacío")
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Platform platform;
	
	private long year;
	
	@Enumerated(EnumType.STRING)
	private Genre genre;
	
	private String publisher;
	private double naSales;
	private double euSales;
	private double jpSales;
	private double otherSales;
	private double globalSales;
	
	// Contructores	
	public Juego(int id, int rank, String name, Platform platform, int year, Genre genre, String publisher,
			double naSales, double euSales, double jpSales, double otherSales, double globalSales) {
		super();
		this.id = id;
		this.rank = rank;
		this.name = name;
		this.platform = platform;
		this.year = year;
		this.genre = genre;
		this.publisher = publisher;
		this.naSales = naSales;
		this.euSales = euSales;
		this.jpSales = jpSales;
		this.otherSales = otherSales;
		this.globalSales = globalSales;
	}
	
	public Juego() {
		super();
	}

	// Getters & Setters
	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public long getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public double getNaSales() {
		return naSales;
	}

	public void setNaSales(double naSales) {
		this.naSales = naSales;
	}

	public double getEuSales() {
		return euSales;
	}

	public void setEuSales(double euSales) {
		this.euSales = euSales;
	}

	public double getJpSales() {
		return jpSales;
	}

	public void setJpSales(double jpSales) {
		this.jpSales = jpSales;
	}

	public double getOtherSales() {
		return otherSales;
	}

	public void setOtherSales(double otherSales) {
		this.otherSales = otherSales;
	}

	public double getGlobalSales() {
		return globalSales;
	}

	public void setGlobalSales(double globalSales) {
		this.globalSales = globalSales;
	}
			
	// toString
	@Override
	public String toString() {
		return "Juego [id=" + id + ", rank=" + rank + ", name=" + name + ", year=" + year + ", publisher=" + publisher + ", platform="
				+ platform + ", genre=" + genre + ", naSales=" + naSales + ", euSales=" + euSales + ", jpSales="
				+ jpSales + ", otherSales=" + otherSales + ", globalSales=" + globalSales + "]";
	}
	
}
