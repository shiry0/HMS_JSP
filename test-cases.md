# Test Cases

The table below is written for the MediCore HMS project. The `Actual Output` column is intentionally left as a fill-in field so you can update it after you run each test in your local environment.

| Test Case ID | Test Description | Input Data | Expected Output | Actual Output |
| --- | --- | --- | --- | --- |
| FT-01 | Verify user login with valid admin credentials | Email: `admin@medicore.com`, Password: `Admin@123` | User is authenticated and redirected to the admin dashboard | To be recorded during testing |
| FT-02 | Verify patient registration with valid details | Full name, email, phone, password, gender, date of birth, address with valid values | New patient account is created and user can log in with the new credentials | To be recorded during testing |
| FT-03 | Verify patient can book an appointment | Valid patient session, selected doctor, department, date, and time | Appointment is saved in the database and appears in appointment lists | To be recorded during testing |
| FT-04 | Verify admin can add a new department | Department name: `Cardiology`, description: valid text | Department is added successfully and displayed in the department list | To be recorded during testing |
| FT-05 | Verify admin can add a new doctor | Valid doctor name, email, phone, department, specialization, fee, password | Doctor record is created and visible in the manage doctors page | To be recorded during testing |
| FT-06 | Verify patient can view billing information | Logged-in patient account with existing bill | Billing page loads and shows total amount, paid amount, and due amount | To be recorded during testing |
| VT-01 | Validate login with empty fields | Email: empty, Password: empty | Login is rejected and validation message is shown | To be recorded during testing |
| VT-02 | Validate registration with invalid email format | Email: `abc123`, other fields valid | Registration is rejected and email validation error is displayed | To be recorded during testing |
| VT-03 | Validate registration with short or weak password | Password shorter than required or invalid format | Registration is rejected and password validation message is shown | To be recorded during testing |
| VT-04 | Validate appointment booking with missing required fields | Missing doctor, date, or time | Appointment is not saved and user sees an error message | To be recorded during testing |
| ET-01 | Verify system behavior when duplicate email is used during registration | Register using an email already stored in the database | Record is not inserted and a duplicate-account error message is shown | To be recorded during testing |
| ET-02 | Verify login behavior when database connection is unavailable | Stop MySQL and submit valid login credentials | System handles the error safely and shows failure or cannot authenticate the user | To be recorded during testing |
| ET-03 | Verify unauthorized page access without login | Open `/admin/dashboard` or `/patient/dashboard` directly without session | User is redirected to login page or access is denied | To be recorded during testing |
| ET-04 | Verify patient cannot pay another patient's bill | Logged-in patient tries to access a bill that belongs to a different patient | Payment is blocked and unauthorized action is prevented | To be recorded during testing |
| UI-01 | Verify login page layout and form visibility | Open login page in browser | Login form, headings, buttons, and links are aligned and readable | To be recorded during testing |
| UI-02 | Verify dashboard cards and tables display properly | Log in as admin and open dashboard | Statistic cards, tables, and action buttons appear properly without broken layout | To be recorded during testing |
| UI-03 | Verify navigation works across key pages | Click Home, Login, Register, Dashboard, Logout links | Each navigation item opens the correct page without layout breakage | To be recorded during testing |
| UI-04 | Verify CSS styling loads correctly | Open public page and dashboard page | Styles, spacing, colors, and buttons load correctly without raw unstyled HTML | To be recorded during testing |

## Coverage Summary

- Functional testing: `FT-01` to `FT-06`
- Validation testing: `VT-01` to `VT-04`
- Exception handling testing: `ET-01` to `ET-04`
- UI testing: `UI-01` to `UI-04`
