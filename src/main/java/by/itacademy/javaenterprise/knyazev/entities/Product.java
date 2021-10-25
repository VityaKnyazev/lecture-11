package by.itacademy.javaenterprise.knyazev.entities;

public class Product {
	private String name;
	private String sort;
	private String category;
	private String producer;
	
	public Product() {}
	
	public Product(String name, String sort, String category, String producer) {
		this();
		this.name = name;
		this.sort = sort;
		this.category = category;
		this.producer = producer;
	}

	public String getName() {
		return name;
	}

	public String getSort() {
		return sort;
	}

	public String getCategory() {
		return category;
	}

	public String getProducer() {
		return producer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", sort=" + sort + ", category=" + category + ", producer=" + producer + "]";
	}
	
	
}