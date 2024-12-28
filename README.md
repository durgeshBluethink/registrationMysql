"# Registration and Login System\n\nThis project is a registration and login system built using Spring Boot for the backend and HTML, CSS, and JavaScript for the frontend. It supports user registration, login, and stores user data in a MySQL database.\n\n## Table of Contents\n\n- [Project Overview](#project-overview)\n- [Features](#features)\n- [Installation](#installation)\n- [Usage](#usage)\n- [Technologies Used](#technologies-used)\n- [Contributing](#contributing)\n- [License](#license)\n- [Contact](#contact)\n\n## Project Overview\n\nThe registration and login system allows users to register with their details, log in with their credentials, and store their information in a MySQL database. It includes CORS configurations to enable cross-origin requests and secure user authentication using Spring Security.\n\n## Features\n\n- User Registration\n- User Login\n- Password Encryption\n- Cross-Origin Resource Sharing (CORS) Configuration\n- Secure Authentication with Spring Security\n\n## Installation\n\n### Prerequisites\n\n- Java 11 or higher\n- Maven\n- MySQL\n- Node.js (for running a local server if needed)\n\n### Setup\n\n1. **Clone the Repository**:\n   ```bash\n   git clone https://github.com/durgeshBluethink/registrationMysql.git\n   cd registrationMysql\n   ```\n\n2. **Backend Setup**:\n   - Update `application.properties` with your MySQL database credentials.\n   - Run the following commands to set up and start the Spring Boot application:\n     ```bash\n     mvn clean install\n     mvn spring-boot:run\n     ```\n\n3. **Frontend Setup**:\n   - Navigate to the `src/main/resources/static` directory.\n   - Serve the static files using a local server:\n     ```bash\n     http-server\n     ```\n\n## Usage\n\n### Register a User\n\n1. Open `http://127.0.0.1:8080/registration.html` in your browser.\n2. Fill out the registration form and submit.\n\n### Log In\n\n1. Open `http://127.0.0.1:8080/login.html` in your browser.\n2. Enter your registered email and password, and submit the login form.\n\n## Technologies Used\n\n- **Backend**: Spring Boot, Spring Security, JPA, MySQL\n- **Frontend**: HTML, CSS, JavaScript\n- **Tools**: Maven, Git, Node.js\n\n## Contributing\n\nContributions are welcome! Please fork the repository and submit a pull request.\n\n## License\n\nThis project is licensed under the MIT License.\n\n## Contact\n\nFor any questions or suggestions, please contact:\n- **Name**: Durgesh\n- **Email**: [your-email@example.com]" 
