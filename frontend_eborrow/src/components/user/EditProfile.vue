<template>
    <div>
        <div class="container">
            <div  v-if="currentUser"  class="main-body">
                <!-- Page Heading -->
                <h2 class="my-4 gs-title">Settings: </h2>
                <!-- Page Heading -->

                <!-- Edit-Block -->
                <form @submit.prevent="handleEditUser">
                <div class="row gutters-sm">
                    <!-- Profile-Image -->
                    <div class="col-md-4 mb-3 mr-0">
                        <div class="card m-0 p-0"  style="border: none;">
                            <div class="card-body m-0 p-0">
                            <div class="d-flex flex-column align-items-center text-center">
                                <img v-if="newProfilePicUrl" :src="newProfilePicUrl" alt="Profile picture"
                                    class="img-thumbnail"
                                    width="230"
                                >
                                <img v-else onerror='this.onerror=null;this.src="https://via.placeholder.com/150"'
                                    :src="user.clonedUserPic ? getUserPicture(user.clonedUserPic) : getUserPicture(user.profilepicture)" alt="Profile picture"
                                    class="img-thumbnail"
                                    width="230">
                                <div class="mt-3">
                                    <div class="custom-file">
                                        <input
                                            type="file"
                                            @change="onImageUpload"
                                            id="fileImage"
                                            name="fileImage"
                                            accept="image/png, image/jpeg"
                                            :ref="user.profilepicture"
                                            class="custom-file-input"
                                        />
                                        <label id="fileImageLabel" class="custom-file-label" for="fileImage"></label>
                                    </div>
                                </div>
                            </div>

                            </div>
                        </div>
                    </div>
                    <!-- Profile-Image -->

                    <!-- Edit-Form -->
                    <div class="col-md-8">
                        <div class="card mb-3">
                            <div class="card-body">
