
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DataBaseManager {

	private JdbcTemplate jdbcTemplate;

	public void init() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		try {
			dataSource.setDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dataSource.setUrl("jdbc:mysql://localhost:3306");
		dataSource.setUsername("root");
		dataSource.setPassword("");

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<String> getColleges() {
		return jdbcTemplate.queryForList(SQLQueries.SCHOOLS, String.class);
	}

	public List<String> getCollegesWithNoLATLNG() {
		return jdbcTemplate.queryForList(SQLQueries.SCHOOL_NO_COORDS, String.class);
	}

	public int attachImageToCollege(String college, double lat, double lng) {
		return jdbcTemplate.update(SQLQueries.UPDATE_SCHOOL_COORDS, lat, lng, college);
	}

	public int attachColorToCollege(String college, String prim, String seco) {
		return jdbcTemplate.update(SQLQueries.UPDATE_SCHOOL_COLORS, prim, seco, college);
	}
}
