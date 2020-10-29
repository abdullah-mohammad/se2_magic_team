import axios from "axios";
import store from "./store";

export const http = axios.create({
  baseURL: "http://localhost:8080/",
  headers: {
    "Content-type": "application/json",
  },
});

http.interceptors.request.use(
  function(config) {
    // Do something before request is sent
    if (store.state.auth.user) {
      const token = store.state.auth.user.accessToken;
      config.headers.Authorization = "Bearer " + token;
    }
    return config;
  },
  function(error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

http.interceptors.response.use(
  function(response) {
    return response;
  },
  function(error) {
    if (403 === error.response.status) {
      window.location = "/login";
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);
