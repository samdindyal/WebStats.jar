/*		Name:   Daniel Tran, Samuel Dindyal
 *		Course: CPS506, Winter 2016, Assignment #1
 *		Due:    2016.02.11 23:59
 *		This is entirely my own work.
 */

import cps506.a1.core.WebStatsRuntime;

public class WebStats {
	public static void main (String[] args){
		int maxFollow = 0, maxDepth = 0;
		String url = "";
		if (args[0].equals("-pages"))
			maxFollow = Integer.parseInt(args[1]);
		if (args[2].equals("-path"))
		{
			maxDepth = Integer.parseInt(args[3]);
			url = args[4];
		}

		WebStatsRuntime rt = new WebStatsRuntime(5, maxDepth, maxFollow);
		rt.execute(url);
	}
}