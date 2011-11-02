/* ******************************************************************************
 * Copyright (c) 2011 Red Hat and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jonathan Fuerth - additional class snapshot agent and GWT support
 *
 * ******************************************************************************/

/**
 * This package contains a Java Agent which will take snapshots of every class
 * loaded by every classloader matching a user-supplied wildcard.
 * <p>
 * <b>IMPORTANT NOTE</b>: this package is self-contained. There must be no
 * dependencies outside the standard Java SE 6 API.
 */
package org.jboss.errai.gwtmaven.agent;
