import AuthService from '../services/AuthService';
import userDataService from '../services/UserDataService';

const user = JSON.parse(localStorage.getItem('user'));
const initialState = {user}

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit, dispatch }, user) {
      return AuthService.login(user).then(
        user => {
          commit('loginSuccess', user);
          dispatch('setLoggedUserPass');
          return Promise.resolve(user);
        },
        error => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }) {
      AuthService.logout();
      commit('logout');
    },
    async setLoggedUserPass(context) {
      const res = await userDataService.getUser(context.state.user.id);
      context.commit('SET_LOGGED_USER_PASS', res.data);
      return Promise.resolve(res.data);
    }
    // register({ commit }, user){
    //   return AuthService.register(user).then(
    //     user => {
    //       return Promise.resolve(user)
    //     }
    //   )
    // }
  },
  mutations: {
    loginSuccess(state, user) {
      state.user = user;
    },
    loginFailure(state) {
      state.user = null;
    },
    logout(state) {
      state.user = null;
    },
    SET_LOGGED_USER_PASS(state, pass) {
      state.user = {...state.user, password: pass}
    }
  }
};
