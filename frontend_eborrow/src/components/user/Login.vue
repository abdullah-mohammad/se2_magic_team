<template>
    <div class="col-md-12">
        <div class="card card-container">
            <img
                    id="profile-img"
                    src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                    class="profile-img-card"
            />
            <form name="form" @submit.prevent="handleLogin">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input
                            v-model="user.username"
                            type="text"
                            class="form-control"
                            name="username"

                    />

                    <div v-if="messageUsername" class="alert alert-danger" role="alert">
                        {{ messageUsername }}
                    </div>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input
                            v-model="user.password"
                            type="password"
                            class="form-control"
                            name="password"

                    />
                    <div v-if="messagePassword" class="alert alert-danger" role="alert">
                        {{ messagePassword }}
                    </div>
                </div>
                <div class="form-group">
                    <button class="btn btn-primary btn-block" :disabled="loading"
                        style="font-family: 'GoShareFont'; background: #539AC5; border-radius: 5px; font-weight: 600; letter-spacing:1.5px; border:none"
                    >
            <span
                    v-show="loading"
                    class="spinner-border spinner-border-sm"
            ></span>
                        <span>Login</span>
                    </button>
                </div>
                <div class="form-group">
                    <div v-if="message" class="alert alert-danger" role="alert">
                        {{ message }}
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
    import User from "../../models/user";

    export default {
        name: "Login",
        data() {
            return {
                user: new User(),
                loading: false,
                message: "",
              messageUsername:"",
              messagePassword:"",
            };
        },
        computed: {},
        created() {
        },
        methods: {
            handleLogin() {
                this.message = "";
                if (this.user.username === undefined || this.user.username === "") {
                    this.messageUsername = "please fill in your username.\n"
                }
                if (this.user.password === undefined || this.user.password === "") {
                    this.messagePassword = "please fill in your password.\n"
                }

                if (this.user.username && this.user.password) {
                    this.loading = true;
                    this.$store.dispatch("auth/login", this.user).then(
                        () => {
                            this.$router.push("/profile");
                        }
                    ).catch(() => {
                        this.loading = false;
                        this.message = "Login data does not match, please try again.";
                    })
                }
            },
        },
    }
    ;
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
