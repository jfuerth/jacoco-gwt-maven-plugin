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
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.regex.Pattern;

public class ClassSnapshotAgent {

  public static void premain(final String options, final Instrumentation inst) throws Exception {
    Pattern loaderPattern = Pattern.compile(".*gwt.*", Pattern.CASE_INSENSITIVE); // TODO parse from options
    File baseDir = new File("target/snapshot-classes"); // TODO allow override in options
    System.out.println("jacoco-gwt-maven-plugin agent: base dir for snapshots is " + baseDir.getAbsolutePath());
    ClassFileTransformer transformer = new ClassSnapshotSaver(baseDir, loaderPattern);
    inst.addTransformer(transformer);
  }
}
