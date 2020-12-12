<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img id="profile-img" v-if="url" :src="url" class="profile-img-card"/>
      <form name="form" @submit.prevent="handleRegister">
        <div v-if="!successful">
          <div class="form-group">
            <label for="username">Username</label>
            <input
                id="username"
                v-model="user.username"
                type="text"
                class="form-control"
                name="username"
            />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input
                id="password"
                v-model="user.password"
                type="password"
                class="form-control"
                name="password"
            />
          </div>
          <div class="form-group">
            <label for="firstname">firstname</label>
            <input
                id="firstname"
                v-model="user.firstname"
                type="text"
                class="form-control"
                name="firstname"
            />
          </div>
          <div class="form-group">
            <label for="lastname">lastname</label>
            <input
                id="lastname"
                v-model="user.lastname"
                type="text"
                class="form-control"
                name="lastname"
            />
          </div>
          <div class="form-group">
            <label for="email">email</label>
            <input
                id="email"
                v-model="user.email"
                type="email"
                class="form-control"
                name="email"
            />
          </div>
          <div class="form-group">
            <label for="gender">gender</label>
            <input
                id="gender"
                v-model="user.gender"
                type="gender"
                class="form-control"
                name="gender"
            />
          </div>
          <div class="form-group">
            <label for="birthdate">birthdate</label>
            <input
                id="birthdate"
                v-model="user.birthdate"
                type="date"
                class="form-control"
                name="birthdate"
            />
          </div>
          <div class="form-group">
            <label for="picture">picture</label>
            <input
                @change="onFileSelected"
                type="file"
                id="picture"
                name="fileImage"
                accept="image/png,image/jpeg"
            />
          </div>

          <div class="form-group">
            <button class="btn btn-primary btn-block">Sign Up</button>
          </div>
        </div>
        <div v-if="successful">Account created, nice to have you on board!</div>
      </form>

      <div
          v-if="message"
          class="alert"
          :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import User from "../../models/user";
import authService from "../../services/AuthService";


export default {
  name: "Register",

  data() {
    return {
      user: new User(),
      submitted: false,
      successful: false,
      message: "",
      selectedFile: null,
      url: null,
    };

  },
  computed: {},
  mounted() {},
  methods: {
    handleRegister() {
      this.message = "";
      this.submitted = true;

      var userdata = new FormData();
      userdata.append("username", this.user.username);
      userdata.append("password", this.user.password);
      userdata.append("firstname", this.user.firstname);
      userdata.append("lastname", this.user.lastname);
      userdata.append("email", this.user.email);
      userdata.append("gender", this.user.gender);
      userdata.append("birthdate", this.user.birthdate);
      userdata.append("profilepicture", this.user.profilepicture);

      authService.register(userdata).then(
          (data) => {
            this.message = data.message;
            this.successful = true;
          },
          (error) => {
            this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
            this.successful = false;
          }
      );
    },
    onFileSelected(event) {
      this.user.profilepicture = event.target.files[0];
      this.url = URL.createObjectURL(this.user.profilepicture);
    },
  },
};
</script>

<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}
</style>
