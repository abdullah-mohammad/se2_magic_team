<template>
    <div>
        <div v-if="!getCurrentUser">
            If you
            <router-link to="/login">log in</router-link>
            , you can add you items to share them.
        </div>
        <div class="submit-form" v-else>
            <h1>add new item:</h1>
            <div v-if="!submitted">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input
                            type="text"
                            class="form-control"
                            id="title"
                            v-model="item.title"/>

                    <div
                            v-if="messageTitle"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'">
                        {{ messageTitle }}
                    </div>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea
                            class="form-control"
                            id="description"
                            v-model="item.description"/>

                    <div
                            v-if="messageDescription"
                            class="alert"
                            :class="successful ? 'alert-success' : 'alert-danger'">
                        {{ messageDescription }}
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-6">
                        <div class="form-group">
                            <label for="availableFrom">available from</label>
                            <input
                                    type="date"
                                    class="form-control"
                                    id="availableFrom"
                                    v-model="item.availableFrom"/>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <label for="availableTo">available to</label>
                            <input
                                    type="date"
                                    class="form-control"
                                    id="availableTo"
                                    v-model="item.availableTo"/>
                        </div>
                    </div>
                    <div class="col form-group">
                        <div v-if="messageAvailableTo || messageAvailableFrom"
                             class="alert"
                             :class="successful ? 'alert-success' : 'alert-danger'">
                            {{ messageAvailableFrom }}{{ messageAvailableTo }}
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="fileImage">Upload image</label>
                    <input
                            type="file"
                            @change="onImageUpload"
                            id="fileImage"
                            name="fileImage"
                            accept="image/png, image/jpeg"
                            ref="fileImage"
                    />
                </div>
                <div class="form-group">
                    <div id="preview">
                        <img v-if="url" :src="url"/>
                    </div>
                </div>
                <button @click="saveItem" class="btn btn-success">Share</button>
            </div>

            <div v-else>
                <h4>You submitted successfully!</h4>
                <button class="btn btn-success" @click="newItem">Add</button>
            </div>
        </div>
    </div>
</template>

<script>
    import ItemDataService from "../../services/ItemDataService";
    import {mapGetters} from 'vuex';

    export default {
        name: "add-item",
        data() {
            return {
                item: {
                    id: null,
                    title: "",
                    description: "",
                    fileImage: null,
                    published: false,
                    availableFrom: null,
                    availableTo: null,
                },
                submitted: false,
                url: null,
                messageTitle: "",
                messageDescription: "",
                messageAvailableFrom: "",
                messageAvailableTo: "",
            };
        },
        computed: mapGetters('user', ['getCurrentUser']),
        methods: {
            saveItem() {
                if (!this.validAddItemData()) {
                    var data = new FormData();
                    data.append("title", this.item.title);
                    data.append("description", this.item.description);
                    data.append("available", false);
                    data.append("user", String(this.getCurrentUser.id));
                    data.append("fileImage", this.item.fileImage);

                    ItemDataService.create(data)
                        .then((response) => {
                            this.item.id = response.data.id;
                            this.submitted = true;
                        })
                        .catch((e) => {
                            console.log(e);
                        });
                }
            },

            newItem() {
                this.submitted = false;
                this.item = {};
                this.url = null;
            },

            onImageUpload(event) {
                if (event.target.files[0] != null) {
                    this.item.fileImage = event.target.files[0];
                    this.url = URL.createObjectURL(this.item.fileImage);
                } else {
                    this.url = null;
                }
            },

            validAddItemData() {

                this.messageTitle = "";
                this.messageDescription = "";
                this.messageAvailableFrom = "";
                this.messageAvailableTo = "";

                var isInvalid = false

                if (this.item.title === undefined || this.item.title === "") {
                    this.messageTitle = "please fill in the titel. \n";
                    isInvalid = true;
                }
                if (this.item.title && (this.item.title.length < 3 || this.item.title.length > 40)) {
                    this.messageTitle = "Length is out of bound. \n";
                    isInvalid = true;
                }
                if (this.item.description === undefined || this.item.description === "") {
                    this.messageDescription = "please fill in the description. \n";
                    isInvalid = true;
                }
                if (this.item.description && (this.item.description.length < 3 || this.item.description.length > 200)) {
                    this.messageDescription = "Length is out of bound. \n";
                    isInvalid = true;
                }

                if (!this.item.availableFrom) {
                    this.messageAvailableFrom = "please choose the start date. \n";
                    isInvalid = true;
                }

                if (!this.item.availableTo) {
                    this.messageAvailableTo = "please choose the end date. \n";
                    isInvalid = true;
                }

                if (this.item.availableFrom > this.item.availableTo) {
                    this.messageAvailableFrom += "start date can not be earlier than end date. \n";
                    isInvalid = true;
                }
                return isInvalid
            },
        },
    };
</script>

<style>
    .submit-form {
        max-width: 300px;
        margin: auto;
    }

    #preview {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    #preview img {
        max-width: 100%;
        max-height: 500px;
    }
</style>
