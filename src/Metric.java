import java.nio.file.Path;
import java.nio.file.Paths;

/** Which speed a heat map shows, and the file its measured areas are stored in. */
public enum Metric {
	DOWNLOAD("Download", "Points.txt"),
	UPLOAD("Upload", "Points2.txt");

	private final String label;
	private final Path pointsFile;

	Metric(String label, String pointsFile) {
		this.label = label;
		this.pointsFile = Paths.get(pointsFile);
	}

	public String getLabel() {return label;}
	public Path getPointsFile() {return pointsFile;}
}