<!--                                <form @submit.prevent="handleEditUser">-->
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">First name</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control"
                                                aria-describedby="FirstNameHelp" placeholder="Enter your first name"
                                                v-model="user.firstname">
                                            <div
                                                    v-if="messageFirstname"
                                                    class="alert"
                                                    :class="successful ? 'alert-success' : 'alert-danger'">
                                                {{ messageFirstname }}
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Last name</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control" id="" aria-describedby="emailHelp"
                                                placeholder="Enter your last name"
                                                v-model="user.lastname">
                                            <div
                                                    v-if="messageLastname"
                                                    class="alert"
                                                    :class="successful ? 'alert-success' : 'alert-danger'">
                                                {{ messageLastname }}
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Email</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input type="text" class="form-control" placeholder="Enter email"
                                                v-model="user.email">
                                            <div
                                                    v-if="messageEmail"
                                                    class="alert"
                                                    :class="successful ? 'alert-success' : 'alert-danger'">
                                                {{ messageEmail }}
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Old password</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input v-model="oldpass" type="password" class="form-control"
                                                placeholder="Enter old password">

                                            <div
                                                    v-if="messageOldPass"
                                                    class="alert"
                                                    :class="successful ? 'alert-success' : 'alert-danger'">
                                                {{ messageOldPass }}
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">New password</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input v-model="newpass" type="password" class="form-control"
                                                placeholder="Enter new password">

                                            <div
                                                    v-if="messageNewPass"
                                                    class="alert"
                                                    :class="successful ? 'alert-success' : 'alert-danger'">
                                                {{ messageNewPass }}
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Gender</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <select v-model="user.gender" name="gender" class="custom-select custom-select">
                                                <option :selected="user.gender === 'm'" value="m">M</option>
                                                <option :selected="user.gender === 'f'" value="f">F</option>
                                            </select>
                                            <div
                                                    v-if="messageGender"
                                                    class="alert alert-danger">
                                                {{ messageGender }}
                                            </div>
                                        </div>
                                    </div>
                                    <hr>

                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0">Birthdate</h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <input id="birthdate" v-model="user.birthdate" type="date" class="form-control"
                                            name="birthdate"/>
                                            <div
                                                v-if="messageBirthDate"
                                                class="alert"
                                                :class="successful ? 'alert-success' : 'alert-danger'">
                                              {{ messageBirthDate }}
                                            </div>
                                        </div>

                                    </div>
                                    <br>
                                    <div
                                        v-if="errMsge"
                                        class="alert"
                                        :class="successful ? 'alert-success' : 'alert-danger'">
                                      {{ errMsge }}
                                    </div>
                                    <br>
                                    <input
                                        type="submit"
                                        class="btn btn-sm btn-primary pt-1 pb-1 pl-4 pr-4"
                                        style="font-family: 'GoShareFont'; background: #539AC5; border-radius: 5px; font-weight: 600; letter-spacing:1.5px; border:none"
                                        value="Save">
                                    <span style="padding-left:5px"> &nbsp; </span> &nbsp;
                                    <button
                                        class="btn btn-sm btn-danger pt-1 pb-1 pl-4 pr-4"
                                        style="font-family: 'GoShareFont'; background: #C55353; border-radius: 5px; font-weight: 600; letter-spacing:1.5px; border:none"
                                        @click={}>Delete</button>
                            </div>
                        </div>
                    </div>
                    <!-- Edit-Form -->
                </div>
                <!-- Edit-Block -->
                </form>
            </div>
            <div v-else>
                {{errMsge}}
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from 'vuex';
    import userDataService from "../../services/UserDataService";
    const API_IMG_RESOURCE = process.env.VUE_APP_API_URL+"users/get-img/";


    export default {
        name: "edit-profile",
        data() {
            return {
                url: null,
                fileImage: null,
                errMsge: "",
                oldpass: "",
                oldpassValid: false,
                isChecked: false,
                newpass: "",
                messageUsername: "",
                messageOldPass: "",
                messageNewPass: "",
                messageFirstname: "",
                messageLastname: "",
                messageEmail: "",
                messageGender: "",
                messageBirthDate: "",
                newProfilePicUrl: null,
                successful: null
            }
        },
        computed: {
            currentUser() {
                return this.$store.state.auth.user;
            },
            ...mapState('user', ['user', 'clonedUserPic'])
        },
        methods: {
            ...mapActions({
                logout: "auth/logout",
                login: "auth/login",
                setCurrentUser: "user/setCurrentUser",
                editUser: "user/editUser"
            }),
            getUserPicture(img) {
                if (img !== undefined) {
                    return `${API_IMG_RESOURCE}${img}/`
                }
            },
            onImageUpload(event) {
                if (event.target.files[0] != null) {
                    this.user.profilepicture = event.target.files[0];
                    this.newProfilePicUrl = URL.createObjectURL(this.user.profilepicture);
                    document.getElementById("fileImageLabel").classList.add("selected")
                    document.getElementById("fileImageLabel").innerHTML = this.user.profilepicture.name
                } else {
                    this.newProfilePicUrl = null;
                    document.getElementById("fileImageLabel").classList.remove("selected")
                    document.getElementById("fileImageLabel").innerHTML = ""
                    this.user.profilepicture = this.clonedUserPic
                }
            },
            handleEditUser() {
                if (!this.validEditUserData()) {
                    const pass = this.newpass !== "" ? this.newpass : this.user.password;
                    const editUserPass = this.newpass !== "";
                    const newPic = this.user.profilepicture === this.clonedUserPic ? null : this.user.profilepicture;
                    var data = new FormData();
                    data.append("username", this.user.username);
                    data.append("password", pass);
                    data.append("firstname", this.user.firstname);
                    data.append("lastname", this.user.lastname);
                    data.append("email", this.user.email);
                    data.append("gender", this.user.gender);
                    data.append("birthdate", this.user.birthdate);
                    data.append("editPass", editUserPass);
                    data.append("newPic", newPic);
                    userDataService.editUser(this.currentUser.id, data)
                        .then((res) => {
                            this.user.profilepicture = res.data.profilepicture
                            this.$router.push("/profile");
                        })
                        .catch(e => this.errMsge = e)
                }
            },
            validEditUserData() {

                this.messageUsername = "";
                this.messageOldPass = "";
                this.messageNewPass = "";
                this.messageFirstname = "";
                this.messageLastname = "";
                this.messageEmail = "";
                this.messageGender = "";
                this.messageBirthDate = "";

                var isInvalid = false
                if (this.user.username === undefined || this.user.username === "") {
                    this.messageUsername = "please fill in your username. \n";
                    isInvalid = true;
                }
                if (this.user.username && (this.user.username.length < 4 || this.user.username.length > 30)) {
                    this.messageUsername = "Length is out of bound. \n";
                    isInvalid = true;
                }

                if (this.newpass || this.newpass.trim() !== ""){
                  if (!this.oldpass || (this.oldpass && this.handleCheckOldPass())) {
                    this.messageOldPass = "please fill in your old password. \n";
                    isInvalid = true;
                  }

                  if (!this.newpass || this.newpass.trim() === "") {
                    this.messageNewPass = "please fill in your new password. \n";
                    isInvalid = true;
                  }
                  if (this.newpass && !this.validPassword(this.newpass)) {
                    this.messageNewPass += "Password is invalid: the password must contain at least:\n" +
                        " 1 lowercase alphabetical character,\n" +
                        " 1 uppercase alphabetical character,\n" +
                        " 1 numeric character,\n" +
                        " 1 special character\n" +
                        " and must be eight characters or longer. (Example!!123!!) \n";
                    isInvalid = true;
                  }
                }

                if (this.user.firstname === undefined || this.user.firstname === "") {
                    this.messageFirstname = "please fill in your firstname. \n"
                    isInvalid = true;
                }
                if (this.user.firstname && (this.user.firstname.length < 2 || this.user.firstname.length > 30)) {
                    this.messageFirstname = "Length is out of bound. \n";
                    isInvalid = true;
                }
                if (this.user.lastname === undefined || this.user.lastname === "") {
                    this.messageLastname = "please fill in your lastname.\n"
                    isInvalid = true;
                }
                if (this.user.lastname && (this.user.lastname.length < 2 || this.user.lastname.length > 30)) {
                    this.messageLastname = "Length is out of bound. \n";
                    isInvalid = true;
                }
                if (this.user.email === undefined || this.user.email === "") {
                    this.messageEmail = "please fill in your email.\n"
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

                if (!this.user.birthdate) {
                    this.messageBirthDate = "please choose your birthdate.\n";
                    isInvalid = true;
                }

                return isInvalid;
            },
            /** Validate an e-mail address.
             * @param email Input to be checked if it is a valid e-mail expression.
             * @returns 'true' if the e-mail check was successful, 'false' otherwise. */
            validEmail(email) {
                var re = /\S+@\S+\.\S+/;
                return re.test(email);
            },
            /** Validate an password.
             * @param email Input to be checked if it is a valid password expression.
             * @returns 'true' if the password check was successful, 'false' otherwise. */
            validPassword(password) {
                var re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&]).{5,}$/;
                return re.test(password);
            },
            goBack() {
                this.$router.push("/profile")
            },
            handleCheckOldPass() {
                var passIsValid = false
                userDataService.checkUserPass(this.currentUser.id, this.oldpass)
                    .then(() => {
                        passIsValid = true
                    })
                    .catch(e => {
                        this.errMsge = e.message
                        passIsValid = false
                    })
                return passIsValid
            },
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


<style lang="css">
    .custom-file-label::after {
        left: 0;
        right: auto;
        border-left-width: 0;
        border-right: inherit;
        font-family: FontAwesome;
        content: "\f03e"!important;
        font-weight: 900;
    }
</style>