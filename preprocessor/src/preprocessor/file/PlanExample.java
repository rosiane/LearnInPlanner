package preprocessor.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PlanExample {

	public static List<String> getPlan(String path) throws IOException {
		List<String> plan = new ArrayList<>();
		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			fileInputStream = new FileInputStream(path);
			dataInputStream = new DataInputStream(fileInputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			int countLine = 1;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (countLine > 2) {
					plan.add(strLine.split(": ")[1]);
				}
				countLine++;
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}

		return plan;
	}

}
