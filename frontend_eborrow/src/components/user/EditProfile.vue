<template>
    <div>
        <div class="container">
      <div v-if="currentUser" class="main-body">
            <!-- Breadcrumb -->
            <nav aria-label="breadcrumb" class="main-breadcrumb">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="index.html">Home</a></li>
                <li class="breadcrumb-item"><a href="javascript:void(0)">User</a></li>
                <li class="breadcrumb-item active" aria-current="page">Edit User Profile</li>
              </ol>
            </nav>
            <!-- /Breadcrumb -->
      
            <div class="row gutters-sm">
              <div class="col-md-4 mb-3">
                <div class="card">
                  <div class="card-body">
                    <div class="d-flex flex-column align-items-center text-center">
                      <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
                      <div class="mt-3">
                        <input type="email" class="form-control text-center" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="user.username"
                          > <br>
                        <h4>{{currentUser.username}}</h4>
                        <a :href="'edit-profile/' + currentUser.id" class="btn btn-primary">Edit</a>
                        &nbsp;
                        <button class="btn btn-outline-danger">Delete</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-md-8">
                <div class="card mb-3">
                  <div class="card-body">
                    <form @submit.prevent="handleEditUser">
                        <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Firstname</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" id="" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="user.firstname"
                            >
                        </div>
                        </div>
                        <hr>
                        <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Lastname</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" id="" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="user.lastname"
                            >
                        </div>
                        </div>
                        <hr>
                        <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Email</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="user.email"
                            >
                        </div>
                        </div>
                        <hr>
                         <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Old password</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                          <input v-model="oldpass" type="password" class="form-control" placeholder="Enter old password">
                          <div class="input-group-append">
                            <button class="btn btn-outline-default" type="button" @click="handleCheckOldPass">OK</button>
                          </div>
                        </div>
                        </div>
                        <hr>
                         <div v-if="oldpassValid" class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">New password</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                          <input v-model="newpass" type="password" class="form-control" placeholder="Enter old password">
                        </div>
                        </div>
                        <div v-if="!oldpassValid && isChecked" class="text-danger">Your old password doesn't match!</div>
                        <hr>
                        <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Gender</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="user.gender"
                            >
                        </div>
                        </div>
                        <hr>
                        <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Birthdate</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="user.birthdate"
                            >
                        </div>
                        </div>
                        <p v-if="errMsge" class="text-danger">{{errMsge}}</p>
                        <input type="submit" class="btn btn-success" value="Save">
                        &nbsp;
                        <a @click="goBack()" class="btn btn-primary">Back</a>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else>
            {{errMsge}}
          </div>
      </div>
    </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import User from '../../models/User';
import userDataService from "../../services/UserDataService";

export default {
    name: "edit-profile",
    data() {
        return {
            errMsge: "",
            oldpass:"",
            oldpassValid: false,
            isChecked:false,
            newpass: ""
        }
    },
    computed: {
        currentUser() {
            return this.$store.state.auth.user;
        },
        ...mapState('user', ['user'])
    },
    methods: {
        ...mapActions({
            logout: "auth/logout",
            login: "auth/login",
            setCurrentUser: "user/setCurrentUser",
            editUser: "user/editUser"
        }),
        /* handleEditUser() {
            const pass = this.newpass ? this.newpass : this.user.password;
            const user = new User(this.user.username, pass, this.user.firstname, this.user.lastname, this.user.email, this.user.gender, this.user.profilepicture, this.user.birthdate)
            this.editUser(user);
        } */
        handleEditUser() {
            const pass = this.newpass != "" ? this.newpass : this.user.password;
            const user = new User(this.user.username, pass, this.user.firstname, this.user.lastname, this.user.email, this.user.gender, this.user.profilepicture, this.user.birthdate)
            const editUserPass = this.newpass != "" ? true : false;
            const data = {...user, editPass: editUserPass}
            userDataService.editUser(this.currentUser.id, data)
                .then(res => {
                    console.log(res.data)
                    this.$router.push("/profile");
                })
                .catch(e => this.errMsge = e)
        },
        goBack() {
          this.$router.push("/profile")
        },
        handleCheckOldPass() {
          this.isChecked=true
          userDataService.checkUserPass(this.currentUser.id, this.oldpass)
            .then(res => {
              console.log(res)
              this.oldpassValid = res.data
            })
            .catch(e => this.errMsge = e)
        }
    },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    }
    this.setCurrentUser(this.currentUser.id)
    this.isChecked = false
    this.newpass = ""
  },
}
</script>