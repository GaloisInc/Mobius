package mobius.cct.certificates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mobius.cct.certificates.writer.SecondConstantPool;
import mobius.cct.repositories.InvalidFormatException;
import mobius.cct.repositories.classfile.Attribute;
import mobius.cct.repositories.classfile.ClassFile;
import mobius.cct.repositories.classfile.ClassName;
import mobius.cct.repositories.classfile.ClassVisitor;
import mobius.cct.repositories.classfile.MethodName;
import mobius.cct.repositories.classfile.MethodVisitor;
import mobius.cct.repositories.cp.ConstantPool;
import mobius.cct.repositories.cp.ConstantPoolFactory;
import mobius.cct.repositories.cp.DefaultFactory;
import mobius.cct.repositories.cp.DefaultPool;
import mobius.cct.util.Version;
import mobius.cct.util.VisitorException;

/**
 * Default implementation of CertificateParser.
 * @param <C> Type of class files.
 * @author Tadeusz Sznuk (ts209501@gmail.com)
 */
public final class DefaultCertificateParser<C extends ClassFile>
  implements CertificateParser<C> {

  /**
   * Read class certificates and visit them.
   * @param c Class file.
   * @param v Object used to visit parsed certificates.
   * @return Class with parsed certificates.
   * @throws InvalidFormatException If certificate format
   * is invalid
   */
  public void parse(C c, ClassCertificateVisitor v) 
    throws InvalidFormatException, VisitorException {
    
    try {
      c.visit(new Visitor(v));
    } catch (VisitorException e) {
      if (e.getCause() instanceof InvalidFormatException) {
        throw (InvalidFormatException)e.getCause();
      } else {
        throw e;
      }
    }
  }
  
  /**
   * Read second constant pool from an attribute.
   * @param a Attribute.
   * @result Constant pool.
   * @throws InvalidFormatException .
   */
  public static ConstantPool parseSCP(final Attribute a) 
    throws InvalidFormatException {
    
    try {
      final ConstantPoolFactory factory = new DefaultFactory();
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      a.writeData(bos);
      final ByteArrayInputStream bis = 
        new ByteArrayInputStream(bos.toByteArray());
      return factory.read(bis);
    } catch (Exception e) {
      throw new InvalidFormatException(e.getMessage());
    }
  }
  
}

/**
 * Visitor used to extract class certificates.
 * @author Tadeusz Sznuk (ts209501@gmail.com)
 */
final class Visitor implements ClassVisitor {  
  /**
   * Visitor used to visit parsed certificates.
   */
  private final ClassCertificateVisitor fVisitor;
  
  /**
   * Second constant pool (null until found).
   */
  private ConstantPool fSecondConstantPool;
  
  /**
   * Certificates found before SCP was parsed.
   */
  private final List<Attribute> fBuffer;
  
  /**
   * Constructor.
   * @param v Visitor used to visit parsed certificates.
   */
  public Visitor(final ClassCertificateVisitor v) {
    fVisitor = v;
    fSecondConstantPool = null;
    fBuffer = new LinkedList<Attribute>();
  }
  
  /**
   * Begin visiting class.
   * @param cls Class name.
   * @throws VisitorException .
   */
  @Override
  public void begin(final ClassName cls) throws VisitorException {
    fVisitor.begin(cls);
  }

  /**
   * End visit.
   * @throws VisitorException .
   */
  @Override
  public void end() throws VisitorException {
    if ((fSecondConstantPool == null) && (!fBuffer.isEmpty())) {
      throw new VisitorException(
        new InvalidFormatException(
          "Certificates present but no SCP found"
        )
      );
    }
    fVisitor.end();
  }

  /**
   * Visit attribute.
   * @param attr Attribute.
   * @throws VisitorException .
   */
  @Override
  public void visitAttribute(final Attribute attr) 
    throws VisitorException {
    try {
      if (SecondConstantPool.ATTR.equals(attr.getName())) {
        if (fSecondConstantPool == null) {
          fSecondConstantPool = DefaultCertificateParser.parseSCP(attr);
          Iterator<Attribute> i = fBuffer.iterator();
          while (i.hasNext()) {
            fVisitor.visitClassCert(parseCert(i.next()));
          }
        } else {
          throw new InvalidFormatException("Multiple SCPs");
        }
      } else if (ClassCertificate.ATTR.equals(attr.getName())) {
        if (fSecondConstantPool != null) {
          fVisitor.visitClassCert(parseCert(attr));
        } else {
          fBuffer.add(attr);
        }
      }
    } catch (Exception e) {
      throw new VisitorException(e);
    }
  }

  /**
   * Visit method.
   * @param m Method name.
   * @throws VisitorException .
   */
  @Override
  public MethodVisitor visitMethod(final MethodName m) 
    throws VisitorException {
    if (fSecondConstantPool == null) {
      return null;
    }
    final MethodCertificateVisitor v = fVisitor.visitMethod(m);
    if (v != null) {
      return new MVisitor(v, m, fSecondConstantPool);
    } else {
      return null;
    }
  }

