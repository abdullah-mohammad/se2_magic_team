<template>
  <div class="container">
    <div class="container">
      <div v-if="currentUser" class="main-body">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="main-breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="index.html">Home</a></li>
            <li class="breadcrumb-item"><a href="javascript:void(0)">User</a></li>
            <li class="breadcrumb-item active" aria-current="page">User Profile</li>
          </ol>
        </nav>
        <!-- /Breadcrumb -->

        <div class="row gutters-sm">
          <div class="col-md-4 mb-3">
            <div class="card">
              <div class="card-body">
                <div class="d-flex flex-column align-items-center text-center">
                  <img onerror='this.onerror=null;this.src="https://via.placeholder.com/150"'
                       :src="getUserPicture(user.profilepicture)" alt="Profile picture" class="rounded-circle"
                       width="150">
                  <div class="mt-3">
                    <h4>{{ user.username }}</h4>
                    <a :href="'edit-profile/'+ user.id" class="btn btn-primary">Edit</a>
                    &nbsp;
                    <button class="btn btn-outline-danger" @click="deleteUser">Delete</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-8">
            <div class="card mb-3">
              <div class="card-body">
                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">Full Name</h6>
                  </div>
                  <div class="col-sm-9 text-secondary">
                    {{ user.firstname }} {{ user.lastname }}
                  </div>
                </div>
                <hr>
                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">Email</h6>
                  </div>
                  <div class="col-sm-9 text-secondary">
                    {{ user.email }}
                  </div>
                </div>
                <hr>
                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">Gender</h6>
                  </div>
                  <div class="col-sm-9 text-secondary">
                    {{ user.gender }}
                  </div>
                </div>
                <hr>
                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">Birthdate</h6>
                  </div>
                  <div class="col-sm-9 text-secondar" type="date" >
                           {{ user.birthdate }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card mt-3">
            <ul class="list-group list-group-flush">
              <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">

                <a :href="'myitems/'" class="text-secondary">My tools</a>
              </li>
              <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">

                <a :href="''" class="text-secondary">borrowed</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapActions, mapState} from 'vuex';
import userDataService from '../../services/UserDataService'

const API_IMG_RESOURCE = process.env.VUE_APP_API_URL+"users/get-img/";

export default {
  name: "Profile",
  data(){
    return {
     // birthdate :this.currentUser.birthdate,
       dateVal : new Date(),
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    ...mapState('user', {
      user: state => state.user
    }),
  },
  methods: {
    ...mapActions({
      setCurrentUser: "user/setCurrentUser",
    }),
    deleteUser() {
      userDataService.delete(this.currentUser.id)
          .then(() => {
            this.$router.push({name: "login"});
          })
          .catch((e) => {
            console.log(e);
          });
    },
    getUserPicture(img) {

      if (img != undefined) {
        // TODO es muss das Bild richtig gelesen werden.
        // ich habe damit auch versucht es wird daten geliefert aber nicht geklappt
        // wenn man das Bild in img zeigen möchte.
        // userDataService.getPicture(img);
        console.log(`${API_IMG_RESOURCE}${img}/`);
        return `${API_IMG_RESOURCE}${img}/`
      }
    },
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    }
    this.setCurrentUser(this.currentUser.id);
  },
};
</script>
