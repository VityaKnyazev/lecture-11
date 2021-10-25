package by.itacademy.javaenterprise.knyazev;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.db.DbConnection;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Product;
import by.itacademy.javaenterprise.knyazev.queries.Saver;
import by.itacademy.javaenterprise.knyazev.queries.Selecter;

public class App {
	final static Logger logger = LoggerFactory.getLogger(App.class.getName());

	public static void main(String[] args) {
		categoriesService();	
		nativeSaveQueries();
		nativeSelectQueries();
		DbConnection.getDBO().closePool();
	}

	private static void nativeSaveQueries() {
		Saver saver = new Saver();
		saver.saveNative(
				"Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО \"Ягода-малина\"', '212030', 'Республика Беларусь', 'Минская обл.', 'деревня Гудки', 'ул. Красная', '6')");
		saver.saveNative(
				"Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО \"Плодоовощное\"', '220265', 'Республика Беларусь', 'Минская обл.', 'д. Верба', 'ул. Синяя', '18')");
		saver.saveNative(
				"Insert Into producers(name, postal_code, country, region, locality) values('ООО \"Урожай\"', '245879', 'Республика Беларусь', 'Минская обл.', 'д. Весна')");

		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Яблоко', 'Черный принц', 'Яблоко для производства сока', 1, 3)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Груша', 'Дюймовочка', 'Груша для производства сока', 1, 2)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Банан', 'Медовый', 'Банан мелкий сладкий и сочный', 1, 1)");

		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Картофель', 'Адретта', 'Картофель для жарки', 2, 2)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Морковь', 'Московская зимняя А 515', 'Морковь для хранения на зиму', 2, 3)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Лук репчатый', 'Сноуболл', 'Лук для салата', 2, 1)");

		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Укроп', 'Зонтик', 'Укроп для салата', 3, 1)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Петрушка', 'Сахарная', 'Петрушка для сушки и замораживания', 3, 2)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Лук зеленый', 'Порей', 'Лук для супа', 3, 3)");

		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Глория', 'Малина ароматная с кислинкой', 4, 1)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Золотой гигант', 'Малина ароматная сладкая', 4, 2)");
		saver.saveNative(
				"Insert Into goods(name, sort, description, category_id, producer_id) values('Клубника', 'Садовая', 'Клубника сладкая средняя', 4, 3)");
	}

	private static void nativeSelectQueries() {
		List<Map<String, Object>> result;
		List<Product> products = new ArrayList<>();

		result = new Selecter().selectNative(
				"SELECT goods.name, goods.sort, categories.name as category, producers.name as producer FROM goods, categories, producers WHERE goods.producer_id BETWEEN 1 AND 2 AND categories.id = goods.category_id AND goods.producer_id = producers.id ORDER BY goods.name ASC LIMIT 4 OFFSET 2");
		
		if (!result.isEmpty()) {

			result.forEach(rs -> {
				Product product = new Product();
				if (rs.containsKey("name")) {
					product.setName(String.valueOf(rs.get("name")));
				}
				if (rs.containsKey("category")) {
					product.setCategory(String.valueOf(rs.get("category")));
				}
				if (rs.containsKey("sort")) {
					product.setSort(String.valueOf(rs.get("sort")));
				}
				if (rs.containsKey("producer")) {
					product.setProducer(String.valueOf(rs.get("producer")));
				}
				products.add(product);
			});

			if (!products.isEmpty()) {
				logger.info("Products from first native query: ");
				products.forEach(p -> logger.info(p.toString()));
			}
		}

		//2
		result.clear();
				
		Selecter selecter2 = new Selecter();
		result = selecter2.selectNative(
				"SELECT name as product, sort FROM goods WHERE name like '%ый' OR name like '%а' order by name asc LIMIT 3 OFFSET 2");
		logger.info("Result from native query without object mapping: ");
		result.forEach(r -> logger.info(r.toString()));
	}

	private static void categoriesService() {
		Saver saver = new Saver();
		Selecter selecter = new Selecter();

		CategoriesDAO categoriesDao = new CategoriesDAO(saver, selecter);

		categoriesDao.save();

		categoriesDao.update(5);

		Category category = categoriesDao.select(5);
		logger.info("Object " + category.toString() + " have been selected");

		List<Category> categories = categoriesDao.select();
		logger.info("Object selected from categories: ");
		categories.forEach((c) -> logger.info(c.toString()));
	}
}