  /**
   * Read class level certificate from an attribute.
   * @param a Attribute.
   * @return Certificate.
   * @throws InvalidFormatException .
   */
  private ClassCertificate parseCert(final Attribute a)
    throws InvalidFormatException {
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      a.writeData(bos);
      final ByteArrayInputStream bis = 
        new ByteArrayInputStream(bos.toByteArray());
      final DataInputStream ds = new DataInputStream(bis);
      
      final int typeIndex = ds.readUnsignedShort();
      final String type = 
        DefaultPool.getString(fSecondConstantPool, typeIndex);
      if (type == null) {
        throw new InvalidFormatException("Invalid certificate type");
      }
      final int major = ds.readUnsignedByte();
      final int minor = ds.readUnsignedByte();
      final Version version = new Version(major, minor);
      final String[] imports = readImports(ds);
      final byte data[] = readData(ds);
      return new ClassCertificate(type, version, imports, data);
    } catch (IOException e) {
      throw new InvalidFormatException(e.getMessage());
    }
  }
  
  /**
   * Read import list.
   * @param ds Input stream.
   * @return Import list.
   * @throws IOException .
   */
  private String[] readImports(final DataInputStream ds) 
    throws IOException {
    
    final int n = ds.readUnsignedShort();
    final String[] result = new String[n];
    for (int i = 0; i < n; i++) {
      final int index = ds.readUnsignedShort();
      result[i] = DefaultPool.getString(fSecondConstantPool, index);
      if (result[i] == null) {
        throw new InvalidFormatException("Invalid import");
      }
    }
    return result;
  }

  /**
   * Read certificate data.
   * @param ds Input stream.
   * @return Data.
   * @throws IOException .
   */
  private byte[] readData(final DataInputStream ds) 
    throws IOException {
    
    final int n = ds.readInt();
    final byte[] result = new byte[n];
    ds.readFully(result);
    return result;
  }
  
}

/**
 * Visitor used to extract certificates from method attributes.
 * @author Tadeusz Sznuk (ts209501@gmail.com)
 */
final class MVisitor implements MethodVisitor {
  /**
   * Name of attribute which holds certificates.
   */
  private static final String CERT_ATTR = "mobius.PCCMethodCert";
  
  /**
   * Visitor used to visit parsed certificates.
   */
  final MethodCertificateVisitor fVisitor;
  
  /**
   * Second constant pool.
   */
  private final ConstantPool fSecondConstantPool;
  
  /**
   * Method name.
   */
  private final MethodName fMethod;
  
  /**
   * Constructor.
   * @param v Visitor used to visit parsed certificates.
   * @param m Method name.
   * @param scp Second constant pool.
   */
  public MVisitor(final MethodCertificateVisitor v, 
                  final MethodName m,
                  final ConstantPool scp) {
    fVisitor = v;
    fSecondConstantPool = scp;
    fMethod = m;
  }
  
  @Override
  public void begin(MethodName m) throws VisitorException {
    fVisitor.begin(m);
  }

  @Override
  public void end() throws VisitorException {
    fVisitor.end();
  }

  @Override
  public void visitAttribute(Attribute attr) throws VisitorException {
    try {
      if (CERT_ATTR.equals(attr.getName())) {
        fVisitor.visitMethodCert(parseMethodCert(attr, fMethod));
      }
    } catch (Exception e) {
      throw new VisitorException(e);
    }
  }
  
  /**
   * Read method level certificate from an attribute.
   * @param a Attribute.
   * @param m Method name.
   * @return Certificate.
   * @throws InvalidFormatException .
   */
  private MethodCertificate parseMethodCert(final Attribute a,
                                            final MethodName m)
    throws InvalidFormatException {
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      a.writeData(bos);
      final ByteArrayInputStream bis = 
        new ByteArrayInputStream(bos.toByteArray());
      final DataInputStream ds = new DataInputStream(bis);
      
      final int typeIndex = ds.readUnsignedShort();
      final String type = 
        DefaultPool.getString(fSecondConstantPool, typeIndex);
      if (type == null) {
        throw new 
          InvalidFormatException("Invalid method certificate type");
      }
      final int major = ds.readUnsignedByte();
      final int minor = ds.readUnsignedByte();
      final Version version = new Version(major, minor);
      final byte data[] = readData(ds);
      return new MethodCertificate(m, type, version, data);
    } catch (IOException e) {
      throw new InvalidFormatException(e.getMessage());
    }
  }
  
  /**
   * Read certificate data.
   * @param ds Input stream.
   * @return Data.
   * @throws IOException .
   */
  private byte[] readData(final DataInputStream ds) 
    throws IOException {
    
    final int n = ds.readInt();
    final byte[] result = new byte[n];
    ds.readFully(result);
    return result;
  }
  
}
