# MediCore HMS

MediCore HMS is a beginner-friendly Java web project built with JSP, Servlets, JDBC, and MVC architecture. The code stays close to classroom concepts from Week 2 to Week 6 and focuses on the core milestone features:

- Login
- Register
- Dashboard
- Basic admin, doctor, and patient flows

## Project Structure

```text
DSA/
|-- database/
|   |-- schema.sql
|   `-- sample-data.sql
|-- src/
|   `-- main/
|       |-- java/
|       |   |-- controller/
|       |   |-- dao/
|       |   |-- filter/
|       |   |-- model/
|       |   |-- service/
|       |   `-- util/
|       `-- webapp/
|           |-- css/
|           |-- js/
|           |-- WEB-INF/
|           |   |-- lib/
|           |   `-- views/
|           |       |-- admin/
|           |       |-- auth/
|           |       |-- common/
|           |       |-- doctor/
|           |       `-- patient/
|           |-- about.jsp
|           |-- contact.jsp
|           `-- index.jsp
`-- build/
```

## MVC Mapping

- Model: JavaBeans inside `src/main/java/model`
- View: JSP files inside `src/main/webapp` and `src/main/webapp/WEB-INF/views`
- Controller: Servlets inside `src/main/java/controller`

## Database Setup

1. Create the database by running `database/schema.sql`.
2. Optional: load demo records by running `database/sample-data.sql`.

The project uses MySQL and connects with these default values in `src/main/java/dao/DBConnection.java`:

- Database URL: `jdbc:mysql://localhost:3306/medicore_db?useSSL=false&serverTimezone=UTC`
- Username: `root`
- Password: empty string

You can also override them with environment variables:

- `MEDICORE_DB_URL`
- `MEDICORE_DB_USER`
- `MEDICORE_DB_PASSWORD`

## Demo Credentials

After running only `schema.sql`:

- Admin email: `admin@medicore.com`
- Admin password: `Admin@123`

After running `sample-data.sql` as well:

- Doctor email: `doctor@medicore.com`
- Doctor password: `Doctor@123`
- Patient email: `patient@medicore.com`
- Patient password: `Patient@123`

You can also test the register feature by creating a new patient account from the registration page.

## Core Features Included

- User login with database validation
- Patient registration
- Role-based dashboards for admin, doctor, and patient
- Admin doctor management
- Admin patient management
- Department management
- Appointment booking and status update
- Medical records and billing screens

## Notes For Milestone Submission

- Static files are separated inside `webapp/css` and `webapp/js`
- Dynamic JSP files are separated inside `webapp/WEB-INF/views`
- JDBC is used directly for database operations
- JavaBeans are used for model classes
- Servlets handle request processing and page navigation
