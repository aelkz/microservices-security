## Banking Account records

### API Endpoints

| Método | URI | Descrição |
| ------ | --- | ---------- |
| GET    |/v2/api-docs    | swagger json |
| GET    |/swagger-ui.html| swagger html |
| GET    |/actu`ator/info  | info / heartbeat - provided by boot |
| GET    |/actuator/health| application health - provided by boot |
| GET    |/v1/extract/{id}| get chef by id |
| GET    |/v1/analytic-extract| get N chefs with an offset|
| POST   |/v1/custom-extract| add/update chef|

### Development Environment
#### H2 console
http://localhost:8080/h2-console
