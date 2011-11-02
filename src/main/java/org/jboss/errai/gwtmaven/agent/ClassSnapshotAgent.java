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

package org.jboss.errai.gwtmaven.agent;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;


public class ClassSnapshotAgent {

  /**
   * Parses the options provided to this agent, including defaults 
   * @param options
   * @return
   */
  private static final Map<String, String> parseOptionsWithDefaults(String options) {
    Map<String, String> map = new HashMap<String, String>();
    
    // load map with defaults
    map.put("debugAgent", Boolean.FALSE.toString());
    map.put("snapshotDirectory", "target/snapshot-classes");
    map.put("snapshotClassLoaders", "");
    
    for (String keyval : options.split(",")) {
      String key;
      String val;
      int equalsIdx = keyval.indexOf("=");
      if (equalsIdx >= 0) {
        key = keyval.substring(0, equalsIdx);
        val = keyval.substring(equalsIdx + 1);
      } else {
        key = keyval;
        val = null;
      }
      map.put(key, val);
    }
    
    return map;
  }
  
  public static void premain(final String optionStr, final Instrumentation inst) throws Exception {
    
    Map<String, String> options = parseOptionsWithDefaults(optionStr);
    boolean debug = Boolean.parseBoolean(options.get("debugAgent"));
    WildcardMatcher loaderMatcher = new WildcardMatcher(options.get("snapshotClassLoaders"));
    File baseDir = new File(options.get("snapshotDirectory"));
    
    if (debug) {
      System.out.println("jacoco-gwt-maven-plugin agent: base dir for snapshots is " + baseDir.getAbsolutePath());
      System.out.println("jacoco-gwt-maven-plugin agent: will snapshot classes loaded into classloaders matching " + loaderMatcher);
    }
    ClassFileTransformer transformer = new ClassSnapshotSaver(debug, baseDir, loaderMatcher);
    inst.addTransformer(transformer);
  }
}
