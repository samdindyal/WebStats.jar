/*		Name:   Daniel Tran, Samuel Dindyal
 *		Course: CPS506, Winter 2016, Assignment #1
 *		Due:    2016.02.11 23:59
 *		This is entirely my own work.
 */

package cps506.a1.core;

import java.util.HashMap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URL;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class WebCrawler {

	private URL url = null;
	private InputStream is;
	private BufferedReader br;

	private String pageIn;
	private int currentDepth, maxDepth, maxFollow;

	private HashMap<String,Integer> tagCounts;
	private ActionListener runtime;

 /**
    WebCrawler Constructor (Designated Constructor)
    @param url 				The URL to start at
    @param currentDepth  	Default 0, current depth in crawling
    @param maxDepth			Maximum pages to crawl in any path.
    @param runtime 			Runtime object to report to.
   */
	public WebCrawler(String url, int currentDepth, int maxDepth, int maxFollow, WebStatsRuntime runtime) {
		pageIn = "";
		tagCounts = new HashMap<String,Integer>();
		this.runtime = runtime;
		this.maxFollow = maxFollow;

		this.currentDepth = currentDepth;
		this.maxDepth = maxDepth;

		try {
			this.url = new URL(url);
			is = this.url.openStream();
			br = new BufferedReader(new InputStreamReader(is));
		}catch (IOException e){ System.err.println("Could not access url: " + url); }
	}

/**
    WebCrawler Constructor
    @param url 				The URL to start at
    @param currentDepth  	Default 0, current depth in crawling
    @param runtime 			Runtime object to report to.
  */

	public WebCrawler(String url, int currentDepth, int maxFollow, WebStatsRuntime runtime) {
		this(url, currentDepth, 10, maxFollow, runtime);
	}

 /**
    fethHtml()
	@return 	The HTML fetched from the URL corresponding to this object.
   */
	public String fetchHtml(){
		String buffer = "";
		try {
			String line;
			while ((line = br.readLine()) != null){
				buffer += line;
			}
		}catch (IOException e){ System.err.println("Could not fetch the page: " + url.toString()); }

		return pageIn = buffer;
	}

/**
    parseHtml()
    @return		A hashmap containing all of the counts of each tag found.
  */
	public HashMap<String, Integer> parseHtml() {

		pageIn = fetchHtml() ;
		Pattern p = Pattern.compile("[<][^!/<][^<>]*[>]") ;
		Matcher m = p.matcher(pageIn) ;
		int count = 0, counter = 0;
		String tag = "", link;

		while (m.find()) {
			int tagBounds = (tagBounds = m.group(0).indexOf(" ")) == -1 ? m.group(0).indexOf(">") : tagBounds;
			tag = m.group(0).substring(1, tagBounds).toLowerCase();

			if (tag.equals("a"))
			{
				int lowerBound = m.group(0).indexOf("href=\"") + 6;
				int upperBound = m.group(0).indexOf("\"", lowerBound);

				if ((link = m.group(0).substring(lowerBound, upperBound)).toLowerCase().startsWith("http")
					&& currentDepth < maxDepth
					&& counter < maxFollow)
				{
					runtime.actionPerformed(new ActionEvent(new WebCrawler(link, currentDepth+1, maxDepth, maxFollow, (WebStatsRuntime)runtime),
						WebStatsRuntime.NEW_LINK_CODE, null, System.currentTimeMillis(), 0));
					counter++;
				}
			}
			if (tagCounts.containsKey(tag)) {
				tagCounts.put(tag, tagCounts.get(tag) + 1) ;
			}
			else {
				tagCounts.put(tag, 1);
			}
			count++;
		}

		runtime.actionPerformed(new ActionEvent(tagCounts, WebStatsRuntime.DONE_PARSING_CODE,
			null, System.currentTimeMillis(), 0));
		return tagCounts;
	}

/**
    getURL()
    @return 	The URL.
   */
	public String getURL() {
		return url.toString();
	}

/**
    toString()
    @return 	Formatted string of all counts with the URL as the header.
   */

	public String toString() {
		tagCounts = parseHtml();
		String output = "\n---------------------------------------------------\n"
		+ url.toString() + "\n---------------------------------------------------";
		for (String tag : tagCounts.keySet())
			output += "\n" + tag + ": " + tagCounts.get(tag);
		return output + "\n";
	}
}
