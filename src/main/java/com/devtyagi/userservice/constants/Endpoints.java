package com.devtyagi.userservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class Endpoints {

    // Base URL for all endpoints
    public static final String BASE_URL = "/api/v1";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AuthAPI {

        // Base URL for Auth APIs
        public static final String AUTH_BASE_URL = "/auth";

        // Login Endpoint
        public static final String LOGIN_USER = AUTH_BASE_URL + "/login";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserAPI {

        // Base URL for User APIs
        public static final String USER_BASE_URL = "/user";

        // [POST] Create User Endpoint
        public static final String CREATE_USER = USER_BASE_URL + "/create";

        // [GET] Get all users Endpoint
        public static final String GET_USERS = USER_BASE_URL + "/all";

        // [DELETE] Delete user endpoint
        public static final String DELETE_USER = USER_BASE_URL + "/delete/{id}";

        // [PUT] Activate (Update Status and Password) Endpoint.
        public static final String ACTIVATE_USER = USER_BASE_URL + "/activate";

    }

}
