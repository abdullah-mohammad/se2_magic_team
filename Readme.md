# Eborrow 

## Usage 

You need to start both the backend and the frontend individually.
For the Backend, you need to `gradle bootRun` in the root repo to start the api on Port 8080.

For the Frontend, you first need to `npm install`(only once) in the the folder `frontend_eborrow` and then `npm run
 serve`. This will
 start the Vue Frontend on Port 8081. 
  
 When both are running, you can open your browser at `localhost:8081` to see the App.

 Access to the database - localhost:8080/h2-console
 
 Username:sa 

 Password should be left blank.
 
 JDBK URL: jdbc:h2:mem:eborrow