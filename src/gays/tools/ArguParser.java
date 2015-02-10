package gays.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import flib.util.JDebug;
import gays.tools.enums.EArguQuantity;
import gays.tools.enums.EArguRestrict;

/**
 * BD : Simple tool kit to parse the argument from console
 * 
 * @author John-Lee
 */
public class ArguParser {
	private HashMap<String, Argument> 	argsSet; // Defination of argument
	private boolean 					empty = true;	
	private Logger 						debugKit;
	public boolean 						isSuccessive = false;
	public List<String>					lastArgument = new LinkedList<String>();
	public Argument						missArgument=null;
	public boolean 						showAA=true; // Show argument attributes
	public List<String>					arguRegList;

	public ArguParser() {
		debugKit = JDebug.getLogger("ArguParser");
	}
	
	
	protected void _parse(HashMap<String, Object> def, List<String> argList) {
		// System.out.printf("\t[Test] def=%d\n", def.size());
		argsSet = new HashMap<String, Argument>();
		arguRegList = new ArrayList<String>();
		Set<Entry<String, Object>> s = def.entrySet();
		Iterator<Entry<String, Object>> iter = s.iterator();
		boolean isErr = false;
		while (iter.hasNext()) {
			Entry<String, Object> e = iter.next(); // definition of argument
			Object key = e.getValue();
			Argument argStruct = null;
			if (key instanceof String) {
				argStruct = new Argument(e.getKey(), (String) e.getValue());
			} else if (key instanceof ArguConfig) {
				argStruct = new Argument(e.getKey(), (ArguConfig) e.getValue());
			} else {
				System.err.printf("\t[Error] Illegal argument setting=%s!\n",
						key);
				debugKit.warning(String.format(
						"Illegal argument setting! (%s)", key));
				continue;
			}
			arguRegList.add(argStruct.getKey());
			argsSet.put(argStruct.getKey(), argStruct);
			// arguRegList.add(argStruct.getKey());
			// System.out.printf("\t[Test] %s\n", argStruct);

			/* Parsing argument from command line */
			if (!isErr && !argStruct.parseArgu(argList)) {
				/* Handle: Fail to parsing */
				debugKit.warning(String.format(
						"Parsing error on argument->%s!", argStruct));
				// System.err.printf("\t[Error] Parsing error on argument->%s!\n",
				// argStruct);
				// this.showArgus();
				isErr = true;
				// continue;
			}
			if (!isErr) {
				if (!argStruct.isSet()
						&& argStruct.config != null
						&& argStruct.config.restrict
								.equals(EArguRestrict.Required)) {
					// System.err.printf("\t[Error] Argument='%s: %s' is required!\n",
					// argStruct.getKey(), argStruct.getDescript());
					missArgument = argStruct;
					//return;
				}
			}

			/*
			 * for (String a : argList) { if (argStruct.checkArgu(a)) {
			 * argList.remove(a); break; } }
			 */
		}
		for (String larg : argList)
			lastArgument.add(larg);
		if(missArgument==null) isSuccessive = true;
	}
	
	public ArguParser(HashMap<String, Object> def, List<String> argList)
	{
		this();
		_parse(def, argList);		
	}

	/**
	 * BD : Constructor to receive argument defination and actual argument from console.
	 * 
	 * @param def: Defination of argument
	 * @param arg: Argument from console
	 */
	public ArguParser(HashMap<String, Object> def, String[] arg) {
		this();
		List<String> argList = new LinkedList<String>(); // Console argument
															// list
		if (arg != null && arg.length > 0) {
			empty = false;
			for (String a : arg) {
				debugKit.info("Add argu from console:" + a);
				argList.add(a);
			}
		}
		
		_parse(def, argList);
	}

	/**
	 * BD : Show Argument messages.
	 */
	public String showArgus() {
		StringBuffer ssb = new StringBuffer("");
		StringBuffer dsb = new StringBuffer("");
		Set<String> keys = argsSet.keySet();
		Iterator<String> ikeys = arguRegList.iterator();
		//while (ikeys.hasNext()) {
		if(arguRegList.size()==0) while(ikeys.hasNext()) arguRegList.add(ikeys.next()); 
		for(int i=0; i<arguRegList.size(); i++) {
			Argument a = argsSet.get(arguRegList.get(i));			
			if(a.getKey().startsWith("--")) dsb.append(String.format("%s: %s %s\r\n", a.getKey(), a.getDescript(), 
					showAA?String.format("(%s)", a.quantity):""));
			else ssb.append(String.format("%s: %s %s", a.getKey(), a.getDescript(), showAA?String.format("(%s)", a.quantity):""));
			
			if(showAA && a.config!=null && a.config.restrict.equals(EArguRestrict.Required)) ssb.append(" *");
			ssb.append("\r\n");
		}
		System.out.println(String.format("%s%s", ssb.toString(), dsb.toString()));
		return String.format("%s%s", ssb.toString(), dsb.toString());
	}

