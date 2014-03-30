import java.util.HashMap;

import java.util.Map.Entry;

import jp.crestmuse.cmx.processing.*;
import jp.crestmuse.cmx.amusaj.sp.*;
public class ModuleManager {
	public static CMXController cmx = CMXController.getInstance(); 
//	private CMXApplet cmx;
	private HashMap<String, SPModule> modules = new HashMap<String, SPModule>();
	private HashMap<String, ModuleUser> users = new HashMap<String, ModuleUser>();

//    public ModuleManager(CMXController cmx) {
    public ModuleManager() {
	}
	
	public void printModules() {
		for (Entry<String, SPModule> e : this.modules.entrySet()) {
			System.out.println(e.getKey() + ":" + e.getValue());
		}
	}
	
	public void manageModuleUser(String name, ModuleUser user) {
		user.setCmx(this.cmx);
        this.users.put(name, user);
	}
	
	public void manageModule(String name, SPModule mod) {
		this.modules.put(name, mod);
	}
	
	public void addModule2Cmx(String name) {
		SPModule mod = this.getModule(name);
		this.cmx.addSPModule(mod);
	}
		
	public SPModule getModule(String name) {
		return this.modules.get(name);
	}
	
	public ModuleUser getModuleUser(String name) {
		return this.users.get(name);
	}
	
	public void connectModules(String name1, String name2, boolean stft, boolean mi) {
		SPModule mod1 = this.getModule(name1);
		SPModule mod2 = this.getModule(name2);

		if (stft)  {
            this.cmx.connect(mod1, 0, mod2, 0);
            this.cmx.connect(mod1, 1, mod2, 1);
            this.cmx.connect(mod1, 2, mod2, 2);
		} else if(mi){
			this.cmx.connect(mod1, 0, mod2, 1);
		} else {
			this.cmx.connect(mod1, 0, mod2, 0);
		}
	

	}
	
	public void startSP() {
		this.cmx.startSP();
	}
}
