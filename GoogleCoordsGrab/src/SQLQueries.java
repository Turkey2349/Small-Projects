
public class SQLQueries {

	/**
	 * TESTING
	 */
	public static final String SCHOOLS = "SELECT SCHOOL_NAME FROM school_data;";
	public static final String SCHOOL_NO_COORDS = "SELECT SCHOOL_NAME FROMschool_data WHERE LAT = '';";
	public static final String UPDATE_SCHOOL_COORDS = "UPDATE school_data SET LAT = ?, LNG = ? WHERE SCHOOL_NAME = ?;";
	public static final String UPDATE_SCHOOL_COLORS = "UPDATE school_data SET COLOR_PRIMARY = ?, COLOR_SECONDARY = ? WHERE SCHOOL_NAME = ?;";
}
