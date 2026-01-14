# Internship Recommendation Engine  

A **Smart India Hackathon (SIH) project** that combines **React.js frontend**, **Spring Boot backend**, and a **Django + Machine Learning model** to provide personalized internship recommendations for students.  

The system collects user preferences, applies machine learning models (Logistic Regression), and integrates eligibility filters to recommend the most suitable internships. It is designed to be scalable, accessible, and continuously improving with upcoming features like voice assistance, multilingual support, and advanced ranking mechanisms.

---

## Features

### Current Features
-  **User Preference Collection** – Collect user preferences through an intuitive graphical interface.  
-  **Personalized Internship Recommendations** – Suggest internships based on past applications and user behavior across the platform.  
-  **Internship Ranking System**– Uses a machine learning model (logistic regression) to analyze how well the current user-internship details match with past applications and ranks internships accordingly.
-  **Eligibility Filtering** – Filters internships based on qualification, age, and gender.
-  **Advanced Eligibility Filtering** – Skill-based compatibility checks between user skills and internship requirements.
-  **Database Integration** – Efficient storage and retrieval of user, internship, and recommendation data.  
-  **Preference-Based Scoring** - Based on applied count, mode preference, distance between preferred location and internship location, posting time.
---

###  Upcoming Features
-  **Voice Assistance** – Collect preferences through voice input and provide voice-over for internship descriptions.  
-  **Multilingual Support** – Text and voice assistance in multiple languages for better accessibility.  
-  **Feedback-Based Recommendations** – Refined internship suggestions based on user feedback and past choices.  
-  **Additional Preference-Based Scoring** – Based employment type. 
-  **Score Normalization & Hybrid Ranking** – Normalize and combine multiple factors (preference score, ML regression score, feedback score) for improved ranking accuracy.  

---

## Setting up the Project  

&nbsp;1.  Clone the repository

```bash
git clone https://github.com/ramya1106/internship-recommendation-engine

```
&nbsp;2. Navigate to the internship-recommendation-engine folder
```bash
cd internship-recommendation-engine

```
---

## Frontend (React JS)  

### Prerequisites  

Before running the project, make sure you have:  
- [Node.js](https://nodejs.org/) (v18+ recommended)  
- npm (comes with Node.js) or [yarn](https://yarnpkg.com/)  

If you don’t have Node installed:  

&nbsp;&nbsp;&nbsp; 1. Download from [Node.js official site](https://nodejs.org/)  
&nbsp;&nbsp;&nbsp; 2. Install it (npm will be installed automatically)  
&nbsp;&nbsp;&nbsp; 3. Verify installation:  

```bash
node -v
npm -v
```

### Setup Instructions
&nbsp;1. Move to the frontend folder

```bash
cd frontend
```

&nbsp;2.  Install dependencies 

```bash
npm install
```

    (This will pull everything listed in `package.json`, including react, react-router-dom, react-icons, etc.)  

&nbsp;3.  Run the development server 

```bash
npm start
```

The app will be available at: http://localhost:3000

---

### Build for Production  

To generate an optimized production build:  

```bash
npm run build
```

---

### Integration Notes

- This frontend consumes APIs from the Spring Boot backend and the Python ML model.
- Make sure the backend services are running before using the full flow.

---
## Database Creation 
&nbsp;1. Create a new MySQL database:  

```sql
CREATE DATABASE <your_db_name>;
```
---
## Backend (Spring Boot)  

### Prerequisites  
Before running the project, make sure you have:  
- Java 17+  (If you don't have Java installed, download from [JDK_Download](https://www.oracle.com/in/java/technologies/downloads/))
- Maven 3.9+ (If you don't have Maven installed, download from [Maven_Download](https://maven.apache.org/download.cgi)) 
- MySQL Server & MySQL Workbench (If you don't have MySQL installed, download from [MySQL_Download](https://www.mysql.com/downloads/))
- IntelliJ IDEA (with **Lombok** plugin installed)

  **Notes**
  - Don't install *Maven Daemon*, install *Maven*
---
### Setup Instructions
&nbsp;1. Navigate to:

    internship-recommendation-engine/recommendation_system/com/example/src/main/resources


&nbsp;2. Create a new file named `application-local.properties` and copy the contents from `application.properties`.

Update it like this:

    spring.application.name=recommendation_system

    # MySQL Database Configuration
    spring.datasource.url=jdbc:mysql://localhost:3306/<mysql_database_name>
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    spring.datasource.username=<mysql_user_name>
    spring.datasource.password=<mysql_password>

    # JPA / Hibernate
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    # Run ONLY for first-time DB initialization
    # spring.sql.init.mode=always


**Notes**

- Replace `<mysql_database_name>`, `<mysql_user_name>`, and `<mysql_password>` with your MySQL credentials.
- On the first run, tables will be created automatically.
- If initializing data: uncomment spring.sql.init.mode=always for the second run only, then comment it again.

&nbsp;3. Move to reccomendation_system folder

    cd reccomendation_system


&nbsp;4. Build with Maven

    mvn clean install


&nbsp;5. Run the application

    mvn spring-boot:run "-Dspring-boot.run.profiles=local"

---

### API Endpoints  

| Method | Endpoint | Request Body | Description |
|--------|-----------|-----------|-------------|
| GET    | `/users/{userId}` |-| Fetch user details |
| GET    | `/internship/details/{internshipId}` |-| Internship details |
| POST    | `/internships/filtered/{userId}` |{"preferred_mode": "[preferred_mode]","preferred_city": "[city_name]","preferred_state": "[state_name]"}| Get top 5 internships for the user|


The app will be available at: http://localhost:8080

---

## ML Model (Django + ML)

## Prerequisites

Before running the ML model, make sure you have:

- Python 3.10+

---

### Setup Instructions

&nbsp;1. Move to ml_model folder

    cd ml_model


&nbsp;2. (Optional) Create a Virtual Environment

    python -m venv venv
    # Activate venv
    # Linux/Mac
    source venv/bin/activate
    # Windows
    venv\Scripts\activate


&nbsp;3. Install dependencies

    python -m pip install --upgrade pip
    pip install -r requirements.txt


&nbsp;4. Configure MySQL Database

&nbsp;Update settings.py:

    DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': '<mysql_database_name>',
        'USER': '<mysql_username>',
        'PASSWORD': '<mysql_password>',
        'HOST': 'localhost',
        'PORT': '3306',
    }
}

**Notes**

- Replace `<mysql_database_name>`, `<mysql_user_name>`, and `<mysql_password>` with your MySQL credentials.

&nbsp;5. Apply Migrations

    python manage.py makemigrations
    python manage.py migrate


&nbsp;6. Generate Sample Data

    python generate_applications.py


&nbsp;7. Train the ML Model

    python manage.py shell


&nbsp;Inside the shell:

    from recommend_internship.ml.trainer import train_model
    train_model()
    exit()


&nbsp;8. Test with Dummy API Call

    python test_batch_api.py


&nbsp;9. Run the Development Server

    python manage.py runserver


The app will be available at http://127.0.0.1:8000/


---

## Running Order

To run the complete system, start in this sequence:

&nbsp;1. ML Model (Django)

    python manage.py runserver


&nbsp;2. Backend (Spring Boot)

    mvn spring-boot:run "-Dspring-boot.run.profiles=local"


&nbsp;3. Frontend (React.js)

    npm start
