#TimeStr usage

# Introduction #
再跑程序的過程中, 很多時候我們需要知道某個流程或某個 API 到底花多少時間. 底下是一個範例:
```
long st = System.currentTimeMillis(); // 紀錄執行前系統時間
CALL_API();  // 呼叫 API
System.out.printf("API spends %d ms!\n", System.currentTimeMillis()-st); // 計算執行後系統時間與之前紀錄的系統時間差.
```
但這樣的表示到底是幾分, 幾秒呢? 可能你還要手動計算一下. 透過 TimeStr 類別, 它能幫助你將 ms 轉換成你容易理解的時間字串.

# TimeStr 類別使用說明 #
例如上面的範例, 我們將 CALL\_API() 停止的時間變為 90 秒:
```
	public static void CALL_API() throws Exception
	{
		Thread.sleep(90000);
	}
	
	public static void main(String args[]) throws Exception
	{
		long st = System.currentTimeMillis();
		CALL_API();
		System.out.printf("API spends %s!\n", TimeStr.ToStringFrom(st));
	}
```
如此執行結果便會如下:
> API spends **1 min 30 sec!**