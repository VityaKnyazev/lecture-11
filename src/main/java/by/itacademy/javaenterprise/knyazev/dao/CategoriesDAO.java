package by.itacademy.javaenterprise.knyazev.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.queries.Saver;
import by.itacademy.javaenterprise.knyazev.queries.Selecter;

public class CategoriesDAO {

	private final static String TABLE_NAME = "categories";
	private final static String[] COLUMN_NAMES = { "name", "description" };

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Saver saver;
	private Selecter selecter;

	public CategoriesDAO(Saver saver, Selecter selecter) {
		this.saver = saver;
		this.selecter = selecter;
	}

	public void save() {
		int result = 0;

		result += saver.save(TABLE_NAME, COLUMN_NAMES,
				new String[] { "фрукты", "Фрукт — сочный съедобный плод растения. Пример: яблоко, груша и т.д." },
				Saver.SAVING_ID);
		result += saver.save(TABLE_NAME, COLUMN_NAMES,
				new String[] { "овощи", "Пример: Картофельк морковь, свекла и т.д." }, Saver.SAVING_ID);
		result += saver.save(TABLE_NAME, COLUMN_NAMES,
				new String[] { "зелень", "Пример: петрушка, укроп, зеленый лук и т.д." }, Saver.SAVING_ID);
		result += saver.save(TABLE_NAME, COLUMN_NAMES,
				new String[] { "ягоды", "Пример: брусника, клубника, смародина и т.д." }, Saver.SAVING_ID);

		logger.info("Saving affected " + result + " rows in method save()");
	}

	public void update(int id) {
		int result = 0;

		result += saver.save(TABLE_NAME, COLUMN_NAMES, new String[] { "Хурма", "Хурма для детей" }, id);

		logger.info("Updating affecting " + result + " rows in method update()");
	}

	public Category select(int id) {
		Category category = new Category();
		List<Map<String, Object>> result;

		if (id > 1) {

			result = selecter.selectNative("SELECT * FROM " + TABLE_NAME + " WHERE id = " + id);

			if (!result.isEmpty()) {
				result.get(0).forEach((k, v) -> {
					if (k.equals("id")) {
						category.setId(Integer.valueOf(String.valueOf(v)));
					}
					if (k.equals("name")) {
						category.setName(String.valueOf(v));
					}
					if (k.equals("description")) {
						category.setDescription(String.valueOf(v));
					}
				});
			} else {
				logger.error("Unexistable id = " + id + " was given on method select(int id). Empty Category will be returned.");
			}
		} else {
			logger.error("Bad id < 1 was given on method select(int id). Empty Category will be returned.");
		}

		return category;
	}

	public List<Category> select() {
		List<Category> categories = new ArrayList<>();
		List<Map<String, Object>> result;

		result = selecter.selectNative("SELECT * FROM " + TABLE_NAME);
		
		result.forEach((rs) -> {
			Category category = new Category();
			if (rs.containsKey("id")) {
				category.setId(Integer.valueOf(String.valueOf(rs.get("id"))));
			}
			if (rs.containsKey("name")) {
				category.setName(String.valueOf(rs.get("name")));
			}
			if (rs.containsKey("description")) {
				category.setDescription(String.valueOf(rs.get("description")));
			}

			categories.add(category);
		});

		return categories;
	}

}
