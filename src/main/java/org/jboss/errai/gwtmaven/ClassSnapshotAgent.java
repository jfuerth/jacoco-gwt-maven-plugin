package org.jboss.errai.gwtmaven;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.regex.Pattern;

public class ClassSnapshotAgent {

  public static void premain(final String options, final Instrumentation inst) throws Exception {
    Pattern loaderPattern = Pattern.compile(".*gwt.*", Pattern.CASE_INSENSITIVE); // TODO parse from options
    File baseDir = new File("target/class-snapshots");
    ClassFileTransformer transformer = new ClassSnapshotSaver(baseDir, loaderPattern);
    inst.addTransformer(transformer);
  }
}
