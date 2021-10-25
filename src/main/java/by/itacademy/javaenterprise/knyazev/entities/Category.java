package by.itacademy.javaenterprise.knyazev.entities;


public class Category {
	private int id;
	private String name;
	private String description;
	
	public Category() {}
	
	public Category(int id, String name, String description) {
		this();
		this.id = id;
		this.name = name;
		this.description = description;
	}


	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", description=" + description + "]";
	}	
}
