/*		Name:   Daniel Tran, Samuel Dindyal
 *		Course: CPS506, Winter 2016, Assignment #1
 *		Due:    2016.02.11 23:59
 *		This is entirely my own work.
 */

package cps506.a1.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;

public class WebStatsRuntime implements ActionListener {
	
	public static final int DONE_PARSING_CODE = 0,
							NEW_LINK_CODE = 1;

	private int maxThreads, maxDepth, maxFollow;
	private HashMap<String, Integer> tagCount;

	private ArrayList<Thread> threadPool;
	private ArrayList<String> urls;
	private LinkedList<Thread> queue;

	/**
    WebStatsRuntime Constructor
    
    @param maxThreads 	Maximum number of concurrent threads
    @param maxDepth 	Maximum depth to crawl at any path.
    @param maxDepth 	Maximum number of links to follow on a given page.
   */
	public WebStatsRuntime(int maxThreads, int maxDepth, int maxFollow) {
		this.maxThreads = maxThreads;
		this.maxDepth = maxDepth;
		this.maxFollow = maxFollow;
		tagCount = new HashMap<String, Integer>();
		threadPool = new ArrayList<Thread>();
		queue = new LinkedList<Thread>();
		urls = new ArrayList<String>();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        	public void run() {
            	printOut();
        	}
    	}, "Shutdown-thread"));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getID() == DONE_PARSING_CODE)
			combineTagCounts((HashMap<String,Integer>)e.getSource());
		else if (e.getID() == NEW_LINK_CODE)
			spawnThread(((WebCrawler)(e.getSource())));
	}


/**
    combineTagCounts()

    @param inputTagCount 	HashMap to combine with.
  */
	private void combineTagCounts(HashMap<String, Integer> inputTagCount){
		for (String key : inputTagCount.keySet())
		{
			if (tagCount.containsKey(key))
				tagCount.put(key, tagCount.get(key)+inputTagCount.get(key));
			else
				tagCount.put(key, inputTagCount.get(key));
		}
	}

	/**
    spawnThread()

	@param crawler 	The crawler being given a new thread.
   */
	private void spawnThread(WebCrawler crawler)
	{
		if (!urls.contains(crawler.getURL()))
		{
			urls.add(crawler.getURL());
			Thread t = new Thread(new Runnable() {
				public void run() {
					System.out.println(crawler.toString());
					threadPool.remove(Thread.currentThread());
					if (queue.size() > 0)
						threadPool.add(queue.removeFirst());
					if (queue.size() == 0 && threadPool.size() == 1)
						printOut();
				}
			});

			if (threadPool.size() < maxThreads)
			{
				threadPool.add(t);
				t.start();
			}
			else
				queue.add(t);
		}
	}

/**
    execute()

    @param 	startURL 		The url to start crawling at.
 */

	public void execute(String startURL) {
		WebCrawler crawler = new WebCrawler(startURL, 0, maxDepth, maxFollow, this);
		System.out.println(crawler.toString());
	}

/**
    printOut()
    
    @return String representation of all counts.
  */

	public String printOut() {
		String output = "\n---------------------------------------------------\nTOTAL COUNT\n---------------------------------------------------";
		for (String tag : tagCount.keySet())
			output += "\n" + tag + ": " + tagCount.get(tag);
		System.out.println(output += "\n");
		return output;
	}
}