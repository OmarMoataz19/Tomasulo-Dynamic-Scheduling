import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Parser {

	public String[] parse(String path) throws Exception {

		String line;
		String[] lines = new String[20];
		try {
			File myFile = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(myFile));

			int i = 0;
			while ((line = reader.readLine()) != null) {
				lines[i] = line;
				i++;
			}
			lines[i] = "HALT";
			reader.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return lines;
	}
}
