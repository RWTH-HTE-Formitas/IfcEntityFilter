import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entity filter for ifc files.
 * Removes all property lines, which are unnecessary for 3D visualisation.
 * The resulting file is named as the original ifc file with ending '_filtered_.ifc'.
 *
 */
public class IfcEntityFilter {
	/**
	 * File path of the ifc file to be filtered.
	 */
	Path originalFilePath;
	/**
	 * File path of filtered ifc file. Ends with '_filtered_.ifc'.
	 */
	Path outputNewFilePath;
	/**
	 * File path of file with extracted additional information. Ends with '_info_.ifc'.
	 */
	Path outputInfoFilePath;
	
	/**
	 * Initialises original file and creates two new files, one for filtered ifc file and one for info file.
	 * @param ifcPath Original ifc file path
	 */
	public IfcEntityFilter(String ifcPath) {
		originalFilePath = Paths.get(ifcPath);
		outputNewFilePath = Paths.get(ifcPath.substring(0, ifcPath.length()-4) + "_filtered_.ifc");
		outputInfoFilePath = Paths.get(ifcPath.substring(0, ifcPath.length()-4) + "_info_.ifc");
	}
	
	/**
	 * Filtering function.
	 * Reads line by line from the original file and writes it to output file depending if the line contains information important for visualisation or additional information.
	 * Additional information lines have entities named 'IFCPROPERTYSINGLEVALUE' and 'IFCPROPERTYSET'.
	 * @return Filtered ifc file path
	 */
	public String filter() {
		
		try(BufferedReader fileReader = new BufferedReader(new FileReader(originalFilePath.toFile()))) {
			try(
				BufferedWriter fileWriter = Files.newBufferedWriter(outputNewFilePath, StandardCharsets.UTF_8);
				BufferedWriter infoWriter = Files.newBufferedWriter(outputInfoFilePath, StandardCharsets.UTF_8)
				) {
					String line;
					while((line = fileReader.readLine()) != null) {
						if(line.contains("IFCPROPERTYSINGLEVALUE") || line.contains("IFCPROPERTYSET")) {
				    		infoWriter.write(line + "\n");
				    	}
				    	else {
				    		fileWriter.write(line + "\n");
				    	}
					}
					
					fileWriter.flush();
					infoWriter.flush();
					
			} catch (IOException e) {
				System.err.println("Error in writing files.");
			}
		} catch (IOException e) {
			System.err.println("Error in reading file.");
		}
		
		return outputNewFilePath.toString();
	}
}
