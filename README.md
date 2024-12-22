# RideEase - Ride Booking Application (Spring Boot)

RideEase is an innovative ride-booking platform tailored to effortlessly connect travellers with reliable transportation solutions. Whether you're a commuter seeking a hassle-free ride or a driver aiming to maximize earnings, RideEase provides a seamless, intuitive, and secure environment for all. With its robust multi-user system, dedicated admin panels, and user-friendly features, RideEase transforms the traditional ride-hailing experience into a smarter and more efficient journey. By focusing on convenience, reliability, and community, RideEase redefines urban mobility for everyone involved.


# Live Demo | API Documentation(Swagger)


# Key Features of RideEase:

- **Multi-Driver Support**: A dynamic platform where multiple drivers can register and offer their services, giving riders access to a variety of transportation options.

- **Role-Based Access Control**: Securely manage different user roles (admin, driver, passenger) to ensure a controlled and safe environment for all participants.

- **Real-Time Notifications**: Riders and drivers receive instant email notifications for ride requests, confirmations, cancellations, and updates.

- **Rating System**: Riders and drivers can provide ratings to maintain transparency and continually improve service quality.

- **Trip History**: Riders and drivers can view detailed records of past trips, including dates, destinations, and payments, for reference and convenience.

# Security Features:

- **Authentication**: JWT tokens are used for secure authentication and session management.
  
- **Authorization**: Role-based access control is implemented to manage permissions, ensuring that only authorized users can perform specific actions.
  
- **Data Encryption**: Sensitive data, including passwords and payment details, is encrypted to protect user information from unauthorized access.

# Technologies Used:

- **Back-End**: Spring Boot, Spring Security, Spring Data JPA
- **Database**: Postgres
- **Authentication**: JWT, Spring Security
- **Notifications**: Email notifications for ride confirmations and ride requests
- **APIs**: OSRM

# Project Description:
## Controllers:

- **Auth Controller**: Handles user authentication using JWT token for login and signup.
- **Driver Controller**: Allows drivers to manage actions such as accepting, cancelling, ending the ride, rating rider and getting historical rides.
- **Rider Controller**:  Allows riders to manage actions such as requesting ride, cancelling ride & ride request, rating driver and getting historical rides.


## Database Diagram:  
![Ride Booking Application (2)](https://github.com/user-attachments/assets/df111767-81ff-4b17-b8cd-f5b78e53a29e)

## Rough flow of the program(Ride booking to Ride completion):
![Blank board](https://github.com/user-attachments/assets/6f7109c9-f1f7-420e-8ebb-52c947720dcc)


# Why I Created This Project:
RideEase reflects my passion for coding and my commitment to developing practical solutions that can have a positive impact on users' daily lives. It’s a project that merges my technical skills with my enthusiasm for solving real-world problems in transportation.

# Getting Started:
## Prerequisites:
- **Java 17+**
- **Postgres**
- **Maven**
- **OSRM API**

## Installation and Setup
- **Clone the repository to your local computer**:
```
git clone https://github.com/Tagore-Duppala/RideBookingApplication.git
```

- **Configure the Database**:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username= your_username
spring.datasource.password= your_password
```

- **Access RideEase**:
```
Open your browser and go to http://localhost:8080
```

# Contribute:

Feel free to contribute to this project by submitting issues, suggesting features, or creating pull requests. Your input is valuable to make this tool even better! 😃

# Planned Future Enhancements:

- **Password Reset via Email**: Implementing a secure and convenient method for users to reset their passwords through email.
- **UI/UX Improvements**: Introducing a responsive and visually appealing frontend interface to complement the backend functionalities.
- **Payment service additions**: Adding more payment services using Razorpay API.
- **RideFareCalculationStrategy**: Adding more fare calculating strategies for more accurate pricing.
- **Review System Enhancements**: Improving the rating system to include reviews and more granular feedback options, verified reviews.
- **More Transport Modes**: Bringing more transportation modes, ride-sharing options and rentals.

# Feedback
I appreciate your feedback! Please open an issue on GitHub if you encounter any problems or have suggestions for improvement. Your feedback helps enhance the project for everyone. Please reach out to me at duppalatagore@gmail.com

