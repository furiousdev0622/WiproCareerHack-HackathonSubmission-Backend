# API Documentation

There are 5 endpoints:
- Create User
- Login User
- Activate User
- Get All Users
- Delete User

# Create User

**Endpoint**: ```/api/v1/user/create```    
**Method**: POST

Create user permissions have been provided only to LEVEL2 and LEVEL3 users.

### Request Body
```
{
    "username": "john69",
    "fullName": "John Watson",
    "emailAddress": "tyagidev179@gmail.com",
    "role": "LEVEL1"
}
```

### Header
JWT Token of a LEVEL2 / LEVEL3 user has to be provided.    
```
Authorization: Bearer <jwt_token>
```

### Response
```
{
    "userId": "fabe4b18-94c5-4bc0-b86b-95428590e292",
    "username": "john69",
    "fullName": "John Watson",
    "emailAddress": "tyagidev179@gmail.com",
    "status": "PENDING",
    "role": "LEVEL1"
}
```

# Activate User

**Endpoint**: ```api/v1/user/activate```   
**Method**: PUT

### Request Body 

```
{
    "invitationCode": "431733c3-2530-4f6f-8bdb-1df05007acea",
    "password": "12345678"
}
```

### Response

```
{
    "userId": "fabe4b18-94c5-4bc0-b86b-95428590e292",
    "username": "john69",
    "emailAddress": "tyagidev179@gmail.com",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0eWFnaWRldjE3OUBnbWFpbC5jb20iLCJleHAiOjE2NDUxMTQ5MDUsImlhdCI6MTY0NDI1MDkwNX0.HKNflG7mjEk5Sq_pHDk7i6RlFksDf7XTdayrrr-ycHQ",
    "role": "LEVEL1"
}
```

# Login User

**Endpoint**: ```/api/v1/auth/login```    
**Method**: POST

### Request Body

```
{
    "emailAddress": "admin@devtyagi.com",
    "password": "12345678"
}
```

### Response

```
{
    "userId": "590d973a-6381-4a6c-bbb3-87bb02ae5f96",
    "username": "admin",
    "emailAddress": "admin@devtyagi.com",
    "accessToken": "eyJhbGahduJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBkZXZ0eWFnaS5axsyiLCJleHAiOjE2NDUxMDYwMTUsImlhdCI6MTY0NDI0MjAxNX0.e8EyM_WISJclZU_7CvFNJVepNjxGrmI4-uHBFba_7Ok",
    "role": "LEVEL3"
}
```

# Get All Users

**Endpoint**: ```/api/v1/user/all```    
**Method**: POST    
**Query Params**: ```?sort=<SORT BY>&order=<SORT DIRECTION>```    
- Accepted Values for ```sort```: "fullName", "username", "emailAddress", "status", "role"
- Accepted Values for ```order```: "asc", "desc"

### Header
JWT Token of 'Any User' is required, all roles have the permissions to read all users.
```
Authorization: Bearer <jwt_token>
```

### Response

```
[
    {
        "userId": "590d973a-6381-4a6c-bbb3-87bb02ae5f96",
        "username": "admin",
        "fullName": "Admin User",
        "emailAddress": "admin@devtyagi.com",
        "status": "ACTIVE",
        "role": "LEVEL3"
    },
    {
        "userId": "774454b4-9244-44e5-ae4a-7f2c5c5601e8",
        "username": "bruce00wayne",
        "fullName": "Bruce Wayne",
        "emailAddress": "191306@juitsolan.in",
        "status": "PENDING",
        "role": "LEVEL1"
    },
    {
        "userId": "b4cf70bc-f297-45e7-875f-026570596dd6",
        "username": "johnwatson123",
        "fullName": "John Watson",
        "emailAddress": "tyagidev179@gmail.com",
        "status": "ACTIVE",
        "role": "LEVEL2"
    }
]
```

# Delete User

**Endpoint**: ```/api/v1/user/delete/{id}```    
**Method**: DELETE

### Header
JWT Token of a LEVEL3 is required.    
Only a LEVEL3 user will be able to delete any user.
```
Authorization: Bearer <jwt_token>
```

### Response

200 OK