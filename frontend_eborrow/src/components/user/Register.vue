<template>
  <div class="col-md-12">
    <!-- Page Heading -->
    <h2 class="my-4 gs-title">Create your account in GoShare community:</h2>
    <!-- Page Heading -->
    <br />
    <!-- Register-Block -->
    <form name="form" @submit.prevent="handleRegister">
      <div v-if="!successful" class="row gutters-sm">
        <!-- Profile-Image -->
        <div class="col-md-4 mb-3 mr-0">
          <div
            class="card m-0 p-0"
            style="border: none; box-shadow: none; background: none"
          >
            <div class="card-body m-0 p-0">
              <div class="d-flex flex-column align-items-center text-center">
                <img
                  :src="url ? url : 'https://via.placeholder.com/150'"
                  alt="Profile picture"
                  class="img-thumbnail"
                  width="230"
                />
                <div class="mt-3 mb-3">
                  <div class="custom-file">
                    <input
                      @change="onFileSelected"
                      type="file"
                      id="picture"
                      name="fileImage"
                      accept="image/png,image/jpeg"
                      class="custom-file-input"
                    />
                    <label
                      id="fileImageLabel"
                      class="custom-file-label"
                      for="fileImage"
                    ></label>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Profile-Image -->

        <!-- Edit-Form -->
        <div class="col-md-7">
          <div class="card mb-3 mt-0">
            <div class="card-body">
              <div class="row">
                <div class="col-sm-12 text-secondary">
                  <input
                    type="text"
                    class="form-control"
                    id="username"
                    v-model="user.username"
                    name="username"
                    placeholder="Username"
                  />
                  <div
                    v-if="messageUsername"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messageUsername }}
                  </div>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="col-sm-12 text-secondary">
                  <input
                    type="text"
                    class="form-control"
                    id="firstname"
                    v-model="user.firstname"
                    name="firstname"
                    placeholder="Firstname"
                  />
                  <div
                    v-if="messageFirstname"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messageFirstname }}
                  </div>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="col-sm-12 text-secondary">
                  <input
                    type="text"
                    class="form-control"
                    id="lastname"
                    v-model="user.lastname"
                    name="lastname"
                    placeholder="Lastname"
                  />
                  <div
                    v-if="messageLastname"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messageLastname }}
                  </div>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="col-sm-12 text-secondary">
                  <input
                    type="text"
                    class="form-control"
                    placeholder="Email"
                    id="email"
                    v-model="user.email"
                    name="email"
                  />
                  <div
                    v-if="messageEmail"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messageEmail }}
                  </div>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="col-sm-12 text-secondary">
                  <input
                    type="password"
                    class="form-control"
                    id="password"
                    v-model="user.password"
                    name="password"
                    placeholder="Password"
                  />
                  <div
                    v-if="messagePassword"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messagePassword }}
                  </div>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="col-sm-6 mb-3 text-secondary">
                  <select
                    v-model="user.gender"
                    id="gender"
                    name="gender"
                    class="custom-select custom-select"
                  >
                    <option value="m">M</option>
                    <option value="f">F</option>
                    <option value="d">D</option>
                  </select>
                  <div
                    v-if="messageGender"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messageGender }}
                  </div>
                </div>
                <div class="col-sm-6 text-secondary">
                  <input
                    id="birthdate"
                    v-model="user.birthdate"
                    type="date"
                    class="form-control"
                    name="birthdate"
                  />
                  <div
                    v-if="messageBirthDate"
                    class="alert"
                    :class="successful ? 'alert-success' : 'alert-danger'"
                  >
                    {{ messageBirthDate }}
                  </div>
                </div>
              </div>
              <hr>
              <div class="row">
                    <div class="col-sm-9 text-secondary mb-3">
                        <input id="street" v-model="address.street" type="text" class="form-control"
                            name="street"
                            placeholder="Street"
                            />
                        <div
                            v-if="messageStreet"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageStreet }}
                        </div>
                    </div>
                    <div class="col-sm-3 mb-3 text-secondary">
                        <input id="streetnumber" v-model="address.streetnumber" type="number" class="form-control"
                            name="streetnumber"
                            placeholder="Nr."
                            />
                        <div
                            v-if="messageStreetNr"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageStreetNr }}
                        </div>
                    </div>
              </div>

              <div class="row">
                    <div class="col-sm-4 text-secondary mb-3">
                        <input id="zip" v-model="address.zipcode" type="number" class="form-control"
                            name="zip"
                            placeholder="Zip"
                            />
                        <div
                            v-if="messageZip"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageZip }}
                        </div>
                    </div>
                    <div class="col-sm-8 mb-3 text-secondary">
                        <input id="city" v-model="address.city" type="text" class="form-control"
                            name="city"
                            placeholder="City"
                            />
                        <div
                            v-if="messageCity"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageCity }}
                        </div>
                    </div>
              </div>
              <div class="row">
                    <div class="col-sm-12 mb-3 text-secondary">
                        <input type="text" class="form-control"
                            id="country"
                            v-model="address.country"
                            name="country"
                            placeholder="Country"
                        >
                        <div
                            v-if="messageCountry"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageCountry }}
                        </div>
                    </div>
              </div>
              <br />
              <div class="row">
                <div class="col-sm-12 text-center">
                  <input
                    type="submit"
                    class="btn btn-sm btn-primary pt-1 pb-1 pl-4 pr-4"
                    style="
                      font-family: 'GoShareFont';
                      background: #539ac5;
                      border-radius: 5px;
                      font-weight: 600;
                      letter-spacing: 1.5px;
                      border: none;
                    "
                    value="Let's do it"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Edit-Form -->
      </div>
      <!-- Edit-Block -->
      <div v-if="successful">Account created, nice to have you on board!</div>
    </form>
    <!-- Register-Block -->

    <div
      v-if="message"
      class="alert"
      :class="successful ? 'alert-success' : 'alert-danger'"
    >
      {{ message }}
    </div>
  </div>
