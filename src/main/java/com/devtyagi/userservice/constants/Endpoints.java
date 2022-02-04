package com.devtyagi.userservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class Endpoints {

    public static final String BASE_URL = "/api/v1";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AuthAPI {

        public static final String AUTH_BASE_URL = "/auth";

        public static final String LOGIN_USER = AUTH_BASE_URL + "/login";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserAPI {

        public static final String USER_BASE_URL = "/user";

        public static final String CREATE_USER = USER_BASE_URL + "/create";

        public static final String GET_USERS = USER_BASE_URL + "/all";

        public static final String DELETE_USER = USER_BASE_URL + "/delete/{id}";

        public static final String ACTIVATE_USER = USER_BASE_URL + "/activate";

    }

}
