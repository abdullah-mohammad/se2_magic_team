<template>
    <div>
        <div class="container">
      <div class="main-body">
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
                            <h6 class="mb-0">Full Name</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" id="" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="currentUser.firstname"
                            > <br>
                            <input type="text" class="form-control" id="" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="currentUser.lastname"
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
                                v-model="currentUser.email"
                            >
                        </div>
                        </div>
                        <hr>
                        <div class="row">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Gender</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"
                                v-model="currentUser.gender"
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
                                v-model="currentUser.birthdate"
                            >
                        </div>
                        </div>
                        <p v-if="errMsge" class="text-danger">{{errMsge}}</p>
                        <input type="submit" class="btn btn-success" value="Save">
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
      </div>
    </div>
</template>

<script>
import { mapActions } from 'vuex';
import User from '../../models/user';
import userDataService from "../../services/UserDataService";

export default {
    name: "edit-profile",
    data() {
        return {
            errMsge: ""
        }
    },
    computed: {
        currentUser() {
            return this.$store.state.auth.user;
        },
    },
    methods: {
        ...mapActions({
            logout: "auth/logout",
            login: "auth/login",
        }),
        handleEditUser() {
            const user = new User(this.currentUser.username, this.currentUser.password, this.currentUser.firstname, this.currentUser.lastname, this.currentUser.email, this.currentUser.gender, this.currentUser.profilepicture, this.currentUser.birthdate)
            console.log("BEISPIEL" , user)
            userDataService.editUser(this.currentUser.id, user)
                .then(res => {
                    console.log(res.data)
                    this.$router.push("/profile");
                })
                .catch(e => this.errMsge = e)
        }
    },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    }
  },
}
</script>