	/**
	 * BD : Get argument object.
	 * @param arg
	 * @return
	 */
	public Argument getArgument(String arg)
	{
		return argsSet.get(arg);
	}
	
	/**
	 * BD : Getting all given argument from console.
	 * 
	 * @return ArrayList of Object:Argument which contains argument info.
	 */
	public ArrayList<Argument> getSettingArgu() {
		ArrayList<Argument> tmp = new ArrayList<Argument>();
		Set<Entry<String, Argument>> s = argsSet.entrySet();
		Iterator<Entry<String, Argument>> iter = s.iterator();
		while (iter.hasNext()) {
			Entry<String, Argument> e = iter.next();
			if (e.getValue().isSet()) {
				tmp.add(e.getValue());
			}
		}
		return tmp;
	}

	/**
	 * BD : Getting description of specific argument.
	 * 
	 * @param key
	 *            : argument key
	 * @return
	 */
	public String getArguDescrip(String key) {
		Argument argu = argsSet.get(key);
		if (argu != null) {
			return argu.getDescript();
		}
		return "Unknown Argument!";
	}

	/**
	 * BD : Retrieve argument value with given argument name as <key>.
	 * @param key: Argument name
	 * @return
	 */
	public String getArguValue(String key) {
		Argument argu = argsSet.get(key);
		if (argu != null) {
			return argu.getValue();
		}		
		return null;
	}
	
	/**
	 * BD: Retrieve argument value in Int type with given argument name as <key>;
	 * @param key: Argument name
	 * @return
	 */
	public Integer getArguIntValue(String key) 
	{
		Argument argu = argsSet.get(key);
		if (argu != null) {
			return argu.getIntValue();
		}		
		return null;
	}
	
	public List<Object> getArguValues(String key)
	{
		Argument argu = argsSet.get(key);
		if (argu != null) {
			return argu.getValues();
		}		
		return null;
	}
	
	public List<String> getArguStrValues(String key)
	{
		Argument argu = argsSet.get(key);
		if (argu != null) {
			return argu.getStringValues();
		}		
		return null;
	}

	/**
	 * BD : Check if specific argument is set. If the argument is given from
	 * console, return true. Vice versa.
	 * 
	 * @param key
	 *            : argument key
	 * @return
	 */
	public boolean isSet(String key) {
		Argument argu = argsSet.get(key);
		if (argu != null) {
			return argu.isSet();
		}
		return false;
	}

	/**
	 * BD : Check if the given argument is empty. If empty, return true. Vice
	 * versa.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return empty;
	}

	public static void main(String args[]) {
		/* The first mark of argument will be the unique key for searching */
		/* Step1 : Define argument meaning */
		String fargs[] = { "-s", "single value", "-a1=test", "-b",  "-m", "value1", "value2" , "value3", "-c" };
		//String fargs[] = {  };
		HashMap<String, Object> defineOfArgu = new HashMap<String, Object>();
		defineOfArgu.put("-a1,--auto", "Auto Setup.");
		defineOfArgu.put("-b,--block", "Block Until...");
		defineOfArgu.put("-d,-D,--decode", "Decode");
		defineOfArgu.put("-c,-C,--Check", new ArguConfig("Check Mail...", EArguQuantity.SIGN));
		defineOfArgu.put("-e,-E,--eg", "Show Example");
		defineOfArgu.put("-m,--multiple", new ArguConfig("Multiple argument(s)", EArguQuantity.MULTIPLE, EArguRestrict.Required));
		defineOfArgu.put("-s,--single", new ArguConfig("single", EArguQuantity.SINGLE));
		//System.out.printf("\t[Test] DF=%d\n", defineOfArgu.size());
		/*
		 * Step2 : Pass argument defination and actualy argu read from console
		 * into Class:ArguParser
		 */
		ArguParser parser = new ArguParser(defineOfArgu, fargs);

		/* Step3 : Fetch argument and analysing */
		System.out.printf("\t[Info] Parsing argument:\n");
		if(parser.isSuccessive)
		{
			ArrayList<Argument> argSet = parser.getSettingArgu();
			for (Argument a : argSet) {
				System.out.print("You have give " + a.getKey() + " which means: " + a.getDescript());
				if (a.getValue()!=null && !a.getValue().isEmpty()) {
					System.out.printf(" with value='%s'\n", a.getValue());
				} else if(a.getValues().size()>0){
					List<Object> vals = a.getValues();
					System.out.printf(" with value='%s'", vals.get(0));
					for(int i=1; i<vals.size(); i++) System.out.printf(", %s", vals.get(i));
					System.out.println();
					
				} else {
					System.out.println();
				}
			}
		}
		System.out.println();
		System.out.printf("\t[Info] Show argument usage:\n");
		parser.showArgus();
	}
}
