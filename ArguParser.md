# Introduction #
常常在寫Code 的人一定會面對到的問題, 就是如何從 Console 讀入使用者下的參數即Argument. 因為寫久了有點悶, 不想再面對成山成海的 if/else 迴圈, 所以就寫了這個 **ArguParser** 類別並放入 Flib 這個 library 裡面, 主要就是解決開發人員需要頻繁的讀取 Console 參數所面臨的問題. 而這個類別有一部分是參考 Python 中的 [std library argparse](http://docs.python.org/dev/library/argparse.html)

# 功能介紹 #
首先我些來介紹它的使用方法, 首先我們會用到 **[java.util.HashMap](http://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)** 來存放我們對 Console 參數的定義, 其中Key 是存放參數的標示如 '-a,--auto' 等, 如果有多個標示, 可以用',' 分隔開來. 再來是Value存放該參數的定義, 稍後透過 **Argument** 的介面可以將你設定過的參數說明取出來. 最後再將實際由 Main 函數讀入的 String args[.md](.md) 與你定義的參數傳入類別 **ArguParser**. 接下來就參考下面的使用範例.

# 使用說明 #
要使用這個類別來解析命令列參數前, 你必須先定義好你有那些參數, 這便是第一步. 而在定義參數的過程中你可能對每個參數期望不一. 譬如有些參數只是當作 flag 使用, 並不會接值; 但是有些參數會給值, 甚至是多個值. 而這些都必須在定義參數時說明的. 底下是一個定義參數的使用範例:
```
		/* Step1 : Define argument meaning */
		HashMap<String, Object> defineOfArgu = new HashMap<String, Object>();
		defineOfArgu.put("-a,--Auto", new ArguConfig("Auto Setup.", EArguQuantity.SIGN)); 
		defineOfArgu.put("-b,--Block", "Block Until..."); 
		defineOfArgu.put("-c,--Check", new ArguConfig("Check", EArguQuantity.SINGLE)); 
		defineOfArgu.put("-d,--D", new ArguConfig("Datas", EArguQuantity.MULTIPLE)); 
```
上面代碼透過列舉 **[EArguQuantity](https://code.google.com/p/frequent-used-functionality/source/browse/src/gays/tools/enums/EArguQuantity.java)** 決定參數可以接的值的行為:
  * MULTIPLE: More than one, including one
  * SINGLE: nly one and no empty
  * SIGN: Empty. Just as sign
  * QUESTION: One or empty
  * QUESTIONS: More than one or empty
接著你便可以將 main 函數的 _args_ 陣列與剛剛定義的參數一同傳遞給 **ArguParser** 的建構子進行命令列參數的解析:
```
		/*
		 * Step2 : Pass argument defination and actualy argu read from console into Class:ArguParser
		 */		
		ArguParser parser = new ArguParser(defineOfArgu, args);
```
最後便是透過 **ArguParser** 判斷並取出對應參數的設定:
```
		/* Step3 : Fetch argument and analysing */
		System.out.printf("\t[Info] Parsing argument:\n");
		if(parser.isSuccessive)
		{
			ArrayList<Argument> argSet = parser.getSettingArgu();
			for (Argument a : argSet) {
				System.out.print("You have give " + a.getKey() + " which means: " + a.getDescript());
				if (a.getValue()!=null) {
					System.out.printf(" with value='%s'\n", a.getValue());
				} else if(a.getValues().size()>0){
					List<Object> vals = a.getValues();
					System.out.printf(" with values='%s'", vals.get(0));
					for(int i=1; i<vals.size(); i++) System.out.printf(", '%s'", vals.get(i));
					System.out.println();					
				} else {
					System.out.println();
				}
			}
		}
		System.out.println();
		if(parser.lastArgument.size()>0)
		{			
			StringBuffer lastArguBuf = new StringBuffer();
			for(String arg:parser.lastArgument) lastArguBuf.append(String.format("%s ", arg));
			System.out.printf("\t[Info] Undef argument(s): [%s]\n", lastArguBuf.toString().trim());
		}
		System.out.printf("\t[Info] Show argument usage:\n");
		parser.showArgus();
```

## 使用範例 ##
下面的範例簡單示範如何定義參數與物件化 **ArguParser**, 並取出 Console 設定的參數並將之列印出來, 在這裡簡單使用 String 陣列 _fargs_ 模擬 Console 給的參數. 範例代碼如下:
```
		String fargs[] = {"-s", "-a", "-b=12","-c", "xxx", "--D", "d1", "d2", "d3", "-e=123"};
		StringBuffer strBuf = new StringBuffer();
		for(String arg:fargs) strBuf.append(String.format("%s ", arg));
		System.out.printf("\t[Info] Arguments: [%s]\n", strBuf.toString().trim());
		
		/* Step1 : Define argument meaning */
		HashMap<String, Object> defineOfArgu = new HashMap<String, Object>();
		defineOfArgu.put("-a,--Auto", new ArguConfig("Auto Setup.", EArguQuantity.SIGN)); 
		defineOfArgu.put("-b,--Block", "Block Until..."); 
		defineOfArgu.put("-c,--Check", new ArguConfig("Check", EArguQuantity.SINGLE)); 
		defineOfArgu.put("-d,--D", new ArguConfig("Datas", EArguQuantity.MULTIPLE)); 
		
		/*
		 * Step2 : Pass argument defination and actualy argu read from console into Class:ArguParser
		 */		
		ArguParser parser = new ArguParser(defineOfArgu, fargs);
		
		/* Step3 : Fetch argument and analysing */
		System.out.printf("\t[Info] Parsing argument:\n");
		if(parser.isSuccessive)
		{
			ArrayList<Argument> argSet = parser.getSettingArgu();
			for (Argument a : argSet) {
				System.out.print("You have give " + a.getKey() + " which means: " + a.getDescript());
				if (a.getValue()!=null) {
					System.out.printf(" with value='%s'\n", a.getValue());
				} else if(a.getValues().size()>0){
					List<Object> vals = a.getValues();
					System.out.printf(" with values='%s'", vals.get(0));
					for(int i=1; i<vals.size(); i++) System.out.printf(", '%s'", vals.get(i));
					System.out.println();					
				} else {
					System.out.println();
				}
			}
		}
		System.out.println();
		if(parser.lastArgument.size()>0)
		{			
			StringBuffer lastArguBuf = new StringBuffer();
			for(String arg:parser.lastArgument) lastArguBuf.append(String.format("%s ", arg));
			System.out.printf("\t[Info] Undef argument(s): [%s]\n", lastArguBuf.toString().trim());
		}
		System.out.printf("\t[Info] Show argument usage:\n");
		parser.showArgus();  
```
## 執行結果 ##
```
	[Info] Arguments: [-s -a -b=12 -c xxx --D d1 d2 d3 -e=123]
	[Info] Parsing argument:
You have give -c which means: Check with value='xxx'
You have give -d which means: Datas with values='d1', 'd2', 'd3'
You have give -a which means: Auto Setup.
You have give -b which means: Block Until... with value='12'

	[Info] Undef argument(s): [-s -e=123]
	[Info] Show argument usage:
-c: Check (SINGLE)
-d: Datas (MULTIPLE)
-a: Auto Setup. (SIGN)
-b: Block Until... (QUESTION)
```