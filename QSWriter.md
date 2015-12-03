# Introduction #
使用 JDK 對檔案寫入的代碼如下:
```
	public static void NormalWrite() throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("test1.txt")));
		bw.append("line1\r\n");
		bw.append("line2\r\n");
		bw.close();
		bw = new BufferedWriter(new FileWriter(new File("test2.txt")));
		bw.append("line3\r\n");
		bw.append("line4\r\n");
		bw.close();
	}
```
看起來相當繁瑣, 每次開啟一個新的檔案, 就需要關閉舊的 **[BufferedWriter](http://docs.oracle.com/javase/6/docs/api/java/io/BufferedWriter.html)**, 再 new 一個新的 **[BufferedWriter](http://docs.oracle.com/javase/6/docs/api/java/io/BufferedWriter.html)**. 如果使用類別 QSWriter 則如下面範例代碼簡潔:
```
	public static void QSWriterTest() throws Exception
	{
		QSWriter qsw = new QSWriter("test1.txt");
		qsw.line("line1");
		qsw.line("line2");
		qsw.reopen("test2.txt");
		qsw.line("line3");
		qsw.line("line4");
		qsw.close();
	}
```

# Details #
Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages