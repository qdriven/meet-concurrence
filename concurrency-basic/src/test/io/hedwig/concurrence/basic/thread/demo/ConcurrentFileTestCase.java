package io.hedwig.concurrence.basic.thread.demo;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;


import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConcurrentFileTestCase {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File tempFile;

	@Before
	public void before() throws IOException {
		tempFile = testFolder.newFile("file.txt");

	}

	@After
	public void after() {
		tempFile.delete();
	}

	public int countLines(File file) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		int cnt = 0;
		@SuppressWarnings("unused")
		String lineRead;
		while ((lineRead = reader.readLine()) != null) {
			//System.out.println(lineRead);
		}

		cnt = reader.getLineNumber();
		reader.close();
		return cnt;
	}
	
	public void checkLines(File file) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		String lineRead;
		while ((lineRead = reader.readLine()) != null) {
			assertThat(lineRead, CoreMatchers.containsString("something"));
		}
		reader.close();
	}

	@Test
	public void testNoParallel() throws InterruptedException, IOException {

		MasterThread t = new MasterThread();
		t.launch(tempFile, false, false);

		checkLines(tempFile);
		assertEquals(countLines(tempFile), MasterThread.loopSize
				* MasterThread.threadPoolSize);
	}

	@Test
	public void testParallelNoSynchronized() throws InterruptedException, IOException {
		MasterThread t = new MasterThread();
		t.launch(tempFile, true, false);

		
		//checkLines(tempFile);
		System.out.println(countLines(tempFile));
		assertTrue(countLines(tempFile) <= MasterThread.loopSize
				* MasterThread.threadPoolSize);
	}

	@Test
	public void testParallelSynchronized() throws InterruptedException, IOException {

		MasterThread t = new MasterThread();
		t.launch(tempFile, true, true);

		checkLines(tempFile);
		assertEquals(countLines(tempFile), MasterThread.loopSize
				* MasterThread.threadPoolSize);
	}

}