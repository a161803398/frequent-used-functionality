# Introduction #
常常我們需要將 byte 使用 hex string 列印出來, 檢視 binary 內容. 這時你便可以使用到這個類別上 static 的函數


# Usage #
下面簡單介紹 **[HexByteKit](https://code.google.com/p/frequent-used-functionality/source/browse/src/flib/util/HexByteKit.java)** 上面提供的靜態方法的使用. 底下我們先定義一個測試使用的字串:
```
		String str = "abcABC123繁體简体!@#$";
		System.out.printf("Str='%s'\n", str);
```
## Byte array to hex string ##
使用範例:
```
		System.out.printf("Str='%s'\n", str);
		byte b[] = str.getBytes("Unicode");
		System.out.printf("Hex string(Little-endian): 0x%s\n", HexByteKit.Byte2Hex(b));
		System.out.printf("Hex string(Big-endian): 0x%s\n", HexByteKit.Byte2Hex(b,1));
		System.out.printf("Hex string(Byte sep=':'): 0x%s\n", HexByteKit.Byte2Hex(b,":"));
		System.out.printf("Hex string(Little-endian, offset=2 bytes): 0x%s\n", HexByteKit.Byte2Hex(b, 2, b.length, "", 0));
		System.out.printf("Hex string(Big-endian, offset=2 bytes): 0x%s\n", HexByteKit.Byte2Hex(b, 2, b.length, "", 1));
```
輸出結果:
```
Str='abcABC123繁體简体!@#$'
Hex string(Little-endian): 0xFEFF0061006200630041004200430031003200337E419AD47B804F530021004000230024
Hex string(Big-endian): 0x2400230040002100534F807BD49A417E330032003100430042004100630062006100FFFE
Hex string(Byte sep=':'): 0xFE:FF:00:61:00:62:00:63:00:41:00:42:00:43:00:31:00:32:00:33:7E:41:9A:D4:7B:80:4F:53:00:21:00:40:00:23:00:24:
Hex string(Little-endian, offset=2 bytes): 0x0061006200630041004200430031003200337E419AD47B804F530021004000230024
Hex string(Big-endian, offset=2 bytes): 0x230040002100534F807BD49A417E330032003100430042004100630062006100FFFE
```
有關 Big-endian/Little-endian 的說明, 可以參考 [Endianness](http://en.wikipedia.org/wiki/Endianness). 另外在第二行輸出有 "FEFF" 的奇怪 hex string, 這是因為 Unicode 的 [BOM](http://en.wikipedia.org/wiki/Byte_order_mark) (<font color='brown'>Byte order mark</font>) 造成. 另外預設 **[HexByteKit](https://code.google.com/p/frequent-used-functionality/source/browse/src/flib/util/HexByteKit.java)** 在轉換 byte array 到 hex string 是使用 Little-endian.

## Hex String to byte array ##
使用範例:
```
		System.out.printf("Original Str='%s'\n", str);
		byte b[] = str.getBytes("Unicode");
		String hexStr = HexByteKit.Byte2Hex(b);
		System.out.printf("Hex string(Little-endian): 0x%s\n", hexStr);
		byte tb[] = HexByteKit.Hex2Byte(hexStr);
		String ns = new String(tb, "Unicode");
		System.out.printf("New Str='%s'\n", ns);
```
這邊我先將字串轉成 hex string, 再利用 **[HexByteKit](https://code.google.com/p/frequent-used-functionality/source/browse/src/flib/util/HexByteKit.java)** 將 hex string 轉為 byte array 後, 建立一個新的字串物件並將這個 byte array 視為 "Unicode" 的編碼. 輸出結果可以發現原字串與新字串相同, 說明 hex string 轉 byte array 正確. 輸出結果如下:
```
Original Str='abcABC123繁體简体!@#$'
Hex string(Little-endian): 0xFEFF0061006200630041004200430031003200337E419AD47B804F530021004000230024
New Str='abcABC123繁體简体!@#$'
```