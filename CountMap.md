#CountMap usage

# Introduction #
在統計資料時, 時常需要將資料丟進去 Map 中並使用 key 為要統計的物件; value 為該物件出現的次數. 最後在依序 (Desc or Asc) 將資料取出. 常見統計使用代碼如下:
```
		// 0) Counting data
		String [] datas = {"a", "a", "b", "a", "c", "d", "b", "c", "a", "e", "b", "c", "e", "f"};
		
		// 1) Start Counting
		HashMap<String,Integer> cntMap = new HashMap<String,Integer>();
		for(int i=0;i<datas.length;i++)
		{
			Integer cnt = cntMap.get(datas[i]);
			if(cnt==null) cntMap.put(datas[i], 1); // First time exist
			else cntMap.put(datas[i], cnt+1); // Increase count
		}
		
		// 2) Show Counting Info
		Iterator<Entry<String,Integer>> iter = cntMap.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String,Integer> e = iter.next();
			System.out.printf("%s has %d\n", e.getKey(), e.getValue());
		}
```
輸出結果如下:
```
f has 1
d has 1
e has 2
b has 3
c has 3
a has 4
```
如果你希望依照 Desc 順序列印出結果, 則需要透過 [PriorityQueue](http://download.java.net/jdk6/archive/b104/docs/api/java/util/PriorityQueue.html) 與自訂的 [Comparator](http://download.java.net/jdk6/archive/b104/docs/api/java/util/Comparator.html):
```
		Comparator<Tuple> cmp = new Comparator<Tuple>(){
			@Override
			public int compare(Tuple p1, Tuple p2) {
				int i1=p1.getInt(1), i2=p2.getInt(1);
				if(i1>i2) return -1;
				else if(i1<i2) return 1;
				else return 0;
			}			
		};
		PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>(10, cmp);
		for(Entry<String,Integer> e:cntMap.entrySet()) pq.add(new Tuple(e.getKey(), e.getValue()));
		while(!pq.isEmpty())
		{
			Tuple t = pq.poll();
			System.out.printf("%s has %d\n", t.get(0), t.get(1));
		}
```
輸出結果如下:
```
a has 4
c has 3
b has 3
e has 2
f has 1
d has 1
```
# CountMap Usage #
但是透過 CountMap 這些工作就變得簡單. 範例代碼如下:
```
		// 0) Counting data
		String[] datas = { "a", "a", "b", "a", "c", "d", "b", "c", "a", "e", "b", "c", "e", "f" };
		
		// 1) Start Counting
		CountMap cm = new CountMap();
		for (int i = 0; i < datas.length; i++) cm.count(datas[i]);
		
		// 2) Show Data in Des Order
		System.out.printf("\t[Info] Desc:\n%s\n", cm);
		cm.asc(); // Using Asc order
		System.out.printf("\t[Info] Asc:\n%s\n", cm);
```
輸出結果:
```
	[Info] Desc:
a(4)
c(3)
b(3)
e(2)
f(1)
d(1)

	[Info] Asc:
f(1)
d(1)
e(2)
b(3)
c(3)
a(4)
```