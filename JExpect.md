# Introduction #
Python 的 **[Pexpect 模組](https://pexpect.readthedocs.org/en/latest/index.html)** 提供使用者能夠與互動的程式進行溝通. 而這裡的 <font color='blue'>JExpect</font>**可以想像成 Pexpect 模組 的 Java 版本, 並封裝到**[Flib.jar](https://drive.google.com/file/d/0B3JEkc9JW7BOZDVKdUFJVWtsdUU/view?usp=sharing)**模組. 底下將針對該類別的使用進行介紹.**

# Usage Of JExpect #
<font color='blue'>JExpect</font>**上提供靜態方法**<font color='blue'>Spawn(String cmd)</font> 讓你可以透過 Spawn 一個新的 Process 來執行給定命令:
```
/** 
 * BD: Spawn a Process by executing given command. 
 *  
 * @param cmd: Command to execute in Spawn process. 
 * @return 
 * @throws Exception 
 */  
public static ISpawnProcess Spawn(String cmd) throws Exception  
```
假設我們有一個互動的程式 <font color='olive'>HelloShell.jar</font> ([download](https://drive.google.com/file/d/0B3JEkc9JW7BOQlVzLTl6OTRWcGM/view?usp=sharing)), 執行過程如下:
https://lh5.googleusercontent.com/-I0ctnkTtVWA/VNmeyaysHPI/AAAAAAAATWQ/dWhE0a2SrhM/w1143-h381-no/JExpect_1.PNG

則你可以使用下面代碼啟動此互動程式, 並使用返回的 <font color='violet'>ISpawnProcess</font>**介面的實作物件進行與程式互動:
```
ISpawnProcess sp = JExpect.Spawn("java -jar HelloShell.jar")  
```
接著你可以使用**<font color='violet'>ISpawnProcess</font>**介面上的方法**<font color='blue'>sendLine()</font> 將輸入送到 **[Stdin](http://en.wikipedia.org/wiki/Standard_streams#Standard_input_.28stdin.29)** 中 (<font color='brown'>模擬從鍵盤輸入</font>):
```
/** 
 * Send input line to System.in. 
 *  
 * @param line: Line to be sent to System.in 
 * @throws Exception 
 */  
public void sendLine(String line) throws Exception;   
```
或是透過下面 API 對 **[Stdout](http://en.wikipedia.org/wiki/Standard_streams#Standard_output_.28stdout.29)**/**[Stderr](http://en.wikipedia.org/wiki/Standard_streams#Standard_error_.28stderr.29)** 進行 Pattern 比對:
```
public Future<Boolean> expect_asyn(String pattern) throws Exception;  
public boolean expect_async(String pattern, int timeout) throws Exception;  
public boolean expect(String pattern, int timeout) throws Exception;  
public boolean expect(String pattern) throws Exception;  
public boolean expect_exact(String pattern, int timeout) throws Exception;  
public boolean expect_exact(String pattern) throws Exception; 
```
譬如你可以在進入 Interactive 前針對 Hello Shell 在 Stdout 上輸出的 "This is Hello Shell" 中的 "Hello Shell" 字串來判斷目前已經成功進入互動模式, 範例代碼如下:
```
ISpawnProcess sp = JExpect.Spawn("java -jar HelloShell.jar")    
if(sp.expect_async("Hello Shell", 2000))  
{  
    printf("\t[Info] Entering Interactive console...\n")  
}    
else  
{  
    printf("\t[Error] Fail to open Interactive console!\n")  
    sp.close()  // Close Spawn Process  
}
```
接著你便可以透過 <font color='blue'>sendLine(String msg)</font> 傳送命令到 **[Stdin](http://en.wikipedia.org/wiki/Standard_streams#Standard_input_.28stdin.29)** 與程式互動, 並從 <font color='violet'>lastExpect</font>