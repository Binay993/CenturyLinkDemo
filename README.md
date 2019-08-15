# Instructions

This micro service allows you to find followers for a given id with a custom depth level and size per id.


Simply clone or download the project, build using maven (mvn clean install) and run using command line 'mvn spring-boot:run' 
If it fails to run that way, simply import to IDE, go to Application.java file, right click and run.

The endpoint can be accessed at http://localhost:8080/user/{id}/followers
(The id corresponds to the username provided in the Github documentation)

Note: If you are unable to access all the users, it might be due to github API rate access limit. I have created and added client_id and client_secret to solve this. If the problem arise, which is unlikely, reach out to me.

