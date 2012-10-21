package preprocessor.file;

import java.io.IOException;

import common.Data;

public interface ReaderFeature {

	public Data readTraining(int[] indexes) throws IOException;

	public Data readTest(int[] indexes) throws IOException;

	public Data readValidation(int[] indexes) throws IOException;

}
