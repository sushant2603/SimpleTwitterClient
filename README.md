Basic Twitter Client
==============

This app displays latest feed of tweets for a user. Uses OAuth to authenticate with Twitter

Time spent: 20hrs
Stories Completed:
Required
 -User can sign in to Twitter using OAuth login
 -User can view the tweets from their home timeline
 -User should be displayed the username, name, and body for each tweet
 -User should be displayed the relative timestamp for each tweet "8m", "7h"
 -User can view more tweets as they scroll with infinite pagination
 -User can compose a new tweet
 -User can click a “Compose” icon in the Action Bar on the top right
 -User can then enter a new tweet and post this to twitter
 -User is taken back to home timeline with new tweet visible in timeline
Optional:
 -Links in tweets are clickable and will launch the web browser (see autolink)
 -User can see a counter with total number of characters left for tweet
 
Advanced Stories:
 -User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
 -Improve the user interface and theme the app to feel "twitter branded"
 -Compose activity is replaced with a modal overlay

 Partially Completed:
 -User can open the twitter app offline and see last loaded tweets
 -Tweets are persisted into sqlite and can be displayed from the local DB
 
 Notes:
 -I did basic work to persist last 25 tweets. Was having issues which could be because of emulator
  set up for internet. Commented the code.

![Video Walkthrough](SimpleTwitterClient.gif)