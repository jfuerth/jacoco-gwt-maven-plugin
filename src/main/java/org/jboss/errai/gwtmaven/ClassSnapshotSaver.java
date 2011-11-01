/*******************************************************************************
 * Copyright (c) 2011 Red Hat and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jonathan Fuerth - additional class snapshot agent and GWT support
 *
 *******************************************************************************/

package org.jboss.errai.gwtmaven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

/**
 * Saves the bytes of classes to the filesystem moments before they are defined
 * in the VM. This allows JaCoCo to understand coverage metrics calculated on
 * classes that were created on-the-fly by utilities such as GWT's JUnit test
 * runner.
 * <p>
 * This snapshot saver is normally installed in the VM by the
 * {@link ClassSnapshotAgent}.
 * 
 * @author Jonathan Fuerth <jfuerth@gmail.com>
 */
public class ClassSnapshotSaver implements ClassFileTransformer {

  /**
   * The base directory under which the package structure for snapshots will be
   * created.
   */
  private final File baseDir;
  
  /**
   * Classes will have their snapshots taken only if they are being defined in
   * classloaders whose fully-qualified class name matches this pattern.
   * <p>
   * Example format for a classloader name that will be matched with this pattern: "org/xyz/TransmogrifyingClassloader"
   */
  private final Pattern classLoaderPattern;

  /**
   * 
   * @param baseDir
   *          The base directory under which the package structure for snapshots
   *          will be created.
   * @param classLoaderPattern
   *          Classes will have their snapshots taken only if they are being
   *          defined in classloaders whose fully-qualified class name matches
   *          this pattern.
   *          <p>
   *          Example format for a classloader name that will be matched with
   *          this pattern: "org/xyz/TransmogrifyingClassloader"
   */
  public ClassSnapshotSaver(File baseDir, Pattern classLoaderPattern) {
    this.baseDir = baseDir;
    this.classLoaderPattern = classLoaderPattern;
    
  }
  
  public byte[] transform(ClassLoader loader, String classname, Class<?> clazz,
      ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
    
    try {
      String loaderClassname = loader == null ? "" : loader.getClass().getName();
      if (classLoaderPattern.matcher(loaderClassname).matches()) {
        System.out.println("Taking snapshot of " + loaderClassname + ":" + classname);
        saveSnapshot(classname, classBytes);
      }
    } catch (Throwable e) {
      System.out.println("Error saving snapshot for class " + classname);
      e.printStackTrace();
    }
    
    // null return value means we don't want to transform this class, which is always the case
    return null;
  }

  private void saveSnapshot(String name, byte[] classBytes) throws IOException {
    String dir = name.substring(0, name.lastIndexOf('/'));
    String file = name.substring(name.lastIndexOf('/') + 1) + ".class";

    File packageDir = new File(baseDir, dir);
    packageDir.mkdirs();
    File classFile = new File(packageDir, file);
    FileOutputStream out = new FileOutputStream(classFile);
    try {
        out.write(classBytes);
    } finally {
        out.close();
    }
}

}
