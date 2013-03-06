# mlb-notifier-gae

You want to know about everything important happening in today's baseball games, but maybe you're away from your computer, or you're doing something else, or for some reason you only have one TV and can't watch all the games. And which game should you watch, what's happening?!

Enter the MLB Notifier. It runs out in the cloud -- specifically in Google's cloud, AppEngine -- and it constantly monitors MLB games, looking for things you might care about.

If there's a lead change in the 7th inning or later, you'll get an email.

If a pitcher has a perfect game (or a no-hitter, or a shutout) through 5 innings or more, you'll get an email.

So now, finally, you'll know which game to flip to.

# Can I just use your server?

No. This is only for personal use. My server only emails me.

If you want to get the emails, you have to run it yourself.

# Okay, let's do that. How do I set it up?

Step 1: Sign up for [Google AppEngine](https://appengine.google.com/) and create a new application. Remember your app's name, let's say you create "my-cool-mlb-thing".

Step 2: Clone this repo and open it in Eclipse. Make sure you've installed the AppEngine tooling for Eclipse. And make sure you [set up Lombok so it works in Eclipse](http://projectlombok.org/). (Lombok is a library that helps make Java quicker to write. I used it.)

Step 3: Open "war/WEB-INF/appengine-web.xml", and change the <application> element to your app's name ("my-cool-mlb-thing"), and change the "from-email" and "to-email" fields to your email address. (Note that AppEngine will only send an email from your Gmail account. You can send the emails _to_ any email address, but don't be a jerk. Only send it to yourself.)

Step 4: Deploy to AppEngine! There's a little blue button with a "g" on it in the Eclipse toolbar that will do that for you. 

Once you've deployed, you don't have to do anything but sit back and wait for your notifications to roll in.