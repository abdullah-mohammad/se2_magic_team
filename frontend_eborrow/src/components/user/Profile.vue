<template>
  <div class="container">
        <div v-if="currentUser" class="main-body">
            <!-- Page Heading -->
            <h2 class="my-4 gs-title">My profile: </h2>
            <!-- Page Heading -->

            <div v-if="user" class="row gutters-sm">
              <!-- Profile-Image -->
              <div class="col-md-3 mb-3 mr-0">
                  <div class="card m-0 p-0"  style="border: none;">
                    <div class="card-body m-0 p-0">
                      <div class="d-flex flex-column align-items-center text-center">
                        <img onerror='this.onerror=null;this.src="https://via.placeholder.com/150"'
                            :src="getUserPicture(user.profilepicture)" alt="Profile picture"
                            class="img-thumbnail"
                            width="230">
                        <div class="mt-3">
                          <a :href="'edit-profile/'+ user.id" class="btn btn-sm btn-primary pt-1 pb-1 pl-5 pr-5" style="font-family: 'GoShareFont'; background: #539AC5; border-radius: 12px; font-weight: 600; letter-spacing:1.5px; border:none">Edit</a>
                        </div>
                      </div>
                      
                    </div>
                  </div>
              </div>
              <!-- Profile-Image -->
              
              <!-- Infos-Block -->
              <div class="col-md-8 ml-0" style="font-family: 'GoShareFont'">
                <div class="card mb-3">
                  <div class="card-body">
                    <div class="row">
                      <div class="col-sm-3">
                        <h6 class="mb-0">Username</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        {{ user.username }}
                      </div>
                    </div>
                    <hr>
                    <div class="row">
                      <div class="col-sm-3">
                        <h6 class="mb-0">Firstname</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        {{ user.firstname }}
                      </div>
                    </div>
                    <hr>
                    <div class="row">
                      <div class="col-sm-3">
                        <h6 class="mb-0">Lastname</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        {{ user.lastname }}
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
                      <div class="col-sm-9 text-secondary" type="date" >
                              {{ user.birthdate }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- Infos-Block -->
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
       birthdate : null,
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
    this.setCurrentUser(this.currentUser.id).then(()=>this.birthdate = this.currentUser.birthdate);
  },
};
</script>
