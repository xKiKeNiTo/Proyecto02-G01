package com.grupo01.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Clase Juego Modelo para los objetos de tipo Juego con los atributos, getters,
 * setters y toString 29/11/2024
 * 
 * @version 1
 * @author raul_ 29/11/2014
 */

@Entity
@Table(name = "juegos")
public class Juego {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull(message = "El ranking no puede estar vacío")
	@Min(value = 1, message = "El ranking debe ser mayor o igual a 1")
	private long rank;

	@NotEmpty(message = "El nombre del juego no puede estar vacío")
	private String name;

	@NotNull(message = "La plataforma no puede estar vacía")
	@Enumerated(EnumType.STRING)
	private Platform platform;

	@Min(value = 1950, message = "El año debe ser mayor o igual a 1950")
	private long year;

	@NotNull(message = "El género no puede estar vacío")
	@Enumerated(EnumType.STRING)
	private Genre genre;

	@Size(max = 100, message = "El nombre del publicador no puede tener más de 100 caracteres")
	private String publisher;

	@Min(value = 0, message = "Las ventas en Norteamérica no pueden ser negativas")
	private double naSales;

	@Min(value = 0, message = "Las ventas en Europa no pueden ser negativas")
	private double euSales;

	@Min(value = 0, message = "Las ventas en Japón no pueden ser negativas")
	private double jpSales;

	@Min(value = 0, message = "Las ventas en otras regiones no pueden ser negativas")
	private double otherSales;

	@Min(value = 0, message = "Las ventas globales no pueden ser negativas")
	private double globalSales;

	// Contructores
	public Juego(long id, long rank, String name, Platform platform, long year, Genre genre, String publisher,
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

	public void setId(long id) {
		this.id = id;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
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

	public void setYear(long year) {
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
		return "Juego [id=" + id + ", rank=" + rank + ", name=" + name + ", year=" + year + ", publisher=" + publisher
				+ ", platform=" + platform + ", genre=" + genre + ", naSales=" + naSales + ", euSales=" + euSales
				+ ", jpSales=" + jpSales + ", otherSales=" + otherSales + ", globalSales=" + globalSales + "]";
	}

}
