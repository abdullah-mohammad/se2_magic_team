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
                                    <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin"
                                         class="rounded-circle" width="150">
                                    <div class="mt-3">
                                        <input type="text" class="form-control text-center" placeholder="Enter username"
                                               v-model="user.username"> <br>
                                        <div
                                                v-if="messageUsername"
                                                class="alert"
                                                :class="successful ? 'alert-success' : 'alert-danger'">
                                            {{ messageUsername }}
                                        </div>
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
                                                   placeholder="Enter old password"
                                                   @change="handleCheckOldPass">

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
                                            <input type="text" class="form-control" placeholder="Choose gender"
                                                   v-model="user.gender">
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

                                        <input id="birthdate" v-model="user.birthdate" type="date" class="form-control"
                                               name="birthdate"/>
                                        <div
                                                v-if="messageBirthdate"
                                                class="alert"
                                                :class="successful ? 'alert-success' : 'alert-danger'">
                                            {{ messageBirthdate }}
                                        </div>
                                    </div>
                                    <p v-if="errMsge" class="text-danger">{{errMsge}}</p>
                                    <br>
                                    <input type="submit" class="btn btn-success" value="Save">
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
    import {mapActions, mapState} from 'vuex';
    import User from '../../models/user';
    import userDataService from "../../services/UserDataService";

    export default {
        name: "edit-profile",
        data() {
            return {
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
            formatDate(date) {
                var d = new Date(date),
                    month = '' + (d.getMonth() + 1),
                    day = '' + d.getDate(),
                    year = d.getFullYear();

                if (month.length < 2)
                    month = '0' + month;
                if (day.length < 2)
                    day = '0' + day;

                return [day, month, year].join('-');
            },
            handleEditUser() {
                if (!this.validUserData() && this.handleCheckOldPass()) {
                    const pass = this.newpass != "" ? this.newpass : this.user.password;
                    const user = new User(this.user.username, pass, this.user.firstname, this.user.lastname, this.user.email, this.user.gender, this.user.profilepicture, this.user.birthdate)
                    const editUserPass = this.newpass != "" ? true : false;
                    const data = {...user, editPass: editUserPass}
                    userDataService.editUser(this.currentUser.id, data)
                        .then(() => {
                            this.$router.push("/profile");
                        })
                        .catch(e => this.errMsge = e)
                }
            },

            validUserData() {

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
                if (this.oldpass === undefined || this.oldpass === "") {
                    this.messageOldPass = "please fill in your old password. \n";
                    isInvalid = true;
                }
                if (this.newpass === undefined || this.newpass === "") {
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

                if (this.user.birthdate === undefined) {
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
                        this.messageOldPass = ""
                        //this.oldpassValid = true
                        passIsValid = true
                    })
                    .catch(e => {
                        this.errMsge = e
                        this.messageOldPass = "old password doesn't match"
                        // this.oldpassValid = false
                        passIsValid = false
                    })
                return passIsValid
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