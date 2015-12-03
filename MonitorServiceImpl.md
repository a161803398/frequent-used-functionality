#Monitor System CPU Memory Status

# Introduction #
在某些情況, 你可能想知道目前 JVM Heap 的記憶體使用量, 或是系統的記憶體總共有多少, 目前使用多少, 還剩多少, 這時你可以使用這邊介紹的 MonitorServiceImpl 來取得相關訊息.


# Usage #
這個類別目前只支援 Linux/Windows 兩種作業系統. Windows 是利用 wmic (Windows Management Instrumentation Command) 命令來取的作業系統的相關訊息, 有關 wmic 的使用可以參考 [這裡](http://msdn.microsoft.com/en-us/library/aa394531(v=vs.85).aspx). 接著來看一個使用範例:
```
    	MonitorServiceImpl monitorSrvImpl = new MonitorServiceImpl();    	
    	Set<Integer> testSet = new HashSet<Integer>();
    	MonitorInfoBean pb=null;
    	System.out.printf("\t[Info] OS Name=%s\n", MonitorServiceImpl.OsName);
    	for(int i=0; i<1000000; i++)
    	{
    		testSet.add(i);
    		
    		if(i%100000==0)
    		{
    			MonitorInfoBean bean = monitorSrvImpl.getMonitorInfoBean();    			
    			System.out.printf("\t[Info] Set size=%,d...\n", testSet.size());
    			System.out.printf("\t[Info] Total JVM Memory Size=%,d KB (%s)\n", bean.getTotalJVMMemory()/1024, pb!=null?pb.diffTotalJVMSize(bean):"-");
            	System.out.printf("\t[Info] Free JVM Memory Size=%,d KB (%s)\n", bean.getFreeJVMMemory()/1024, pb!=null?pb.diffFreeJVMSize(bean):"-");
            	System.out.printf("\t[Info] Used JVM Memory Size=%,d KB (%s)\n", (bean.getTotalJVMMemory()-bean.getFreeJVMMemory())/1024, pb!=null?pb.diffUsedJVMSize(bean):"-");
            	System.out.printf("\t[Info] Total System Memory Size=%,d KB\n", bean.getTotalPhyscailMemorySize());
            	System.out.printf("\t[Info] Free System Memory Size=%,d KB\n", bean.getFreePhysicalMemorySize());
            	System.out.printf("\t[Info] Used System Memory Size=%,d KB\n", bean.getUsedPyhsicalMemory());
            	System.out.println();
            	pb = bean;
            	Thread.sleep(1000);
    		}
    	}
```
上面的範例會不斷的往 **[HashSet](http://docs.oracle.com/javase/6/docs/api/java/util/HashSet.html)** 添加元素, 藉以觀察 JVM 與 系統記憶體的使用狀況的變化. 如果你的作業系統是 Linux 執行結果如下:
```
        [Info] OS Name=Linux
        [Info] Set size=1...
        [Info] Total JVM Memory Size=958 KB (-)
        [Info] Free JVM Memory Size=953 KB (-)
        [Info] Used JVM Memory Size=5 KB (-)
        [Info] Total System Memory Size=15,847 KB
        [Info] Free System Memory Size=9,606 KB
        [Info] Used System Memory Size=6,241 KB

        [Info] Set size=100,001...
        [Info] Total JVM Memory Size=958 KB (0.0 B)
        [Info] Free JVM Memory Size=927 KB (-25.5 KB)
        [Info] Used JVM Memory Size=30 KB (25.5 KB)
        [Info] Total System Memory Size=15,847 KB
        [Info] Free System Memory Size=9,591 KB
        [Info] Used System Memory Size=6,256 KB
...
```
如果是 Windows, 執行結果:
```
	[Info] OS Name=Windows 7
	[Info] Set size=1...
	[Info] Total JVM Memory Size=981 KB (-)
	[Info] Free JVM Memory Size=976 KB (-)
	[Info] Used JVM Memory Size=5 KB (-)
	[Info] Total System Memory Size=8,302,412 KB
	[Info] Free System Memory Size=4,091,952 KB
	[Info] Used System Memory Size=4,210,460 KB

	[Info] Set size=100,001...
	[Info] Total JVM Memory Size=981 KB (0.0 B)
	[Info] Free JVM Memory Size=971 KB (-5.1 KB)
	[Info] Used JVM Memory Size=10 KB (5.1 KB)
	[Info] Total System Memory Size=8,302,412 KB
	[Info] Free System Memory Size=4,083,144 KB
	[Info] Used System Memory Size=4,219,268 KB
...
```