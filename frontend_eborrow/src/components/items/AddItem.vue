<template>
  <div>
    <div v-if="!getCurrentUser">
      If you <router-link to="/login">log in</router-link>, you can add you items to share them. 
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
            required
            v-model="item.title"
            name="title"
        />
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <textarea
            class="form-control"
            id="description"
            required
            v-model="item.description"
            name="description"
        />
      </div>
      <div class="row">
        <div class="col-6">
          <div class="form-group">
            <label for="description">available from</label>
            <input
                type="date"
                class="form-control"
                id="availableFrom"
                required
                v-model="item.availableFrom"
                name="description"
            />
          </div>
        </div>
        <div class="col-6">
          <div class="form-group">
            <label for="availableTo">available to</label>
            <input
                type="date"
                class="form-control"
                id="availableTo"
                required
                v-model="item.availableTo"
                name="availableTo"
            />
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
import { mapGetters } from 'vuex';

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
        availableFrom: false,
        availableTo: false,
      },
      submitted: false,
      url: null,
    };
  },
  computed: mapGetters('user',['getCurrentUser']),
  methods: {
    saveItem() {
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
      }else{
        this.url = null;
      }
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
