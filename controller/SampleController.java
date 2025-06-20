package org.eclipse.om2m.ipe.sample.controller;

import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.ipe.sample.RequestSender;
import org.eclipse.om2m.ipe.sample.constants.SampleConstants;
import org.eclipse.om2m.ipe.sample.model.SampleModel;
import org.eclipse.om2m.ipe.sample.util.ObixUtil;
import org.eclipse.om2m.ipe.sample.model.Lamp;

public class SampleController {
	
	public static CseService CSE;
	protected static String AE_ID;
	
	public static void setLampState(String lampId, boolean value){
	    // 先設定燈狀態
	    SampleModel.setLampState(lampId, value);
	    boolean allOn = SampleModel.getLampValue(SampleConstants.LAMP_0)
	    && SampleModel.getLampValue(SampleConstants.LAMP_1)
	    && SampleModel.getLampValue(SampleConstants.LAMP_2);

	if (allOn) {
	    // 找出亮最久的燈
	    String longestOnLampId = null;
	    long maxDuration = -1;

	    for (String id : new String[] {
		    SampleConstants.LAMP_0,
		    SampleConstants.LAMP_1,
		    SampleConstants.LAMP_2 }) {
		Lamp lamp = SampleModel.getLamp(id);
		if (lamp.getState()) {
		    long duration = lamp.getOnDurationMillis();
		    if (duration > maxDuration) {
			maxDuration = duration;
			longestOnLampId = id;
		    }
		}
	    }

	    // 關掉亮最久的那顆
	    if (longestOnLampId != null) {
		SampleModel.setLampState(longestOnLampId, false);
	    }
	}
	
	

	    // 同步更新到 CSE
	    String targetID = SampleConstants.CSE_PREFIX + "/" + lampId + "/" + SampleConstants.DATA;
	    ContentInstance cin = new ContentInstance();
	    cin.setContent(ObixUtil.getStateRep(lampId, value));
	    cin.setContentInfo(MimeMediaType.OBIX + ":" + MimeMediaType.ENCOD_PLAIN);
	    RequestSender.createContentInstance(targetID, cin);
	}
	
	public static String getFormatedLampState(String lampId){
		return ObixUtil.getStateRep(lampId, getLampState(lampId));
	}
	
	public static boolean getLampState(String lampId){
		return SampleModel.getLampValue(lampId);
	}
	
	public static void toggleLamp(String lampId){
		boolean newState = !SampleModel.getLampValue(lampId);
		setLampState(lampId, newState);
	}
	
	public static void setAllOn(){
		setLampState(SampleConstants.LAMP_0, true);
		setLampState(SampleConstants.LAMP_1, true);
		setLampState(SampleConstants.LAMP_2, true); // 新增 LAMP_2
	}
	
	public static void setAllOff(){
		setLampState(SampleConstants.LAMP_0, false);
		setLampState(SampleConstants.LAMP_1, false);
		setLampState(SampleConstants.LAMP_2, false); // 新增 LAMP_2
	}
	
	public static void toogleAll(){
		boolean newState = !(SampleModel.getLampValue(SampleConstants.LAMP_0) 
				&& SampleModel.getLampValue(SampleConstants.LAMP_1)
				&& SampleModel.getLampValue(SampleConstants.LAMP_2)); // 修改邏輯加入 LAMP_2
		setLampState(SampleConstants.LAMP_0, newState);
		setLampState(SampleConstants.LAMP_1, newState);
		setLampState(SampleConstants.LAMP_2, newState); // 新增 LAMP_2
	}

	public static void setCse(CseService cse){
		CSE = cse;
	}
}

