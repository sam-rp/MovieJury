/**
 * 
 */
package com.sidekick.moviejury.model;

import java.io.Serializable;

/**
 * @author Srinath.Karkhani
 *
 */
public class Movie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1173967486310299261L;
	private String name;
	private long id;
	private String synopsis;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	
	

}
