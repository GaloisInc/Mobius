package mobius.cct.tests.repositories.classpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;

import mobius.cct.repositories.Resource;
import mobius.cct.repositories.classpath.ZipEntry;
import mobius.cct.repositories.NotFoundException;
import mobius.cct.tests.testutil.Util;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests fpr class mobius.cct.repositories.classpath.ZipEntry.
 * @author Tadeusz Sznuk (ts209501@gmail.com)
 */
public class ZipEntryTest {
  /**
   * Entry used in tests.
   */
  private ZipEntry fEntry;
  
  /**
   * Method called before each test.
   */
  @Before
  public void setUp() {
    fEntry = new ZipEntry(new File("./tests/data/test.jar"));
  }
  
  /**
   * Test loading of a class - positive case.
   */
  @Test
  public void testClass1() throws Exception {
    Resource r = fEntry.getClassFile("mobius.cct.testdata.Test1");
    assertNotNull(r);
    InputStream is = r.open();
    assertNotNull(is);
    assertEquals("12872b2fb305213f2c7adac2a945f3da", 
                 Util.toHex(Util.digest(is, Util.MD5)));
    is.close();
  }
  
  /**
   * Test loading of a class - class not found.
   */
  @Test(expected=NotFoundException.class)
  public void testClass2() throws Exception {
    fEntry.getClassFile("mobius.cct.testdata.FalseTest2");
  }
}
