import static org.junit.Assert.assertEquals;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class SolutionTests {
	// for testing System.in and System.out
	// see: http://stefanbirkner.github.io/system-rules/
	@Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
	
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	
	@Test
	public void out() throws IOException {
		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		//File inFile = new File(classLoader.getResource("input.txt").getFile());
		File inFile = new File("input.txt");
		List<String> linesIn = Files.readAllLines(inFile.toPath());
		String[] inArr = linesIn.toArray(new String[0]);
		
		//File outFile = new File(classLoader.getResource("expectedOutput.txt").getFile());
		File outFile = new File("expectedOutput.txt");
		List<String> expectedLinesOut = Files.readAllLines(outFile.toPath());
		String[] expectedOutArr = expectedLinesOut.toArray(new String[0]);

		systemInMock.provideLines(inArr);
		Solution.main(null);
		
		String[] outArr = systemOutRule.getLog().split("\r\n");
		
		assertEquals(expectedOutArr.length, outArr.length);
		
		for (int i = 0; i < outArr.length; i++)
			assertEquals(expectedOutArr[i], outArr[i]);
	}
}
