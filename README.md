# DeeSeeComics
A superhero distribution platform to keep track of employed superheroes 

###build application
`mvn clean package`

###Run application
`java -jar DeeSeeComics.jar`

###Docker
1. `docker build -t deeseecomics .`
2. `docker run -it -p 8080:8080 deeseecomics`

###Openapi link:
http://localhost:8080/api/swagger-ui/index.html

###Request examples

1. Find all stored superheroes

`curl --location --request GET 'http://localhost:8080/api/superhero'`
2. Find superheroes by superpower

`curl --location --request GET 'http://localhost:8080/api/superhero/by?superpowers=invulnerability'`

3. Find superheroes with encrypted identity

`curl --location --request GET 'http://localhost:8080/api/superhero/encrypted?shiftCharTo=3'`

4. Find superheroes with encrypted identity by two superpowers

`curl --location --request GET 'http://localhost:8080/api/superhero/encrypted/by?superpowers=healing&superpowers=speed&shiftCharTo=1'`

5. Save superhero

`curl --location --request POST 'http://localhost:8080/api/superhero' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "superman",
"identity": {
"firstName": "clark",
"lastName": "kent"
},
"birthday": "1977-04-18",
"superpowers": [
"flight",
"strength",
"invulnerability"
]
}'`
