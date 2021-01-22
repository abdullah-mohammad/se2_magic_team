import User from "../models/user";
import userDataService from "../services/UserDataService";

export const user = {
  namespaced: true,
  state: {
      user: User,
      clonedUserPic: null,
      errMsg: ""
  },
  mutations: {
    SET_CURRENT_USER(state, editedUser) {
      state.user = editedUser
      state.clonedUserPic = editedUser.profilepicture
    },
    SET_ERR_MSG(state, msg) {
        state.errMsg = msg
    }, 
    logout(state){
      state.user = undefined;
    }
  },
  actions: {
    setErrMsg(context, msg) {context.commit('SET_ERR_MSG', msg)},  
    editUser(context, editedUser) {
      userDataService.editUser(editedUser.id, editedUser)
        .then(res => {
            context.commit('SET_CURRENT_USER', editedUser)
            return Promise.resolve(res.data)
        })
        .catch(err => {
            console.log("error", err)
            return Promise.reject(err)
        })
    },
    setCurrentUser(context, id){
        userDataService.getUser(id)
            .then(res => {
                context.commit('SET_CURRENT_USER', res.data)
                return Promise.resolve(res.data)
            })
            .catch(err => {
                context.setErrMsg(err)
                return Promise.reject(err)
            })
    },
    deleteUser({ commit }) {
      commit('logout');
    },
  },
  getters: {
    getCurrentUser: state => {
      return state?.user;
    }
  }
};
