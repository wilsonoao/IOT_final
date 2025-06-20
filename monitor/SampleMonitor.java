/*******************************************************************************
 * Copyright (c) 2013-2020 LAAS-CNRS (www.laas.fr)
 * 7 Colonel Roche 31077 Toulouse - France
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Initial Contributors:
 *     Thierry Monteil : Project manager, technical co-manager
 *     Mahdi Ben Alaya : Technical co-manager
 *     Samir Medjiah : Technical co-manager
 *     Khalil Drira : Strategy expert
 *     Guillaume Garzone : Developer
 *     François Aïssaoui : Developer
 *
 * New contributors :
 *******************************************************************************/
package org.eclipse.om2m.ipe.sample.monitor;

import org.eclipse.om2m.ipe.sample.controller.SampleController;

/**
 * This class is simply to show the architecture and to reflect the action
 * from the real devices. Here we simulate the reception of the switch signal.
 *
 */
public class SampleMonitor {
	
	/**
	 * Switch on or off a specific lamp
	 * @param lampId
	 */
	public static void switchLamp(String lampId){
		SampleController.toggleLamp(lampId);
	}
	
	/**
	 * Toggle all lamps 
	 */
	public static void switchAll(){
		SampleController.toogleAll();
	}

}