</template>

<script>
import User from "../../models/user";
import Address from "../../models/address";
import authService from "../../services/AuthService";
import addressService from "../../services/AddressDataService";
import userDataService from '../../services/UserDataService';

export default {
  name: "Register",

  data() {
    return {
      user: new User(),
      submitted: false,
      successful: false,
      message: "",
      messageUsername: "",
      messagePassword: "",
      messageFirstname: "",
      messageLastname: "",
      messageEmail: "",
      messageGender: "",
      messageBirthDate: "",
      selectedFile: null,
      url: null,
      address: new Address(),
      messageStreet: "",
      messageStreetNr: "",
      messageZip: "",
      messageCity: "",
      messageCountry: "",
    };
  },
  computed: {},
  mounted() {},
  methods: {
    handleRegister() {
      this.message = "";
      this.submitted = true;
      if (!this.validUserData()) {
        var addressData = new FormData();
        addressData.append("street", this.address.street);
        addressData.append("streetnumber", this.address.streetnumber);
        addressData.append("zipcode", this.address.zipcode);
        addressData.append("city", this.address.city);
        addressData.append("country", this.address.country);

        var userdata = new FormData();
        userdata.append("username", this.user.username);
        userdata.append("password", this.user.password);
        userdata.append("firstname", this.user.firstname);
        userdata.append("lastname", this.user.lastname);
        userdata.append("email", this.user.email);
        userdata.append("gender", this.user.gender);
        userdata.append("birthdate", this.user.birthdate);
        userdata.append("profilepicture", this.user.profilepicture);

        var object = {};
        userdata.forEach(function (value, key) {
          object[key] = value;
        });
        var json = JSON.stringify(object);
        console.log(json);

        // save user
        authService.register(userdata).then(
          (data) => {
              // save address
              addressData.append("userId", data.data.id);
              addressService.save(addressData)
                            .then(resAddr=>{
                                // update user-Address
                                userDataService.editUserAddress(data.data.id, resAddr.data.id)
                            })
            this.message = data.message;
            this.successful = true;
          },
          () => {
            this.message = "";

            this.successful = false;
          }
        );

      }
    },
    onFileSelected(event) {
      if (event.target.files[0] != null) {
        this.user.profilepicture = event.target.files[0];
        this.url = URL.createObjectURL(this.user.profilepicture);
        document.getElementById("fileImageLabel").classList.add("selected");
        document.getElementById(
          "fileImageLabel"
        ).innerHTML = this.user.profilepicture.name;
      } else {
        this.url = null;
        document.getElementById("fileImageLabel").classList.remove("selected");
        document.getElementById("fileImageLabel").innerHTML = "";
      }
    },
    validUserData() {
      this.messageUsername = "";
      this.messagePassword = "";
      this.messageFirstname = "";
      this.messageLastname = "";
      this.messageEmail = "";
      this.messageGender = "";
      this.messageBirthDate = "";

      this.messageStreet = "";
      this.messageStreetNr = "";
      this.messageZip = "";
      this.messageCity = "";
      this.messageCountry = "";

      var isInvalid = false;
      if (this.user.username === undefined || this.user.username === "") {
        this.messageUsername = "please fill in your username. \n";
        isInvalid = true;
      }
      if (
        this.user.username &&
        (this.user.username.length < 4 || this.user.username.length > 30)
      ) {
        this.messageUsername = "Length is out of bound. \n";
        isInvalid = true;
      }
      if (this.user.password === undefined || this.user.password === "") {
        this.messagePassword = "please fill in your password. \n";
        isInvalid = true;
      }
      if (this.user.password && !this.validPassword(this.user.password)) {
        this.messagePassword +=
          "Password is invalid: the password must contain at least:\n" +
          " 1 lowercase alphabetical character,\n" +
          " 1 uppercase alphabetical character,\n" +
          " 1 numeric character,\n" +
          " 1 special character\n" +
          " and must be eight characters or longer. (Example!!123!!) \n";
        isInvalid = true;
      }

      if (this.user.firstname === undefined || this.user.firstname === "") {
        this.messageFirstname = "please fill in your firstname. \n";
        isInvalid = true;
      }
      if (
        this.user.firstname &&
        (this.user.firstname.length < 2 || this.user.firstname.length > 30)
      ) {
        this.messageFirstname = "Length is out of bound. \n";
        isInvalid = true;
      }
      if (this.user.lastname === undefined || this.user.lastname === "") {
        this.messageLastname = "please fill in your lastname.\n";
        isInvalid = true;
      }
      if (
        this.user.lastname &&
        (this.user.lastname.length < 2 || this.user.lastname.length > 30)
      ) {
        this.messageLastname = "Length is out of bound. \n";
        isInvalid = true;
      }
      if (this.user.email === undefined || this.user.email === "") {
        this.messageEmail = "please fill in your email.\n";
        isInvalid = true;
      }
      if (this.user.email && !this.validEmail(this.user.email)) {
        this.messageEmail += "E-Mail Address is invalid.\n";
        isInvalid = true;
      }
      if (this.user.gender === undefined || this.user.gender === "") {
        this.messageGender = "please choose your gender.\n";
        isInvalid = true;
      }

      if (this.user.birthdate === undefined) {
        this.messageBirthDate = "please choose your birthdate.\n";
        isInvalid = true;
      }

      if (this.address.street === undefined || this.address.street === "") {
        this.messageStreet = "please fill in the street. \n"
        isInvalid = true;
      }
      if (this.address.street && (this.address.street.length < 2 || this.address.street.length > 50)) {
        this.messageStreet = "Length is out of bound. \n";
        isInvalid = true;
      }

      if (this.address.streetnumber === undefined || this.address.streetnumber === "") {
        this.messageStreetNr = "please fill in the street-number. \n"
        isInvalid = true;
      }
      if (this.address.zipcode === undefined || this.address.zipcode === "") {
        this.messageZip = "please fill in the address-zipcode. \n"
        isInvalid = true;
      }

      if (this.address.city === undefined || this.address.city === "") {
        this.messageCity = "please fill in the city. \n"
        isInvalid = true;
      }
      if (this.address.city && (this.address.city.length < 2 || this.address.city.length > 50)) {
        this.messageCity = "Length is out of bound. \n";
        isInvalid = true;
      }

      if (this.address.country === undefined || this.address.country === "") {
        this.messageCountry = "please fill in the country. \n"
        isInvalid = true;
      }
      if (this.address.country && (this.address.country.length < 2 || this.address.country.length > 50)) {
        this.messageCountry = "Length is out of bound. \n";
        isInvalid = true;
      }
      
      return isInvalid;
    },
    /** Validate an e-mail address.
     * @param email Input to be checked if it is a valid e-mail expression.
     * @returns 'true' if the e-mail check was successful, 'false' otherwise. */
    validEmail(email) {
      var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
    /** Validate an password.
     * @param email Input to be checked if it is a valid password expression.
     * @returns 'true' if the password check was successful, 'false' otherwise. */
    validPassword(password) {
      var re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&]).{5,}$/;
      return re.test(password);
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
