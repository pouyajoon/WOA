package com.red5;



import java.util.List;

import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectSecurity;

import com.woa.Application;

  public class MySecurityHandler implements ISharedObjectSecurity
  {
	  private Application appli;
	  
	  public MySecurityHandler(Application _appli)
	  {
		  this.appli = _appli;
	  }
	  
	  public boolean isCreationAllowed(IScope scope, String name, boolean persistent)
	  {
		 try
		 {
			appli.log.debug("check if creation is allowed : [" + name + "]; appli=" + appli.toString());
		    if ("tempo".equals(name))
		    {
				//appli.log.debug("tempo created allowed catch : world=" + appli.toString());
		    	appli.world.mytime.create_tempo();
		    }
		    
		    String synchro_object = "woalshare";
		    String type = name.substring(0, synchro_object.length());
		    String id = name.substring(synchro_object.length() + 1, name.length());
		    appli.log.debug("id : " + id + ", type:" + type);
		    if (type.equals(synchro_object));
		    {
		    	Integer i = new Integer(id);
		    	appli.world.woal.woaobject_screen_share(appli.world.antzone.boxes.get(i));
		    }
		    
		    if ("woalshare".equals(name))
		    {
		      //appli.world.woal.woaobject_screen_share();
		    }	    
		    return true;
		 }
		 catch (Exception e)
		 {
			 appli.log.debug("failed on creation allowed");
			 e.printStackTrace();
		 }
		return true;
	  }
	  
public boolean isConnectionAllowed(ISharedObject arg0) {
	// TODO Auto-generated method stub
	return true;
}


public boolean isDeleteAllowed(ISharedObject arg0, String arg1) {
	// TODO Auto-generated method stub
	return false;
}

@SuppressWarnings("unchecked")
public boolean isSendAllowed(ISharedObject arg0, String name, List arg2) {
	// TODO Auto-generated method stub
	if ( "tempo".equals(name) ) return true;
	if ( "woalshare".equals(name) ) return true;
	return false;
}


public boolean isWriteAllowed(ISharedObject arg0, String arg1, Object arg2) {
	// TODO Auto-generated method stub
	return false;
}



}
