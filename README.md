
# Movie catalog

The movie catalog is the application that provides REST api for creation/update/deletetion of: 
- Movies
- Movie Directors
- Movie Ratings

The Application uses the in-memory relational database to persist data. 
To access the H2 console in the browser by invoking the URL http://localhost:8080/h2-console.

## Database Model
![](https://github.com/miteply/movieCatalogue/blob/main/uml_diagrams/database_diagram.png?raw=true)

## Class diagramm 
![](https://github.com/miteply/movieCatalogue/blob/main/uml_diagrams/diagramm_class.png?raw=true)


## Features

- Create, update and delete a Movie
- Add or delete a Rating to movie
- Create, update and delete a Director
- Add or delete a Director to Movie
- Search movies by Director
- Search movies where total ratings are above a provided number of ratings
- Search movies where the rating score is above a provided score.


## Technologies

 - Java 8
 - Maven 4.0.0
 - Spring boot 2.5.2
 - H2 Database


 ## Project Structure
- Classes Movie,Rating and Director are Entitties which are corresponding to the relational database tables.
 - Classes MovieRequest,RatingRequest,DirectorRequest,MovieResponse,RatingResponse,DirectorResponse are used by controllers to transfer data between theiself and server.
- Class ObjectConverter converts from Entity to DTO and from DTO to Entity
- Interfaces MovieRepository,RatingRepository,DirectorRepository are extending JpaRepository which provides methods to interact with the database like inserting, updating, and deleting data.
- Services MovieService,RatingService,DirectorService are referring repository methods and custom finder methods to manage business logic and will be injected in controllers.
- Controllers: MovieController,RatingController,DirectorControllers have request mapping methods for REST request such as findAll,findById, deleteById
- ExceptionHandlerController allows to handle exceptions across the whole application in one global handling component.



# APIs
#####  MovieController:
#
| Methods                                   | Urls                       | Actions 
| :------------                             |:---------------            |:-----   
| POST     | /api/v1/movies                 | create a new movie         |
| GET      | /api/v1/movies                 | retrieve all movies        |
| GET      | /api/v1/movies/{id}            | retrieve the movie by id   |
| GET      | /api/v1/movies/?title=:title   | retrieve the movie by title|
| PUT      | /api/v1/movies/{id}            | update the movie by id, passing in the body new values  |
| DELETE   | /api/v1/movies/{id}   | delete the movie by id|
| DELETE   | /api/v1/movies   | delete all movies |
| GET   | /api/v1/movies/totalRatings/{numOfRatings}   | retrieve all movies with total ratings greater than provided |
| GET   | /api/v1/movies/greaterScore/{score}   | retrieve all movies with score  greater than provided |
| GET      | /api/v1/movies/directors/?firstname=:firstname&lastname=:lastname   | retrieve all movies by Director fullname|

#####  RatingController:
#
| Methods                                   | Urls                       | Actions 
| :------------                             |:---------------            |:-----   
| GET     | /api/v1/ratings/{id}           | retrieve rating by id       |
| PUT     | /api/v1/ratings/{id}            | update rating by id, passing in the body new values      |
| DELETE      | /api/v1/ratings/{id}         | delete rating by id |
| PUT      | /api/v1/ratings/movies/{movieId}   | add a new rating to existing movie|

#####  DirectorController:
#
| Methods                                   | Urls                       | Actions 
| :------------                             |:---------------            |:-----   
| POST     | /api/v1/directors              | create a new director         |
| PUT      | /api/v1/directors/{id}            | update director by id, passing in the body new values      |
| GET      | /api/v1/directors/{id}          | retrieve director by id         |
| GET      | /api/v1/directors                | retrieve all directors   |
| DELETE   | /api/v1/directors/{id}         | delete director by id|
| DELETE   | /api/v1/directors              | delete all directors |
| PUT      | /api/v1/directors/{id}/movies/{movieId}            | add director to movie |
| DELETE    | /api/v1/directors/{id}/movies/{movieId}            | remove director from movie |

### Installation && Tests


## Run Locally

Clone the project

```bash
  git clone https://github.com/miteply/movieCatalogue.git
```

Go to the project directory

```bash
  cd movie_catalogue
```

Open command terminal and run the application by using

```bash
  .\mvnw spring-boot:run
```

Verify the deployment by navigating to your server address in
your preferred browser.

```sh
http://127.0.0.0:8080
```

## Testing with POSTMAN
Test in POSTMAN by importing the [json file](https://github.com/miteply/movieCatalogue/blob/main/uml_diagrams/movie_catalogue.postman_collection.json) 