# Introduction #
在 Java coding 中, 要打開檔案並一行行讀取內容的代碼常見如下:
```
	public static void NormalRead() throws Exception
	{
		File f = new File("test.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while((line=br.readLine())!=null)
		{
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		br.close();
	}
```
假設有個測試檔案 "test.txt":
```
This ls line1
# This is note message
This is line3
This is line4
中文字

This is line4
```
輸出如下:
```
	[Info] Read 'This ls line1'
	[Info] Read '# This is note message'
	[Info] Read 'This is line3'
	[Info] Read 'This is line4'
	[Info] Read '����r'
	[Info] Read ''
	[Info] Read 'This is line4'
```
上面輸出有下面的問題:
  * 因為檔案是 big5 編碼, 但是 Java 在讀出時使用 utf-8 編碼, 造成中文亂碼.
  * 空白與註解行不希望列印出來.
  * 代碼過於繁瑣
底下將透過類別 **QSReader** 來解決上面三個問題.
## 代碼過於繁瑣 ##
使用 **QSReader** 類別來讀取檔案相當簡單, 概念是來自 [Python 的檔案處理](http://docs.python.org/2/library/stdtypes.html#bltin-file-objects).一個使用範例代碼如下:
```
	public static void QSReaderEx() throws Exception
	{
		QSReader qsr = new QSReader("test.txt");
		qsr.open();  // 開啟檔案
		for(String line:qsr) // QSReader 可以使用 for-loop 一行行讀出內容
		{
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		qsr.close(); // 關閉檔案
	}
```
## 忽略空白行與註解行 ##
使用類別 **QSReader** 還有個好處, 如果是一般代碼要略過空白行與註解行, 可能如下處理:
```
	public static void NormalRead() throws Exception
	{
		File f = new File("test.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while((line=br.readLine())!=null)
		{
			if(line.isEmpty() || line.trim().startsWith("#")) continue; // 略過空白行與註解行
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		br.close();
	}
```
但如果是類別 **QSReader** 只要設定兩個屬性就搞定:
```
	public static void QSReaderEx() throws Exception
	{
		QSReader qsr = new QSReader("test.txt");
		// 下面設定略過空白行與註解行
		qsr.skipCommentLine = qsr.skipEmptyLine = true;
		qsr.open();
		for(String line:qsr)
		{
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		qsr.close();
	}
```
## 編碼不同造成中文亂碼 ##
由於文件編碼與 Java 讀取使用編碼不同造成亂碼, 一般會如下處理:
```
	public static void NormalRead() throws Exception
	{
		File f = new File("test.txt");
		// 下面代碼使用 "big5" 開啟檔案
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"big5"));
		String line;
		while((line=br.readLine())!=null)
		{
			if(line.isEmpty() || line.trim().startsWith("#")) continue;
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		br.close();
	}
```
如果是使用類別 **QSReader** ,則只要設定類別屬性 _ENCODING_ 即可:
```
	public static void QSReaderEx() throws Exception
	{
		QSReader.ENCODING = "big5"; // 設定開啟檔案的編碼
		QSReader qsr = new QSReader("test.txt");
		qsr.skipCommentLine = qsr.skipEmptyLine = true;
		qsr.open();
		for(String line:qsr)
		{
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		qsr.close();
	}
```
## 開啟壓縮檔 ##
更方便的是如果你的檔案是個壓縮檔 (.gz, bz2), 使用 QSReader 就跟開啟一般檔案沒有兩樣. 底下是範歷代碼:
```
	public static void QSReaderEx() throws Exception
	{
		QSReader.ENCODING = "big5"; // 設定開啟軮案的編碼
		QSReader qsr = new QSReader("test.csv.bz2", EFileType.BZ2); // 使用 BZ2 格式開啟檔案
		qsr.skipCommentLine = qsr.skipEmptyLine = true;
		qsr.open();
		for(String line:qsr)
		{
			System.out.printf("\t[Info] Read '%s'\n", line);
		}
		qsr.close();
	}
```

# Details #
Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages
