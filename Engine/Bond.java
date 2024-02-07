package eg.edu.guc.atomix.engine;

public class Bond {

	private String name;
	private String type;
	private int index;

	public Bond(int index, String t) {

		switch (index) {
		case 0:
			name = "N";
			break;
		case 1:
			name = "NE";
			break;
		case 2:
			name = "E";
			break;
		case 3:
			name = "SE";
			break;
		case 4:
			name = "S";
			break;
		case 5:
			name = "SW";
			break;
		case 6:
			name = "W";
			break;
		case 7:
			name = "NW";
		}
		this.index = index;
		type = t;
	}

	public Bond() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
