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

                        <div
                                v-if="messageUsername"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageUsername }}
                        </div>

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
                        <div
                                v-if="messagePassword"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messagePassword }}
                        </div>
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

                        <div
                                v-if="messageFirstname"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageFirstname }}
                        </div>

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

                        <div
                                v-if="messageLastname"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageLastname }}
                        </div>

                    </div>
                    <div class="form-group">
                        <label for="email">email</label>
                        <input
                                id="email"
                                v-model="user.email"
                                type="text"
                                class="form-control"
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
                    <div class="form-group">
                        <label for="gender">gender</label>
                        <input
                                id="gender"
                                v-model="user.gender"
                                type="gender"
                                class="form-control"
                                name="gender"
                        />
                        <div
                                v-if="messageGender"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageGender }}
                        </div>
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
                        <div
                                v-if="messageBirthDate"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >
                            {{ messageBirthDate }}
                        </div>
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
                messageUsername: "",
                messagePassword: "",
                messageFirstname: "",
                messageLastname: "",
                messageEmail: "",
                messageGender: "",
                messageBirthDate: "",
                selectedFile: null,
                url: null,
            };

        },
        computed: {},
        mounted() {
        },
        methods: {
            handleRegister() {
                this.message = "";
                this.submitted = true;
                if (!this.validUserData()) {
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

                    authService.register(userdata).then(
                        (data) => {
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
                } else {
                    this.url = null;
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

                var isInvalid = false
                if (this.user.username === undefined || this.user.username === "") {
                    this.messageUsername = "please fill in your username. \n";
                    isInvalid = true;
                }
                if (this.user.username && (this.user.username.length < 4 || this.user.username.length > 30)) {
                    this.messageUsername = "Length is out of bound. \n";
                    isInvalid = true;
                }
                if (this.user.password === undefined || this.user.password === "") {
                    this.messagePassword = "please fill in your password. \n";
                    isInvalid = true;
                }
                if (this.user.password && !this.validPassword(this.user.password)) {
                    this.messagePassword += "Password is invalid: the password must contain at least:\n" +
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
                var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            },
            /** Validate an password.
             * @param email Input to be checked if it is a valid password expression.
             * @returns 'true' if the password check was successful, 'false' otherwise. */
            validPassword(password) {
                var re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&]).{5,}$/;
                return re.test(password);
            }
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
