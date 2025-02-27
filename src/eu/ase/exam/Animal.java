package eu.ase.exam;

import java.io.Serializable;

import org.json.JSONObject;

public class Animal implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	private String name;
	private int noLegs;
	private float weight;
	public Animal(String name, int noLegs, float weight) {
		super();
		this.name = name;
		this.noLegs = noLegs;
		this.weight = weight;
	}
	public String getName() {
		return name;
	}
	public int getNoLegs() {
		return noLegs;
	}
	public float getWeight() {
		return weight;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNoLegs(int noLegs) {
		this.noLegs = noLegs;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Animal a))
			return false;

        return a.name.equals(this.name)
				&& (int)a.weight==(int)this.weight
				&& a.noLegs == this.noLegs;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Animal copy = (Animal) super.clone();
		copy.name = this.name;
		copy.noLegs = this.noLegs;
		copy.weight = this.weight;
		return copy;
	}
	@Override
	public String toString() {
		return "Animal [name=" + name + ", noLegs=" + noLegs + ", weight=" + weight + "]";
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		json.put("noLegs", this.noLegs);
		json.put("weight", this.weight);
		return json;
	}
}
