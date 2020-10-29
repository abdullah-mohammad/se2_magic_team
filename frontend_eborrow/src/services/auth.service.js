import {http} from "../http-common";

class AuthService {
  login(user) {
    return http
      .post('users/signin', {
        username: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }

  register(user) {
    return http.post('users/signup', {
      username: user.username,
      password: user.password
    });
  }
}

export default new AuthService();
