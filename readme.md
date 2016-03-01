# Slack Message Tracker

### What is it?
Slack Message Tracker is a simple Java application that will run indefnitly and track all your messages in every channel it is in. After that you can use /search to search for previous messages.

### Requirements
You will need Java installed and create an API key for a bot.

### Installation
Installation is extremely simple.
If you want to get up and running quickly just follow these few steps
- Download `java-slack-message-tracker.jar` from the root of this repository
- Put it somewhere and executed it using Java. E.g. on OSX or Linux: `java -jar java-slack-message-tracker.jar`
- It will start and throw an Slack error message. Just ignore it.
- It created a `config.properties` file. Fill in the details. `database_type` should be set top `mysql` or `sqlite`. If `mysql` is set don't forget to set the other `mysql_*` values.
- Create a API key for a Slack bot
- Enter your api key in the `config.properties` file.
- Run the jar again. (`java -jar java-slack-message-tracker.jar`)
It will now start and create the tables if needed.

### Build it from source
If you want to build it from source, clone this repo, build it, generate a jar file and follow the steps above. :)

### Can you add.... ?
Ofcourse! Its just a simple project bit hopefully it is helpfull to some people. If you want something added just create an issue. :)
