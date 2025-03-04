# exchange-rate-app

## Running the Application with Docker

To run both the backend and frontend applications using Docker, follow the steps below:

### Prerequisites

Ensure you have Docker installed on your machine.

### Running the Backend (API)

To run the backend API in a Docker container, use the following command:

```bash
docker run -p 8081:8081 alexisal/exrate-back
```

- This will run the backend application on port `8081` of your host machine.

### Running the Frontend (UI)

To run the frontend application in a Docker container, use the following command:

```bash
docker run -p 8080:80 alexisal/exrate-front
```

- This will run the frontend application on port `8080` of your host machine.
### Accessing the Application

Once the containers are running, you can access the web applications through [http://localhost:8080](http://localhost:8080).

More details about the backend and the frontend applications can be found in the corresponding README.md files:
* [backend/README.md](backend/README.md)
* [frontend/README.md](frontend/README.md)