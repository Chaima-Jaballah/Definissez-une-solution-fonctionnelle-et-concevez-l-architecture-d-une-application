# Chat Feature POC – Car Rental Application  
**Your Car Your Way**

Steps to follow to run the project

---

## Technologies Used

- **Frontend**: Angular 19  
- **Backend**: Java Spring Boot 3.5  
- **Language**: Java 17  

---

## How to Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/Chaima-Jaballah/Definissez-une-solution-fonctionnelle-et-concevez-l-architecture-d-une-application.git
```

### 2. Frontend Setup

cd ../chatPOC_front

npm install

ng serve

The app will be available at http://localhost:4200/

Or:

ng serve -o



### 3. Backend Setup

cd ../ChatPOC_back


On Windows CMD:

set DB_USER=your_user

set DB_PASSWORD=your_password


On Linux/Mac:

export DB_USER=your_user

export DB_PASSWORD=your_password

mvn spring-boot:run

Make sure Java 17 and Maven are installed.

---
## Final Notes

- The frontend will call the backend APIs — make sure both are running.

- This is a Proof of Concept for the online chat feature of the car rental booking application.
