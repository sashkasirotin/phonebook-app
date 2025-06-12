# phonebook-app
phone-book api for home assignment
in this phonebook app i created a service for crud operation on contacts.
atached relvant
-files postman collection to run the api's,postman env for token veraible,contact api swagger.yaml

main dev points:
- created a backend service .
- integration with postgres db.
- created contact table to preform the croud operation on;
- created useres table for saving user for authentication & authorization porpesus
- ran postgres on docker as i had problems runing it on my local.
- created a docker compose that contains the app and the postgress db.
- created postman collection to call the crud and user creation endpoints.
- created a swagger yaml describing the api for documentation.
- integrated open api swagger-ui for documentation purpesus






instructions of runing the flow 
-run create user password api add role as "USER_ROLE" can also skip and use user:alexander and password:siro1
-run create authentication with meantioned user and pass in body; and a token would be created;
- add token in next requests headers in the form of Bearer {{token}}
- can add contact api with authorization in header and body with all fileds-get response 200;
- run search contact by name full or partial-get response 200;
- run update get response 200;
- run search get response 200;
- run delete get response 200;
- run get contact by id get response 200 with contact details;
- run contactlist with pagination with a param for page int and size of values you want to see on the response get 200;









 -sources: youtube,stack overflow,(https://docs.spring.io/),https://www.linkedin.com/pulse/use-postgresql-docker-access-from-intellij-idea-sanjay-shah-yyz6f/,docs.docker.com,geeksforgeeks website etc..
