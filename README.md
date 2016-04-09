# WebStats (Java)

This is an implementation of WebStats in Java. WebStats is a multithreaded, statistical web crawler which gathers cumulative, as well as separate, tag counts for a starting page and other pages found through anchor tags respectively. The pages followed per web page and the maximum depth are provided by command line arguments.

WebStats can be run from the command line as follows:

```bash
java WebStats -pages 10 -path 3 "http://www.google.ca"
```
where the <code>-pages</code> flag denotes the maximum amount of pages to follow from each page reached and the <code>-path</code> flag denotes the maximum depth of which to stray from the starting url, which, is the last argument.

WebStats will then output individual tag counts for each page it has crawled of each tag it has found and then conclude its execution with a global count of all tags counted.