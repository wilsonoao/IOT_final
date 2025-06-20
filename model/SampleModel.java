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
package org.eclipse.om2m.ipe.sample.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.om2m.commons.exceptions.BadRequestException;
import org.eclipse.om2m.ipe.sample.model.Lamp;

public class SampleModel {
	
	private static Map<String,Lamp> LAMPS = new HashMap<String, Lamp>();
	private static List<LampObserver> OBSERVERS = new ArrayList<LampObserver>();
	
	private SampleModel(){
	}
	
	/**
	 * Sets the lamp state.
	 * @param lampId - Application ID
	 * @param value - measured state
	 */
	public static void setLampState(final String lampId, boolean value) {
		checkLampIdValue(lampId);
		LAMPS.get(lampId).setState(value);
		notifyObservers(lampId, value);
	}
	
	/**
	 * Gets the direct current lamp state
	 * @param lampId
	 * @return the direct current lamp state
	 */
	public static boolean getLampValue(String lampId) {
		checkLampIdValue(lampId);
		return LAMPS.get(lampId).getState();
	}

	/**
	 * Check if the provided id is correct
	 * @param lampId
	 */
	public static void checkLampIdValue(String lampId){
		if(lampId == null || !LAMPS.containsKey(lampId)){
			throw new BadRequestException("Unknow lamp id");
		}
	}
	
	public static void addObserver(LampObserver obs){
		if(!OBSERVERS.contains(obs)){
			OBSERVERS.add(obs);
		}
	}
	
	public static void deleteObserver(LampObserver obs){
		if(OBSERVERS.contains(obs)){
			OBSERVERS.remove(obs);
		}
	}
	
	private static void notifyObservers(final String lampId, final boolean state){
		new Thread(){
			@Override
			public void run() {
				for(LampObserver obs: OBSERVERS){
					obs.onLampStateChange(lampId, state);
				}
			}
		}.start();
	}
	
	public static interface LampObserver{
		void onLampStateChange(String lampId, boolean state);
	}

	public static void setModel(
			Map<String, Lamp> lamps2) {
		LAMPS = lamps2;
	}
	
	public static Lamp getLamp(String lampId){
    		checkLampIdValue(lampId);
    		return LAMPS.get(lampId);
	}
			
}
