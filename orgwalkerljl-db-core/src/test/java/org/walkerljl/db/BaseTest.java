package org.walkerljl.db;

import org.junit.Test;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 *
 * BaseTest
 *
 * @author lijunlin
 */
public class BaseTest {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Test
	public void run() {
		try {
			doTest();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void doTest() {}
}
